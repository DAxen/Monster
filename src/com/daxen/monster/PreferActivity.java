package com.daxen.monster;

import java.util.List;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.view.MenuItem;

import com.daxen.monster.utils.AlarmReceiver;

public class PreferActivity extends PreferenceActivity {
	private static final String SHARED_PREFER_NAME = "prefer";
	private Preference mPreferAccount;
	private Preference mPreferType;
	private SwitchPreference mPreferNotif;
	private Preference mPreferBugReport;
	
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
	
	private void EnableNotification() {
		Intent i = new Intent(this, AlarmReceiver.class);
		i.putExtra("test", "hello world");
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, i, 0);
		// Alarm start time
		long firstime=SystemClock.elapsedRealtime();
		
		// Schedule the alarm!
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        
        // repeat every 6 hours
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP
                , firstime, 21600*1000, sender);
	}
	
	private void DisableNotification() {
		Intent i = new Intent(this, AlarmReceiver.class);
		i.putExtra("test", "hello world");
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, i, 0);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.cancel(sender);
	}
}
