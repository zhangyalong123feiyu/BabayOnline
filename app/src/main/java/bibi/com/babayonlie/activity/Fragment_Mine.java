package bibi.com.babayonlie.activity;


import android.content.Intent;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.application.Constant;
import bibi.com.babayonlie.bean.EntryResultData;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Mine extends Fragment {


    private View view;
    private RelativeLayout login;
    private TextView username;
    private TextView signature;
    private ImageView headphoto;
    private RelativeLayout minedata;

    public Fragment_Mine() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment__mine, container, false);
        initview();
        setlistioner();
        return view;
    }

    private void initview() {
        login=(RelativeLayout)view.findViewById(R.id.login);
        username=(TextView)view.findViewById(R.id.username);
        signature=(TextView)view.findViewById(R.id.signature);
        headphoto=(ImageView)view.findViewById(R.id.headphoto);
        minedata=(RelativeLayout)view.findViewById(R.id.minedata);
    }
    private void setlistioner() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.logdata==null){
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }
            }
        });
        minedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MineDataActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Constant.logdata!=null){
            Gson gson=new Gson();
            EntryResultData entryresultinfo = gson.fromJson(Constant.logdata, EntryResultData.class);
            String headimageurl = entryresultinfo.getParent().getHeadImg();
            ImageOptions options=new ImageOptions.Builder().setCircular(true).build();
            x.image().bind(headphoto,headimageurl,options);
            username.setText(entryresultinfo.getParent().getName());
            if (entryresultinfo.getParent().getSignature()!=null){
                signature.setText(entryresultinfo.getParent().getSignature());
            }
            minedata.setVisibility(View.VISIBLE);
        }
    }


}
