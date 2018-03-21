package com.mybatis.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 */
public class Demo1 {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Date parse(String dateStr) throws ParseException {
		return dateFormat.parse(dateStr);
	}

	public static void main(String[] args) {
		final String[] strs = new String[] { "2017-01-01 10:24:00", "2017-01-02 20:48:00", "2017-01-11 12:24:00" };
		for (int i = 0; i < 20; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 2; i++) {
						try {
							System.out.println(parse(strs[i]).toLocaleString());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

		}
	}
}
