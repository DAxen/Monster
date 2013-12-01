package com.daxen.monster.utils;

/**
 * 
 * @author Gsy
 * @version 1.0
 * 
 * class TallyRaw
 * 原始的账目信息，采集原始的，未被加工过的账目信息
 * 
 * History: 2013-03-24
 * 
 */

public class TallyRaw {
	public static final int TALLY_MODE_INCOME = 0;
	public static final int TALLY_MODE_EXPEND = 1;
	
	public int    mId;         // 账目信息ID
	public int    mTallyMode;  // 账目类型：收入/支出
	public int    mAccount;    // 账户
	public int    mType;       // 记账类型
	public double mAmount;     // 金额
	public String mSeller;     // 商家
	public String mPayer;      // 支付者
	public String mTime;       // 日期时间
	public String mRemark;     // 备注信息
	public String mLendto;     // 借给
	public String mFrom;       // 从XX处借入
	public String mDeadline;   // 归还日期
	
	public TallyRaw() {
		mTallyMode = 0;
		mAccount   = 0;
		mType      = 0;
		mAmount    = 0;
		mSeller    = null;
		mPayer     = null;
		mTime      = null;
		mRemark    = null;
		mLendto    = null;
		mFrom      = null;
		mDeadline  = null;
	}
}
