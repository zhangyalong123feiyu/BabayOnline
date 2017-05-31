package bibi.com.babayonlie.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.activity.ArticleActivity;
import bibi.com.babayonlie.bean.ImageInfo;
import bibi.com.babayonlie.bean.KnowledgeInfo;

/**
 * Created by bibinet on 2016/11/29.
 */
public class ArtcleAdapter extends BaseAdapter {
    private Context context;
    private List<KnowledgeInfo> infos;

    public ArtcleAdapter(Context context, List<KnowledgeInfo> infos) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;
        if (convertView==null){
            viewholder=new ViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.item_newacticle,null);
            viewholder.imageView= (ImageView) convertView.findViewById(R.id.articlepic);
            viewholder.title=(TextView)convertView.findViewById(R.id.articletitile);
            viewholder.content=(TextView)convertView.findViewById(R.id.content);
            viewholder.leavcontent=(TextView)convertView.findViewById(R.id.leavecount);
            viewholder.time=(TextView)convertView.findViewById(R.id.time);
            convertView.setTag(viewholder);
        }else{
            viewholder= (ViewHolder) convertView.getTag();
        }
        viewholder.title.setText(infos.get(position).getTitle());
        viewholder.content.setText(infos.get(position).getDescription());
        viewholder.leavcontent.setText(String.valueOf(infos.get(position).getType()));
        viewholder.time.setText(String.valueOf(infos.get(position).getCreateTime()));

        String imageurl = infos.get(position).getPicUrl();

        Gson gson=new Gson();
        ImageInfo iamgeinfo = gson.fromJson(imageurl, ImageInfo.class);
        ImageOptions options=new ImageOptions.Builder().setFailureDrawableId(R.drawable.logo_launcher).setRadius(5).build();
        x.image().bind(viewholder.imageView,iamgeinfo.getUrl(),options);
        Log.i("TAG","lists"+infos.toString());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ArticleActivity.class);
                intent.putExtra("articlecontent",infos.get(position).getDescription());
                intent.putExtra("articleurl",infos.get(position).getUrl());
                intent.putExtra("articletype",infos.get(position).getType());
                intent.putExtra("articletime",String.valueOf(infos.get(position).getCreateTime()));
                (context).startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView title,content,time,leavcontent;
    }
}
