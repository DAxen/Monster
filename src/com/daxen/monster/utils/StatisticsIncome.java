package com.daxen.monster.utils;

import android.content.Context;
import android.database.Cursor;

/**
 * 
 * @author  Gsy
 * @version 1.0
 * @date    2013-3-25
 * @description
 * 收入统计
 * 
 */
public class StatisticsIncome extends StatisticsCom {
	private DbAdapter mDbadp;
	
	StatisticsIncome(Context context) {
		mDbadp = DbAdapter.Instance(context);
	}
	
	@Override
	public double GetSum() {
		Cursor cursor = mDbadp.Query_IncomeNormal_All();
		if (null == cursor) {
			return 0;
		}
		
		int columnindex = 0;
		double amount   = 0;
		double sum      = 0;
		while (cursor.moveToNext()) {
			columnindex = cursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_AMOUNT);
			if (-1 == columnindex) {
				cursor.moveToNext();
				continue;
			}
			amount      = cursor.getDouble(columnindex);
			sum        += amount;
		}
			
		return sum;
	}

	@Override
	public double GetSumByTime(int year, int month, int day) {
		Cursor cursor = mDbadp.Query_IncomeNormal_All();
		if (null == cursor) {
			return 0;
		}
		
		int columnindex = 0;
		double amount   = 0;
		double sum      = 0;
		String db_time  = null;
		while (cursor.moveToNext()) {
			columnindex = cursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_TIME);
			if (-1 == columnindex) {
				cursor.moveToNext();
				continue;
			}
			db_time = cursor.getString(columnindex);
			if (IsTimeEqual(year, month, day, db_time)) {
				columnindex = cursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_AMOUNT);
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
		Cursor cursor = mDbadp.Query_IncomeNormal_ByAccount(account);
		if (null == cursor) {
			return 0;
		}
		
		int columnindex = 0;
		double amount   = 0;
		double sum      = 0;
		while (cursor.moveToNext()) {
			columnindex = cursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_AMOUNT);
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
		Cursor cursor = mDbadp.Query_IncomeNormal_ByAccount(account);
		if (null == cursor) {
			return 0;
		}
		
		int columnindex = 0;
		double amount   = 0;
		double sum      = 0;
		while(cursor.moveToNext()) {
			columnindex = cursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_TIME);
			if (-1 == columnindex) {
				cursor.moveToNext();
				continue;
			}
			if (IsTimeEqual(year, month, day, cursor.getString(columnindex))) {
				columnindex = cursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_AMOUNT);
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
		Cursor cursor = mDbadp.Query_IncomeNormal_ByType(type);
		if (null == cursor) {
			return 0;
		}
		
		int columnindex = 0;
		double amount   = 0;
		double sum      = 0;
		while(cursor.moveToNext()) {
			columnindex  = cursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_AMOUNT);
			if (-1 == columnindex) {
				cursor.moveToNext();
				return 0;
			}
			amount = cursor.getDouble(columnindex);
			sum   += amount;
		}
		
		return sum;
	}

	@Override
	public double GetSumByTypeAndTime(int type, int year, int month, int day) {
		Cursor cursor = mDbadp.Query_IncomeNormal_ByType(type);
		if (null == cursor) {
			return 0;
		}
		
		int columnindex = 0;
		double amount   = 0;
		double sum      = 0;
		while (cursor.moveToNext()) {
			columnindex = cursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_TIME);
			if (-1 == columnindex) {
				cursor.moveToNext();
				continue;
			}
			if (IsTimeEqual(year, month, day, cursor.getString(columnindex))) {
				columnindex = cursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_AMOUNT);
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
}
