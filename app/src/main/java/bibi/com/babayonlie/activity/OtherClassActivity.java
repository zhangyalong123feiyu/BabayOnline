package bibi.com.babayonlie.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import bibi.com.babayonlie.R;

/**
 * Created by bibinet on 2016/11/29.
 */
@ContentView(R.layout.activity_otherclass)
public class OtherClassActivity extends BaseActivity {
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.title_imageleft)
    private ImageView imageleft;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
    }

    private void initview() {
        title.setText("查看全部");
        imageleft.setVisibility(View.GONE);
    }
}
