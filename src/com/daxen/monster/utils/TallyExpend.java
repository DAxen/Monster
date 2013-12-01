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
public class TallyExpend extends TallyItem {
	private String mSeller;
	
	TallyExpend(Context context) {
		super(context);
		mSeller = null;
		mTallyMode = TallyRaw.TALLY_MODE_EXPEND;
	}
	
	public void SetSeller(String seller) {
		mSeller = seller;
	}
	
	@Override
	public long WriteTbDb() {
		if (-1 == mDbadp.Add_ExpendNormal(mAmount, mAccount, mType, mTime, mSeller, mRemark)) {
			return ErrorCode.WRITE_DB_FAIL;
		}
		return ErrorCode.SUCCESS;
	}

	@Override
	public long DelFromDb() {
		if (mDbadp.Del_ExpendNormal(mId)) {
			return ErrorCode.SUCCESS;
		}
		else {
			return ErrorCode.DEL_DB_FAIL;
		}
	}

}
