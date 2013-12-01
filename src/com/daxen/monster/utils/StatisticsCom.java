package com.daxen.monster.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * @author  Gsy
 * @version 1.0
 * @date    2013-3-25
 * @description
 * 统计功能接口定义，在子类实现
 * 
 */
public abstract class StatisticsCom {
	public static final int TIME_TYPE_YEAR  = 0;
	public static final int TIME_TYPE_MONTH = 1;
	public static final int TIME_TYPE_DATE  = 2;
	
	public abstract double GetSum();
	public abstract double GetSumByTime(int year, int month, int day);
	public abstract double GetSumByAccount(int account);
	public abstract double GetSumByAccountAndTime(int account, int year, int month, int day);
	public abstract double GetSumByType(int type);
	public abstract double GetSumByTypeAndTime(int type, int year, int month, int day);
	
	public double GetPercentByType(int type) {
		double sum = GetSum();
		double sumbytype = GetSumByType(type);
		if (0 == sum) {
			return 0;
		}
		return sumbytype*100/sum;
	}

	public double GetPercentByTypeAndTime(int type, int year, int month, int day) {
		double sum = GetSumByTime(year, month, day);
		double sumbytype = GetSumByTypeAndTime(type, year, month, day);
		if (0 == sum) {
			return 0;
		}
		return sumbytype*100/sum;
	}

	public double GetPercentByAccount(int account) {
		double sum = GetSum();
		double sumbyaccount = GetSumByAccount(account);
		if (0 == sum) {
			return 0;
		}
		return sumbyaccount*100/sum;
	}

	public double GetPercentByAccounyAndTime(int account, int year, int month, int day) {
		double sum = GetSumByTime(year, month, day);
		double sumbyaccount = GetSumByAccountAndTime(account, year, month, day);
		if (0 == sum) {
			return 0;
		}
		return sumbyaccount*100/sum;
	}
	
/*	public static boolean IsTimeEqual(int time, int timetype, String db_time) {
		GregorianCalendar  dbcal  = new GregorianCalendar ();
		GregorianCalendar  curcal = new GregorianCalendar ();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = format.parse(db_time);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		dbcal.setTime(date);
		
		switch (timetype) {
		case TIME_TYPE_YEAR:
			if (time == dbcal.get(Calendar.YEAR)) {
				return true;
			}
			break;
		case TIME_TYPE_MONTH:
			if ((curcal.get(Calendar.YEAR) == dbcal.get(Calendar.YEAR))
			&& (time == dbcal.get(Calendar.MONTH))) {
				return true;
			}
			break;
		case TIME_TYPE_DATE:
			if ((curcal.get(Calendar.YEAR) == dbcal.get(Calendar.YEAR))
			&& (curcal.get(Calendar.MONTH) == dbcal.get(Calendar.MONTH))
			&& (time == dbcal.get(Calendar.DATE))) {
				return true;
			}
			break;
		default:
			return false;
		}
		
		return false;
	}*/
	
	public static boolean IsTimeEqual(int year, int month, int day, String db_time) {
		GregorianCalendar  dbCalendar  = new GregorianCalendar ();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = format.parse(db_time);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		dbCalendar.setTime(date);
		
		if ((year == dbCalendar.get(Calendar.YEAR)) 
		 && ((month == 0) || (month == dbCalendar.get(Calendar.MONTH))) 
		 && ((day == 0) || (day == dbCalendar.get(Calendar.DATE)))) {
			return true;
		}
		
		return false;
	}
}
