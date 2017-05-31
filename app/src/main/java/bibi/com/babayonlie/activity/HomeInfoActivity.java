package bibi.com.babayonlie.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.adapter.InfoAdapter;
import bibi.com.babayonlie.application.Constant;
import bibi.com.babayonlie.bean.AnnoumentInfo;
import bibi.com.babayonlie.bean.EntryResultChildren;
import bibi.com.babayonlie.bean.EntryResultData;
import bibi.com.babayonlie.bean.KnowledgeResult;
import bibi.com.babayonlie.bean.NoticeInfo;
import bibi.com.babayonlie.bean.NotifyItem;
import bibi.com.babayonlie.projecturl.projecturl;
import bibi.com.babayonlie.utils.DateUtils;
import bibi.com.babayonlie.widget.MyCallBack;

/**
 * Created by bibinet on 2016/11/30.
 */
@ContentView(R.layout.activity_homeinfo)
public class HomeInfoActivity extends  BaseActivity{
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.title_imageleft)
    private ImageView imageleft;
    @ViewInject(R.id.infolistview)
    private ListView infolistiview;
    @ViewInject(R.id.classinfo)
    private Button classinfo;
    @ViewInject(R.id.schoolinfo)
    private Button schoolinfo;
    private List<NotifyItem> lists;
    private Set<Integer> schoolsid = new HashSet<Integer>();
    private Set<Integer> classesid = new HashSet<Integer>();
    private Map<Integer, String> classesName = new HashMap<Integer, String>();
    private boolean hasMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initclassinfodata();
        setlistioner();
    }

    private void setlistioner() {
        classinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classinfo.setSelected(true);
                schoolinfo.setSelected(false);

                initclassinfodata();
            }
        });
        schoolinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classinfo.setSelected(false);
                schoolinfo.setSelected(true);
                for (Integer schoolid : schoolsid) {
                    initschooldata(schoolid);
                }
            }
        });
    }

    private void initschooldata(int schoolid) {
        lists=new ArrayList<>();
        String imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        RequestParams requestParams=new RequestParams(projecturl.schoolinfourl);
        requestParams.addBodyParameter("schoolId",String.valueOf(schoolid));
        requestParams.addBodyParameter("imei",imei);
        requestParams.addBodyParameter("v","1");
        x.http().get(requestParams,new MyCallBack(){
            @Override
            public void onSuccess(String s) {
                Log.i("TAG","homeinfo"+s);
                super.onSuccess(s);
                Gson gson=new Gson();
                KnowledgeResult konresultinfo = gson.fromJson(s, KnowledgeResult.class);
                if(konresultinfo.getHead().getCode()==200){
                    hasMore = konresultinfo.getData().isHasMore();
                    hasMore = konresultinfo.getData().isHasMore();
                    for (AnnoumentInfo info : konresultinfo.getData().getAnnoument()) {
                        lists.add(new NotifyItem(info.getId(), info.getUrl(), info.getPicUrl(), info.getTitle(), info
                                .getSchoolId() + "", 2, DateUtils.datetimeFormatter(new Date(info.getAnnmentTime())),
                                info.getContent()));
                    }
                    InfoAdapter adapter = new InfoAdapter(HomeInfoActivity.this, lists);
                    infolistiview.setAdapter(adapter);
                }
                InfoAdapter adapter=new InfoAdapter(HomeInfoActivity.this,lists);
                infolistiview.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                Log.i("TAG","homeinfo"+throwable.getMessage());
            }
        });
    }




    private void initview() {
        title.setText("通知");
        imageleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        classinfo.setSelected(true);
    }

    private void initclassinfodata() {
        if(Constant.logdata==null){
            return;
        }
        Gson gson=new Gson();
        EntryResultData entryresultinfo = gson.fromJson(Constant.logdata, EntryResultData.class);
        for (EntryResultChildren child : entryresultinfo.getChildren()) {
            classesid.add(child.getClassId());
            classesName.put(child.getClassId(), child.getClazz().getName());
            schoolsid.add(child.getSchool().getId());
        }

        for (Integer classid : classesid) {
            loadClassNotify(classid);
        }

    }
    private void loadClassNotify(Integer classid) {
        lists=new ArrayList<>();
        String imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        RequestParams requestParams=new RequestParams(projecturl.homeinfourl);
        requestParams.addBodyParameter("v","1");
        requestParams.addBodyParameter("classId",String.valueOf(classid));
        requestParams.addBodyParameter("imei",imei);
        x.http().get(requestParams,new MyCallBack(){
            @Override
            public void onSuccess(String s) {
                Log.i("TAG","homeinfo"+s);
                super.onSuccess(s);
                Gson gson=new Gson();
                KnowledgeResult konresultinfo = gson.fromJson(s, KnowledgeResult.class);
                if(konresultinfo.getHead().getCode()==200){
                    hasMore = konresultinfo.getData().isHasMore();
                    for (NoticeInfo info : konresultinfo.getData().getNotice()) {
                        lists.add(new NotifyItem(info.getId(), info.getUrl(), info.getPicUrl(), info.getTitle(),
                                classesName.get(info.getClassId()), 1, DateUtils.datetimeFormatter(new Date(info
                                .getNoticeTime())), info.getContent()));
                    }
                }
                InfoAdapter adapter=new InfoAdapter(HomeInfoActivity.this,lists);
                infolistiview.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
                Log.i("TAG","homeinfo"+throwable.getMessage());
            }
        });
    }

}
