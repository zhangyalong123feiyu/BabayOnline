package bibi.com.babayonlie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import bibi.com.babayonlie.adapter.ClassFicationAdapter;
import bibi.com.babayonlie.bean.KnowledgeBean;
import bibi.com.babayonlie.bean.KnowledgeInfo;
import bibi.com.babayonlie.bean.KnowledgeResult;
import bibi.com.babayonlie.projecturl.projecturl;
import bibi.com.babayonlie.utils.DateUtils;
import bibi.com.babayonlie.widget.MyCallBack;

/**
 * Created by bibinet on 2016/12/6.
 */
@ContentView(R.layout.activity_classfication)
public class ClassfiCationActivity  extends  BaseActivity{
    @ViewInject(R.id.title)
    private TextView titile;
    @ViewInject(R.id.title_imageleft)
    private ImageView imageleft;
    @ViewInject(R.id.swiperefesh)
    private SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.classficationlistview)
    private ListView classficationlistview;
    private String type;
    private List<KnowledgeBean> lists=new ArrayList<>();
    private boolean isfristloade=true;
    private ClassFicationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initdata();
    }

    private void initview() {
        Intent intent=getIntent();
         type = intent.getStringExtra("type");
        if (TextUtils.equals(type,"1")){
            titile.setText("政策法规");
        }else if (TextUtils.equals(type,"3")){
            titile.setText("日常护理");
        }else if (TextUtils.equals(type,"4")){
            titile.setText("教育措施");
        }else if (TextUtils.equals(type,"5")){
            titile.setText("家长读物");
        }
        imageleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initdata() {
        String imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        RequestParams requestParams=new RequestParams(projecturl.classficationurl);
        requestParams.addBodyParameter("v","1");
        requestParams.addBodyParameter("type",type);
        requestParams.addBodyParameter("idList",imei);
        requestParams.addBodyParameter("imei",imei);
        x.http().get(requestParams,new MyCallBack(){
            @Override
            public void onSuccess(String s) {
                Log.i("TAG","classfiacaton"+s);
                super.onSuccess(s);
                Gson gson=new Gson();
                KnowledgeResult knowledgeresultinfo = gson.fromJson(s, KnowledgeResult.class);
                int code = knowledgeresultinfo.getHead().getCode();
                if (code==200){
                    for (KnowledgeInfo info : knowledgeresultinfo.getData().getKnowledage()) {
                        lists.add(new KnowledgeBean(info.getId(), info.getUrl(), info.getPicUrl(), info.getTitle(),
                                info.getDescription(), DateUtils.datetimeFormatter(new Date(info.getUpdateTime())),
                                titile.getText().toString()));

                        if (isfristloade){
                             adapter=new ClassFicationAdapter(ClassfiCationActivity.this,lists);
                            classficationlistview.setAdapter(adapter);
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                Log.i("TAG","classfiacaton"+throwable.getMessage());
            }
        });
    }

}
