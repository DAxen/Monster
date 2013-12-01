/**
 * 
 */
package com.daxen.monster;

import com.daxen.monster.utils.ErrorCode;
import com.daxen.monster.utils.RequestCode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.util.TimeFormatException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * @author  Gsy
 * @version 1.0
 * @date    2013-7-31
 * @description
 * 选择时间的dialog类
 */
public class TimePickerActivity extends Activity {
	private static final String TAG = "TimePickerActivity";
	private Button     mButtonCancle;
	private Button     mButtonNext;
	private String     mTime;
	private String     mDate;
	private TimePicker mTimePicker;
	
	private class CancleButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			finish();
		}
	}
	
	private class NextButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			mTime = GetTimePickerTime(mTimePicker);
			Intent i = new Intent(TimePickerActivity.this, DatePickerActivity.class);
			i.putExtra("time", mTime);
			startActivityForResult(i, RequestCode.DATE);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_time_picker_dialog);
		mButtonCancle = (Button)findViewById(R.id.time_picker_cancle);
		mButtonNext   = (Button)findViewById(R.id.time_picker_next);
		mTimePicker   = (TimePicker)findViewById(R.id.timePicker1);
		
		mButtonCancle.setOnClickListener(new CancleButtonClickListener());
		mButtonNext.setOnClickListener(new NextButtonClickListener());
				
		Time cur = new Time();
		cur.setToNow();
		mDate = cur.format("%Y-%m-%d");
		
		InitTimePicker();
		
		Log.v(TAG, "onCreate");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RequestCode.DATE == requestCode) {
			if (ErrorCode.SUCCESS == resultCode) {
				if (null == data) {
					return;
				}
				Bundle bundle = data.getExtras();
				mDate = bundle.getString("date");
				Log.v(TAG, "onActivityResult date="+mDate);
				
				Intent iRes = new Intent();  
				iRes.putExtra("date_time", mDate+" "+mTime);  
		        setResult((int)ErrorCode.SUCCESS, iRes);
				finish();
			}
		}
		Log.v(TAG, "onActivityResult requestCode="+requestCode+" resultCode="+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
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
	
	private void InitTimePicker() {
		if (null == mTime) {
			return;
		}
		SetTimePickerTime(mTimePicker, mTime);
	}
	
	private String GetTimePickerTime(TimePicker timePicker) {
		int hour = timePicker.getCurrentHour();
		int minu = timePicker.getCurrentMinute();
		Time cur = new Time();
		cur.set(0, minu, hour, 0, 0, 0);
		return cur.format("%H:%M");
	}
	
	private void SetTimePickerTime(TimePicker timePicker, String time) {
		Time cur = new Time();
		String parseTime = "2008-10-13T"+time+".000Z";
		try {
			cur.parse3339(parseTime);
		}
		catch (TimeFormatException e) {
			cur.setToNow();
		}
		timePicker.setCurrentHour(cur.hour);
		timePicker.setCurrentMinute(cur.minute);
	}
}
