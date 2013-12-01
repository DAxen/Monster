package com.daxen.monster.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.daxen.monster.R;

/**
 * 
 * @author Gsy
 * @version 1.0
 * 
 * class DbAdapter
 * 提供访问数据库的通用方法的适配类，单例模式，适配器模式
 * 简单的提供不同的访问方式，用方法名区分：操作+表名+修饰
 * 
 * History: 2013-03-24
 *
 */
public class DbAdapter {
	private static final String TAG = "DbAdapter";
	// 数据库名称
	private static final String DATABASE_NAME = "tally_monster";
	/****************************************
	 * database version history
	 * 2013-07-24 add key in accout_info
	 ****************************************/
	private static final int DATABASE_VERSION = 3; 
	
	// 表名称
	public static final String TABEL_INCOME_NORMAL   = "income_normal";
	public static final String TABLE_EXPEND_NORMAL   = "expend_normal";
	public static final String TABLE_INCOME_DEBIT    = "income_debit";
	public static final String TABLE_EXPEND_DEBIT    = "expend_debit";
	public static final String TABLE_INCOME_TYPE     = "income_type";
	public static final String TABLE_EXPEND_TYPE     = "expend_type";
	public static final String TABLE_ACCOUNT_INFO    = "account_info";
	public static final String TABLE_ACCOUNT_BUDGET  = "account_budget";
	public static final String TABLE_INITIAL_BALANCE = "initial_balance";
	
	// 各表项字段定义
	// income_normal
	public static final String KEY_INCOME_NORMAL_ID      = "_id";
	public static final String KEY_INCOME_NORMAL_AMOUNT  = "amount";
	public static final String KEY_INCOME_NORMAL_ACCOUNT = "account";
	public static final String KEY_INCOME_NORMAL_TYPE    = "type";
	public static final String KEY_INCOME_NORMAL_TIME    = "time";
	public static final String KEY_INCOME_NORMAL_PAYER   = "payer";
	public static final String KEY_INCOME_NORMAL_REMARK  = "remark";
	// expend_normal
	public static final String KEY_EXPEND_NORMAL_ID      = "_id";
	public static final String KEY_EXPEND_NORMAL_AMOUNT  = "amount";
	public static final String KEY_EXPEND_NORMAL_ACCOUNT = "account";
	public static final String KEY_EXPEND_NORMAL_TYPE    = "type";
	public static final String KEY_EXPEND_NORMAL_TIME    = "time";
	public static final String KEY_EXPEND_NORMAL_SELLER  = "seller";
	public static final String KEY_EXPEND_NORMAL_REMARK  = "remark";
	// income_debit
	public static final String KEY_INCOME_DEBIT_ID       = "_id";
	public static final String KEY_INCOME_DEBIT_AMOUNT   = "amount";
	public static final String KEY_INCOME_DEBIT_FROM     = "from";
	public static final String KEY_INCOME_DEBIT_DEADLINE = "deadline";
	public static final String KEY_INCOME_DEBIT_REPAID   = "repaid";
	// expend_debit
	public static final String KEY_EXPEND_DEBIT_ID       = "_id";
	public static final String KEY_EXPEND_DEBIT_AMOUNT   = "amount";
	public static final String KEY_EXPEND_DEBIT_LENDTO   = "lendto";
	public static final String KEY_EXPEND_DEBIT_DEADLINE = "deadline";
	public static final String KEY_EXPEND_DEBIT_REPAID   = "repaid";
	// income_type
	public static final String KEY_INCOME_TYPE_ID        = "_id";
	public static final String KEY_INCOME_TYPE_NAME      = "name";
	// expend_type
	public static final String KEY_EXPEND_TYPE_ID        = "_id";
	public static final String KEY_EXPEND_TYPE_NAME      = "name";
	// account_info
	public static final String KEY_ACCOUNT_INFO_ID       = "_id";
	public static final String KEY_ACCOUNT_INFO_NAME     = "name";
	public static final String KEY_ACCOUNT_INFO_BASE     = "base";
	public static final String KEY_ACCOUNT_INFO_CARD_NO  = "card";
	// account_budget
	public static final String KEY_ACCOUNT_BUDGET_ACCOUNT = "_account";
	public static final String KEY_ACCOUNT_BUDGET_BUDGET  = "budget";
	// initial_balance
	public static final String KEY_BALANCE_ACCOUNT       = "_account";
	public static final String KEY_BALANCE_BALANCE       = "balance";
	
	// SQL查询语句
	private static final String SQL_CREATE_TABLE_INCOME_NORMAL   = 
			"create table [" + TABEL_INCOME_NORMAL + "] (" +
		    "[" + KEY_INCOME_NORMAL_ID      + "] integer primary key autoincrement, " +
			"[" + KEY_INCOME_NORMAL_AMOUNT  + "] double not null, " +
			"[" + KEY_INCOME_NORMAL_ACCOUNT + "] integer not null, " +
			"[" + KEY_INCOME_NORMAL_TYPE    + "] integer not null, " +
			"[" + KEY_INCOME_NORMAL_TIME    + "] date not null, " +
			"[" + KEY_INCOME_NORMAL_PAYER   + "] text, " +
			"[" + KEY_INCOME_NORMAL_REMARK  + "] text)";
	private static final String SQL_CREATE_TABLE_EXPEND_NORMAL   = 
			"create table [" + TABLE_EXPEND_NORMAL + "] (" +
		    "[" + KEY_EXPEND_NORMAL_ID      + "] integer primary key autoincrement, " +
			"[" + KEY_EXPEND_NORMAL_AMOUNT  + "] double not null, " +
			"[" + KEY_EXPEND_NORMAL_ACCOUNT + "] integer not null, " +
			"[" + KEY_EXPEND_NORMAL_TYPE    + "] integer not null, " +
			"[" + KEY_EXPEND_NORMAL_TIME    + "] date not null, " +
			"[" + KEY_EXPEND_NORMAL_SELLER  + "] text, " +
			"[" + KEY_EXPEND_NORMAL_REMARK  + "] text)";
	private static final String SQL_CREATE_TABLE_INCOME_DEBIT    = 
			"create table [" + TABLE_INCOME_DEBIT + "] (" +
			"[" + KEY_INCOME_DEBIT_ID       + "] integer primary key autoincrement, " +
			"[" + KEY_INCOME_DEBIT_AMOUNT   + "] double not null, " +
			"[" + KEY_INCOME_DEBIT_FROM     + "] text not null, " +
			"[" + KEY_INCOME_DEBIT_DEADLINE + "] date not null, " +
			"[" + KEY_INCOME_DEBIT_REPAID   + "] integer not null)";
	private static final String SQL_CREATE_TABLE_EXPEND_DEBIT    = 
			"create table [" + TABLE_EXPEND_DEBIT + "] (" +
			"[" + KEY_EXPEND_DEBIT_ID       + "] integer primary key autoincrement, " +
			"[" + KEY_EXPEND_DEBIT_AMOUNT   + "] double not null, " +
			"[" + KEY_EXPEND_DEBIT_LENDTO   + "] text not null, " +
			"[" + KEY_EXPEND_DEBIT_DEADLINE + "] date not null, " +
			"[" + KEY_EXPEND_DEBIT_REPAID   + "] integer not null)";
	private static final String SQL_CREATE_TABLE_INCOME_TYPE     = 
			"create table [" + TABLE_INCOME_TYPE + "] (" +
			"[" + KEY_INCOME_TYPE_ID        + "] integer primary key autoincrement, " +
			"[" + KEY_INCOME_TYPE_NAME      + "] text not null)";
	private static final String SQL_CREATE_TABLE_EXPEND_TYPE     = 
			"create table [" + TABLE_EXPEND_TYPE + "] (" +
			"[" + KEY_EXPEND_TYPE_ID        + "] integer primary key autoincrement, " +
			"[" + KEY_EXPEND_TYPE_NAME      + "] text not null)";
	private static final String SQL_CREATE_TABLE_ACCOUNT_INFO    = 
			"create table [" + TABLE_ACCOUNT_INFO + "] (" +
			"[" + KEY_ACCOUNT_INFO_ID       + "] integer primary key autoincrement, " +
		    "[" + KEY_ACCOUNT_INFO_NAME     + "] text not null, " + 
		    "[" + KEY_ACCOUNT_INFO_BASE     + "] double not null, " + 
			"[" + KEY_ACCOUNT_INFO_CARD_NO  + "] text)";
	private static final String SQL_CREATE_TABLE_ACCOUNT_BUDGET  = 
			"create table [" + TABLE_ACCOUNT_BUDGET + "] (" +
			"[" + KEY_ACCOUNT_BUDGET_ACCOUNT+ "] integer primary key, " +
			"[" + KEY_ACCOUNT_BUDGET_BUDGET + "] text not null)";
	private static final String SQL_CREATE_TABLE_INITIAL_BALANCE = 
			"create table [" + TABLE_INITIAL_BALANCE + "] (" +
			"[" + KEY_BALANCE_ACCOUNT       + "] integer primary key, " +
			"[" + KEY_BALANCE_BALANCE       + "] text not null)";
	
	// DbAdaper成员变量
	private static DbAdapter _instance;
	private DatabaseHelper   mDbHelper;
	private SQLiteDatabase   mDb;
	private static Context   mContext;
	private static boolean   mFirstCreate = false;
	
	// 静态内部类，创建数据库数据表
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			Log.v(TAG, "context: "+context);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_TABLE_INCOME_NORMAL);
			db.execSQL(SQL_CREATE_TABLE_EXPEND_NORMAL);
			db.execSQL(SQL_CREATE_TABLE_INCOME_DEBIT);
			db.execSQL(SQL_CREATE_TABLE_EXPEND_DEBIT);
			db.execSQL(SQL_CREATE_TABLE_INCOME_TYPE);
			db.execSQL(SQL_CREATE_TABLE_EXPEND_TYPE);
			db.execSQL(SQL_CREATE_TABLE_ACCOUNT_INFO);
			db.execSQL(SQL_CREATE_TABLE_ACCOUNT_BUDGET);
			db.execSQL(SQL_CREATE_TABLE_INITIAL_BALANCE);
			mFirstCreate = true;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
			db.execSQL("DROP TABLE IF EXISTS " + TABEL_INCOME_NORMAL);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPEND_NORMAL);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME_DEBIT);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPEND_DEBIT);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME_TYPE);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPEND_TYPE);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT_INFO);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT_BUDGET);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_INITIAL_BALANCE);
			onCreate(db);
		}
	}
	
	// 构造函数
	DbAdapter(Context context) {
		mContext  = context;
	}
	
	// 单例模式，获取DbAdapter实例，同时保存最新的上下文
	public static DbAdapter Instance(Context context) {
		if (null == _instance) {
			_instance = new DbAdapter(context);
		}
		else {
			mContext = context;
		}
		_instance.Open();
		return _instance;
	}
	
	// 打开可写数据库，抛一个SQLException异常
	public void Open() throws SQLException {
		mDbHelper = new DatabaseHelper(mContext);
		mDb       = mDbHelper.getWritableDatabase();
		if (mFirstCreate) {
			InitDefaulCfg();
			mFirstCreate = false;
		}
	}
	
	// 关闭可写数据库
	public void Close() {
		if (null == mDbHelper) {
			Log.v(TAG, "mDbHelper is null when close db");
			return;
		}
		mDbHelper.close();
		mDbHelper = null;
		mDb       = null;
	}
	
	private void InitDefaulCfg() {
		Resources res = mContext.getResources();
		String[] defTypeIncome = res.getStringArray(R.array.default_type_income);
		String[] defTypeExpend = res.getStringArray(R.array.default_type_expend);
		String[] defAccount    = res.getStringArray(R.array.default_account);
		for (int i = 0; i < defTypeIncome.length; i++) {
			Add_IncomeType(defTypeIncome[i]);
		}
		for (int i = 0; i < defTypeExpend.length; i++) {
			Add_ExpendType(defTypeExpend[i]);
		}
		for (int i = 0; i < defAccount.length; i++) {
			_instance.Add_AccountInfo(defAccount[i], 0, null);
		}
	}
	
	// TODO: 核对错误码，返回值并不代表成功失败
	// 数据库操作，三板斧：添加、删除、修改
	// TABEL_INCOME_NORMAL
	public long Add_IncomeNormal(double amount, int account, int type, String time, String payer, String remark) {
		ContentValues value = new ContentValues();
		value.put(KEY_INCOME_NORMAL_AMOUNT,  amount);
		value.put(KEY_INCOME_NORMAL_ACCOUNT, account);
		value.put(KEY_INCOME_NORMAL_TYPE,    type);
		value.put(KEY_INCOME_NORMAL_TIME,    time);
		value.put(KEY_INCOME_NORMAL_PAYER,   payer);
		value.put(KEY_INCOME_NORMAL_REMARK,  remark);
		return mDb.insert(TABEL_INCOME_NORMAL, null, value);
	}
	
	public boolean Del_IncomeNormal(int id) {
		return mDb.delete(TABEL_INCOME_NORMAL, KEY_INCOME_NORMAL_ID + "=" + id, null) > 0;
	}
	
	public boolean Modi_IncomeNormal(int id, double amount, int account, int type, String time, String payer, String remark) {
		ContentValues value = new ContentValues();
		value.put(KEY_INCOME_NORMAL_AMOUNT,  amount);
		value.put(KEY_INCOME_NORMAL_ACCOUNT, account);
		value.put(KEY_INCOME_NORMAL_TYPE,    type);
		value.put(KEY_INCOME_NORMAL_TIME,    time);
		value.put(KEY_INCOME_NORMAL_PAYER,   payer);
		value.put(KEY_INCOME_NORMAL_REMARK,  remark);
		return mDb.update(TABEL_INCOME_NORMAL, value, KEY_INCOME_NORMAL_ID + "=" + id, null) > 0;
	}
	
	// TABLE_EXPEND_NORMAL
	public long Add_ExpendNormal(double amount, int account, int type, String time, String seller, String remark) {
		ContentValues value = new ContentValues();
		value.put(KEY_EXPEND_NORMAL_AMOUNT,  amount);
		value.put(KEY_EXPEND_NORMAL_ACCOUNT, account);
		value.put(KEY_EXPEND_NORMAL_TYPE,    type);
		value.put(KEY_EXPEND_NORMAL_TIME,    time);
		value.put(KEY_EXPEND_NORMAL_SELLER,  seller);
		value.put(KEY_EXPEND_NORMAL_REMARK,  remark);
		return mDb.insert(TABLE_EXPEND_NORMAL, null, value);
	}
	
	public boolean Del_ExpendNormal(int id) {
		return mDb.delete(TABLE_EXPEND_NORMAL, KEY_EXPEND_NORMAL_ID + "=" + id, null) > 0;
	}
	
	public boolean Modi_ExpendNormal(int id, double amount, int account, int type, String time, String seller, String remark) {
		ContentValues value = new ContentValues();
		value.put(KEY_EXPEND_NORMAL_AMOUNT,  amount);
		value.put(KEY_EXPEND_NORMAL_ACCOUNT, account);
		value.put(KEY_EXPEND_NORMAL_TYPE,    type);
		value.put(KEY_EXPEND_NORMAL_TIME,    time);
		value.put(KEY_EXPEND_NORMAL_SELLER,  seller);
		value.put(KEY_EXPEND_NORMAL_REMARK,  remark);
		return mDb.update(TABLE_EXPEND_NORMAL, value, KEY_EXPEND_NORMAL_ID + "=" + id, null) > 0;
	}
	
	// TABLE_INCOME_DEBIT
	public long Add_IncomeDebit(double amount, String form, String deadline, int repaid) {
		ContentValues value = new ContentValues();
		value.put(KEY_INCOME_DEBIT_AMOUNT,   amount);
		value.put(KEY_INCOME_DEBIT_FROM,     form);
		value.put(KEY_INCOME_DEBIT_DEADLINE, deadline);
		value.put(KEY_INCOME_DEBIT_REPAID,   repaid);
		return mDb.insert(TABLE_INCOME_DEBIT, null, value);
	}
	
	public boolean Del_IncomeDebit(int id) {
		return mDb.delete(TABLE_INCOME_DEBIT, KEY_INCOME_DEBIT_ID + "=" + id, null) > 0;
	}
	
	public boolean Modi_IncomeDebit(int id, double amount, String form, String deadline, int repaid) {
		ContentValues value = new ContentValues();
		value.put(KEY_INCOME_DEBIT_AMOUNT,   amount);
		value.put(KEY_INCOME_DEBIT_FROM,     form);
		value.put(KEY_INCOME_DEBIT_DEADLINE, deadline);
		value.put(KEY_INCOME_DEBIT_REPAID,   repaid);
		return mDb.update(TABLE_INCOME_DEBIT, value, KEY_INCOME_DEBIT_ID + "=" + id, null) > 0;
	}
	
	// TABLE_EXPEND_DEBIT
	public long Add_ExpendDebit(double amount, String lendto, String deadline, int repaid) {
		ContentValues value = new ContentValues();
		value.put(KEY_EXPEND_DEBIT_AMOUNT,   amount);
		value.put(KEY_EXPEND_DEBIT_LENDTO,   lendto);
		value.put(KEY_EXPEND_DEBIT_DEADLINE, deadline);
		value.put(KEY_EXPEND_DEBIT_REPAID,   repaid);
		return mDb.insert(TABLE_EXPEND_DEBIT, null, value);
	}
	
	public boolean Del_ExpendDebit(int id) {
		return mDb.delete(TABLE_EXPEND_DEBIT, KEY_EXPEND_DEBIT_ID + "=" + id, null) > 0;
	}
	
	public boolean Modi_ExpendDebit(int id, double amount, String lendto, String deadline, int repaid) {
		ContentValues value = new ContentValues();
		value.put(KEY_EXPEND_DEBIT_AMOUNT,   amount);
		value.put(KEY_EXPEND_DEBIT_LENDTO,   lendto);
		value.put(KEY_EXPEND_DEBIT_DEADLINE, deadline);
		value.put(KEY_EXPEND_DEBIT_REPAID,   repaid);
		return mDb.update(TABLE_EXPEND_DEBIT, value, KEY_EXPEND_DEBIT_ID + "=" + id, null) > 0;
	}
	
	// TABLE_INCOME_TYPE
	public long Add_IncomeType(String name) {
		ContentValues value = new ContentValues();
		value.put(KEY_INCOME_TYPE_NAME, name);
		return mDb.insert(TABLE_INCOME_TYPE, null, value);
	}
	
	public boolean Del_IncomeType(int id) {
		return mDb.delete(TABLE_INCOME_TYPE, KEY_INCOME_TYPE_ID + "+" + id, null) > 0;
	}
	
	public boolean Modi_IncomeType(int id, String name) {
		ContentValues value = new ContentValues();
		value.put(KEY_INCOME_TYPE_NAME, name);
		return mDb.update(TABLE_INCOME_TYPE, value, KEY_INCOME_TYPE_ID + "=" + id, null) > 0;
	}
	
	// TABLE_EXPEND_TYPE
	public long Add_ExpendType(String name) {
		ContentValues value = new ContentValues();
		value.put(KEY_EXPEND_TYPE_NAME, name);
		return mDb.insert(TABLE_EXPEND_TYPE, null, value);
	}
	
	public boolean Del_ExpendType(int id) {
		return mDb.delete(TABLE_EXPEND_TYPE, KEY_EXPEND_TYPE_ID + "+" + id, null) > 0;
	}
	
	public boolean Modi_ExpendType(int id, String name) {
		ContentValues value = new ContentValues();
		value.put(KEY_EXPEND_TYPE_NAME, name);
		return mDb.update(TABLE_EXPEND_TYPE, value, KEY_EXPEND_TYPE_ID + "=" + id, null) > 0;
	}
	
	// TABLE_ACCOUNT_INFO
	public long Add_AccountInfo(String name, double base, String card) {
		ContentValues value = new ContentValues();
		value.put(KEY_ACCOUNT_INFO_NAME, name);
		value.put(KEY_ACCOUNT_INFO_BASE, base);
		value.put(KEY_ACCOUNT_INFO_CARD_NO, card);
		return mDb.insert(TABLE_ACCOUNT_INFO, null, value);
	}
	
	public boolean Del_AccountInfo(int id) {
		return mDb.delete(TABLE_ACCOUNT_INFO, KEY_ACCOUNT_INFO_ID + "=" + id, null) > 0;
	}
	
	public boolean Modi_AccountInfo(int id, String name, double base, String card) {
		ContentValues value = new ContentValues();
		value.put(KEY_ACCOUNT_INFO_NAME, name);
		value.put(KEY_ACCOUNT_INFO_BASE, base);
		value.put(KEY_ACCOUNT_INFO_CARD_NO, card);
		return mDb.update(TABLE_ACCOUNT_INFO, value, KEY_ACCOUNT_INFO_ID + "=" + id, null) > 0;
	}
	
	// TABLE_ACCOUNT_BUDGET
	public long Add_AccountBudget(int account, double budget) {
		ContentValues value = new ContentValues();
		value.put(KEY_ACCOUNT_BUDGET_ACCOUNT, account);
		value.put(KEY_ACCOUNT_BUDGET_BUDGET,  budget);
		return mDb.insert(TABLE_ACCOUNT_BUDGET, null, value);
	}
	
	public boolean Del_AccountBudget(int account) {
		return mDb.delete(TABLE_ACCOUNT_BUDGET, KEY_ACCOUNT_BUDGET_ACCOUNT + "+" + account, null) > 0;
	}
	
	public boolean Modi_AccountBudget(int account, double budget) {
		ContentValues value = new ContentValues();
		value.put(KEY_ACCOUNT_BUDGET_BUDGET,  budget);
		return mDb.update(TABLE_ACCOUNT_BUDGET, value, KEY_ACCOUNT_BUDGET_ACCOUNT + "=" + account, null) > 0;
	}
	
	// TABLE_INITIAL_BALANCE
	public long Add_InitialBalance(int account, double balance) {
		ContentValues value = new ContentValues();
		value.put(KEY_BALANCE_ACCOUNT, account);
		value.put(KEY_BALANCE_BALANCE, balance);
		return mDb.insert(TABLE_INITIAL_BALANCE, null, value);
	}
	
	public boolean Del_InitialBalance(int account) {
		return mDb.delete(TABLE_INITIAL_BALANCE, KEY_BALANCE_ACCOUNT + "+" + account, null) > 0;
	}
	
	public boolean Modi_InitialBalance(int account, double balance) {
		ContentValues value = new ContentValues();
		value.put(KEY_BALANCE_BALANCE,  balance);
		return mDb.update(TABLE_INITIAL_BALANCE, value, KEY_BALANCE_ACCOUNT + "=" + account, null) > 0;
	}
	
	// 数据库查找
	// TABEL_INCOME_NORMAL
	public Cursor Query_IncomeNormal_ById(int id) {
		Cursor cursor = mDb.query(false, TABEL_INCOME_NORMAL, 
				    new String[] {KEY_INCOME_NORMAL_ID, KEY_INCOME_NORMAL_AMOUNT, 
				                  KEY_INCOME_NORMAL_ACCOUNT, KEY_INCOME_NORMAL_TYPE, 
				                  KEY_INCOME_NORMAL_TIME, KEY_INCOME_NORMAL_PAYER, 
				                  KEY_INCOME_NORMAL_REMARK}, 
				                  KEY_INCOME_NORMAL_ID + "=" + id, // selection
				                  null, null, null, null, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_IncomeNormal_ByAccount(int account) {
		// order by time
		Cursor cursor = mDb.query(false, TABEL_INCOME_NORMAL, 
			    new String[] {KEY_INCOME_NORMAL_ID, KEY_INCOME_NORMAL_AMOUNT, 
			                  KEY_INCOME_NORMAL_ACCOUNT, KEY_INCOME_NORMAL_TYPE, 
			                  KEY_INCOME_NORMAL_TIME, KEY_INCOME_NORMAL_PAYER, 
			                  KEY_INCOME_NORMAL_REMARK}, 
			                  KEY_INCOME_NORMAL_ACCOUNT + "=" + account, // selection
			                  null, null, null, KEY_INCOME_NORMAL_TIME, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_IncomeNormal_ByType(int type) {
		// order by time
	    Cursor cursor = mDb.query(false, TABEL_INCOME_NORMAL, 
					new String[] {KEY_INCOME_NORMAL_ID, KEY_INCOME_NORMAL_AMOUNT, 
					              KEY_INCOME_NORMAL_ACCOUNT, KEY_INCOME_NORMAL_TYPE, 
					              KEY_INCOME_NORMAL_TIME, KEY_INCOME_NORMAL_PAYER, 
					              KEY_INCOME_NORMAL_REMARK}, 
					              KEY_INCOME_NORMAL_TYPE + "=" + type, // selection
					              null, null, null, KEY_INCOME_NORMAL_TIME, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
	    }
		return cursor;
	}
	
	public Cursor Query_IncomeNormal_All() {
		return mDb.query(TABEL_INCOME_NORMAL, 
		   new String[] {KEY_INCOME_NORMAL_ID, KEY_INCOME_NORMAL_AMOUNT, 
	                     KEY_INCOME_NORMAL_ACCOUNT, KEY_INCOME_NORMAL_TYPE, 
	                     KEY_INCOME_NORMAL_TIME, KEY_INCOME_NORMAL_PAYER, 
	                     KEY_INCOME_NORMAL_REMARK}, 
	                     null, null, null, null, KEY_INCOME_NORMAL_TIME);
	}
	
	// TABLE_EXPEND_NORMAL
	public Cursor Query_ExpendNormal_ById(int id) {
		Cursor cursor = mDb.query(true, TABLE_EXPEND_NORMAL, 
				    new String[] {KEY_EXPEND_NORMAL_ID, KEY_EXPEND_NORMAL_AMOUNT, 
				                  KEY_EXPEND_NORMAL_ACCOUNT, KEY_EXPEND_NORMAL_TYPE, 
				                  KEY_EXPEND_NORMAL_TIME, KEY_EXPEND_NORMAL_SELLER, 
				                  KEY_EXPEND_NORMAL_REMARK}, 
				                  KEY_EXPEND_NORMAL_ID + "=" + id,  // selection
				                  null, null, null, null, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_ExpendNormal_ByAccount(int account) {
		// order by time
		Cursor cursor = mDb.query(true, TABLE_EXPEND_NORMAL, 
			        new String[] {KEY_EXPEND_NORMAL_ID, KEY_EXPEND_NORMAL_AMOUNT, 
			                      KEY_EXPEND_NORMAL_ACCOUNT, KEY_EXPEND_NORMAL_TYPE, 
			                      KEY_EXPEND_NORMAL_TIME, KEY_EXPEND_NORMAL_SELLER, 
			                      KEY_EXPEND_NORMAL_REMARK}, 
			                      KEY_EXPEND_NORMAL_ACCOUNT + "=" + account,   // selection
			                      null, null, null, KEY_EXPEND_NORMAL_TIME, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_ExpendNormal_ByType(int type) {
		// order by time
		Cursor cursor = mDb.query(true, TABLE_EXPEND_NORMAL, 
				    new String[] {KEY_EXPEND_NORMAL_ID, KEY_EXPEND_NORMAL_AMOUNT, 
					              KEY_EXPEND_NORMAL_ACCOUNT, KEY_EXPEND_NORMAL_TYPE, 
					              KEY_EXPEND_NORMAL_TIME, KEY_EXPEND_NORMAL_SELLER, 
					              KEY_EXPEND_NORMAL_REMARK}, 
					              KEY_EXPEND_NORMAL_TYPE + "=" + type,  // selection
					              null, null, null, KEY_EXPEND_NORMAL_TIME, null);
	    if (null != cursor)
		{
		    cursor.moveToFirst();
	    }
		return cursor;
	}
	
	public Cursor Query_ExpendNormal_All() {
		// order by time
		return mDb.query(TABLE_EXPEND_NORMAL, 
		   new String[] {KEY_EXPEND_NORMAL_ID, KEY_EXPEND_NORMAL_AMOUNT, 
	                     KEY_EXPEND_NORMAL_ACCOUNT, KEY_EXPEND_NORMAL_TYPE, 
	                     KEY_EXPEND_NORMAL_TIME, KEY_EXPEND_NORMAL_SELLER, 
	                     KEY_EXPEND_NORMAL_REMARK}, 
			             null, null, null, null, KEY_EXPEND_NORMAL_TIME);
	}
	
	// TABLE_INCOME_DEBIT
	public Cursor Query_IncomeDebit_ById(int id) {
		Cursor cursor = mDb.query(true, TABLE_INCOME_DEBIT, 
			        new String[] {KEY_INCOME_DEBIT_ID, KEY_INCOME_DEBIT_AMOUNT, 
				                  KEY_INCOME_DEBIT_FROM, KEY_INCOME_DEBIT_DEADLINE, KEY_INCOME_DEBIT_REPAID}, 
				                  KEY_INCOME_DEBIT_ID + "=" + id,  // selection
				                  null, null, null, null, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_IncomeDebit_All() {
		// order by deadline
	    return mDb.query(TABLE_INCOME_DEBIT, 
		   new String[] {KEY_INCOME_DEBIT_ID, KEY_INCOME_DEBIT_AMOUNT, 
                         KEY_INCOME_DEBIT_FROM, KEY_INCOME_DEBIT_DEADLINE, KEY_INCOME_DEBIT_REPAID}, 
					     null, null, null, null, KEY_INCOME_DEBIT_DEADLINE);
	}
	
	// TABLE_EXPEND_DEBIT
	public Cursor Query_ExpendDebit_ById(int id) {
		Cursor cursor = mDb.query(true, TABLE_EXPEND_DEBIT, 
		            new String[] {KEY_EXPEND_DEBIT_ID, KEY_EXPEND_DEBIT_AMOUNT, 
				                  KEY_EXPEND_DEBIT_LENDTO, KEY_EXPEND_DEBIT_DEADLINE, KEY_EXPEND_DEBIT_REPAID}, 
				                  KEY_EXPEND_DEBIT_ID + "=" + id,  // selection
			                      null, null, null, null, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_ExpendDebit_All() {
		// order by deadline
		return mDb.query(TABLE_EXPEND_DEBIT, 
	       new String[] {KEY_EXPEND_DEBIT_ID, KEY_EXPEND_DEBIT_AMOUNT, 
                         KEY_EXPEND_DEBIT_LENDTO, KEY_EXPEND_DEBIT_DEADLINE, KEY_EXPEND_DEBIT_REPAID}, 
					     null, null, null, null, KEY_EXPEND_DEBIT_DEADLINE);
	}
	
	// TABLE_INCOME_TYPE
	public Cursor Query_IncomeType_ById(int id) {
		Cursor cursor = mDb.query(true, TABLE_INCOME_TYPE, 
	                new String[] {KEY_INCOME_TYPE_ID, KEY_INCOME_TYPE_NAME}, 
	                              KEY_INCOME_TYPE_ID + "=" + id,  // selection
		                          null, null, null, null, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_IncomeType_ByName(String name) {
		Cursor cursor = mDb.query(true, TABLE_INCOME_TYPE, 
                new String[] {KEY_INCOME_TYPE_ID, KEY_INCOME_TYPE_NAME}, 
                              KEY_INCOME_TYPE_NAME + "=" + name,  // selection
	                          null, null, null, null, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_IncomeType_All() {
		// order by id
		return mDb.query(TABLE_INCOME_TYPE, 
	       new String[] {KEY_INCOME_TYPE_ID, KEY_INCOME_TYPE_NAME}, 
					     null, null, null, null, KEY_INCOME_TYPE_ID);
	}
	
	// TABLE_EXPEND_TYPE
	public Cursor Query_ExpendType_ById(int id) {
		Cursor cursor = mDb.query(true, TABLE_EXPEND_TYPE, 
                    new String[] {KEY_EXPEND_TYPE_ID, KEY_EXPEND_TYPE_NAME}, 
                                  KEY_EXPEND_TYPE_ID + "=" + id,  // selection
	                              null, null, null, null, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_ExpendType_ByName(String name) {
		Cursor cursor = mDb.query(true, TABLE_EXPEND_TYPE, 
                new String[] {KEY_EXPEND_TYPE_ID, KEY_EXPEND_TYPE_NAME}, 
                			  KEY_EXPEND_TYPE_NAME + "=" + name,  // selection
                              null, null, null, null, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_ExpendType_All() {
		// order by id
		return mDb.query(TABLE_EXPEND_TYPE, 
		   new String[] {KEY_EXPEND_TYPE_ID, KEY_EXPEND_TYPE_NAME}, 
					     null, null, null, null, KEY_EXPEND_TYPE_ID);
	}
	
	// TABLE_ACCOUNT_INFO
	public Cursor Query_AccountInfo_ById(int id) {
		Cursor cursor = mDb.query(true, TABLE_ACCOUNT_INFO, 
                new String[] {KEY_ACCOUNT_INFO_ID, KEY_ACCOUNT_INFO_NAME}, 
                              KEY_ACCOUNT_INFO_ID + "=" + id,  // selection
                              null, null, null, null, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_AccountInfo_ByName(String name) {
		Cursor cursor = null;
		try {
			cursor = mDb.query(true, TABLE_ACCOUNT_INFO, 
					           new String[] {KEY_ACCOUNT_INFO_ID, KEY_ACCOUNT_INFO_NAME}, 
					           KEY_ACCOUNT_INFO_NAME + "=" + name,  // selection
					           null, null, null, null, null);
		}
		catch (SQLiteException e) {
			cursor = null;
		}
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_AccountInfo_All() {
		// order by id
		return mDb.query(TABLE_ACCOUNT_INFO, 
		   new String[] {KEY_ACCOUNT_INFO_ID, KEY_ACCOUNT_INFO_NAME, KEY_ACCOUNT_INFO_BASE, KEY_ACCOUNT_INFO_CARD_NO}, 
					     null, null, null, null, KEY_ACCOUNT_INFO_ID);
	}
	
	// TABLE_ACCOUNT_BUDGET
	public Cursor Query_AccountBudget_ByAccount(int account) {
		Cursor cursor = mDb.query(true, TABLE_ACCOUNT_BUDGET, 
                new String[] {KEY_ACCOUNT_BUDGET_ACCOUNT, KEY_ACCOUNT_BUDGET_BUDGET}, 
                              KEY_ACCOUNT_BUDGET_ACCOUNT + "=" + account,  // selection
                              null, null, null, null, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_AccountBudget_All() {
		// order by account
		return mDb.query(TABLE_ACCOUNT_BUDGET, 
		   new String[] {KEY_ACCOUNT_BUDGET_ACCOUNT, KEY_ACCOUNT_BUDGET_BUDGET}, 
						 null, null, null, null, KEY_ACCOUNT_BUDGET_ACCOUNT);
	}
	
	// TABLE_INITIAL_BALANCE
	public Cursor Query_InitialBalance_ByAccount(int account) {
		Cursor cursor = mDb.query(true, TABLE_INITIAL_BALANCE, 
                    new String[] {KEY_BALANCE_ACCOUNT, KEY_BALANCE_BALANCE}, 
                                  KEY_BALANCE_ACCOUNT + "=" + account,  // selection
                                  null, null, null, null, null);
		if (null != cursor)
		{
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor Query_InitialBalance_All() {
		// order by account
		return mDb.query(TABLE_INITIAL_BALANCE, 
		   new String[] {KEY_BALANCE_ACCOUNT, KEY_BALANCE_BALANCE}, 
					     null, null, null, null, KEY_BALANCE_ACCOUNT);
	}
}
