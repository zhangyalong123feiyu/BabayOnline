package bibi.com.babayonlie.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

import bibi.com.babayonlie.R;
import bibi.com.babayonlie.utils.Date1Utils;

/**
 * Created by bibinet on 2016/11/30.
 */
@ContentView(R.layout.activity_safesend)
public class SafeSendActivity extends BaseActivity {
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.title_imageleft)
    private ImageView imageleft;
    private View week6;

    private View[] weeks = new View[6];
    private int[] weekIds = {R.id.week1, R.id.week2, R.id.week3, R.id.week4, R.id.week5, R.id.week6};

    private TextView[][] days = new TextView[6][7];
    private int[] daysId = {R.id.textday1, R.id.textday2, R.id.textday3, R.id.textday4, R.id.textday5, R.id.textday6, R.id.textday7};
    private Date todayDate=new Date();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        findCalendar();
        initCalander();
    }



    private void findCalendar() {
        for (int i = 0; i < 6; i++) {
            weeks[i] = findViewById(weekIds[i]);
            for (int j = 0; j < 7; j++) {
                days[i][j] = (TextView) weeks[i].findViewById(daysId[j]);
            }
        }
    }

    private void initCalander() {
        int day=1;
        int nextday=1;
        for (int i = 0; i < 6; i++) {
            //获取某月的1号是星期几
            int firstdayPosition=0;
            try {
                firstdayPosition = Date1Utils.getWeek(Date1Utils.parseFirstDate(todayDate));//判断这个月第一天的位置
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            int lastdayNo = Date1Utils.getDaysOfMonth(todayDate);//上个月一共有几天
            int backlastdayNo=Date1Utils.getDaysOfMonth(Date1Utils.monthOperation(todayDate, -1));
            for (int j = 0; j < 7; j++) {
                days[i][j].setClickable(false);
                days[i][j].setEnabled(false);
                if (i==0&&(j+1)<firstdayPosition) {//(i+0)==1保证是第一行
                    int backdayLastWeek=firstdayPosition-1;//上个月的最后一天是星期几
                    int lastday=backlastdayNo-(backdayLastWeek-(j+1));
                    days[i][j].setText(lastday+"");
                }else if(day>lastdayNo){//大于本月的最后一天
                    days[i][j].setText(nextday+"");
                    nextday++;
                }else{
                    days[i][j].setText(day+"");
                    if (day<Date1Utils.getDayofMonth(todayDate)) {
                        days[i][j].setBackground(getResources().getDrawable(R.drawable.shape_red_circle));
                        if (j==0||j==6) {
                            days[i][j].setBackground(getResources().getDrawable(R.drawable.shape_blue_circle));
                        }
                    }else if(day==Date1Utils.getDayofMonth(todayDate)){
                        days[i][j].setTextColor(Color.BLACK);
                        days[i][j].setBackground(getResources().getDrawable(R.drawable.shape_black_circle));
                    }
                    day++;
                }
            }
        }
    }
    private void initview() {
        title.setText("安全接送");
        imageleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
