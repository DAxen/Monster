package com.daxen.monster.utils;

import android.content.Context;

/**
 * 
 * @author Gsy
 * @version 1.0
 * @description
 * class TallyIncome
 * 收入项目的实现类，继承自TallyItem类
 * @history
 * 2013-03-24
 *
 */
public class TallyIncome extends TallyItem {
	private String mPayer;
	
	TallyIncome(Context context) {
		super(context);
		mPayer = null;
		mTallyMode = TallyRaw.TALLY_MODE_INCOME;
	}
	
	public void SetPayer(String payer) {
		mPayer = payer;
	}
	
	@Override
	public long WriteTbDb() {
		if (-1 == mDbadp.Add_IncomeNormal(mAmount, mAccount, mType, mTime, mPayer, mRemark)) {
			return ErrorCode.WRITE_DB_FAIL;
		}
		return ErrorCode.SUCCESS;
	}

	@Override
	public long DelFromDb() {
		if (mDbadp.Del_IncomeNormal(mId)) {
			return ErrorCode.SUCCESS;
		}
		else {
			return ErrorCode.DEL_DB_FAIL;
		}
	}

}
