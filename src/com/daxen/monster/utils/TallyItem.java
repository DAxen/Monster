package com.daxen.monster.utils;

import android.content.Context;

/**
 * 
 * @author Gsy
 * @version 1.0
 * @description
 * class TallyItem
 * 账目对象，存放账目信息，并填写数据库
 * @history
 * 2013-03-24
 * 
 */
public abstract class TallyItem {
	protected int    mId;
	protected int    mAccount;
	protected int    mType;
	protected int    mTallyMode;
	protected double mAmount;
	protected String mRemark;
	protected String mTime;
	
	protected DbAdapter mDbadp;
	private   Context   mContext;
	
	TallyItem(Context context) {
		mAccount   = 0;
		mType      = 0;
		mAmount    = 0;
		mTallyMode = 0;
		mRemark    = null;
		mTime      = null;
		mContext   = context;
		mDbadp     = DbAdapter.Instance(mContext);
	}
	
	public void SetId(int id) {
		mId = id;
	}
	
	public void SetAccount(int account) {
		mAccount = account;
	}
	
	public void SetType(int type) {
		mType = type;
	}
	
	public void SetAmount(double amount) {
		mAmount = amount;
	}
	
	public void SetRemark(String remark) {
		mRemark = remark;
	}
	
	public void SetTime(String time) {
		mTime = time;
	}
	
	public abstract long WriteTbDb();
	
	public abstract long DelFromDb();
}
