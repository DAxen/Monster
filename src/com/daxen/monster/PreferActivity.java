package com.daxen.monster;

import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.view.MenuItem;

import com.daxen.monster.utils.AlarmReceiver;
import com.daxen.monster.utils.AlmEvenReceiver;
import com.daxen.monster.utils.AlmMorReceiver;
import com.daxen.monster.utils.AlmNightReceiver;
import com.daxen.monster.utils.AlmNoonReceiver;

public class PreferActivity extends PreferenceActivity {
	//private static final String TAG = "PreferenceActivity";
	//private static final String SHARED_PREFER_NAME = "prefer";
	private Preference mPreferAccount;
	private Preference mPreferType;
	private SwitchPreference mPreferNotif;
	private Preference mPreferBugReport;
	
	private int REQ_CODE_MORNING = 1;
	private int REQ_CODE_NOON    = 1;
	private int REQ_CODE_EVENING = 1;
	private int REQ_CODE_NIGHT   = 1;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// 初始化设置列表
		addPreferencesFromResource(R.xml.activity_preference);
		
		// ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		mPreferAccount = findPreference("prefer_func_account_mng");
		mPreferType    = findPreference("prefer_func_type_mng");
		mPreferNotif   = (SwitchPreference)findPreference("prefer_switch_notification");
		mPreferBugReport = findPreference("prefer_func_bug_report");
		if (null != mPreferAccount) {
			Intent iAccount = new Intent(this, AccountMngActivity.class);
			mPreferAccount.setIntent(iAccount);
		}
		
		if (null != mPreferType) {
			Intent iType = new Intent(this, TypeMngActivity.class);
			mPreferType.setIntent(iType);
		}
		
		if (null != mPreferNotif) {
			mPreferNotif.setOnPreferenceChangeListener(new OnPreferNotifChangeListener());
		}
		
		if (null != mPreferBugReport) {
			Intent iReport = new Intent(this, BugReportActivity.class);
			mPreferBugReport.setIntent(iReport);
		}
	}
	
	class OnPreferNotifChangeListener implements Preference.OnPreferenceChangeListener  {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			Boolean swPrefer = (Boolean)newValue;
			if (null == swPrefer) {
				return false;
			}
			
			if (swPrefer) {
				// Enable Notification
				// Toast.makeText(getApplicationContext(), "Notification On", Toast.LENGTH_SHORT).show();
				EnableNotification();
			} else {
				// Disable Notification
				// Toast.makeText(getApplicationContext(), "Notification Off", Toast.LENGTH_SHORT).show();
				DisableNotification();
			}
			return true;
		}
		
	}
	
	@Override
	public void onBuildHeaders(List<Header> target) {
		super.onBuildHeaders(target);
		
		//loadHeadersFromResource(R.xml.activity_prefer_headers, target);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			BackToHome();
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void BackToHome() {
		finish();
	}
	
	// 设置早中晚三次提醒
	private void EnableNotification() {
		// get alarm service
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        
		////////////////////  morning  ////////////////////
		Intent iMor = new Intent(this, AlmMorReceiver.class);
		iMor.putExtra("period", "morning");
		PendingIntent senderMor = PendingIntent.getBroadcast(this, REQ_CODE_MORNING, iMor, 0);
		
		// morning start time 9:00
		Calendar calMor = Calendar.getInstance();
		calMor.setTimeInMillis(System.currentTimeMillis());
		calMor.set(Calendar.HOUR_OF_DAY, 9);
		calMor.set(Calendar.MINUTE, 0);
		calMor.set(Calendar.SECOND, 0);
        
        // repeat every day
        am.setRepeating(AlarmManager.RTC_WAKEUP
                , calMor.getTimeInMillis(), 86400*1000, senderMor);
        
        ////////////////////  noon  ////////////////////
        Intent iNoon = new Intent(this, AlmNoonReceiver.class);
        iNoon.putExtra("period", "noon");
		PendingIntent senderNoon = PendingIntent.getBroadcast(this, REQ_CODE_NOON, iNoon, 0);
		
		// noon start time 12:00
		Calendar calNoon = Calendar.getInstance();
		calNoon.setTimeInMillis(System.currentTimeMillis());
		calNoon.set(Calendar.HOUR_OF_DAY, 12);
		calNoon.set(Calendar.MINUTE, 0);
		calNoon.set(Calendar.SECOND, 0);
        
        // repeat every day
        am.setRepeating(AlarmManager.RTC_WAKEUP
                , calNoon.getTimeInMillis(), 86400*1000, senderNoon);
        
        ////////////////////  evening  ////////////////////
        Intent iEven = new Intent(this, AlmEvenReceiver.class);
        iEven.putExtra("period", "evening");
        PendingIntent senderEven = PendingIntent.getBroadcast(this, REQ_CODE_EVENING, iEven, 0);
        
        // evening start time 18:00
        Calendar calEven = Calendar.getInstance();
        calEven.setTimeInMillis(System.currentTimeMillis());
        calEven.set(Calendar.HOUR_OF_DAY, 18);
        calEven.set(Calendar.MINUTE, 0);
        calEven.set(Calendar.SECOND, 0);
		
		// repeat every day
		am.setRepeating(AlarmManager.RTC_WAKEUP
                , calEven.getTimeInMillis(), 86400*1000, senderEven);
		
		////////////////////  night  ////////////////////
		Intent iNight = new Intent(this, AlmNightReceiver.class);
		iNight.putExtra("period", "night");
		PendingIntent senderNight = PendingIntent.getBroadcast(this, REQ_CODE_NIGHT, iNight, 0);

		// evening start time 18:00
		Calendar calNight = Calendar.getInstance();
		calNight.setTimeInMillis(System.currentTimeMillis());
		calNight.set(Calendar.HOUR_OF_DAY, 21);
		calNight.set(Calendar.MINUTE, 30);
		calNight.set(Calendar.SECOND, 0);

		// repeat every day
		am.setRepeating(AlarmManager.RTC_WAKEUP
				, calNight.getTimeInMillis(), 86400*1000, senderNight);
	}
	
	private void DisableNotification() {
		// get alarm service
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		
		// cancel morning
		Intent iMor = new Intent(this, AlmMorReceiver.class);
		iMor.putExtra("period", "morning");
		PendingIntent senderMor = PendingIntent.getBroadcast(this, REQ_CODE_MORNING, iMor, 0);
		am.cancel(senderMor);
		
		// cancel noon
		Intent iNoon = new Intent(this, AlmNoonReceiver.class);
		iNoon.putExtra("period", "noon");
		PendingIntent senderNoon = PendingIntent.getBroadcast(this, REQ_CODE_NOON, iNoon, 0);
		am.cancel(senderNoon);
		
		// cancel evening
		Intent iEven = new Intent(this, AlmEvenReceiver.class);
		iEven.putExtra("period", "evening");
		PendingIntent senderEven = PendingIntent.getBroadcast(this, REQ_CODE_EVENING, iEven, 0);
		am.cancel(senderEven);
		
		// cancel night
		Intent iNight = new Intent(this, AlmNightReceiver.class);
		iNight.putExtra("period", "night");
		PendingIntent senderNight = PendingIntent.getBroadcast(this, REQ_CODE_NIGHT, iNight, 0);
		am.cancel(senderNight);
	}
}
