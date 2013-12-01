package com.daxen.monster.utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * @author  Gsy
 * @version 1.0
 * @date    2013-3-25
 * @description
 * AccountInfo define 
 */
public class AccountInfo {
	private int    mId;
	private String mName;
	private double mBase;
	private String mCard;
	
	private DbAdapter mDbadp;
	
	public AccountInfo(Context context) {
		Log.v("AccountManager", "context:"+context);
		mDbadp = DbAdapter.Instance(context);
		mId    = 0;
		mName  = null;
		mBase  = 0;
		mCard  = null;
	}
	
	public void SetId(int id) {
		mId = id;
		if (!(ErrorCode.SUCCESS != GetFromDb())) {
			mName = null;
		}
	}
	
	public int GetId() {
		return mId;
	}
	public void SetName(String name) {
		mName = name;
	}
	
	public String GetName() {
		return mName;
	}
	
	public void SetBase(double base) {
		mBase = base;
	}
	
	public double GetBase() {
		return mBase;
	}
	
	public void SetCard(String card) {
		mCard = card;
	}
	
	public String GetCard() {
		return mCard;
	}
	
	public long WriteToDb() {
		if (!IsAccountExsit()){
			return mDbadp.Add_AccountInfo(mName, mBase, mCard);
		}
		return ErrorCode.RECORD_EXSIT;
	}
	
	public long DelFromDb() {
		if (mDbadp.Del_AccountInfo(mId)) {
			return ErrorCode.SUCCESS;
		}
		else {
			return ErrorCode.DEL_DB_FAIL;
		}
	}
	
	public long ModiToDb() {
		if (mDbadp.Modi_AccountInfo(mId, mName, mBase, mCard)) {
			return ErrorCode.SUCCESS;
		}
		return ErrorCode.WRITE_DB_FAIL;
	}
	
	private boolean IsAccountExsit() {
		Cursor cursor = mDbadp.Query_AccountInfo_ByName(mName);
		if (null == cursor) {
			return false;
		}
		return true;
	}
	
	private long GetFromDb() {
		Cursor cursor = mDbadp.Query_AccountInfo_ById(mId);
		if (null == cursor) {
			return ErrorCode.NO_RECORD;
		}
		
		int columnindex = cursor.getColumnIndex(DbAdapter.KEY_ACCOUNT_INFO_NAME);
		if (-1 == columnindex) {
			return ErrorCode.NO_COLUMN;
		}
		
		mName = cursor.getString(columnindex);
		
		return ErrorCode.SUCCESS;
	}
}
