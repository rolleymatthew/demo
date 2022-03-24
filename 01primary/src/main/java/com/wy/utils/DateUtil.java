package com.wy.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
     *
     * @return
     */
    public static String getCurrentDay() {
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

    /**
     * 指定日期的季度开始时间
     *
     * @param date
     * @return
     */
    public static Date getSelectedQuarterStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
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
     * 指定日期的季度的结束时间，即2012-03-31 23:59:59
     */
    public static Date getSelectedQuarterEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getSelectedQuarterStartTime(date));
        cal.add(Calendar.MONTH, 2);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月最后一天
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        return cal.getTime();
    }

    /**
     * 指定日期的季度的上一季度的最后一天
     *
     * @return
     */
    public static Date getOneQuarterEndTime(Date date) {
        return getSelectedQuarterEndTime(date);
    }

    /**
     * 指定日期的季度的上一季度开始第一天
     *
     * @return
     */
    public static Date getOneQuarterStartTime(Date date) {
        return getSelectedQuarterStartTime(date);
    }

    /**
     * 指定日期的季度的前两个季度的最后一天
     *
     * @return
     */
    public static Date getTwoQuarterEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getOneQuarterEndTime(date));
        cal.add(Calendar.MONTH, -3);
        return cal.getTime();
    }

    /**
     * 指定日期的前两个季度的第一天
     *
     * @return
     */
    public static Date getTwoQuarterStartTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getOneQuarterStartTime(date));
        cal.add(Calendar.MONTH, -3);
        return cal.getTime();
    }

    /**
     * 指定日期的前三个季度的最后一天
     *
     * @return
     */
    public static Date getThreeQuarterEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getOneQuarterEndTime(date));
        cal.add(Calendar.MONTH, -6);
        return cal.getTime();
    }

    /**
     * 指定日期的前三个季度的第一天
     *
     * @return
     */
    public static Date getThreeQuarterStartTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getSelectedQuarterStartTime(date));
        cal.add(Calendar.MONTH, -6);
        return cal.getTime();
    }

    /**
     * 指定日期的前四个季度的最后一天
     *
     * @return
     */
    public static Date getFourQuarterEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getOneQuarterEndTime(date));
        cal.add(Calendar.MONTH, -9);
        return cal.getTime();
    }

    /**
     * 指定日期的前四个季度的第一天
     *
     * @return
     */
    public static Date getFourQuarterStartTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getSelectedQuarterStartTime(date));
        cal.add(Calendar.MONTH, -9);
        return cal.getTime();
    }

    /**
     * 获取去年上一季度时间
     *
     * @return
     */
    public static Date getLastYearSameQuarterEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getOneQuarterEndTime(new Date()));
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    public static String getLastYearSameQuarter(String date) {
        Date date1 = parseDate(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        cal.add(Calendar.YEAR, -1);
        return fmtShortDate(cal.getTime());
    }

    /**
     * 获取当月的所有周六
     *
     * @param year
     * @param month
     * @return
     */
    public static List<String> getWeekendInMonth(int year, int month) {
        List<String> list = new ArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);// 不设置的话默认为当年
        calendar.set(Calendar.MONTH, month - 1);// 设置月份
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为当月第一天
        int daySize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 当月最大天数
        for (int i = 0; i < daySize - 1; i++) {
            calendar.add(Calendar.DATE, 1);//在第一天的基础上加1
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            if (week == Calendar.SATURDAY) {// 1代表周日，7代表周六 判断这是一个星期的第几天从而判断是否是周末
                list.add(year + "-" + month + "-" + calendar.get(Calendar.DAY_OF_MONTH));// 得到当天是一个月的第几天
            }
        }
        return list;
    }

    /**
     * 周数
     *
     * @param date
     * @return
     */
    public static int getWeekNumber(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONTH);
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 月份
     *
     * @param date
     * @return
     */
    public static int getMonthNumber(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取指定日期的开始年份，不满当年的按照前一年度算
     *
     * @param date
     * @return
     */
    public static Date getOneYearStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            if (currentMonth != 12) {
                c.add(Calendar.YEAR, -1);
            }
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));// 获取当前月第一天
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 计算年度，12月按照当年算，不满12月按照去年算
     *
     * @param date
     * @return
     */
    public static Date getOneYearEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(getOneYearStartTime(date));
        c.add(Calendar.MONTH, 11);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月第一天
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 计算前两年的日期
     *
     * @param date
     * @return
     */
    public static Date getTwoYearStartDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(getOneYearStartTime(date));
        c.add(Calendar.MONTH, -12);
        return c.getTime();
    }

    /**
     * 计算前两年的日期
     *
     * @param date
     * @return
     */
    public static Date getTwoYearEndDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(getOneYearEndTime(date));
        c.add(Calendar.MONTH, -12);
        return c.getTime();
    }

    public static Date getThreeYearStartDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(getOneYearStartTime(date));
        c.add(Calendar.MONTH, -24);
        return c.getTime();
    }

    public static Date getThreeYearEndDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(getOneYearEndTime(date));
        c.add(Calendar.MONTH, -24);
        return c.getTime();
    }

    public static Date getFourYearStartDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(getOneYearStartTime(date));
        c.add(Calendar.MONTH, -36);
        return c.getTime();
    }

    public static Date getFourYearEndDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(getOneYearEndTime(date));
        c.add(Calendar.MONTH, -36);
        return c.getTime();
    }

    public static void main(String[] args) {
//        System.out.println(fmtShortDate(getSelectedQuarterStartTime(parseDate("2021-12-31"))));
//        System.out.println(fmtShortDate(getOneQuarterStartTime(parseDate("2021-12-31"))));
//        System.out.println(fmtShortDate(getOneQuarterEndTime(parseDate("2021-12-31"))));
//        System.out.println(fmtShortDate(getTwoQuarterStartTime(parseDate("2021-12-31"))));
//        System.out.println(fmtShortDate(getTwoQuarterEndTime(parseDate("2021-12-31"))));
//        System.out.println(fmtShortDate(getThreeQuarterStartTime(parseDate("2021-12-31"))));
//        System.out.println(fmtShortDate(getThreeQuarterEndTime(parseDate("2021-12-31"))));
//        System.out.println(fmtShortDate(getFourQuarterStartTime(parseDate("2021-12-31"))));
//        System.out.println(fmtShortDate(getFourQuarterEndTime(parseDate("2021-12-31"))));
        System.out.println(fmtShortDate(getOneYearStartTime(parseDate("2021-11-29"))));
        System.out.println(fmtShortDate(getOneYearEndTime(parseDate("2021-11-29"))));
        System.out.println(fmtShortDate(getTwoYearStartDate(parseDate("2021-10-29"))));
        System.out.println(fmtShortDate(getTwoYearEndDate(parseDate("2021-10-29"))));
        System.out.println(fmtShortDate(getThreeYearStartDate(parseDate("2021-10-29"))));
        System.out.println(fmtShortDate(getThreeYearEndDate(parseDate("2021-10-29"))));
        System.out.println(fmtShortDate(getFourYearStartDate(parseDate("2021-10-29"))));
        System.out.println(fmtShortDate(getFourYearEndDate(parseDate("2021-10-29"))));
    }

}
