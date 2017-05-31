package bibi.com.babayonlie.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lenovo_user on 2016/12/9.
 */
public class Date1Utils {
    /**
     * 获取星期(今天星期几)
     *
     * @return 1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六
     */
    public static int getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
    /**
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseFirstDate(Date date) throws Exception {
        String dateStr = new SimpleDateFormat("yyyy-MM").format(date);
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr + "-01");
    }
    /**
     * 取得某个月有多少天
     */
    public static int getDaysOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    /**
     * 月份:+-
     */
    public static Date monthOperation(Date date, int nmonths) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, nmonths);
        return cal.getTime();
    }
    public static CharSequence parse2ym(Date date) {
        return new SimpleDateFormat("yyyy-MM").format(date);
    }
    //今天是几号
    public static int getDayofMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
}