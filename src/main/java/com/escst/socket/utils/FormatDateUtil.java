package com.escst.socket.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatDateUtil {
	/**
     * 返回特定格式的日期
     * 格式如下:
     * yyyy-mm-dd
     * @param formatString
     * @return
     */
    public static String getFormatDate(String formatString){
        String currentDate="";
        SimpleDateFormat format1 = new SimpleDateFormat(formatString);
        currentDate = format1.format(new Date());
        return currentDate;
    }
    
    public static String getDateToString(Date date){
        if(null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatDate = sdf.format(date);
            return formatDate;
        }else{
            return "";
        }
    }

    public static String getGYDateToString(Date date){
        if(null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
            String formatDate = sdf.format(date);
            return formatDate;
        }else{
            return "";
        }
    }

    public static boolean getDateCompany(Date startTime,String compareTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String startTimeStr = sdf.format(startTime);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date time1 = df.parse(startTimeStr);
        Date time2 = df.parse(compareTime);

        boolean flag = false;
//        System.out.println(time1.getTime() +"----"+time2.getTime());
        if (time1.getTime() > time2.getTime()) {
            flag = true;
        }

        return flag;
    }

    public static float getWorkHour(Date startTime,Date endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String startTimeStr = sdf.format(startTime);
        String endTimeStr = sdf.format(endTime);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date time1 = df.parse(startTimeStr);
        Date time2 = df.parse(endTimeStr);

        long time = (time2.getTime() - time1.getTime())/1000;//转换成秒
        float hour=(float)time/3600;

        return hour;
    }

    public static Date getStringToDate(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date formatDate = sdf.parse(time);
        return formatDate;
    }

    public static Date getDateTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date formatDate = sdf.parse(time);
        return formatDate;
    }

    public static Date addMinute(Date date,int minute){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }
}
