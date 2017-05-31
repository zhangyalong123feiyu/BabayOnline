package bibi.com.babayonlie.application;

import android.app.Application;

import org.xutils.x;

/**
 * Created by bibinet on 2016/11/28.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
