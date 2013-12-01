package com.daxen.monster;

import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class PreferActivity extends PreferenceActivity {
	private static final String SHARED_PREFER_NAME = "prefer";
	private Preference mPreferAccount;
	private Preference mPreferType;
	
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
		if (null != mPreferAccount) {
			Intent iAccount = new Intent(this, AccountMngActivity.class);
			mPreferAccount.setIntent(iAccount);
		}
		
		if (null != mPreferType) {
			Intent iType = new Intent(this, TypeMngActivity.class);
			mPreferType.setIntent(iType);
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
}
