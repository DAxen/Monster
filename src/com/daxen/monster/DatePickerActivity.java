/**
 * 
 */
package com.daxen.monster;

import com.daxen.monster.utils.ErrorCode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

/**
 * @author  Gsy
 * @version 1.0
 * @date    2013-7-31
 * @description
 * 选择日期的dialog
 */
public class DatePickerActivity extends Activity {
	private static final String TAG = "DatePickerActivity";
	private Button     mButtonCancle;
	private Button     mButtonNext;
	private DatePicker mDatePicker;
	
	private String mDate;
	
	private class PrevButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			finish();
		}
	}
	
	private class OkButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			mDate = GetDatePickerDate(mDatePicker);
			
			Intent iRes = new Intent();  
			iRes.putExtra("date", mDate);  
	        setResult((int)ErrorCode.SUCCESS, iRes);
			finish();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_date_picker_dialog);
		mButtonCancle = (Button)findViewById(R.id.date_picker_prev);
		mButtonNext   = (Button)findViewById(R.id.date_picker_ok);
		mDatePicker   = (DatePicker)findViewById(R.id.time_picker_date);
		
		mButtonCancle.setOnClickListener(new PrevButtonClickListener());
		mButtonNext.setOnClickListener(new OkButtonClickListener());
				
		Log.v(TAG, "onCreate");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.v(TAG, "onStart");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.v(TAG, "onPause");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.v(TAG, "onRestart");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");
	}
	
	private String GetDatePickerDate(DatePicker datePicker) {
		int year = datePicker.getYear();
		int mont = datePicker.getMonth();
		int daym = datePicker.getDayOfMonth();
		Time cur = new Time();
		cur.set(0, 0, 0, daym, mont, year);
		return cur.format("%Y-%m-%d");
	}
}
