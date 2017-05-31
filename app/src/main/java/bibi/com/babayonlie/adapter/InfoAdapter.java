package bibi.com.babayonlie.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.activity.InfoDetailActivity;
import bibi.com.babayonlie.bean.NotifyItem;

/**
 * Created by bibinet on 2016/11/30.
 */
public class InfoAdapter extends BaseAdapter {
    private Context context;
    private List<NotifyItem> lists = new ArrayList<NotifyItem>();

    public InfoAdapter(Context context, List<NotifyItem> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
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
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_info,null);
            viewHolder.classnumber= (TextView) convertView.findViewById(R.id.classnumber);
            viewHolder.classdescrp= (TextView) convertView.findViewById(R.id.infodescrp);
            viewHolder.classinfotime= (TextView) convertView.findViewById(R.id.infotime);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        String content = lists.get(position).getTitle();
        String time = lists.get(position).getTime();
        String id = lists.get(position).getRes();
        viewHolder.classnumber.setText(id);
        viewHolder.classinfotime.setText(time);
        viewHolder.classdescrp.setText(content);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG","onclick");
                Intent intent=new Intent(context,InfoDetailActivity.class);
                NotifyItem item = lists.get(position);
                if (item.getTypeRes()==1){
                    intent.putExtra("title","班级通知");
                    intent.putExtra("type","3");
                }else if (item.getTypeRes()==2){
                    intent.putExtra("title","学校通知");
                    intent.putExtra("type","4");
                }
                intent.putExtra("infoheadname",item.getRes());
                intent.putExtra("url",item.getUrl());
                intent.putExtra("infotitle",item.getTitle());
                intent.putExtra("infotiem",item.getTime());
                intent.putExtra("inforesid",item.getId());
                Log.i("TAG","onclick2");
                context.startActivity(intent);

            }
        });
        return convertView;
    }
    class  ViewHolder{
        TextView classnumber,classdescrp,classinfotime;
    }
}
