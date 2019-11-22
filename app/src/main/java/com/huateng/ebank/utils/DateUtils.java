package com.huateng.ebank.utils;


import com.orhanobut.logger.Logger;
import com.tools.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shanyong on 2019/4/19.
 */

public class DateUtils {

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * %s-%s-%s
     *
     * @param date
     * @return
     */
    public static String formatTimeString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }


    public static String formatTimeString(String pattern, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static Date getDate(String time, String pattern) {
        if (StringUtils.isNotEmpty(time, pattern) && time.length() == pattern.length()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = null;
            try {
                date = simpleDateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }
        Logger.i("传入日期字符串和格式不匹配");
        return new Date();
    }

    /**
     * 过去一个月
     *
     * @return
     */
    public static Date getLastweek() {
        Calendar c = Calendar.getInstance();
        //过去七天
        c.setTime(new Date());
        c.add(Calendar.DATE, -7);
        Date d = c.getTime();
        String day = format.format(d);
        System.out.println("过去七天：" + day);
        return d;
    }

    /**
     * 后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDay(Date date) {
        Calendar c = Calendar.getInstance();
        //过去七天
        c.setTime(date);
        c.add(Calendar.DATE, -1);
        Date d = c.getTime();
        String day = format.format(d);
        System.out.println("过去一天：" + day);
        return d;
    }

    /**
     * 指定日期的过去七天
     *
     * @param date
     * @return
     */
    public static Date getLastweek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -7);
        Date d = c.getTime();
        String day = format.format(d);
        System.out.println("过去七天：" + day);
        return d;
    }

    /**
     * 过去半个月
     *
     * @return
     */
    public static Date getLastHalfMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -14);
        Date d = c.getTime();
        String day = format.format(d);
        System.out.println("过去半月：" + day);
        return d;
    }

    /**
     * 指定日期的过去半个月
     *
     * @param date
     * @return
     */
    public static Date getLastHalfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -14);
        Date d = c.getTime();
        String day = format.format(d);
        System.out.println("过去半月：" + day);
        return d;
    }

    //过去一月
    public static Date getLastMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format.format(m);
        System.out.println("过去一个月：" + mon);
        return m;
    }


    //过去一月
    public static Date getLastMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format.format(m);
        System.out.println("过去一个月：" + mon);
        return m;
    }

    /**
     * 过去一年
     *
     * @return
     */
    public static Date getLastYear() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date m = c.getTime();
        String mon = format.format(m);
        System.out.println("过去一年：" + mon);
        return m;
    }

    /**
     * 指定日期的过去一年
     *
     * @param date
     * @return
     */
    public static Date getLastYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -1);
        Date m = c.getTime();
        String mon = format.format(m);
        System.out.println("过去一年：" + mon);
        return m;
    }


    /**
     * 过去半年
     *
     * @return
     */
    public static Date getLastHalfYear() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -6);
        Date m = c.getTime();
        String mon = format.format(m);
        System.out.println("过去半年：" + mon);
        return m;
    }

    /**
     * 指定日期的过去一年
     *
     * @param date
     * @return
     */
    public static Date getLastHalfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -6);
        Date m = c.getTime();
        String mon = format.format(m);
        System.out.println("过去半年：" + mon);
        return m;
    }


    /**
     * 过去半年
     *
     * @return
     */
    public static Date getLastThreeMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -3);
        Date m = c.getTime();
        String mon = format.format(m);
        return m;
    }

    /**
     * 指定日期的过去一年
     *
     * @param date
     * @return
     */
    public static Date getLastThreeMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -3);
        Date m = c.getTime();
        String mon = format.format(m);
        return m;
    }

    public static int getDaysBetween(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        int days = Math.abs((int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24)));
        Logger.i(days + " ");
        return days;
    }
}
