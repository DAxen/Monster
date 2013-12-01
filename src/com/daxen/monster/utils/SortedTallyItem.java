package com.daxen.monster.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * 
 * @author  Gsy
 * @version 1.0
 * @date    2013-4-25
 * @description
 * ��������ݿ��ж����������ݣ������԰�����Ҫ�������Ŀ
 * 
 */
public class SortedTallyItem {
	private static final String TAG = "SortedTallyItem";
	private List<TallyItem> mSortedList;
	private Context         mContext;
	private DbAdapter       mDbadp;
	
	SortedTallyItem(Context ctx) {
		mContext = ctx;
		mDbadp   = DbAdapter.Instance(mContext);
		mSortedList = new LinkedList<TallyItem>();
		GetCurMonthRecords();
	}
	
	// ��ʼ��map���ݣ�Ĭ��Ϊ����
	private void GetCurMonthRecords() {
		GregorianCalendar  curcal = new GregorianCalendar ();
		Cursor incomeCursor = mDbadp.Query_IncomeNormal_All();
		Cursor expendCursor = mDbadp.Query_ExpendNormal_All();
		int columnindex = 0;
		String db_time  = null;
		while (expendCursor.moveToNext()) {
			columnindex = expendCursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_TIME);
			if (-1 == columnindex) {
				expendCursor.moveToNext();
				continue;
			}
			db_time = expendCursor.getString(columnindex);
			if (StatisticsCom.IsTimeEqual(curcal.get(Calendar.YEAR), curcal.get(Calendar.MONTH), 0, db_time)) {
				TallyExpend tally = new TallyExpend(mContext);
				// ID
				columnindex = expendCursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_ID);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_EXPEND_NORMAL_ID+"]");
				}
				tally.SetId(expendCursor.getInt(columnindex));
				// ACCOUNT
				columnindex = expendCursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_ACCOUNT);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_EXPEND_NORMAL_ACCOUNT+"]");
				}
				tally.SetAccount(expendCursor.getInt(columnindex));
				// AMOUNT
				columnindex = expendCursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_AMOUNT);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_EXPEND_NORMAL_AMOUNT+"]");
				}
				tally.SetAmount(expendCursor.getDouble(columnindex));
				// REMARK
				columnindex = expendCursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_REMARK);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_EXPEND_NORMAL_REMARK+"]");
				}
				tally.SetRemark(expendCursor.getString(columnindex));
				// SELLER
				columnindex = expendCursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_SELLER);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_EXPEND_NORMAL_SELLER+"]");
				}
				tally.SetSeller(expendCursor.getString(columnindex));
				// TIME
				columnindex = expendCursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_TIME);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_EXPEND_NORMAL_TIME+"]");
				}
				tally.SetTime(expendCursor.getString(columnindex));
				// TYPE
				columnindex = expendCursor.getColumnIndex(DbAdapter.KEY_EXPEND_NORMAL_TYPE);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_EXPEND_NORMAL_TYPE+"]");
				}
				tally.SetType(expendCursor.getInt(columnindex));
				
				mSortedList.add(tally);
			} // if (IsTimeEqual) 
		} // while
		
		columnindex = 0;
		db_time  = null;
		while (incomeCursor.moveToNext()) {
			columnindex = incomeCursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_TIME);
			if (-1 == columnindex) {
				incomeCursor.moveToNext();
				continue;
			}
			db_time = incomeCursor.getString(columnindex);
			if (StatisticsCom.IsTimeEqual(curcal.get(Calendar.YEAR), curcal.get(Calendar.MONTH), 0, db_time)) {
				TallyIncome tally = new TallyIncome(mContext);
				// ID
				columnindex = incomeCursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_ID);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_INCOME_NORMAL_ID+"]");
				}
				tally.SetId(incomeCursor.getInt(columnindex));
				// ACCOUNT
				columnindex = incomeCursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_ACCOUNT);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_INCOME_NORMAL_ACCOUNT+"]");
				}
				tally.SetAccount(incomeCursor.getInt(columnindex));
				// AMOUNT
				columnindex = incomeCursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_AMOUNT);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_INCOME_NORMAL_AMOUNT+"]");
				}
				tally.SetAmount(incomeCursor.getDouble(columnindex));
				// REMARK
				columnindex = incomeCursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_REMARK);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_INCOME_NORMAL_REMARK+"]");
				}
				tally.SetRemark(incomeCursor.getString(columnindex));
				// SELLER
				columnindex = incomeCursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_PAYER);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_INCOME_NORMAL_PAYER+"]");
				}
				tally.SetPayer(incomeCursor.getString(columnindex));
				// TIME
				columnindex = incomeCursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_TIME);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_INCOME_NORMAL_TIME+"]");
				}
				tally.SetTime(incomeCursor.getString(columnindex));
				// TYPE
				columnindex = incomeCursor.getColumnIndex(DbAdapter.KEY_INCOME_NORMAL_TYPE);
				if (-1 == columnindex) {
					Log.e(TAG, "columnindex error! ["+DbAdapter.KEY_INCOME_NORMAL_TYPE+"]");
				}
				tally.SetType(incomeCursor.getInt(columnindex));
				
				mSortedList.add(tally);
			} // if (IsTimeEqual) 
		} // while
		
		// sort the list
		SortTheList();
	}
	
	// Ϊlist����
	private void SortTheList() {
		
	}
	
	// ˢ��SortedList�еļ�¼
	private void RefreshList() {
		mSortedList.clear();
		GetCurMonthRecords();
	}
	
	// ��ȡ�����3�μ�¼
	public List<TallyItem> GetLatestRecords() {
		RefreshList();
		if (0 == mSortedList.size()) {
			return null;
		}
		else if (mSortedList.size() < 3) {
			return mSortedList.subList(0, mSortedList.size());
		}
		else {
			return mSortedList.subList(0, 3);
		}
	}
	
	// ��ȡ����õ���Ŀ�б�
	public List<TallyItem> GetSortedList(int month) {
		RefreshList();
		// �������Բ�ѯ��ͬ�·ݵ�
		return mSortedList;
	}
	
	// ����λ�û�ȡtally��Ϣ
	public TallyItem GetTallyItem(int position) {
		return mSortedList.get(position);
	}
}
