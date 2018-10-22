package com.wchm.website.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间转换工具类
 */
public class DateUtil {

	public static String DEFAULT_PATTERN = "yyyy-MM-dd";
	public static String DIR_PATTERN = "yyyy/MM/dd/";
	public static String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static String TIMES_PATTERN = "HH:mm:ss";
	public static String NOCHAR_PATTERN = "yyyyMMddHHmmss";
	public static String CHINESE_PATTERN = "yyyy年MM月dd日";
	public static String CHINESE_PATTERN_HX = "yyyyMMdd";
	private final static SimpleDateFormat sdfms = new SimpleDateFormat("mmss");

	/**
	 * 获取mmss格式
	 * 
	 * @return
	 */
	public static String getMs() {
		return sdfms.format(new Date());
	}

	public static boolean isValidDate(String strDate, String pattern)
	{
		try {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			format.parse(strDate);
			return true;
		}
		catch (Exception e) {return false;}
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            日期格式
	 * @return 指定格式的日期字符串
	 */
	public static String formatDateByFormat(Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 转换为默认格式(yyyy-MM-dd)的日期字符串
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static String formatDefaultDate1(Date date) {
		return formatDateByFormat(date, DEFAULT_PATTERN);
	}

	/**
	 * 转换为默认格式(yyyy-MM-dd)的日期字符串
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static String HxformatDefaultDate(Date date) {
		return formatDateByFormat(date, CHINESE_PATTERN_HX);
	}

	/**
	 * 转换为默认格式(yyyy-MM-dd)的日期字符串
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static String formatDefaultDate(Date date, String formatter) {
		return formatDateByFormat(date, formatter);
	}

	/**
	 * 转换为默认格式(yyyy-MM-dd)的日期字符串
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String formatDefaultDate(Date date) {
		return formatDateByFormat(date, DEFAULT_PATTERN);
	}

	/**
	 * 转换为目录格式(yyyy/MM/dd/)的日期字符串
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static String formatDirDate(Date date) {
		return formatDateByFormat(date, DIR_PATTERN);
	}

	/**
	 * 转换为完整格式(yyyy-MM-dd HH:mm:ss)的日期字符串
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static String formatTimesTampDate(Date date) {
		return formatDateByFormat(date, TIMESTAMP_PATTERN);
	}

	/**
	 * 转换为时分秒格式(HH:mm:ss)的日期字符串
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static String formatTimesDate(Date date) {
		return formatDateByFormat(date, TIMES_PATTERN);
	}

	/**
	 * 转换为时分秒格式(HH:mm:ss)的日期字符串
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static String formatNoCharDate(Date date) {
		return formatDateByFormat(date, NOCHAR_PATTERN);
	}

	/**
	 * 日期格式字符串转换为日期对象
	 * 
	 * @param strDate
	 *            日期格式字符串
	 * @param pattern
	 *            日期对象
	 * @return
	 */
	public static Date parseDate(String strDate, String pattern) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			Date nowDate = format.parse(strDate);
			return nowDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串转换为默认格式(yyyy-MM-dd)日期对象
	 * 
	 * @param date
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public static Date parseDefaultDate(String date) {
		return parseDate(date, DEFAULT_PATTERN);
	}

	/**
	 * 字符串转换为默认格式(yyyy年MM月dd日)日期对象
	 * 
	 *
	 * @return
	 * 
	 * @throws Exception
	 */
	public static String parseDefaultString(String date) {
		return formatDateByFormat(parseDate(date, DEFAULT_PATTERN), CHINESE_PATTERN);
	}

	/**
	 * 字符串转换为完整格式(yyyy-MM-dd HH:mm:ss)日期对象
	 * 
	 * @param date
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public static Date parseTimesTampDate(String date) {
		return parseDate(date, TIMESTAMP_PATTERN);
	}

	/**
	 * 获得当前时间
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	/**
	 * 获得当前时间 返回Timestamp
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * sql Date 转 util Date
	 * 
	 * @param date
	 *            java.sql.Date日期
	 * @return java.util.Date
	 */
	public static Date parseUtilDate(java.sql.Date date) {
		return date;
	}

	/**
	 * util Date 转 sql Date
	 * 
	 * @param date
	 *            java.sql.Date日期
	 * @return
	 */
	public static java.sql.Date parseSqlDate(Date date) {
		return new java.sql.Date(date.getTime());
	}

	/**
	 * 获取年份
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	/**
	 * 获取月份
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取星期
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static int getWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		dayOfWeek = dayOfWeek - 1;
		if (dayOfWeek == 0) {
			dayOfWeek = 7;
		}
		return dayOfWeek;
	}

	/**
	 * 获取日期(多少号)
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当前时间(小时)
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static int getHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取当前时间(分)
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static int getMinute(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}

	/**
	 * 获取当前时间(秒)
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static int getSecond(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.SECOND);
	}

	/**
	 * 获取当前毫秒
	 * 
	 * @param date
	 * 
	 * @return
	 */
	public static long getMillis(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/**
	 * 日期增加
	 * 
	 * @param date
	 *            Date
	 * 
	 * @param day
	 *            int
	 * 
	 * @return Date
	 */
	public static Date addDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 日期相减(返回天数)
	 *
	 * @param date
	 *            Date
	 *
	 * @param date1
	 *            Date
	 *
	 * @return int 相差的天数
	 */
	public static int diffDate(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}

	/**
	 * 日期相减(返回秒值)
	 *
	 * @param date
	 *            Date
	 * @param date1
	 *            Date
	 * @return int
	 * @author
	 */
	public static Long diffDateTime(Date date, Date date1) {
		return (Long) ((getMillis(date) - getMillis(date1)) / 1000);
	}

	/**
	 * 获取今天的凌晨 时间
	 *
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date getCurrentStartDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(getCurrentDate());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取今天的23:59:59时间
	 *
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date getCurrentEndDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(getCurrentDate());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	/**
	 * 获取今天的凌晨 时间
	 *
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date getDateStartDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取今天的23:59:59时间
	 *
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date getDateEndDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	/**
	 * 获取两个时间差##天##时##分
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static String getTimeDifference(Date date1, Date date2) {
		long nd = 1000* 24* 60* 60;
		long nh = 1000* 60* 60;
		long nm = 1000* 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = date1.getTime() - date2.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天"+ hour + "小时"+ min + "分钟";
	}

	/**
	 * 获取两个时间差##天##时##分
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static Long getTimeDifferenceMinute(Date date1, Date date2) {
		return (date1.getTime() - date2.getTime()) / (1000 * 60);
	}

	/**
	 * 得到几天前的时间
	 *
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		return now.getTime();
	}

	/**
	 * 得到几天后的时间
	 *
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * <li>功能描述：时间相减得到秒数
	 *
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static Integer getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginDate = null;
		Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (1000);
		Long lday = day;

		//// System.out.println("相隔的天数="+day);

		return Integer.parseInt(lday.toString());
	}

	public static String getLastMonthString() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return formatDefaultDate(calendar.getTime());
	}

	public static Date getLastMonth() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}

	/**
	 * 获取一天后的日期
	 *
	 * @param dateStr 字符串日期
	 * @return
	 * @throws Exception
	 */
	public static String getAddDayDate(String dateStr) {
		Date date = DateUtil.parseDefaultDate(dateStr);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		return DateUtil.formatDefaultDate(c.getTime());
	}

	/**
	 * 后几月
	 * @param date 时间节点
	 * @param month 几月
	 * @return 格式：2018-07-25 00:00:00
	 */
	public static Date customTimeByAddMonth(Date date, int month) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.MONTH, now.get(Calendar.MONTH) + month);
		return now.getTime();
	}

	/**
	 * 前几月
	 * @param date 时间节点
	 * @param month 几月
	 * @return 格式：2018-07-25 00:00:00
	 */
	public static Date customTimeBySuMonth(Date date, int month) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.MONTH, now.get(Calendar.MONTH) - month);
		return now.getTime();
	}

	/**
	 * 自定义日期-几号
	 * @param date 时间节点
	 * @param day 几号
	 * @return 格式：2018-07-25 00:00:00
	 */
	public static Date customTimeByDay(Date date, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, day);
		return now.getTime();
	}

	/**
	 * 月最后一天
	 *
	 */
	public static Date customTimeByLastMonth(Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH));
		return now.getTime();
	}


	public static void main(String[] args) throws Exception {
		System.out.println(formatTimesTampDate(customTimeByAddMonth(new Date(), 1)));
	}
}
