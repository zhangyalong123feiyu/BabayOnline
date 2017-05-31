package bibi.com.babayonlie.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.adapter.ArtcleAdapter;
import bibi.com.babayonlie.bean.KnowledgeInfo;
import bibi.com.babayonlie.bean.KnowledgeResult;
import bibi.com.babayonlie.projecturl.projecturl;
import bibi.com.babayonlie.widget.MyCallBack;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Children extends Fragment {


    private View view;
    private ListView article_listview;
    private RelativeLayout nurse;
    private RelativeLayout measure;
    private RelativeLayout reading;
    private RelativeLayout policy;
    private List<KnowledgeInfo> articles;
    private ScrollView scrollview;
    private String type;

    public Fragment_Children() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment__children, container, false);
        initview();
        initdata();
        setlistioner();

        return view ;
    }

    private void initview() {
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("育儿");
        ImageView imageleft = (ImageView) view.findViewById(R.id.title_imageleft);
        imageleft.setVisibility(View.GONE);
         policy=(RelativeLayout)view.findViewById(R.id.policy);
         nurse=(RelativeLayout)view.findViewById(R.id.nurse);
         measure=(RelativeLayout)view.findViewById(R.id.measure);
         reading=(RelativeLayout)view.findViewById(R.id.reading);
        article_listview=(ListView)view.findViewById(R.id.currentarticlelistview);
        scrollview=(ScrollView)view.findViewById(R.id.scrollView);


    }
    private void initdata() {
        RequestParams requestParams=new RequestParams(projecturl.articleurl);
        String imei = ((TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        requestParams.addBodyParameter("v","1");
        requestParams.addBodyParameter("imei",imei);

        x.http().get(requestParams,new MyCallBack(){
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Gson gson=new Gson();
                KnowledgeResult knowledgeresultinfo = gson.fromJson(s, KnowledgeResult.class);
                articles=knowledgeresultinfo.getData().getKnowledage();
                article_listview.setAdapter(new ArtcleAdapter(getActivity(),articles));
            }
            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });

    }

    private void setlistioner() {
    policy.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            type="1";
            Intent intent=new Intent(getActivity(),ClassfiCationActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
        }
    });
    nurse.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            type="3";
            Intent intent=new Intent(getActivity(),ClassfiCationActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
        }
    });
    measure.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            type="4";
            Intent intent=new Intent(getActivity(),ClassfiCationActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
        }
    });
    reading.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            type="5";
            Intent intent=new Intent(getActivity(),ClassfiCationActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
        }
    });
    }

}
