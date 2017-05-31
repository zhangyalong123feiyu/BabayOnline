package bibi.com.babayonlie.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.activity.BigImageActivity;
import bibi.com.babayonlie.bean.WonderfulInfo;
import bibi.com.babayonlie.bean.WonderfulPicture;
import bibi.com.babayonlie.utils.DateUtils;

/**
 * Created by bibinet on 2016/12/2.
 */
public class WonderFulTimeAdapter extends BaseAdapter {
    private Context context;
    private List<WonderfulInfo> infos = new ArrayList<>();

    public WonderFulTimeAdapter(Context context, List<WonderfulInfo> infos) {
        this.context = context;
        this.infos = infos;
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_wonderftime, null);
            viewHodler.classphoto = (ImageView) convertView.findViewById(R.id.classphoto);
            viewHodler.classnumber = (TextView) convertView.findViewById(R.id.classnumber);
            viewHodler.classcontent = (TextView) convertView.findViewById(R.id.classcontent);
            viewHodler.classinfotime = (TextView) convertView.findViewById(R.id.classinfotime);
            viewHodler.picgridlayout = (GridLayout) convertView.findViewById(R.id.picgridlayout);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        Gson gson = new Gson();
        WonderfulPicture wonderfulpicinfo = gson.fromJson(infos.get(position).getPicUrl(), WonderfulPicture.class);
        String[] pics = null;
        try {
            String picStrs = infos.get(position).getImg();
            Log.d("TAG", "s1:" + picStrs);

            picStrs = picStrs.substring(1, picStrs.length() - 1);
            Log.d("TAG", "s2:" + picStrs);

            pics = picStrs.split(", ");
            List<String> strs = new ArrayList<String>();
            for (String string : pics) {
                Log.d("TAG", "s3:" + string);
                if (string == null || string.trim().equals("")) {
                    continue;
                }
                strs.add(string);
            }

            pics = new String[strs.size()];
            for (int i = 0; i < strs.size(); i++) {
                Log.d("TAG", "s4:" + strs.get(i));
                pics[i] = strs.get(i);
            }
            Log.d("TAG", "s5:" + pics.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageOptions options=new ImageOptions.Builder().setCircular(true).build();
        x.image().bind(viewHodler.classphoto, wonderfulpicinfo.getUrl(),options);
        addgridpic(viewHodler.picgridlayout,pics);
        viewHodler.classnumber.setText(infos.get(position).getClassName());
        viewHodler.classcontent.setText(infos.get(position).getDescription());
        viewHodler.classinfotime.setText(DateUtils.dateFormatter(new Date(infos.get(position).getCreateTime())));
        return convertView;
    }

    private void addgridpic(GridLayout gridLayout, final String[]pics) {
        gridLayout.removeAllViews();
        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_gridview_item, null);
            ImageView gridimage = (ImageView) view.findViewById(R.id.gridview_item_image);
            x.image().bind(gridimage, pics[i]);
            gridLayout.addView(view);

            gridLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,BigImageActivity.class);
                    intent.putExtra("pics",pics);
                    context.startActivity(intent);
                }
            });
        }

    }

    class ViewHodler {
        ImageView classphoto;
        TextView classnumber, classcontent, classinfotime;
        GridLayout picgridlayout;
    }
}
