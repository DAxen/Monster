package com.daxen.monster.utils;

import com.daxen.monster.R;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @author  Gsy
 * @version 1.0
 * @date    2013-3-25
 * @description 
 * 输入管理类，界面看到的是该类的统一方法
 * 
 */
public class InputManager {
	private static InputManager _instance;
	private static Context      mContext;
	
	InputManager(Context context) {
		mContext = context;
	}
	
	public static InputManager Instance(Context context) {
		if (null == _instance) {
			_instance = new InputManager(context);
		}
		else {
			mContext = context;
		}
		return _instance;
	}
	
	// 添加删除账目信息
	public long AddTallyItem(TallyRaw tally) {
		if (!IsTallyLegal(tally)) {
			return ErrorCode.PARA_ERR;
		}
		if (TallyRaw.TALLY_MODE_INCOME == tally.mTallyMode) {
			return AddTallyIncome(tally);
		}
		else if (TallyRaw.TALLY_MODE_EXPEND == tally.mTallyMode) {
			return AddTallyExpend(tally);
		}
		else {
			return ErrorCode.PARA_ERR;
		}
	}
	
	private boolean IsTallyLegal(TallyRaw tally) {
		if (0 == tally.mAmount) {
			return false;
		}
		return true;
	}
	
	private long AddTallyIncome(TallyRaw tally) {
		TallyIncome tallyincome = new TallyIncome(mContext);
		tallyincome.SetAmount(tally.mAmount);
		tallyincome.SetAccount(tally.mAccount);
		tallyincome.SetType(tally.mType);
		tallyincome.SetPayer(tally.mPayer);
		tallyincome.SetRemark(tally.mRemark);
		tallyincome.SetTime(tally.mTime);
		if (ErrorCode.SUCCESS != tallyincome.WriteTbDb()) {
			return ErrorCode.WRITE_DB_FAIL;
		}
		return ErrorCode.SUCCESS;
	}
	
	private long AddTallyExpend(TallyRaw tally) {
		TallyExpend tallyexpend = new TallyExpend(mContext);
		tallyexpend.SetAmount(tally.mAmount);
		tallyexpend.SetAccount(tally.mAccount);
		tallyexpend.SetType(tally.mType);
		tallyexpend.SetSeller(tally.mSeller);
		tallyexpend.SetRemark(tally.mRemark);
		tallyexpend.SetTime(tally.mTime);
		if (ErrorCode.SUCCESS != tallyexpend.WriteTbDb()) {
			return ErrorCode.WRITE_DB_FAIL;
		}
		return ErrorCode.SUCCESS;
	}
	
	public long DelTallyItem(TallyRaw tally) {
		if (TallyRaw.TALLY_MODE_INCOME == tally.mTallyMode) {
			return DelTallyIncome(tally);
		}
		else if (TallyRaw.TALLY_MODE_EXPEND == tally.mTallyMode) {
			return DelTallyExpend(tally);
		}
		else {
			return ErrorCode.PARA_ERR;
		}
	}
	
	private long DelTallyIncome(TallyRaw tally) {
		TallyIncome tallyincome = new TallyIncome(mContext);
		tallyincome.SetId(tally.mId);
		if (ErrorCode.SUCCESS != tallyincome.DelFromDb())
		{
			return ErrorCode.DEL_DB_FAIL;
		}
		return ErrorCode.SUCCESS;
	}
	
	private long DelTallyExpend(TallyRaw tally) {
		TallyExpend tallyexpend = new TallyExpend(mContext);
		tallyexpend.SetId(tally.mId);
		if (ErrorCode.SUCCESS != tallyexpend.DelFromDb()) {
			return ErrorCode.DEL_DB_FAIL;
		}
		return ErrorCode.SUCCESS;
	}
	
	// 添加删除账户信息
	public long AddAccount(String name, double base, String card) {
		AccountInfo account = new AccountInfo(mContext);
		account.SetName(name);
		account.SetBase(base);
		account.SetCard(card);
		if (-1 == account.WriteToDb()) {
			return ErrorCode.WRITE_DB_FAIL;
		}
		return ErrorCode.SUCCESS;
	}
	
	public long DelAccount(int id) {
		if (0 <= id && id <= 5) {
			return ErrorCode.PARA_ERR;
		}
		AccountInfo account = new AccountInfo(mContext);
		account.SetId(id);
		if (ErrorCode.SUCCESS != account.DelFromDb()) {
			return ErrorCode.DEL_DB_FAIL;
		}
		return ErrorCode.SUCCESS;
	}
	
	public long ModiAccount(int id, String name, double base, String card) {
		AccountInfo account = new AccountInfo(mContext);
		account.SetId(id);
		account.SetName(name);
		account.SetBase(base);
		account.SetCard(card);
		if (ErrorCode.SUCCESS != account.ModiToDb()) {
			return ErrorCode.WRITE_DB_FAIL;
		}
		
		return ErrorCode.SUCCESS;
	}
	
	// 添加删除收入/支出类型
	public long AddTallyType(int mode, String name) {
		TallyType type = new TallyType(mContext);
		type.SetTallyMode(mode);
		type.SetName(name);
		if (ErrorCode.SUCCESS != type.WriteToDb()) {
			return ErrorCode.WRITE_DB_FAIL;
		}
		return ErrorCode.SUCCESS;
	}
	
	public long DelTallyType(int mode, int id) {
		TallyType type = new TallyType(mContext);
		type.SetTallyMode(mode);
		type.SetId(id);
		if (ErrorCode.SUCCESS != type.DelFromDb()) {
			return ErrorCode.DEL_DB_FAIL;
		}
		return ErrorCode.SUCCESS;
	}
	
	// 记录转账操作
	public long TransferAccount(int account_f, int account_t, double amount, String time) {
		AccountInfo accountfrom = new AccountInfo(mContext);
		AccountInfo accountto   = new AccountInfo(mContext);
		accountfrom.SetId(account_f);
		accountfrom.SetId(account_t);
		
		// TODO 硬编码，后续改掉
		String remark = accountfrom.GetName() + "向" + accountto.GetName() + "转账" + amount + "元";
		
		TallyIncome tallyincome = new TallyIncome(mContext);
		tallyincome.SetAmount(amount);
		tallyincome.SetAccount(account_t);
		tallyincome.SetTime(time);
		// TODO 这里等待type定义好默认值，有一个分配给转账
		tallyincome.SetType(0);
		tallyincome.SetRemark(remark);
		
		TallyExpend tallyexpend = new TallyExpend(mContext);
		tallyexpend.SetAmount(amount);
		tallyexpend.SetAccount(account_f);
		tallyexpend.SetTime(time);
		tallyexpend.SetType(0);
		tallyexpend.SetRemark(remark);
		
		long ret = ErrorCode.SUCCESS;
		
		ret |= tallyincome.WriteTbDb();
		ret |= tallyexpend.WriteTbDb();
		if (ErrorCode.SUCCESS != ret) {
			return ErrorCode.WRITE_DB_FAIL;
		}
		
		return ErrorCode.SUCCESS;
	}
}
