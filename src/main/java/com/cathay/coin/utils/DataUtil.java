package com.cathay.coin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 時間轉換工具包
 * 
 * @author Kim Liao
 * 
 */
public class DataUtil {

	private static ThreadLocal<SimpleDateFormat> local = new ThreadLocal<>();

	private static SimpleDateFormat getFormater() {

		SimpleDateFormat simpleDateFormat = local.get();

		if (simpleDateFormat == null) {
			simpleDateFormat = new SimpleDateFormat("MMM d, yyyy HH:mm:ss z", Locale.ENGLISH);
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			local.set(simpleDateFormat);
		}

		return simpleDateFormat;
	}

	/**
	 * 將時間字串轉成 {@code Date} 物件
	 * 
	 * @param strDate 時間字串,格式範例: "Sep 14, 2024 13:57:13 UTC"
	 * @return 與時間字串表示相等的 {@code Date} 物件
	 * @throws ParseException
	 */
	public static Date parseToDate(String strDate) throws ParseException {

		return getFormater().parse(strDate);
	}

}
