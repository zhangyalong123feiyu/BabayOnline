package bibi.com.babayonlie.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.application.Constant;
import bibi.com.babayonlie.bean.EntryResult;
import bibi.com.babayonlie.projecturl.projecturl;
import bibi.com.babayonlie.utils.SharePreferUtil;
import bibi.com.babayonlie.widget.MyCallBack;
import bibi.com.babayonlie.widget.MyTextWatcher;

/**
 * Created by bibinet on 2016/11/29.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.title_imageleft)
    private ImageView imageleft;
    @ViewInject(R.id.headphoto)
    private ImageView headphoto;
    @ViewInject(R.id.headphotobackground)
    private ImageView headphotoback;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.accoutn)
    private EditText edit_account;
    @ViewInject(R.id.password)
    private  EditText edit_password;
    @ViewInject(R.id.rememberpasswrod)
    private CheckBox rememberpasswrod;
    @ViewInject(R.id.login)
    private Button login;
    private ProgressDialog prodialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        setlistioner();
    }

    private void setlistioner() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acount = edit_account.getText().toString().trim();
                String password = edit_password.getText().toString().trim();
                boolean isremember = rememberpasswrod.isChecked();
                String imei = ((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getDeviceId();
                login(acount,password,isremember,imei);
            }
        });
    }

    private void login(final String acount, final String password, final boolean isremember, String imei) {
        prodialog.show();
        RequestParams requestParams =new RequestParams(projecturl.loginurl);
        requestParams.addBodyParameter("v","1");
        requestParams.addBodyParameter("phoneNo",acount);
        requestParams.addBodyParameter("passwd",password);
        requestParams.addBodyParameter("imei",imei);

        x.http().post(requestParams,new MyCallBack(){
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Gson gson=new Gson();
                EntryResult resultinfo = gson.fromJson(s, EntryResult.class);
                int code=resultinfo.getHead().getCode();
                switch (code) {
                		case 200:
                			if (resultinfo.getData()==null){
                                return;
                            }
                            SharePreferUtil sharpre = SharePreferUtil.getSharePreferUtil(LoginActivity.this);
                            sharpre.putString("usertoken",resultinfo.getData().getUserToken());
                            if (isremember){
                                sharpre.saveuserinfo(acount,password,true,true);
                            }else {
                                sharpre.saveuserinfo(acount,password,false,true);
                            }
                            SharedPreferences sharedPreferences=getSharedPreferences("loginresult",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            String logindata = gson.toJson(resultinfo.getData());
                            Constant.logdata=logindata;
                            editor.putString("logindata",logindata);
                            editor.commit();
                            prodialog.dismiss();

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                			break;
                		default:
                			break;
                		}
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });
    }

    private void initview() {
        imageleft.setVisibility(View.VISIBLE);
        prodialog=new ProgressDialog(LoginActivity.this);
        prodialog.setTitle("登录");
        prodialog.setMessage("正在登录。。。");

        imageleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        edit_account.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                loadeface();
            }
        });
    }

    private void loadeface() {
        String acount = edit_account.getText().toString().trim();
        RequestParams params=new RequestParams(projecturl.loadeuserface);
        params.addBodyParameter("v","1");
        params.addBodyParameter("phoneNo",acount);
        x.http().post(params,new MyCallBack(){
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Gson gson=new Gson();
                EntryResult resultinfo = gson.fromJson(s, EntryResult.class);
                int code = resultinfo.getHead().getCode();
                if (code==200){
                    String imageurl = resultinfo.getData().getParent().getHeadImg();
                    ImageOptions options=new ImageOptions.Builder().setCircular(true).setUseMemCache(true).build();
                    x.image().bind(headphoto,imageurl,options);
                    headphotoback.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });
    }
}
