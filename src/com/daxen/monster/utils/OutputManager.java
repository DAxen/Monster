package com.daxen.monster.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.format.Time;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.daxen.monster.R;

/**
 * 
 * @author  Gsy
 * @version 1.0
 * @date    2013-3-26
 * @description
 * 输出管理类，管理所有内容呈现
 * 
 */
public class OutputManager {
	private static OutputManager _instance;
	private static Context       mContext;
	
	private StatisticsIncome mStatisticsIncome;
	private StatisticsExpend mStatisticsExpend;
	private SortedTallyItem  mSortedTally;
	private TypeManager      mTypeMgr;
	private AccountManager   mAccountMgr;
	
	OutputManager(Context context) {
		mContext = context;
		mStatisticsIncome = new StatisticsIncome(mContext);
		mStatisticsExpend = new StatisticsExpend(mContext);
		mSortedTally      = new SortedTallyItem(mContext);
		mTypeMgr          = TypeManager.Instance(mContext);
		mAccountMgr       = AccountManager.Instance(mContext);
	}
	
	public static OutputManager Instance(Context context) {
		if (null == _instance) {
			_instance = new OutputManager(context);
		}
		else
		{
			mContext = context;
		}
		return _instance;
	}
	
	/***********************
	 *  HOME OUTPUT PART   *
	 ***********************/
	public long SetHomeTextIncome(TextView view) {
		GregorianCalendar  curcal = new GregorianCalendar ();
		double income = mStatisticsIncome.GetSumByTime(curcal.get(Calendar.YEAR), 
				                                       curcal.get(Calendar.MONTH), 
				                                       0);
		DecimalFormat format = new DecimalFormat("#0.0");
		String incomeStr = format.format(income);
		view.setText(incomeStr);
		return ErrorCode.SUCCESS;
	}
	
	public long SetHomeTextExpend(TextView view) {
		GregorianCalendar  curcal = new GregorianCalendar ();
		double expend = mStatisticsExpend.GetSumByTime(curcal.get(Calendar.YEAR), 
                                                       curcal.get(Calendar.MONTH), 
                                                       0);
		DecimalFormat format = new DecimalFormat("#0.0");
		String expendStr = format.format(expend);
		view.setText(expendStr);
		return ErrorCode.SUCCESS;
	}
	
	public long SetHomeTextDifference(TextView view) {
		GregorianCalendar  curcal = new GregorianCalendar ();
		double income = mStatisticsIncome.GetSumByTime(curcal.get(Calendar.YEAR), 
                                                       curcal.get(Calendar.MONTH), 
                                                       0);
		double expend = mStatisticsExpend.GetSumByTime(curcal.get(Calendar.YEAR), 
                                                       curcal.get(Calendar.MONTH), 
                                                       0);
		double diff   = income - expend;
		DecimalFormat format = new DecimalFormat("#0.0");
		String diffStr = format.format(diff);
		view.setText(diffStr);
		return ErrorCode.SUCCESS;
	}
	
	public long SetHomeImageSummary(ImageView view, int height, int width) {
		// 创建图像、画布、画笔
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        Paint paint = new Paint();
        
        // 画出边框
        paint.setColor(Color.LTGRAY);
        int i = 0;
        float h_line = 0;
        float v_line = 0;
        float h_delta = 0;
        float v_delta = 0;
        h_delta = (height - 1) / 5;
        v_delta = (width - 1) / 10;
        // 横向
        for (i = 0; i < 6; i++)
        {
            canvas.drawLine(0, h_line, width, h_line, paint);
            h_line += h_delta;
        }
        // 纵向
        for (i = 0; i < 11; i++)
        {
            canvas.drawLine(v_line, 0, v_line, height, paint);
            v_line += v_delta;
        }
        
        float total_income = (float)mStatisticsIncome.GetSum();
        float total_pay    = (float)mStatisticsExpend.GetSum();
        float in_top       = 0;
        float in_left      = width / 5;
        float in_right     = width * 2 / 5;
        float in_bottom    = height - 1;
        float out_top      = 0;
        float out_left     = width * 3 / 5;
        float out_right    = width * 4 / 5;
        float out_bottom   = height - 1;
        if (total_income <= total_pay) {
        	if (0 != total_pay) {
        		out_top = h_delta;
            	in_top  = in_bottom - (total_income * (out_bottom - out_top) / total_pay);
        	}
        	else {
        		out_top = out_bottom - 1;
        		in_top  = in_bottom - 1;
        	}
        }
        else {
        	in_top  = h_delta;
        	out_top = out_bottom - (total_pay * (in_bottom - in_top) / total_income);
        }
        
        // 画出矩形的统计条, 收入
        paint.setColor(0xff33b5e5);        
        canvas.drawRect(in_left, in_top, in_right, in_bottom, paint);
        
        // 画出矩形的统计条, 支出
        paint.setColor(0xffffbb33);
        canvas.drawRect(out_left, out_top, out_right, out_bottom, paint);
        
        // 将绘制出来的bitmap放到image里面去
        view.setImageBitmap(bitmap);
        
		return ErrorCode.SUCCESS;
	}
	
	public long SetHomeTableLatestRecord(TableLayout layout, TableRow norecord) {
		TallyItem tally = null;
		List<TallyItem> list = mSortedTally.GetLatestRecords();
		if (null == list) {
			return ErrorCode.SUCCESS;
		}
		// 删除no record信息
		layout.removeAllViews();
		// 向table中添加行		
		DecimalFormat format = new DecimalFormat("#0.0");
		for (int i = 0; i < list.size(); i++) {
			tally = list.get(i);
			TextView typeName = new TextView(mContext);
			TextView amount   = new TextView(mContext);
			TextView time     = new TextView(mContext);
			TableRow row      = new TableRow(mContext);
			typeName.setText(mTypeMgr.GetTypeName(tally.mType, tally.mTallyMode));
			amount.setText(format.format(tally.mAmount));
			time.setText(tally.mTime);
			row.addView(typeName);
			row.addView(amount);
			row.addView(time);
			layout.addView(row);
		}
		return ErrorCode.SUCCESS;
	}
	
	/***********************
	 *  LIST OUTPUT PART   *
	 ***********************/
	public SimpleAdapter GetListAdapter(int month) {
		List<TallyItem> list = mSortedTally.GetSortedList(month);
		// 准备好simple adapter里需要的list
		List<Map<String, Object>> adpList = new ArrayList<Map<String, Object>>();
		DecimalFormat format = new DecimalFormat("#0.0");
		for (int i = 0; i < list.size(); i++) {
			TallyItem tally = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", mTypeMgr.GetTypeName(tally.mType, tally.mTallyMode));
			if (TallyRaw.TALLY_MODE_EXPEND == tally.mTallyMode)
			{
				map.put("amount", "-"+format.format(tally.mAmount));
			}
			else {
				map.put("amount", "+"+format.format(tally.mAmount));
			}
			map.put("time", tally.mTime);
			map.put("account", mAccountMgr.GetAccountName(tally.mAccount));
			adpList.add(map);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(mContext, adpList, R.layout.list_item, 
				new String[] {"type", "amount", "time", "account"}, 
				new int[] {R.id.list_item_text_type, R.id.list_item_text_amount, R.id.list_item_text_time, R.id.list_item_text_account});
		
		return adapter;
	}
	
	public TallyRaw GetTallyInfo(int position) {
		TallyItem item = mSortedTally.GetTallyItem(position);
		TallyRaw tally = new TallyRaw();
		tally.mId = item.mId;
		tally.mTallyMode = item.mTallyMode;
		return tally;
	}
	
	/***********************
	 *  DETAIL OUTPUT PART *
	 ***********************/
	public long SetDetailTextAmount(TextView view, int position) {
		TallyItem tally = mSortedTally.GetTallyItem(position);
		DecimalFormat format = new DecimalFormat("#0.0");
		String amountStr = format.format(tally.mAmount);
		view.setText(amountStr);
		return ErrorCode.SUCCESS;
	}
	
	public long SetDetailTextType(TextView view, int position) {
		TallyItem tally = mSortedTally.GetTallyItem(position);
		String typeStr = mTypeMgr.GetTypeName(tally.mType, tally.mTallyMode);
		view.setText(typeStr);
		return ErrorCode.SUCCESS;
	}
	
	public long SetDetailTextAccount(TextView view, int position) {
		TallyItem tally = mSortedTally.GetTallyItem(position);
		String accountStr = mAccountMgr.GetAccountName(tally.mAccount);
		view.setText(accountStr);
		return ErrorCode.SUCCESS;
	}
	
	public long SetDetailTextTime(TextView view, int position) {
		TallyItem tally = mSortedTally.GetTallyItem(position);
		view.setText(tally.mTime);
		return ErrorCode.SUCCESS;
	}
	
	public long SetDetailTextRemark(TextView view, int position) {
		TallyItem tally = mSortedTally.GetTallyItem(position);
		view.setText(tally.mRemark);
		return ErrorCode.SUCCESS;
	}
	
	public long SetDetailTextMode(TextView view, int position) {
		TallyItem tally = mSortedTally.GetTallyItem(position);
		if (TallyRaw.TALLY_MODE_INCOME == tally.mTallyMode) {
			view.setText(R.string.detail_row_text_mode_income);
		}
		else if (TallyRaw.TALLY_MODE_EXPEND == tally.mTallyMode) {
			view.setText(R.string.detail_row_text_mode_expend);
		}
		else {
			return ErrorCode.MODE_ERR;
		}
		return ErrorCode.SUCCESS;
	}
	
	public static int GetDp2Px(Context ctx, float dp) {
		float density = ctx.getResources().getDisplayMetrics().density;
		return (int)(dp*density+0.5f);
	}
	
	public static int GetPx2Dp(Context ctx, float px) {
		float density = ctx.getResources().getDisplayMetrics().density;
		return (int) (px/density+0.5f);
	}
	
	/***********************
	 *  STATIS OUTPUT PART *
	 ***********************/
	public long SetStatisTextTotalIncome(TextView view, int year, int month) {
		double income = mStatisticsIncome.GetSumByTime(year, month, 0);
		DecimalFormat format = new DecimalFormat("#0.0");
		String incomeStr = format.format(income);
		view.setText(incomeStr);
		return ErrorCode.SUCCESS;
	}
	
	public long SetStatisTextTotalExpend(TextView view, int year, int month) {
		double expend = mStatisticsExpend.GetSumByTime(year, month, 0);
		DecimalFormat format = new DecimalFormat("#0.0");
		String expendStr = format.format(expend);
		view.setText(expendStr);
		return ErrorCode.SUCCESS;
	}
	
	public long SetStatisTextBalance(TextView view, int year, int month) {
		double income = mStatisticsIncome.GetSumByTime(year, month, 0);
		double expend = mStatisticsExpend.GetSumByTime(year, month, 0);
		DecimalFormat format = new DecimalFormat("#0.0");
		String balanceStr = format.format(income - expend);
		view.setText(balanceStr);
		return ErrorCode.SUCCESS;
	}
	
	public long SetStatisTextYearIncome(TextView view, int year) {
		double income = mStatisticsIncome.GetSumByTime(year, 0, 0);
		DecimalFormat format = new DecimalFormat("#0.0");
		String incomeStr = format.format(income);
		view.setText(incomeStr);
		return ErrorCode.SUCCESS;
	}
	
	public long SetStatisTextYearExpend(TextView view, int year) {
		double expend = mStatisticsExpend.GetSumByTime(year, 0, 0);
		DecimalFormat format = new DecimalFormat("#0.0");
		String expendStr = format.format(expend);
		view.setText(expendStr);
		return ErrorCode.SUCCESS;
	}
	
	public long SetStatisTextRealBalance(TextView view, int year) {
		double income = mStatisticsIncome.GetSumByTime(year, 0, 0);
		double expend = mStatisticsExpend.GetSumByTime(year, 0, 0);
		DecimalFormat format = new DecimalFormat("#0.0");
		String balanceStr = format.format(income-expend);
		view.setText(balanceStr);
		return ErrorCode.SUCCESS;
	}
	
	public long SetStatisTextExpectBalance(TextView view, int year) {
		Time time = new Time();
		time.setToNow();
		if (year == time.year) {
			double income = mStatisticsIncome.GetSumByTime(year, time.month, 0);
			double expend = mStatisticsExpend.GetSumByTime(year, time.month, 0);
			double avgBalance = (income-expend)/(time.month);
			double expectBalance = income-expend + avgBalance*(12-time.month);
			DecimalFormat format = new DecimalFormat("#0.0");
			view.setText(format.format(expectBalance));
			return ErrorCode.SUCCESS;
		}
		else {
			return SetStatisTextRealBalance(view, year);
		}
	}
	
	public long SetStatisTextNetAsset(TextView view) {
		double income = mStatisticsIncome.GetSum();
		double expend = mStatisticsExpend.GetSum();
		DecimalFormat format = new DecimalFormat("#0.0");
		view.setText(format.format(income-expend));
		return ErrorCode.SUCCESS;
	}
	
	public long SetStatisTextHealth(TextView view) {
		// TODO: 做一个打分算法，根据年结余和月结余占收入比重打分，分数越高，财务健康状态越好
		Time time = new Time();
		time.setToNow();
		double income = mStatisticsIncome.GetSumByTime(time.year, time.month, 0);
		double expend = mStatisticsExpend.GetSumByTime(time.year, time.month, 0);
		double balanceRate = 0;
		if (0 == income) {
			balanceRate = 0;
		}
		else {
			balanceRate = (income-expend)*100/income;
		}
		if (balanceRate >= 30) {
			view.setText(R.string.statis_diff_health_perfect);
		}
		else if (balanceRate >= 10) {
			view.setText(R.string.statis_diff_health_good);
		}
		else {
			view.setText(R.string.statis_diff_health_bad);
		}
		
		return ErrorCode.SUCCESS;
	}
	
	/************************
	 *  ACCOUNT OUTPUT PART *
	 ************************/
	public SimpleAdapter GetAccountListAdapter() {
		List<AccountInfo> list = mAccountMgr.GetAccountIdList();
		// 准备好simple adapter里需要的list
		List<Map<String, Object>> adpList = new ArrayList<Map<String, Object>>();
		DecimalFormat format = new DecimalFormat("#0.0");
		for (int i = 0; i < list.size(); i++) {
			AccountInfo account = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("account", account.GetName());
			if (0 == account.GetBase()) {
				map.put("base", mContext.getResources().getString(R.string.account_mng_add_base));
			}
			else {
				map.put("base", format.format(account.GetBase()));
			}
			adpList.add(map);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(mContext, adpList, R.layout.list_item_account, 
				                                  new String[] {"account", "base"}, 
				                                  new int[] {R.id.list_account_name, R.id.list_account_base});
		
		return adapter;
	}
	
	public AccountInfo GetAccount(int position) {
		return mAccountMgr.GetAccount(position);
	}
	
	/************************
	 *   TYPE OUTPUT PART   *
	 ************************/
	public SimpleAdapter GetTypeListAdapter(int mode) {
		List<String> list = mTypeMgr.GetTypeNameList(mode);
		List<Map<String, Object>> adpList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			String type = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", type);
			adpList.add(map);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(mContext, adpList, R.layout.list_item_type, 
				                                 new String[] {"type"}, 
				                                 new int[] {R.id.list_type_name});
		return adapter;
	}
}
