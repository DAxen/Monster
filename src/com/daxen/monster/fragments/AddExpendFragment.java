package com.daxen.monster.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.daxen.monster.R;
import com.daxen.monster.TimePickerActivity;
import com.daxen.monster.utils.AccountManager;
import com.daxen.monster.utils.ErrorCode;
import com.daxen.monster.utils.RequestCode;
import com.daxen.monster.utils.SmartSelection;
import com.daxen.monster.utils.TallyRaw;
import com.daxen.monster.utils.TypeManager;

/**
 * @author  Gsy
 * @version 1.0
 * @date    2013-4-29
 * @description
 * 记录支出的fragment
 * 
 */
public class AddExpendFragment extends AddRecordFragment {
	private static final String TAG = "AddExpendFragment";
	private EditText mAmount;
	private Spinner  mType;
	private Spinner  mAccount;
	// TODO: 后续修改为DatePicker
	private TextView mTime;
	private EditText mRemark;
	private String   mDateTime;
	private int      mTypeNo;
	private int      mAccountNo;
	private TypeManager    mTypeMgr;
	private AccountManager mAccountMgr;
	
	private Activity mActivity;
	
	private class TimeTextClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			// 点击时间TextView以后触发
			Intent i = new Intent(mActivity, TimePickerActivity.class);
			startActivityForResult(i, RequestCode.DATE_TIME);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView");
		return inflater.inflate(R.layout.fragment_add_expend, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mActivity = getActivity();
		mAmount  = (EditText)mActivity.findViewById(R.id.frag_expend_amount_value);
		mType    = (Spinner)mActivity.findViewById(R.id.frag_expend_type_value);
		mAccount = (Spinner)mActivity.findViewById(R.id.frag_expend_account_value);
		mTime    = (TextView)mActivity.findViewById(R.id.frag_expend_text_time_value);
		mRemark  = (EditText)mActivity.findViewById(R.id.frag_expend_remark_value);
		mTypeMgr = TypeManager.Instance(mActivity);
		mAccountMgr = AccountManager.Instance(mActivity);
		InitTypeSpinner();
		InitAccountSpinner();
		InitTimeText();
		mTime.setOnClickListener(new TimeTextClickListener());
		
		Log.v(TAG, "onActivityCreated");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RequestCode.DATE_TIME == requestCode) {
			if (ErrorCode.SUCCESS == resultCode) {
				if (null == data) {
					return;
				}
				Bundle bundle = data.getExtras();
				mDateTime = bundle.getString("date_time");
				Log.v(TAG, "onActivityResult date_time="+mDateTime);
				mTime.setText(mDateTime);
			}
		}
		Log.v(TAG, "onActivityResult requestCode="+requestCode+" resultCode="+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
	}

	public TallyRaw GetTallyRaw() {
		TallyRaw tally = new TallyRaw();
		tally.mTallyMode = TallyRaw.TALLY_MODE_EXPEND;
		try {
			tally.mAmount = Double.parseDouble(mAmount.getText().toString());
		} catch (NumberFormatException e) {
			tally.mAmount = 0;
		}
		tally.mType      = mTypeNo;
		tally.mAccount   = mAccountNo;
		tally.mTime      = mDateTime;
		tally.mRemark    = mRemark.getText().toString();
		
		return tally;
	}
	
	public void ClearContent() {
		mAmount.setText("");
		mRemark.setText("");
	}
	
	private void InitTypeSpinner() {
		mType.setAdapter(new ArrayAdapter<String>(getActivity(), 
				                                  R.layout.fragment_add_spinner_text, 
				                                  mTypeMgr.GetTypeNameList(TallyRaw.TALLY_MODE_EXPEND)));
		mType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mTypeNo = arg2+1;
				arg0.setVisibility(View.VISIBLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				arg0.setVisibility(View.VISIBLE);
			}
		});
		
		// smart selection
		SmartSelection ss = SmartSelection.Instance(mActivity);
		if (null != ss) {
			mTypeNo = ss.GetExpendTypeId();
			mType.setSelection(mTypeNo-1);
		}
		
	}
	
	private void InitAccountSpinner() {
		mAccount.setAdapter(new ArrayAdapter<String>(getActivity(), 
													 R.layout.fragment_add_spinner_text, 
				                                     mAccountMgr.GetAccountNameList()));
		mAccount.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mAccountNo = arg2+1;
				arg0.setVisibility(View.VISIBLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				arg0.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private void InitTimeText() {
		Time cur = new Time();
		cur.setToNow();
		mDateTime = cur.format("%Y-%m-%d %H:%M");
		mTime.setText(mDateTime);
	}

	@Override
	public void SetContent(TallyRaw tally) {
		if (-1 != tally.mAmount) {
			Double a = tally.mAmount;
			mAmount.setText(a.toString());
		}
		
		if (-1 != tally.mType) {
			mType.setSelection(tally.mType-1);
		}
		
		if (-1 != tally.mAccount) {
			mAccount.setSelection(tally.mAccount-1);
		}
		
		if (null != tally.mTime) {
			mTime.setText(tally.mTime);
		}
		
		if (null != tally.mRemark) {
			mRemark.setText(tally.mRemark);
		}
	}
}
