package bibi.com.babayonlie.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.bean.ImageInfo;
import bibi.com.babayonlie.bean.KnowledgeBean;

/**
 * Created by bibinet on 2016/12/6.
 */
public class ClassFicationAdapter extends BaseAdapter {
    private Context context;
    private List<KnowledgeBean> lits=new ArrayList<>();

    public ClassFicationAdapter(Context context, List<KnowledgeBean> lits) {
        this.context = context;
        this.lits = lits;
    }

    @Override
    public int getCount() {
        return lits.size();
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
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_classfication,null);
            viewHolder.classficationphoto=(ImageView)convertView.findViewById(R.id.classficationphoto);
            viewHolder.classficationcontent=(TextView)convertView.findViewById(R.id.classficationcontent);
            viewHolder.classficationtime=(TextView)convertView.findViewById(R.id.classficationtime);
            viewHolder.classficationevalute=(TextView)convertView.findViewById(R.id.classficationevaluate);
            viewHolder.classficationtitle=(TextView)convertView.findViewById(R.id.classficationtitle);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        KnowledgeBean info = lits.get(position);
        String imageurl = info.getImageurl();
        Log.i("TAG","imageurl"+imageurl);
        Gson gson=new Gson();
        ImageInfo iamgeinfo = gson.fromJson(imageurl, ImageInfo.class);
        x.image().bind(viewHolder.classficationphoto,iamgeinfo.getUrl());
        viewHolder.classficationtitle.setText(info.getTitle());
        viewHolder.classficationevalute.setText(String.valueOf(info.getCommentNum()));
        viewHolder.classficationcontent.setText(info.getContent());
        viewHolder.classficationtime.setText(info.getTime());

        return convertView;
    }

    class ViewHolder{
    ImageView classficationphoto;
        TextView classficationtitle,classficationcontent,classficationtime,classficationevalute;
    }
}
