package bibi.com.babayonlie.activity;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.application.Constant;

public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.homepage)
    private RelativeLayout homepage;
    @ViewInject(R.id.child)
    private RelativeLayout child;
    @ViewInject(R.id.mine)
    private RelativeLayout mine;
    @ViewInject(R.id.maincontainer)
    private RelativeLayout container;
    private Fragment[] fragments=new Fragment[3];
    private RelativeLayout[] btns=new RelativeLayout[3];
    private int index;
    private int currentTabIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        SharedPreferences sharedPreferences=getSharedPreferences("loginresult",MODE_PRIVATE);
        Constant.logdata=sharedPreferences.getString("logindata",null);
       initview();
    }
    private void initview() {
        fragments[0]=new Fragement_Home();
        fragments[1]=new Fragment_Children();
        fragments[2]=new Fragment_Mine();

        btns[0]=homepage;
        btns[1]=child;
        btns[2]=mine;
        btns[0].setSelected(true);

        //getSupportFragmentManager().beginTransaction().add(R.id.maincontainer,fragments[0]).hide(fragments[0]).add(R.id.maincontainer,fragments[1]).hide(fragments[1]).
      //  add(R.id.maincontainer,fragments[2]).hide(fragments[2]).show(fragments[0]).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.maincontainer,fragments[0]).commit();
    }
    public void onclick(View v){
        switch (v.getId()) {
        		case R.id.homepage:
        			index=0;
        			break;
        		case R.id.child:
                    index=1;
        			break;
        		case R.id.mine:
                    index=2;
        			break;

        		default:
        			break;
        		}
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.maincontainer, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        btns[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        btns[index].setSelected(true);
        currentTabIndex = index;
    }
    }


