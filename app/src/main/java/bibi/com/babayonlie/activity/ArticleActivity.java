package bibi.com.babayonlie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import bibi.com.babayonlie.R;

/**
 * Created by bibinet on 2016/11/29.
 */
@ContentView(R.layout.activity_article)
public class ArticleActivity extends  BaseActivity{
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.title_imageleft)
    private ImageView imageleft;
    @ViewInject(R.id.articlepic)
    private ImageView articlepic;
    @ViewInject(R.id.articlecomment)
    private TextView articlecomemt;
    @ViewInject(R.id.articlecontent)
    private TextView articlecontent;
    @ViewInject(R.id.articletime)
    private TextView articletime;
    @ViewInject(R.id.articlewebview)
    private WebView articlewebview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
    }

    private void initview() {
        Intent intent = getIntent();
        String content = intent.getStringExtra("articlecontent");
        String articleurl = intent.getStringExtra("articleurl");
        String type = intent.getStringExtra("articletype");
        String time = intent.getStringExtra("articletime");
        if (TextUtils.equals(type,"1")){
            title.setText("政策法规");
        }else if (TextUtils.equals(type,"3")){
            title.setText("日常护理");
        }else if (TextUtils.equals(type,"3")){
            title.setText("教育措施");
        }else if (TextUtils.equals(type,"3")){
            title.setText("家长读物");
        }
        articlecontent.setText(content);
        articletime.setText(time);
        Log.i("TAG","articletime"+time);
        MyViewClient viewclient=new MyViewClient();
        articlewebview.getSettings().setJavaScriptEnabled(true);
        articlewebview.getSettings().setSupportZoom(true);

        articlewebview.getSettings().setLoadWithOverviewMode(true);
        articlewebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        articlewebview.getSettings().setBuiltInZoomControls(true);
        articlewebview.loadUrl(articleurl);
        articlewebview.setWebViewClient(viewclient);

    }

    //Web视图
    private class MyViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    @Override
    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && articlewebview.canGoBack()) {
            articlewebview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        finish();
        return false;
    }
}
