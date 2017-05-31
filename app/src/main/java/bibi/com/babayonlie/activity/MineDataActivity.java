package bibi.com.babayonlie.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.application.Constant;
import bibi.com.babayonlie.bean.EntryResultData;
import bibi.com.babayonlie.widget.MyCallBack;

/**
 * Created by bibinet on 2016-12-7.
 */
@ContentView(R.layout.activity_minedata)
public class MineDataActivity extends BaseActivity {
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.title_imageleft)
    private ImageView imageleft;
    @ViewInject(R.id.headphoto)
    private ImageView headphoto;
    @ViewInject(R.id.minephoto)
    private RelativeLayout minephoto;
    @ViewInject(R.id.minesex)
    private RelativeLayout minesex;
    @ViewInject(R.id.mineidetitynumber)
    private RelativeLayout mineidetitynumber;
    @ViewInject(R.id.minetelenumber)
    private RelativeLayout minetelenumber;
    @ViewInject(R.id.minemail)
    private RelativeLayout minemail;
    @ViewInject(R.id.mineaddress)
    private RelativeLayout mineaddress;
    @ViewInject(R.id.minesignatrue)
    private RelativeLayout minesignatrue;
    @ViewInject(R.id.username)
    private TextView username;
    @ViewInject(R.id.usersex)
    private TextView usersex;
    @ViewInject(R.id.useridtitynumber)
    private TextView useridtitynumber;
    @ViewInject(R.id.usertelenumber)
    private TextView usertelenumber;
    @ViewInject(R.id.useremail)
    private TextView useremail;
    @ViewInject(R.id.useraddress)
    private TextView useraddress;
    @ViewInject(R.id.usersignatrue)
    private TextView usersignatrue;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initdata();
        setlistioner();
    }


    private void initview() {
        title.setText("我的资料");
        imageleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initdata() {
        if (Constant.logdata==null){
            return;
        }
        Gson gson=new Gson();
        EntryResultData entryresultinfo = gson.fromJson(Constant.logdata, EntryResultData.class);
        String imageurl = entryresultinfo.getParent().getHeadImg();
        ImageOptions options=new ImageOptions.Builder().setCircular(true).build();
        x.image().bind(headphoto,imageurl,options);

        username.setText(entryresultinfo.getParent().getName());
        usersex.setText(String.valueOf(entryresultinfo.getParent().getSex()));
        usertelenumber.setText(entryresultinfo.getParent().getPhoneNo());
        useremail.setText(entryresultinfo.getParent().getEmai());
        usersignatrue.setText(entryresultinfo.getParent().getSignature());
        useridtitynumber.setText(entryresultinfo.getParent().getCardNo());
    }
    private void setlistioner() {
        minephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(MineDataActivity.this).inflate(R.layout.item_selectphoto, null);
                AlertDialog.Builder builder=new AlertDialog.Builder( MineDataActivity.this);
                dialog=builder.create();

                dialog.setView(view);
                dialog.show();
            }
        });
    }


}
