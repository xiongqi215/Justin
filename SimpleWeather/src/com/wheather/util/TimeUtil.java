package com.wheather.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
	private static final String[] WEEK = { "天", "一", "二", "三", "四", "五", "六" };
	public static final String XING_QI = "星期";
	public static final String ZHOU = "周";

	public static String getStringDate(String format) {
		return new SimpleDateFormat(format, Locale.getDefault())
				.format(new Date());
	}

	public static String getStringDate(String format, Date date) {
		return new SimpleDateFormat(format, Locale.getDefault()).format(date);
	}

	public static String getChineseDate() {
		return getStringDate("yyyy年MM月dd日");
	}

	public static String getDayName(long milliseconds) {
		String cur = getStringDate("yyyy");
//		Date date=new Date();
		Date target = new Date(milliseconds);
		String tar = getStringDate("yyyy", target);
		if (Integer.valueOf(cur) > Integer.valueOf(tar)) {
			return "N天前";
		}
		cur = getStringDate("MM");
		tar = getStringDate("MM", target);
		if (Integer.valueOf(cur) > Integer.valueOf(tar)) {
			return "N天前";
		}
		cur = getStringDate("dd");
		tar = getStringDate("dd", target);
		int temp = Integer.valueOf(cur) - Integer.valueOf(tar);
//		int temp=getDateDifference(date,target);
		switch (temp) {
		case -1:
			return "明天";
		case -2:
			return "后天";
		case 0:
			return "今天";
		case 1:
			return "昨天";
		case 2:
			return "前天";
		case 7:
			return "一星期前";

		default:
			if (temp < 7) {
				return temp + "天前";
			} else {
				return "N天前";
			}

		}
	}
	public static String getChinessWeek(long date){
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTimeInMillis(date);
		int weekNum = calendar.get(Calendar.DAY_OF_WEEK)-1;
		return XING_QI+WEEK[weekNum];
	}

	public static Date getDateAfterDays(long today, int days) {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.setTimeInMillis(today);
		calendar.add(Calendar.DATE, days);
		Date date=calendar.getTime();
		String s=getStringDate("yyyy-MM-dd", date);
		return calendar.getTime();
	}
	
	public static int getDateDifference (Date beginDate,Date endDate){
			 Long days=((endDate.getTime()-beginDate.getTime())/(1000*3600*24));
			 return days.intValue();
		  
		
	}
}
