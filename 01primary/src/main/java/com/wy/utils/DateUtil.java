package com.wy.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yunwang on 2021/10/18 15:55
 */
public class DateUtil {

    //计算周末，得到上一个指定间隔的工作日
    public static Date getPreviousWorkingDay(Date date, int interval) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int dayOfWeek;
        do {
            cal.add(Calendar.DAY_OF_MONTH, interval);
            dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        } while (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);

        return cal.getTime();
    }

    /**
     * 获取当天日期
     * @return
     */
    public static String getCurrentDay(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * 格式化string为Date
     *
     * @param datestr
     * @return date
     */
    public static Date parseDate(String datestr) {
        if (null == datestr || "".equals(datestr)) {
            return null;
        }
        try {
            String fmtstr = null;
            if (datestr.indexOf(':') > 0) {
                fmtstr = "yyyy-MM-dd HH:mm:ss";
            } else {
                fmtstr = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.PRC);
            return sdf.parse(datestr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 日期转化为String
     *
     * @param date
     * @return date string
     */
    public static String fmtFullDate(Date date) {
        if (null == date) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.PRC);
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String fmtShortDate(Date date) {
        if (null == date) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.PRC);
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     */
    public static Date getCurrentQuarterEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentQuarterStartTime());
        cal.add(Calendar.MONTH, 2);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月最后一天
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        return cal.getTime();
    }

    /**
     * 得到上一季度的最后一天
     * @return
     */
    public static Date getLastQuarterEndTime(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentQuarterStartTime());
        cal.set(Calendar.DATE,0);
        return cal.getTime();
    }

    /**
     * 获取去年上一季度时间
     * @return
     */
    public static Date getLastYearSameQuarterEndTime(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getLastQuarterEndTime());
        cal.add(Calendar.YEAR,-1);
        return cal.getTime();
    }
    public static void main(String[] args) {
        Date currentQuarterEndTime = getCurrentQuarterEndTime();
        System.out.println(fmtShortDate(getCurrentQuarterStartTime()));
        System.out.println(fmtShortDate(currentQuarterEndTime));
        System.out.println(fmtShortDate(getLastQuarterEndTime()));
        System.out.println(fmtShortDate(getLastYearSameQuarterEndTime()));
    }
}
