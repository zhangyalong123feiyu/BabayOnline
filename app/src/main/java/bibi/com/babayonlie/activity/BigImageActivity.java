package bibi.com.babayonlie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import bibi.com.babayonlie.R;

/**
 * Created by bibinet on 2016/12/2.
 */
@ContentView(R.layout.activity_bigimage)
public class BigImageActivity extends BaseActivity {
    @ViewInject(R.id.bigimageviewpager)
    private ViewPager bigimageviewpager;
    @ViewInject(R.id.picpoition)
   private TextView picposion;
    private String[] picsurl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initdata();
    }

    private void initdata() {
        Intent intent = getIntent();
         picsurl = intent.getStringArrayExtra("pics");
        bigimageviewpager.setAdapter(new Myadapter());
    }

    private void initview() {
        bigimageviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                picposion.setText((position+1)+"/"+picsurl.length);
            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class Myadapter extends PagerAdapter{

        @Override
        public int getCount() {
            return picsurl.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv=new ImageView(BigImageActivity.this);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            x.image().bind(iv,picsurl[position]);
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           // super.destroyItem(container, position, object);
            container.removeView((View)object);
            object=null;
        }
    }
}
