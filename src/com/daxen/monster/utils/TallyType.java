package com.daxen.monster.utils;

import android.content.Context;
import android.database.Cursor;

/**
 * 
 * @author  Gsy
 * @version 1.0
 * @date    2013-3-25
 * @description
 * TallyType define
 */
public class TallyType {
	private int    mId;
	private int    mMode;
	private String mName;
	
	private DbAdapter mDbadp;
	
	TallyType(Context context) {
		mDbadp = DbAdapter.Instance(context);
		mId    = 0;
		mName  = null;
		mMode  = TallyRaw.TALLY_MODE_EXPEND;
	}
	
	public void SetId(int id) {
		mId = id;
	}
	
	public void SetName(String name) {
		mName = name;
	}
	
	public void SetTallyMode(int mode) {
		mMode = mode;
	}
	
	public long WriteToDb() {
		if (TallyRaw.TALLY_MODE_INCOME == mMode) {
			if (!IsTypeExsit())
			{
				return mDbadp.Add_IncomeType(mName);
			}
			return ErrorCode.RECORD_EXSIT;
		}
		else if (TallyRaw.TALLY_MODE_EXPEND == mMode) {
			if (!IsTypeExsit())
			{
				return mDbadp.Add_ExpendType(mName);
			}
			return ErrorCode.RECORD_EXSIT;
		}
		else {
			return ErrorCode.PARA_ERR;
		}
	}
	
	public long DelFromDb() {
		if (TallyRaw.TALLY_MODE_INCOME == mMode) {
			if (mDbadp.Del_IncomeType(mId)) {
				return ErrorCode.SUCCESS;
			}
			else {
				return ErrorCode.DEL_DB_FAIL;
			}
		}
		else if (TallyRaw.TALLY_MODE_EXPEND == mMode) {
			if (mDbadp.Del_ExpendType(mId)) {
				return ErrorCode.SUCCESS;
			}
			else {
				return ErrorCode.DEL_DB_FAIL;
			}
		}
		else {
			return ErrorCode.PARA_ERR;
		}
	}
	
	private boolean IsTypeExsit() {
		if (TallyRaw.TALLY_MODE_INCOME == mMode) {
			Cursor cursor = mDbadp.Query_IncomeType_ByName(mName);
			if (null == cursor) {
				return false;
			}
			return true;
		}
		else if (TallyRaw.TALLY_MODE_EXPEND == mMode) {
			Cursor cursor = mDbadp.Query_ExpendType_ByName(mName);
			if (null == cursor) {
				return false;
			}
			return true;
		}
		else {
			return false;
		}
	}
}
