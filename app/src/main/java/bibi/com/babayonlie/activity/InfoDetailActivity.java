package bibi.com.babayonlie.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.bean.CommentBean;
import bibi.com.babayonlie.bean.CommentInfo;
import bibi.com.babayonlie.bean.CommentListResult;
import bibi.com.babayonlie.bean.CommentNumResult;
import bibi.com.babayonlie.projecturl.projecturl;
import bibi.com.babayonlie.utils.DateUtils;
import bibi.com.babayonlie.widget.MyCallBack;

/**
 * Created by bibinet on 2016/12/1.
 */
@ContentView(R.layout.activity_infodetail)
public class InfoDetailActivity extends  BaseActivity {
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.title_imageleft)
    private ImageView imageleft;
    @ViewInject(R.id.infohead)
    private TextView infohead;
    @ViewInject(R.id.infocontent)
    private TextView infocontent;
    @ViewInject(R.id.infotime)
    private TextView infotime;
    @ViewInject(R.id.infocomment)
    private TextView commentnumber;
    @ViewInject(R.id.infowebview)
    private WebView infowebview;
    @ViewInject(R.id.infolistview)
    private LinearLayout infolistiview;
    @ViewInject(R.id.synopsis_comment_saywhat)
    private EditText edit_speek;
    @ViewInject(R.id.synopsis_comment_say)
    private Button btn_send;
    @ViewInject(R.id.synopsis_comment_expression)
    private  Button expression;
    @ViewInject(R.id.expressioncontainer)
    private  LinearLayout expressioncontainer;
    private int taotalitem=124+1;
    private int pagesize=24-1;
    private int[] imageIds = new int[124 + 1];
    private int taotalpage=(taotalitem%pagesize==0)?taotalitem/pagesize:taotalitem/pagesize+1;
    private int relaid;
    private String type="2";
    private String imei;
    private Gson gson;
    private boolean hasMore = false;
    private List<CommentBean> lists = new ArrayList<CommentBean>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initdata();
        initcomemtlist();
        setlistioner();
    }

    private void setlistioner() {

        expression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (expressioncontainer.getVisibility()== View.VISIBLE){
                expressioncontainer.setVisibility(View.GONE);
            }else {
                expressioncontainer.setVisibility(View.VISIBLE);
                initviewpager();
            }
            }
        });

    }

    private void initviewpager() {
        View view = LayoutInflater.from(InfoDetailActivity.this).inflate(R.layout.item_expresslayout, null);
        ViewPager viewpager = (ViewPager) view.findViewById(R.id.expressionviewpager);
        TextView pagecount= (TextView) view.findViewById(R.id.expressionpagecount);
        viewpager.setAdapter(new MypagerAdapter(getView()));
        expressioncontainer.addView(view);
    }

    class MypagerAdapter extends PagerAdapter{
        private List<View> views=new ArrayList<>();

        public MypagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
            object=null;
          //  super.destroyItem(container, position, object);
        }
    }




    private List<View> getView() {
        List<View> list = new ArrayList<View>();

        for (int i = 0; i < taotalpage; i++) {
            final int start = i * pagesize + 1;
            GridView gridView = createGridView(start, pagesize);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Log.d("TAGx", arg2 + "");
                    if (arg2 == pagesize) {
                        String text = edit_speek.getText().toString();
                        if (text.length() <= 0) {
                        } else if (text.length() < 4) {
                            text = text.substring(0, text.length() - 1);
                            edit_speek.setText(text);
                        } else {
                            String textMatch = text.substring(text.length() - 4, text.length());

                            Pattern pattern = Pattern.compile("e[0-9][0-9][0-9]");
                            Matcher matcher = pattern.matcher(textMatch);
                            if (matcher.matches()) {
                                text = text.substring(0, text.length() - 4);
                            } else {
                                text = text.substring(0, text.length() - 1);
                            }

                            edit_speek.setText("");

                            for (int l = 0; l < text.length(); l++) {
                                if (text.charAt(l) == 'e' && l + 4 <= text.length()) {
                                    String textMatch2 = text.substring(l, l + 4);
                                    Pattern pattern2 = Pattern.compile("e[0-9][0-9][0-9]");
                                    Matcher matcher2 = pattern2.matcher(textMatch2);
                                    if (matcher2.matches()) {
                                        l = l + 4 - 1;
                                        int item = Integer.parseInt(textMatch2.substring(1, textMatch2.length()));
                                        Bitmap bitmap = null;
                                        bitmap = BitmapFactory.decodeResource(getResources(), imageIds[item
                                                % imageIds.length]);

                                        Matrix matrix = new Matrix();
                                        matrix.postScale(0.45f, 0.45f); // 长和宽放大缩小的比例
                                        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                                bitmap.getHeight(), matrix, true);

                                        ImageSpan imageSpan = new ImageSpan(InfoDetailActivity.this, resizeBitmap);
                                        String str = null;
                                        if (item < 10) {
                                            str = "e00" + item;
                                        } else if (item < 100) {
                                            str = "e0" + item;
                                        } else {
                                            str = "e" + item;
                                        }
                                        SpannableString spannableString = new SpannableString(str);
                                        spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        edit_speek.append(spannableString);
                                    } else {
                                        edit_speek.append(text.charAt(l) + "");
                                    }
                                } else {
                                    edit_speek.append(text.charAt(l) + "");
                                }
                            }
                        }
                    } else {
                        int item = start + arg2;
                        Bitmap bitmap = null;
                        bitmap = BitmapFactory.decodeResource(getResources(), imageIds[item % imageIds.length]);

                        Matrix matrix = new Matrix();
                        matrix.postScale(0.45f, 0.45f); // 长和宽放大缩小的比例
                        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                                matrix, true);

                        ImageSpan imageSpan = new ImageSpan(InfoDetailActivity.this, resizeBitmap);
                        String str = null;
                        if (item < 10) {
                            str = "e00" + item;
                        } else if (item < 100) {
                            str = "e0" + item;
                        } else {
                            str = "e" + item;
                        }
                        SpannableString spannableString = new SpannableString(str);
                        spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        edit_speek.append(spannableString);
                    }

                    // express.setVisibility(View.GONE);
                    // transportView.setVisibility(View.GONE);
                }
            });
            list.add(gridView);
        }

        return list;
    }



    /**
     * expression
     *
     * 生成一个表情对话框中的gridview
     */
    private GridView createGridView(int start, int pageSize) {
        final GridView view = new GridView(this);
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        // 生成98个表情的id，封装
        for (int i = start; i < start + pageSize; i++) {
            if (i >= taotalitem) {
                break;
            }
            try {
                if (i < 10) {
                    // 通过反射找到图片资源id
                    Field field = R.drawable.class.getDeclaredField("e00" + i);
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    imageIds[i] = resourceId;
                } else if (i < 100) {
                    Field field = R.drawable.class.getDeclaredField("e0" + i);
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    imageIds[i] = resourceId;
                } else {
                    Field field = R.drawable.class.getDeclaredField("e" + i);
                    int resourceId = Integer.parseInt(field.get(null).toString());
                    imageIds[i] = resourceId;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("image", imageIds[i]);
            listItems.add(listItem);
        }

        Map<String, Object> listItem = new HashMap<String, Object>();
        listItem.put("image", R.drawable.expression);
        listItems.add(listItem);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.item_singleexpression,
                new String[] { "image" }, new int[] { R.id.image });
        view.setAdapter(simpleAdapter);
        view.setNumColumns(8);
        view.setBackgroundColor(Color.WHITE);
        view.setHorizontalSpacing(1);
        view.setVerticalSpacing(1);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setGravity(Gravity.CENTER);
        return view;
    }



    private void initview() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (TextUtils.equals(type,"3")){
            Log.i("TAG","type");
            title.setText("班级通知");
        }else if (TextUtils.equals(type,"4")){
            title.setText("学校通知");
        }
        String shoolname=intent.getStringExtra("infoheadname");
        String head=intent.getStringExtra("infotitle");
        String time=intent.getStringExtra("infotiem");
        relaid=intent.getIntExtra("inforesid",0);

        infohead.setText(shoolname);
        infocontent.setText(head);
        infotime.setText(time);
    }
    private void initcomemtlist() {
        RequestParams requestParams =new RequestParams(projecturl.commenturl);
        requestParams.addBodyParameter("v","1");
        requestParams.addBodyParameter("type",type);
        requestParams.addBodyParameter("relativeId",String.valueOf(relaid));
        requestParams.addBodyParameter("psize","5");
        requestParams.addBodyParameter("lastCommentId","999999999999999999l");
        requestParams.addBodyParameter("imei",imei);
        x.http().get(requestParams,new MyCallBack(){
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.i("TAG","commetlist"+s);
                CommentListResult commenlistresultinfo = gson.fromJson(s, CommentListResult.class);
                int res = commenlistresultinfo.getHead().getCode();
                if (res == 200) {
                    hasMore = commenlistresultinfo.getData().isHasMore();
                    lists.clear();
                    for (CommentInfo info : commenlistresultinfo.getData().getComment()) {
                        String time = DateUtils.datetimeFormatter(new Date(Long.parseLong(info.getCreate_time())));
                        lists.add(new CommentBean(info.getUser_id(), info.getId(), relaid + "", info
                                .getUser_name(), time, info.getContent(), info.getHead_img_url()));
                    }
                    initlistview();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                Log.i("TAG","commlisterror"+throwable.getMessage());
            }
        });
    }

    private void initlistview() {
        infolistiview.removeAllViews();
        Log.i("TAG","lists.size"+lists.size());
        	for (int i = 0; i < lists.size(); i++) {
                View view=LayoutInflater.from(InfoDetailActivity.this).inflate(R.layout.syllabus_comment_item,null);
                ImageView headphoto = (ImageView) view.findViewById(R.id.headphoto);
                TextView sendtime = (TextView) view.findViewById(R.id.sendtime);
                TextView username = (TextView) view.findViewById(R.id.username);
                TextView content = (TextView) view.findViewById(R.id.content);

                sendtime.setText(lists.get(i).getTime());
                username.setText(lists.get(i).getWho());
                content.setText(lists.get(i).getContent());
                x.image().bind(headphoto,lists.get(i).getImg_url());
                infolistiview.addView(view);

        	}
    }

    private void initdata() {
        imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        RequestParams requestParams=new RequestParams(projecturl.commentnumurl);
        requestParams.addBodyParameter("v","1");

        requestParams.addBodyParameter("type",type);
        requestParams.addBodyParameter("relativeTd",String.valueOf(relaid));
        requestParams.addBodyParameter("imei",imei);
        x.http().get(requestParams,new MyCallBack(){
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                 gson=new Gson();
                CommentNumResult conmentresultinfo = gson.fromJson(s, CommentNumResult.class);
                int code = conmentresultinfo.getHead().getCode();
                if (code==200){
                    commentnumber.setText("评论"+"("+conmentresultinfo.getData().getNum()+")");
                    Log.i("TAG","评论"+conmentresultinfo.getData().getNum());
                }
            }
            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                Log.i("TAG","error"+throwable.getMessage());
            }
        });
    }

}
