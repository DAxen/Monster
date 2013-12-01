/**
 * 
 */
package com.daxen.monster.utils;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * @author  Gsy
 * @version 1.0
 * @date    2013-4-28
 * @description
 * 收支类型管理类，后续可以加入记账场景
 * 
 */
public class TypeManager {
	private static final String TAG = "TypeManager";
	private static TypeManager _instance;
	private static Context     mContext;
	
	private DbAdapter       mDbadp;
	private List<Integer>   mIncomeTypeIdList;
	private List<String>    mIncomeTypeNameList;
	private List<Integer>   mExpendTypeIdList;
	private List<String>    mExpendTypeNameList;
	
	TypeManager(Context ctx) {
		mContext = ctx;
		mDbadp   = DbAdapter.Instance(mContext);
		mIncomeTypeIdList   = new LinkedList<Integer>();
		mIncomeTypeNameList = new LinkedList<String>();
		mExpendTypeIdList   = new LinkedList<Integer>();
		mExpendTypeNameList = new LinkedList<String>();
		InitTypeList();
	}
	
	public static TypeManager Instance(Context ctx) {
		if (null == _instance) {
			_instance = new TypeManager(ctx);
			Log.v(TAG, "new object");
		}
		else {
			mContext = ctx;
		}
		_instance.InitTypeList();
		return _instance;
	}
	
	private void InitTypeList() {
		mIncomeTypeIdList.clear();
		mIncomeTypeNameList.clear();
		mExpendTypeIdList.clear();
		mExpendTypeNameList.clear();
		Cursor cursor = null;
		int columnididx   = 0;
		int columnnameidx = 0;
		
		// income 
		cursor = mDbadp.Query_IncomeType_All();
		if (null != cursor) {
			while (cursor.moveToNext()) {
				columnididx = cursor.getColumnIndex(DbAdapter.KEY_INCOME_TYPE_ID);
				if (-1 == columnididx) {
					Log.e(TAG, "columnidx error! column="+DbAdapter.KEY_INCOME_TYPE_ID);
					continue;
				}
				columnnameidx = cursor.getColumnIndex(DbAdapter.KEY_INCOME_TYPE_NAME);
				if (-1 == columnididx) {
					Log.e(TAG, "columnidx error! column="+DbAdapter.KEY_INCOME_TYPE_NAME);
					continue;
				}
				Integer typeid   = cursor.getInt(columnididx);
				String  typename = cursor.getString(columnnameidx);
				mIncomeTypeIdList.add(typeid);
				mIncomeTypeNameList.add(typename);
			}
		}
		
		cursor = null;
		columnididx   = 0;
		columnnameidx = 0;
		// expend
		cursor = mDbadp.Query_ExpendType_All();
		if (null != cursor) {
			while (cursor.moveToNext()) {
				columnididx = cursor.getColumnIndex(DbAdapter.KEY_EXPEND_TYPE_ID);
				if (-1 == columnididx) {
					Log.e(TAG, "columnidx error! column="+DbAdapter.KEY_EXPEND_TYPE_ID);
					continue;
				}
				columnnameidx = cursor.getColumnIndex(DbAdapter.KEY_EXPEND_TYPE_NAME);
				if (-1 == columnididx) {
					Log.e(TAG, "columnidx error! column="+DbAdapter.KEY_EXPEND_TYPE_NAME);
					continue;
				}
				Integer typeid   = cursor.getInt(columnididx);
				String  typename = cursor.getString(columnnameidx);
				mExpendTypeIdList.add(typeid);
				mExpendTypeNameList.add(typename);
			}
		}
	}
	
	// para1: 类型值     para2: 收入或者支出
	public String GetTypeName(int type, int mode) {
		Cursor cursor = null;
		int columnidx = 0;
		if (TallyRaw.TALLY_MODE_INCOME == mode) {
			cursor = mDbadp.Query_IncomeType_ById(type);
			if (cursor.moveToFirst()) {
				columnidx = cursor.getColumnIndex(DbAdapter.KEY_INCOME_TYPE_NAME);
				if (-1 == columnidx) {
					return null;
				}
				return cursor.getString(columnidx);
			}
			return null;
		}
		else if (TallyRaw.TALLY_MODE_EXPEND == mode) {
			cursor = mDbadp.Query_ExpendType_ById(type);
			if (cursor.moveToFirst()) {
				columnidx = cursor.getColumnIndex(DbAdapter.KEY_EXPEND_TYPE_NAME);
				if (-1 == columnidx) {
					return null;
				}
				return cursor.getString(columnidx);
			}
			return null;
		}
		else {
			return null;
		}
	}
	
	public List<Integer> GetTypeIDList(int mode) {
		if (TallyRaw.TALLY_MODE_INCOME == mode) {
			return mIncomeTypeIdList;
		}
		else if (TallyRaw.TALLY_MODE_EXPEND == mode) {
			return mExpendTypeIdList;
		}
		else {
			return null;
		}
	}
	
	public List<String> GetTypeNameList(int mode) {
		if (TallyRaw.TALLY_MODE_INCOME == mode) {
			return mIncomeTypeNameList;
		}
		else if (TallyRaw.TALLY_MODE_EXPEND == mode) {
			return mExpendTypeNameList;
		}
		else {
			return null;
		}
	}
	
}
