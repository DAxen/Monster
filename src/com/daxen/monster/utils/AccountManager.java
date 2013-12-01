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
 * @date    2013-5-1
 * @description
 * some description of this file ...
 */
public class AccountManager {
	private static final String TAG = "AccountManager";
	private static AccountManager _instance;
	private static Context mContext;
	
	private DbAdapter mDbadp;
	private List<AccountInfo> mAccountIdList;
	private List<String>  mAccountNameList;
	
	AccountManager(Context ctx) {
		mContext = ctx;
		mDbadp = DbAdapter.Instance(ctx);
		mAccountIdList   = new LinkedList<AccountInfo>();
		mAccountNameList = new LinkedList<String>();
		InitAccountList();
	}
	
	public static AccountManager Instance(Context ctx) {
		if (null == _instance) {
			_instance = new AccountManager(ctx);
			Log.v(TAG, "new object");
		}
		else {
			mContext = ctx;
		}
		// 重新初始化一下
		//_instance.InitAccountList();
		return _instance;
	}
	
	public void InitAccountList() {
		mAccountIdList.clear();
		mAccountNameList.clear();
		Cursor cursor = mDbadp.Query_AccountInfo_All();
		if (null != cursor) {
			int columnididx   = 0;
			int columnnameidx = 0;
			int columnbaseidx = 0;
			int columncardidx = 0;
			while (cursor.moveToNext()) {
				columnididx = cursor.getColumnIndex(DbAdapter.KEY_ACCOUNT_INFO_ID);
				if (-1 == columnididx) {
					Log.e(TAG, "columnidx error! column="+DbAdapter.KEY_ACCOUNT_INFO_ID);
					continue;
				}
				columnnameidx = cursor.getColumnIndex(DbAdapter.KEY_ACCOUNT_INFO_NAME);
				if (-1 == columnididx) {
					Log.e(TAG, "columnidx error! column="+DbAdapter.KEY_ACCOUNT_INFO_NAME);
					continue;
				}
				columnbaseidx = cursor.getColumnIndex(DbAdapter.KEY_ACCOUNT_INFO_BASE);
				if (-1 == columnbaseidx) {
					Log.e(TAG, "columnidx error! column="+DbAdapter.KEY_ACCOUNT_INFO_BASE);
					continue;
				}
				columncardidx = cursor.getColumnIndex(DbAdapter.KEY_ACCOUNT_INFO_CARD_NO);
				if (-1 == columncardidx) {
					Log.e(TAG, "columnidx error! column="+DbAdapter.KEY_ACCOUNT_INFO_CARD_NO);
					continue;
				}
				AccountInfo account = new AccountInfo(mContext);
				account.SetId(cursor.getInt(columnididx));
				account.SetName(cursor.getString(columnnameidx));
				account.SetBase(cursor.getDouble(columnbaseidx));
				account.SetCard(cursor.getString(columncardidx));
				mAccountIdList.add(account);
				mAccountNameList.add(account.GetName());
			}
		}
	}
	
	public String GetAccountName(int id) {
		InitAccountList();
		Cursor cursor = mDbadp.Query_AccountInfo_ById(id);
		if (cursor.moveToFirst())
		{
			int columnidx = cursor.getColumnIndex(DbAdapter.KEY_ACCOUNT_INFO_NAME);
			if (-1 == columnidx) {
				Log.v(TAG, "GetAccountName columnidx error! "+DbAdapter.KEY_ACCOUNT_INFO_NAME);
				return null;
			}
			return cursor.getString(columnidx);
		}
		else {
			Log.v(TAG, "GetAccountName null id=! "+id);
			return null;
		}
	}
	
	public List<AccountInfo> GetAccountIdList() {
		InitAccountList();
		return mAccountIdList;
	}
	
	public List<String> GetAccountNameList() {
		InitAccountList();
		return mAccountNameList;
	}
	
	public AccountInfo GetAccount(int position) {
		InitAccountList();
		return mAccountIdList.get(position);
	}
}
