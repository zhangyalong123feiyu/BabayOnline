package bibi.com.babayonlie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import bibi.com.babayonlie.R;

/**
 * Created by bibinet on 2016/11/29.
 */
@ContentView(R.layout.activity_tourists)
public class TouristsActivity extends BaseActivity {
    @ViewInject(R.id.login)
    private Button login;
    @ViewInject(R.id.browse)
    private Button browse;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        setlistioner();
    }

    private void setlistioner() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TouristsActivity.this,LoginActivity.class));
            }
        });
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TouristsActivity.this,MainActivity.class));
            }
        });
    }
}
