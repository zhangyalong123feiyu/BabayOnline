package bibi.com.babayonlie.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.bean.VersionData;
import bibi.com.babayonlie.bean.VersionResult;
import bibi.com.babayonlie.projecturl.projecturl;
import bibi.com.babayonlie.utils.SharePreferUtil;
import bibi.com.babayonlie.utils.XUtil;
import bibi.com.babayonlie.widget.MyCallBack;
import bibi.com.babayonlie.widget.MyProgressCallBack;

/**
 * Created by bibinet on 2016/11/28.
 */
public class SplashActivity extends AppCompatActivity {
    private boolean islogin;
    private ProgressDialog progressDialog;
    private SharePreferUtil shareutil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(R.drawable.logo_start);
         shareutil = SharePreferUtil.getSharePreferUtil(SplashActivity.this);
        islogin=shareutil.getboolean("islogin",false);
        setContentView(imageView);
        initanimation(imageView);
    }

    private void initanimation(ImageView imageView) {
        Animation animation = new AlphaAnimation(0.7f, 1.0f);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                checkversion();

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                autoLogin();
                if (islogin){
                    islogin=false;
                    shareutil.putboolean("islogin",islogin);
                    startActivity(new Intent(SplashActivity.this, TouristsActivity.class));
                }else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animation);
        animation.start();
    }

    private void checkversion() {
        String imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        RequestParams requestParams = new RequestParams(projecturl.checkversion);
        requestParams.addBodyParameter("v", "1");
        requestParams.addBodyParameter("platform", "android");
        requestParams.addBodyParameter("imei", imei);
        x.http().get(requestParams, new MyCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Gson gson = new Gson();
                final VersionResult resultinfo = gson.fromJson(s, VersionResult.class);
                int code = resultinfo.getHead().getCode();
                if (code == 200) {
                    if (resultinfo.getData().getState() == 2) {
                        islogin = false;

                        // 得到一个Builder对象
                        new AlertDialog.Builder(SplashActivity.this).setCancelable(false)
                                // 设置对话框标题
                                .setTitle("版本更新")
                                // 设置文本输出内容
                                .setMessage(resultinfo.getData().getChanges())
                                // 设置下方按钮及其监听事件
                                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                        updateVersion(resultinfo.getData().getUrl(), true);
                                    }
                                }).create().show();

                    } else if (resultinfo.getData().getState() == 1) {
                        islogin = false;

                        // 得到一个Builder对象
                        new AlertDialog.Builder(SplashActivity.this).setCancelable(false)
                                // 设置对话框标题
                                .setTitle("版本更新")
                                // 设置文本输出内容
                                .setMessage(resultinfo.getData().getChanges())
                                // 设置下方按钮及其监听事件
                                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                        autoLogin();



                                    }
                                }).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                                updateVersion(resultinfo.getData().getUrl(), false);
                            }
                        }).show();
                    }
                } else {

                }

            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });
    }

    private void autoLogin() {


    }

    private void updateVersion(String url, boolean b) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BaBayOnlie" + System.currentTimeMillis() + ".apk";
            RequestParams requestParams = new RequestParams(url);
            requestParams.setAutoRename(true);//断点下载
            requestParams.setSaveFilePath(filepath);
            x.http().get(requestParams, new MyProgressCallBack() {
                @Override
                public void onStarted() {
                    super.onStarted();
                    progressDialog = new ProgressDialog(SplashActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置为水平进行条
                    progressDialog.setMessage("正在下载中...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgress(0);
                    progressDialog.show();
                }

                @Override
                public void onLoading(long l, long l1, boolean b) {
                    super.onLoading(l, l1, b);
                    progressDialog.setMax((int) l);
                    progressDialog.setProgress((int) l1);

                }

                @Override
                public void onSuccess(File file) {
                    super.onSuccess(file);
                    progressDialog.dismiss();
                    installApk(file);
                }

                @Override
                public void onError(Throwable throwable, boolean b) {
                    super.onError(throwable, b);
                }
            });

        } else {
            Toast.makeText(SplashActivity.this, "没有存储卡", Toast.LENGTH_SHORT).show();
        }

    }

    private void installApk(File t) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
        startActivity(intent);
        startActivity(new Intent(SplashActivity.this,TouristsActivity.class));
    }

}
