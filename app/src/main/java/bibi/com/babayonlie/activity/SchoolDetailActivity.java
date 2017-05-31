package bibi.com.babayonlie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import bibi.com.babayonlie.R;
import bibi.com.babayonlie.application.Constant;
import bibi.com.babayonlie.bean.CommentBean;
import bibi.com.babayonlie.bean.CommentInfo;
import bibi.com.babayonlie.bean.CommentListResult;
import bibi.com.babayonlie.bean.EntryResultData;
import bibi.com.babayonlie.bean.SchoolDetailResult;
import bibi.com.babayonlie.projecturl.projecturl;
import bibi.com.babayonlie.utils.ClipboardUtils;
import bibi.com.babayonlie.utils.DateUtils;
import bibi.com.babayonlie.utils.ExpressionUtil;
import bibi.com.babayonlie.widget.MyCallBack;

/**
 * Created by Lenovo_user on 2016/11/29.
 */
@ContentView(R.layout.activity_schooldetail)
public class SchoolDetailActivity extends  BaseActivity{
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.title_imageleft)
    private ImageView imageleft;
    @ViewInject(R.id.allbabayname)
    private TextView baby;
    @ViewInject(R.id.schoolname)
    private  TextView shcoolname;
    @ViewInject(R.id.schooladdress)
    private  TextView schooladdress;
    @ViewInject(R.id.allbabayname)
    private  TextView allbabayname;
    @ViewInject(R.id.schooltele)
    private  TextView schooltele;
    @ViewInject(R.id.schoolpic)
    private  ImageView schoolpic;
    @ViewInject(R.id.schoolcontent)
    private  TextView schoolcontent;
    @ViewInject(R.id.descrp)
    private  TextView descrip;
    @ViewInject(R.id.createtime)
    private  TextView createtime;

    @ViewInject(R.id.synopsis_comment_listview)
    private LinearLayout comment_listview;
    private String userid;
    private int schoolId;
    private boolean hasMore=false;
    private List<CommentBean> lists = new ArrayList<CommentBean>();
    private String imei;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initdata();
        initcomment();
    }

    private void initcomment() {
        RequestParams requestParams=new RequestParams(projecturl.schoolurl);
        requestParams.addBodyParameter("v","1");
        requestParams.addBodyParameter("relativeId",String.valueOf(schoolId));
        requestParams.addBodyParameter("psize","5");
        requestParams.addBodyParameter("lastCommentId","999999999999999999l");
        requestParams.addBodyParameter("imei",imei);

        x.http().get(requestParams,new MyCallBack(){
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Gson gson1=new Gson();
                  CommentListResult commentresultinfo = gson1.fromJson(s, CommentListResult.class);
                int res = commentresultinfo.getHead().getCode();
                if (res == 200) {
                    hasMore = commentresultinfo.getData().isHasMore();
                    lists.clear();
                    for (CommentInfo info : commentresultinfo.getData().getComment()) {
                        String time = DateUtils.datetimeFormatter(new Date(Long.parseLong(info.getCreate_time())));
                        lists.add(new CommentBean(info.getUser_id(), info.getId(), schoolId + "", info.getUser_name(),
                                time, info.getContent(), info.getHead_img_url()));
                    }
                    initlistview();
                }

            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });


    }

    private void initdata() {
        Gson gson=new Gson();
        if (Constant.logdata!=null){

            EntryResultData entryresultinfo = gson.fromJson(Constant.logdata, EntryResultData.class);
            userid=entryresultinfo.getParent().getId();
        }
        Intent intent = getIntent();
        schoolId = intent.getIntExtra("schoolId", -1);
        final String babyNames = intent.getStringExtra("babyNames");

        if (babyNames != null && !"".equals(babyNames)) {
            baby.setVisibility(View.VISIBLE);
            baby.setText(babyNames);
        } else {
            baby.setVisibility(View.GONE);
        }

         imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        RequestParams requestParams=new RequestParams(projecturl.schoolurl);
     /*   requestParams.addBodyParameter("v","1");
        requestParams.addBodyParameter("relativeId",String.valueOf(schoolId));
        requestParams.addBodyParameter("psize","5");
        requestParams.addBodyParameter("lastCommentId","999999999999999999l");
        requestParams.addBodyParameter("imei",imei);*/
        requestParams.addBodyParameter("v","1");
        requestParams.addBodyParameter("schoolId",String.valueOf(schoolId));
        requestParams.addBodyParameter("imei",imei);

        x.http().get(requestParams,new MyCallBack(){
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.i("TAG","成功1"+s);
                Gson gson1 = new Gson();

                SchoolDetailResult shcooldetailresultinfo = gson1.fromJson(s, SchoolDetailResult.class);
                shcoolname.setText(shcooldetailresultinfo.getData().getName());
                // establish.setText("成立时间：" +
                // object.getData().getEstaTime().substring(0, 4) + "年"
                // + object.getData().getEstaTime().substring(4, 6) + "月"
                // + object.getData().getEstaTime().substring(6, 8) + "日");
                createtime.setText("成立时间：" + shcooldetailresultinfo.getData().getEstaTime().substring(0, 4) + "-"
                        + shcooldetailresultinfo.getData().getEstaTime().substring(4, 6) + "-"
                        + shcooldetailresultinfo.getData().getEstaTime().substring(6, 8));
               // page.setText("1/1");
                schooladdress.setText(shcooldetailresultinfo.getData().getAddress());
                schooltele.setText(shcooldetailresultinfo.getData().getTelephone());
                descrip.setText(shcooldetailresultinfo.getData().getIdea());
                schoolcontent.setText(shcooldetailresultinfo.getData().getDescription());
                x.image().bind(schoolpic,shcooldetailresultinfo.getData().getPosterUrl().getUrl());
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                Log.i("TAG","失败1"+throwable.getMessage());
            }
        });

    }
    private void initlistview() {
        // TODO Auto-generated method stub
        comment_listview.removeAllViews();

        for (final CommentBean item : lists) {
            View convertView = LayoutInflater.from(this).inflate(R.layout.syllabus_comment_item, null);
            final ImageView image = (ImageView) convertView.findViewById(R.id.syllabus_comment_item_img);
            final TextView who = (TextView) convertView.findViewById(R.id.username);
            final TextView time = (TextView) convertView.findViewById(R.id.sendtime);
            final TextView content = (TextView) convertView.findViewById(R.id.content);
            final View editview = convertView.findViewById(R.id.syllabus_comment_item_editview);
            final TextView copy = (TextView) convertView.findViewById(R.id.syllabus_comment_item_copy);
            final TextView del = (TextView) convertView.findViewById(R.id.syllabus_comment_item_del);

            who.setText(item.getWho());
            time.setText(item.getTime());

            try {
                String zhengze = "e[0-9][0-9]{2}"; // 正则表达式，用来判断消息内是否有表情
                SpannableString spannableString = ExpressionUtil.getExpressionString(this, item.getContent(), zhengze);
                content.setText(spannableString);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                content.setText(item.getContent());
            }

            if (item.getImg_url() != null && !"".equals(item.getImg_url())) {
                //bitmapUtils.display(image, item.getImg_url());
            x.image().bind(image,item.getImg_url());
            }

            editview.setVisibility(View.GONE);
            if (item.getUserid().equals(userid)) {
                del.setVisibility(View.VISIBLE);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // editview.setVisibility(View.GONE);
                    initlistview();
                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                    editview.setVisibility(View.VISIBLE);
                    return true;
                }
            });

            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    editview.setVisibility(View.GONE);
                    ClipboardUtils.copy(content.getText().toString(), SchoolDetailActivity.this);
                }
            });
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    editview.setVisibility(View.GONE);
               //     delComment(item);
                }
            });

            comment_listview.addView(convertView);
        }

        if (hasMore) {
        /*    View v = LayoutInflater.from(this).inflate(R.layout.syllabus_comment_loadmore, null);
            Button loadmore = (Button) v.findViewById(R.id.syllabus_comment_loadmore);
            loadmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
            *//*        Intent intent = new Intent(SynopsisInfoActivity.this, CommentListActivity.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("relativeid", schoolId + "");
                    startActivity(intent);
                    overridePendingTransition(R.anim.tr_entry, R.anim.tr0);
                }
            });
            comment_listview.addView(v);*/
        }}

    private void initview() {
        title.setText("幼儿园详情");
        imageleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ViewCompat.setTransitionName(schoolpic, "123");
    }

}
