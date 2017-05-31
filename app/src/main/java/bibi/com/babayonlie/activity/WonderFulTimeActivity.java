package bibi.com.babayonlie.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
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
import java.util.Collections;
import java.util.List;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.adapter.WonderFulTimeAdapter;
import bibi.com.babayonlie.application.Constant;
import bibi.com.babayonlie.bean.EntryResultChildren;
import bibi.com.babayonlie.bean.EntryResultData;
import bibi.com.babayonlie.bean.WonderfulInfo;
import bibi.com.babayonlie.bean.WonderfulResult;
import bibi.com.babayonlie.projecturl.projecturl;
import bibi.com.babayonlie.widget.MyCallBack;

/**
 * Created by bibinet on 2016/11/30.
 */
@ContentView(R.layout.activity_wonderfultime)
public class WonderFulTimeActivity extends BaseActivity {
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.title_imageleft)
    private ImageView imageleft;
    @ViewInject(R.id.wonderfultimelistview)
    private ListView woderlistview;
    private Gson gson;
    private List<Integer> classesId = new ArrayList<>();
    private List<Data> datas = new ArrayList<>();
    private List<WonderfulInfo> infos = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initdata();
    }

    private void initdata() {
        if (Constant.logdata==null){
            return;
        }
        gson=new Gson();
        EntryResultData endtryresultinfo = gson.fromJson(Constant.logdata, EntryResultData.class);
        for (EntryResultChildren child : endtryresultinfo.getChildren()) {
            if (classesId.contains(child.getClassId())) {
                continue;
            } else {
                classesId.add(child.getClassId());
                datas.add(new Data(child.getClassId(), child.getSchool().getId()));
            }



        }

        for(Data data : datas){
            String imei=((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getDeviceId();
            RequestParams requestParams=new RequestParams(projecturl.wonderfulurl);
            requestParams.addBodyParameter("v","1");
            requestParams.addBodyParameter("schoolId",String.valueOf(data.schoolId));
            requestParams.addBodyParameter("classId",String.valueOf(data.classId));
            requestParams.addBodyParameter("state","1");
            requestParams.addBodyParameter("imei",imei);
            x.http().get(requestParams,new MyCallBack(){
                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);
                    WonderfulResult woderfulresultinfo = gson.fromJson(s, WonderfulResult.class);
                    int code = woderfulresultinfo.getHead().getCode();
               if (code==200){
                   infos.addAll(woderfulresultinfo.getData().getNotice());
                   Collections.sort(infos);
                   WonderFulTimeAdapter adapter=new WonderFulTimeAdapter(WonderFulTimeActivity.this,infos);
                   woderlistview.setAdapter(adapter);

               }
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    super.onError(throwable, b);
                }

            });
        }

    }

    private void initview() {
        title.setText("精彩瞬间");
        imageleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class Data {
        int classId;
        int schoolId;

        public Data(int classId, int schoolId) {
            this.classId = classId;
            this.schoolId = schoolId;
        }
    }

}
