/**
 * 
 */
package com.daxen.monster.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daxen.monster.R;
import com.daxen.monster.utils.ErrorCode;
import com.daxen.monster.utils.OutputManager;

/**
 * @author  Gsy
 * @version 1.0
 * @date    2013-6-2
 * @description
 * 输出收支对比统计
 */
public class DifferenceStatisFragment extends Fragment {
	private static final String TAG = "DifferenceStatisFragment";
	private static final boolean FULLY_REFRESH_CONTENT = true;
	
	private Button   mBtnPre;
	private Button   mBtnNxt;
	private TextView mTxtMonth;
	private TextView mTxtTotalIncome;
	private TextView mTxtTotalExpend;
	private TextView mTxtBalance;
	private TextView mTxtYearIncome;
	private TextView mTxtYearExpend;
	private TextView mTxtRealBalance;
	private TextView mTxtExpectBalance;
	private TextView mTxtNetAsset;
	private TextView mTxtHealth;
	
	private int mCurYear;
	private int mCurMonth;
	private OutputManager mOutputMgr;
	
	class ButtonPreClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			boolean flag = false;
			if (0 == --mCurMonth) {
				mCurMonth = 12;
				mCurYear--;
				flag = FULLY_REFRESH_CONTENT;
			}
			mTxtMonth.setText(String.format("%04d-%02d", mCurYear, mCurMonth));
			ContentPresent(flag);
		}
	}
	
	class ButtonNxtClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			boolean flag = false;
			if (13 == ++mCurMonth) {
				mCurMonth = 1;
				mCurYear++;
				flag = FULLY_REFRESH_CONTENT;
			}
			mTxtMonth.setText(String.format("%04d-%02d", mCurYear, mCurMonth));
			ContentPresent(flag);
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v(TAG, "onAttach");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		Log.v(TAG, "onCreate");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView");
		return inflater.inflate(R.layout.fragment_statis_diff, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Activity activity = getActivity();
		mBtnPre           = (Button)activity.findViewById(R.id.statis_month_nav_button_pre);
		mBtnNxt           = (Button)activity.findViewById(R.id.statis_month_nav_button_next);;
		mTxtMonth         = (TextView)activity.findViewById(R.id.statis_month_value);
		mTxtTotalIncome   = (TextView)activity.findViewById(R.id.statis_row_value_total_income);
		mTxtTotalExpend   = (TextView)activity.findViewById(R.id.statis_row_value_total_expend);
		mTxtBalance       = (TextView)activity.findViewById(R.id.statis_row_value_balance);
		mTxtYearIncome    = (TextView)activity.findViewById(R.id.statis_row_value_year_income);
		mTxtYearExpend    = (TextView)activity.findViewById(R.id.statis_row_value_year_expend);
		mTxtRealBalance   = (TextView)activity.findViewById(R.id.statis_row_value_real_balance);
		mTxtExpectBalance = (TextView)activity.findViewById(R.id.statis_row_value_expect_balance);
		mTxtNetAsset      = (TextView)activity.findViewById(R.id.statis_row_value_net_asset);
		mTxtHealth        = (TextView)activity.findViewById(R.id.statis_row_value_financial_health);
		
		mOutputMgr = OutputManager.Instance(activity);
		
		mBtnPre.setOnClickListener(new ButtonPreClickListener());
		mBtnNxt.setOnClickListener(new ButtonNxtClickListener());
		
		Time cur = new Time();
		cur.setToNow();
		mTxtMonth.setText(cur.format("%Y-%m"));
		mCurYear  = cur.year;
		mCurMonth = cur.month;
		
		ContentPresent(FULLY_REFRESH_CONTENT);
		
		Log.v(TAG, "onActivityCreated");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.v(TAG, "onStart");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.v(TAG, "onPause");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.v(TAG, "onDestroyView");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.v(TAG, "onDetach");
	}
	
	private void ContentPresent(boolean flag) {
		long ret = ErrorCode.SUCCESS;
		ret |= mOutputMgr.SetStatisTextTotalIncome(mTxtTotalIncome, mCurYear, mCurMonth);
		ret |= mOutputMgr.SetStatisTextTotalExpend(mTxtTotalExpend, mCurYear, mCurMonth);
		ret |= mOutputMgr.SetStatisTextBalance(mTxtBalance, mCurYear, mCurMonth);
		if (FULLY_REFRESH_CONTENT == flag) {
			ret |= mOutputMgr.SetStatisTextYearIncome(mTxtYearIncome, mCurYear);
			ret |= mOutputMgr.SetStatisTextYearExpend(mTxtYearExpend, mCurYear);
			ret |= mOutputMgr.SetStatisTextRealBalance(mTxtRealBalance, mCurYear);
			ret |= mOutputMgr.SetStatisTextExpectBalance(mTxtExpectBalance, mCurYear);
			ret |= mOutputMgr.SetStatisTextNetAsset(mTxtNetAsset);
		}
		ret |= mOutputMgr.SetStatisTextHealth(mTxtHealth);
		if (ErrorCode.SUCCESS != ret) {
			Log.v(TAG, "ContentPresent return["+ret+"]");
		}
	}
}
