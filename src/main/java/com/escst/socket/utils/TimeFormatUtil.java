package com.escst.socket.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatUtil {
	/**
	 * 时间格式化
	 * 
	 * @param dateStr
	 *            时间字符串
	 * @return 返回格式化后的时间字符串
	 * @throws Exception
	 *             抛出异常
	 */
	public static String dateStrFormat(String dateStr) throws Exception {
		SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmssS");
		SimpleDateFormat time2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		Date dd = time.parse(dateStr);
		return time2.format(dd);
	}

	/**
	 * 时间中月日时分秒小于10的补零
	 * 
	 * @param dateStr
	 *            月日时分秒
	 * @return 返回结果
	 */

	public static String StrFormatDate(String dateStr) {
		int dateInt = Integer.parseInt(dateStr);
		String str = "0";
		if (dateInt < 10) {
			str = str + dateStr;
		} else if (dateInt >= 10) {
			str = dateStr;
		}

		return str;
	}
}
