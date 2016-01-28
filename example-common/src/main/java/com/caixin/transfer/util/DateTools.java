package com.caixin.transfer.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTools {
	
	public final static String DateMode_3 = "yyyyMMddHHmmssSSS";
    /**
     * 返回指定格式的系统当前日期时间
     * @param String fomat 指定格式
     * @return String yyyymmdd
     * @author zhangxj
     */
    public static String getDateAndTime(String fomat) {
        SimpleDateFormat today;
        today = new SimpleDateFormat(fomat);
        return today.format(new Date());
    }

    /**
     * 返回指定格式的时间
     * @param String fomat 指定格式
     * @return String yyyymmdd
     * @author zhangxj
     */
    public static String getDateAndTime(Date date, String fomat) {
        SimpleDateFormat today;
        today = new SimpleDateFormat(fomat);
        return today.format(date);
    }

    /**
     * 返回系统当前日期 格式为 yyyy-MM-dd HH:mm:ss
     * 
     * @return String yyyy-MM-dd HH:mm:ss
     * @author xin.cai
     */
    public static String getDate() {
        return getDateAndTime("yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 返回系统当前日期 格式为 YYYY-MM-dd
     * 
     * @return String YYYY-MM-dd
     * @author zhangxj
     */
    public static String getShortDate() {
        return getDateAndTime("yyyy-MM-dd");
    }

    /**
     * 按照指定的格式,格式化指定的日期
     * @param Date 指定的日期
     * @param String 指定的日期格式
     * @return String 格式化好的字符串
     */
    public static String formatDate(Date pidate, String strFormat) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(strFormat);
        String str = simpledateformat.format(pidate);
        return str;
    }

    /**
     * 按照指定的格式,转化字符串为日期
     * @param String 指定的字符串
     * @param String 指定的日期格式
     * @return Date 转换好的日期
     */
    public static Date strToDate(String str, String strFormat) {
        Date reDate = null;
        SimpleDateFormat simpledateformat = new SimpleDateFormat(strFormat);
        try {
            reDate = simpledateformat.parse(str);
        } catch (Exception e) {
            //Common.log("无效的日期字符串");
        }
        return reDate;
    }

    /**
     * 在某一日期上增加天数，返回多少天以后的日期
     * @param Date 指定的日期
     * @param long 增加的天数
     * @return Date 增加天数以后的日期
     */
    public static Date addDay(Date date, long days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, (int) days);
        Date date1 = calendar.getTime();
        return date1;
    }

    /**
     * 在某一日期上增加分钟数，返回多少分钟以后的日期
     * @param Date 指定的日期
     * @param long 增加的分钟数
     * @return Date 增加分钟数以后的日期
     */
    public static Date addMinute(Date pidate, long minutes) {
        long adate = pidate.getTime() + minutes * 60 * 1000;
        return new Date(adate);
    }

    /**
     * 取得两个日期之间的差值
     * @param date 第一个日期
     * @param date 第二个日期
     * @return long 两个日期之间的差值，类型是微秒
     */
    public static long dateDiff(Date pidate1, Date pidate2) {
        return pidate1.getTime() - pidate2.getTime();
    }

    /**
     * 取得两个日期之间的差值
     * @param String 差值的类型
     * @param date 第一个日期
     * @param date 第二个日期
     * @return long 两个日期之间的差值，类型由第一个String参数指定
     */
    public static long dateDiff(String style, Date fromdate, Date todate) {
        byte byte0;
        byte byte1 = 0;
        int i = 1;
        Date date2;
        Date date3;
        if (fromdate.getTime() > todate.getTime()) {
            i = -1;
            date2 = todate;
            date3 = fromdate;
        } else {
            date2 = fromdate;
            date3 = todate;
        }
        if (style.equals("yyyy"))
            byte0 = 1;
        else if (style.equals("m"))
            byte0 = 2;
        else if (style.equals("d"))
            byte0 = 5;
        else if (style.equals("y"))
            byte0 = 5;
        else if (style.equals("w"))
            byte0 = 4;
        else if (style.equals("ww"))
            byte0 = 3;
        else if (style.equals("h")) {
            byte0 = 5;
            byte1 = 11;
        } else if (style.equals("n")) {
            byte0 = 5;
            byte1 = 12;
        } else if (style.equals("s")) {
            byte0 = 5;
            byte1 = 13;
        } else {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date2);
        long l = 0;
        calendar.add(byte0, 1);
        for (Date date4 = calendar.getTime(); date4.getTime() <= date3.getTime();) {
            calendar.add(byte0, 1);
            date4 = calendar.getTime();
            l++;
        }
        if (byte1 == 11 || byte1 == 12 || byte1 == 13) {
            calendar.setTime(date2);
            calendar.add(byte0, (int) l);
//            Date date5 = calendar.getTime();
            switch (byte1) {
            case 11:
                l *= 24;
                break;
            case 12:
                l = l * 24 * 60;
                break;
            case 13:
                l = l * 24 * 60 * 60;
                break;
            }
            calendar.add(byte1, 1);
            for (Date date6 = calendar.getTime(); date6.getTime() <= date3.getTime();) {
                calendar.add(byte1, 1);
                date6 = calendar.getTime();
                l++;
            }
        }
        return l * i;
    }

    /**
     * 取得指定日期所在的星期
     * @param date 指定的日期
     * @return int 所在的星期
     */
    public static int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(7);
    }

    /**
	/**
	 * 转换日期型数据为指定格式字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String D2S(Date date, String pattern) {
		if(null == date) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	
	public static int getIntervalDays(Date fDate, Date toDate) {

	       Calendar aCalendar = Calendar.getInstance();

	       aCalendar.setTime(fDate);

	       int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       aCalendar.setTime(toDate);

	       int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

	       return day2 - day1;

	    }
	
	
	
	
}