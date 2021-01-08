package com.examples.test.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 *
 * @author cz
 * @date 2017-1-31
 */
public class DateUtils {

    private static Log logger = LogFactory.getLog(DateUtils.class);
    static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    // 时间格式
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // 时间格式
    public static final String DATE_FOMATE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String DATE_FOMATE_YYMMDDHHMM = "yyMMddHHmm";

    /**
     * 把date truncate到日期为止,去掉时分秒
     *
     * @param date
     * @return
     */
    public static Date truncateDay(Date date) {
        //把date truncate到日期为止,去掉时分秒
        return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
    }

    public static String formatToBussiness(Date date) {
        if (date == null) {
            return "";
        }
        String result = "";
        try {
            // 格式化日期格式
            SimpleDateFormat sdf3 = new SimpleDateFormat("HHmms");
            result = sdf3.format(date);
        } catch (Exception e) {
            return "";
        }
        return result;
    }


    /**
     * 获取dete前num天
     *
     * @param date 时间
     * @param num  天数
     * @return
     */
    public static Date getBeforeDay(Date date, int num) {
        // 创建一个 GregorianCalendar 对象
        GregorianCalendar cal = new GregorianCalendar();
        // 设置时间
        cal.setTime(date);
        // 获取dete前num天
        cal.add(Calendar.DATE, -num);
        return truncateDay(cal.getTime());
    }

    public static Timestamp getTimestampBeforeDay(Date date, int num) {
        // 创建一个 GregorianCalendar 对象
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -num);
        // 返回一个时间戳对象
        return new Timestamp(cal.getTime().getTime());
    }

    /**
     * 获取dete后num天
     *
     * @param date 时间
     * @param num  天数
     * @return
     */
    public static Date getAfterDay(Date date, int num) {
        // 创建一个 GregorianCalendar 对象
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, num);
        // 返回一个 date 对象
        return truncateDay(cal.getTime());
    }

    /**
     * 格式化时间
     *
     * @param date   时间
     * @param format 时间格式
     * @return
     */
    public static String format(Date date, String format) {
        //格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 《线程安全的》格式化日期为字符串
     *
     * @param localDateTime
     * @param format
     * @return String
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(format);
        String time = localDateTime.format(formatter2);
        return time;
    }

    /**
     * 格式化时间
     *
     * @param date 时间
     * @return
     */
    public static String format(Date date) {
        //格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
        return sdf.format(date);
    }

    /**
     * @param timestamp
     * @return
     */

    public static String format2(Timestamp timestamp) {
        if (timestamp != null) {
            //  格式化时间戳格式
            return format(new Date(timestamp.getTime()));
        }
        return null;
    }

    /**
     * 字符串格式化为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 时间格式
     * @return
     */
    public static Date parseDate(String dateStr, String pattern) {
        Date date = null;
        if (StringUtils.isNotBlank(dateStr)) {
            //字符串格式化为时间
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                // 异常记录
                logger.error(e.getMessage(), e);
            }
        }
        return date;
    }

    /**
     * 字符串格式化为时间
     *
     * @param dateStr 时间字符串
     * @return
     */
    public static Date parseDate(String dateStr) {
        Date date = null;
        if (StringUtils.isNotBlank(dateStr)) {
            //字符串格式化为时间
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
            try {
                // 字符串解析成时间
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                // 日志 异常记录
                logger.error(e.getMessage(), e);
            }
        }
        return date;
    }

    /**
     * 获得两个日期相隔的天数
     *
     * @param first  第一个日期
     * @param second 第二个日期
     * @return 相隔的天数，如果第一个日期大于第二个日期，返回负整数
     */
    public static int getIntervalDays(Date first, Date second) {
        int maxValue = Integer.MAX_VALUE;
        // 参数校验
        if (first == null || second == null) {
            return maxValue;
        }
        if (first.before(second)) {
            // 获取 日历 对象实例
            Calendar firCal = Calendar.getInstance();
            Calendar secCal = Calendar.getInstance();
            secCal.setTime(second);
            // 循环遍历 变量数组
            for (int i = 0; i < maxValue; i++) {
                firCal.setTime(first);
                firCal.add(Calendar.DATE, i);
                if (compareTwoCalendar(firCal, secCal) == 0) {
                    return i;
                }
            }
        } else {
            // 获取 日历 对象实例
            Calendar firCal = Calendar.getInstance();
            Calendar secCal = Calendar.getInstance();
            secCal.setTime(first);
            // 循环遍历 变量数组
            for (int i = 0; i < maxValue; i++) {
                firCal.setTime(second);
                firCal.add(Calendar.DATE, i);
                if (compareTwoCalendar(firCal, secCal) == 0) {
                    return -i;
                }
            }
        }
        return maxValue;
    }

    /**
     * 比较两个日期的大小,只精确到天
     *
     * @param first  第一个日期
     * @param second 第二个日期
     * @return 如果第一个日期大于第二个日期，返回1 如果第一个日期小于第二个日期，返回-1 如果两个日期相等，返回0
     */
    public static int compareTwoCalendar(Calendar first, Calendar second) {
        // 参数检验
        if (first == null) {
            throw new RuntimeException("The first calendar is null");
        }
        // 参数检验
        if (second == null) {
            throw new RuntimeException("The second calendar is null");
        }
        // 参数检验
        if (first.get(Calendar.YEAR) < second.get(Calendar.YEAR)) {
            return -1;
        } else if (first.get(Calendar.YEAR) > second.get(Calendar.YEAR)) {
            return 1;
        } else {
            // 比较两个日期的大小,只精确到天
            if (first.get(Calendar.MONTH) < second.get(Calendar.MONTH)) {
                return -1;
            } else if (first.get(Calendar.MONTH) > second.get(Calendar.MONTH)) {
                return 1;
            } else {
                //  比较两个日期的大小,只精确到天
                if (first.get(Calendar.DATE) < second.get(Calendar.DATE)) {
                    return -1;
                } else if (first.get(Calendar.DATE) > second.get(Calendar.DATE)) {
                    return 1;
                }
                return 0;
            }
        }
    }

    /**
     * 将日期格式的字符串转换为长整型
     *
     * @param date
     * @param format
     * @return
     */
    public static long convert2long(String date, String format) {
        try {
            if (StringUtils.isNotBlank(date)) {
                if (StringUtils.isBlank(format)) {
                    format = DEFAULT_FORMAT;
                }
                SimpleDateFormat sf = new SimpleDateFormat(format);
                //  将日期格式的字符串转换为长整型
                return sf.parse(date).getTime() / 1000;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static int getIntDate(String qryStartTm) {
        // 日期字符串 转int
        return Integer.parseInt(qryStartTm.replace("-", ""));
    }

    public static String trimDate(String date) {
        // 日期字符串  去除 "-"
        return date.replace("-", "");
    }

    public static int getCurrentIntDate() {
        Date date = new Date();
        // 获得 对应日期的int
        return getIntDate(format(date, "yyyy-MM-dd"));
    }

    public static int getMonthAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return getIntDate(format(calendar.getTime(), "yyyy-MM-dd"));
    }

    public static String parseLongDate(String data) {
        if (data.length() == 10) {
            data += "000";
        }
        Date date = new Date(Long.parseLong(data));
        return format(date, DEFAULT_FORMAT);
    }

    public static String getCurrentDate() {
        Date date = new Date();
        return format(date, "yyyy-MM-dd");
    }

    public static String getCurrentTrimDate() {
        // 获取到一个 Date 对象
        Date date = new Date();
        return trimDate(format(date, "yyyy-MM-dd"));
    }

    public static String getDayAgoDate() {
        // 获取到一个 Calendar 对象
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return format(calendar.getTime(), "yyyy-MM-dd");
    }

    public static String getDayAgoTrimDate() {
        // 获取到一个 Calendar 对象
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return trimDate(format(calendar.getTime(), "yyyy-MM-dd"));
    }

    public static String getWeekAgoDate() {
        // 获取到一个 Calendar 对象
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        return format(calendar.getTime(), "yyyy-MM-dd");
    }

    public static String getWeekAgoTrimDate() {
        // 获取到一个 Calendar 对象
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        return trimDate(format(calendar.getTime(), "yyyy-MM-dd"));
    }

    public static String getMonthAgoDate() {
        // 获取到一个 Calendar 对象
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return format(calendar.getTime(), "yyyy-MM-dd");
    }

    public static String getTrimDate(String start, String end) {
        // 定义 date 类型
        Date startDate = parseDate(start, "yyyy-MM-dd");
        Date endDate = parseDate(end, "yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        // 定义一个StringBuilder 对象
        StringBuilder sb = new StringBuilder();
        // 循环遍历对象
        while (calendar.getTimeInMillis() < endDate.getTime()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(trimDate(format(calendar.getTime(), "yyyy-MM-dd")));
            calendar.add(Calendar.DATE, 1);
        }
        if (sb.length() > 0) {
            sb.append(",");
        }
        sb.append(trimDate(end));
        return sb.toString();
    }

    /**
     * 获取指定月份指定的前月份.
     *
     * @param date
     * @param pre
     * @return
     */
    public static String getPreMonth(String date, int pre) {
        Date startDate = parseDate(date, "yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, -pre);
        //获取指定月份指定的前月份.
        return format(calendar.getTime(), "yyyy-MM");
    }

    /**
     * 获取指定月份后指定数据量的月份.
     *
     * @param date
     * @param pre
     * @return
     */
    public static String getLastMonth(String date, int pre) {
        Date startDate = parseDate(date, "yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, pre);
        return format(calendar.getTime(), "yyyy-MM");
    }

    /**
     * 获取指定年份的前推年份.
     *
     * @param date
     * @param pre
     * @return
     */
    public static String getPreYear(String date, int pre) {
        Date startDate = parseDate(date, "yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, -pre);
        return format(calendar.getTime(), "yyyy");
    }

    /**
     * 获取指定年份后推的年份.
     *
     * @param date
     * @param pre
     * @return
     */
    public static String getLastYear(String date, int pre) {
        Date startDate = parseDate(date, "yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, pre);
        return format(calendar.getTime(), "yyyy-MM");
    }

    /**
     * 计算出指定两个月份之间的间隔月数.
     *
     * @param start
     * @param end
     * @return
     */
    public static int getMonthsSpace(String start, String end) {
        int result = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            // 定义一个 Calendar 对象
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            // 设置时间
            c1.setTime(sdf.parse(start));
            c2.setTime(sdf.parse(end));

            result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

            return result == 0 ? result : Math.abs(result);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取两个指定年份之间的间隔数.
     *
     * @param start
     * @param end
     * @return
     */
    public static int getYearsSpace(String start, String end) {
        int result = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            // 定义一个 Calendar 对象
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            // 设置时间
            c1.setTime(sdf.parse(start));
            c2.setTime(sdf.parse(end));

            result = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);

            return result == 0 ? result : Math.abs(result);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

//	public static List<String> getDateRange(Date date1 , Date date2) throws ParseException{
//		// 定义一个字符创类型的集合
//		List<String> list = new ArrayList<String>();
//		 String startDate = sdf2.format(date1);
//		 long startDateL = sdf2.parse(startDate).getTime();
//		 for(long i = startDateL;i<=date2.getTime();i=i+1000*3600*24){
//			 list.add(sdf2.format(new Date(i)));
//		 }
//		 return list;
//	 }

    public static long addEightHour(long time) {
        return addHour(time, 8);
    }

    public static long addHour(long time, int increHour) {
        long result = time + increHour * 60 * 60 * 1000L;
        return result;
    }

    public static long addMinute(long time, int increMinute) {
        long result = time + increMinute * 60 * 1000L;
        return result;
    }

    public static int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
            return 0;
        }
    }

}
