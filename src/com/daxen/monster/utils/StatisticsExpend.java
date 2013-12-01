package com.daxen.monster.utils;

import android.content.Context;
import android.database.Cursor;

/**
 * 
 * @author  Gsy
 * @version 1.0
 * @date    2013-3-25
 * @description
 * 支出统计
 * 
 */
public class StatisticsExpend extends StatisticsCom {
	private DbAdapter mDbadp;
	
	StatisticsExpend(Context context) {
		mDbadp = DbAdapter.Instance(context);
	}
	
	@Override
	public double GetSum() {
		Cursor cursor = mDbadp.Query_ExpendNormal_All();
		if (null == cursor) {
			return 0;
		}
		
		int columnindex = 0;
		double amount   = 0;
		double sum      = 0;
		while (cursor.moveToNext()) {
			columnindex = cursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_AMOUNT);
			if (-1 == columnindex) {
				cursor.moveToNext();
				continue;
			}
			amount = cursor.getDouble(columnindex);
			sum   += amount;
		}
		
		return sum;
	}

	@Override
	public double GetSumByTime(int year, int month, int day) {
		Cursor cursor = mDbadp.Query_ExpendNormal_All();
		if (null == cursor) {
			return 0;
		}
		
		int columnindex = 0;
		double amount   = 0;
		double sum      = 0;
		while (cursor.moveToNext()) {
			columnindex = cursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_TIME);
			if (-1 == columnindex) {
				cursor.moveToNext();
				continue;
			}
			if (IsTimeEqual(year, month, day, cursor.getString(columnindex))) {
				columnindex = cursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_AMOUNT);
				if (-1 == columnindex) {
					cursor.moveToNext();
					continue;
				}
				amount = cursor.getDouble(columnindex);
				sum   += amount;
			}
		}
		
		return sum;
	}

	@Override
	public double GetSumByAccount(int account) {
		Cursor cursor = mDbadp.Query_ExpendNormal_ByAccount(account);
		if (null == cursor) {
			return 0;
		}
		
		int columnindex = 0;
		double amount   = 0;
		double sum      = 0;
		while (cursor.moveToNext()) {
			columnindex = cursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_AMOUNT);
			if (-1 == columnindex) {
				cursor.moveToNext();
				continue;
			}
			amount = cursor.getDouble(columnindex);
			sum   += amount;
		}
		return sum;
	}

	@Override
	public double GetSumByAccountAndTime(int account, int year, int month, int day) {
		Cursor cursor = mDbadp.Query_ExpendNormal_ByAccount(account);
		if (null == cursor) {
			return 0;
		}
		
		int columnindex = 0;
		double amount   = 0;
		double sum      = 0;
		while (cursor.moveToNext()) {
			columnindex = cursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_TIME);
			if (-1 == columnindex) {
				cursor.moveToNext();
				continue;
			}
			if (IsTimeEqual(year, month, day, cursor.getString(columnindex))) {
				columnindex = cursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_AMOUNT);
				if (-1 == columnindex) {
					cursor.moveToNext();
					continue;
				}
				amount = cursor.getDouble(columnindex);
				sum   += amount;
			}
		}
		return sum;
	}

	@Override
	public double GetSumByType(int type) {
		Cursor cursor = mDbadp.Query_ExpendNormal_ByType(type);
		if (null == cursor) {
			return 0;
		}
		
		int columnindex = 0;
		double sum      = 0;
		while (cursor.moveToNext()) {
			columnindex = cursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_AMOUNT);
			if (-1 == columnindex) {
				cursor.moveToNext();
				continue;
			}
			sum += cursor.getDouble(columnindex);
		}
		
		return sum;
	}

	@Override
	public double GetSumByTypeAndTime(int type, int year, int month, int day) {
		Cursor cursor = mDbadp.Query_ExpendNormal_ByType(type);
		if (null == cursor) {
			return 0;
		}
		
		int columnindex = 0;
		double sum      = 0;
		while (cursor.moveToNext()) {
			columnindex = cursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_TIME);
			if (-1 == columnindex) {
				cursor.moveToNext();
				continue;
			}
			if (IsTimeEqual(year, month, day, cursor.getString(columnindex))) {
				columnindex = cursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_AMOUNT);
				if (-1 == columnindex) {
					cursor.moveToNext();
					continue;
				}
				sum += cursor.getDouble(columnindex);
			}
		}
		return sum;
	}
}
