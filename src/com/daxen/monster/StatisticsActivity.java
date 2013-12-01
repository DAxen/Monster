/**
 * 
 */
package com.daxen.monster;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.daxen.monster.fragments.DifferenceStatisFragment;
import com.daxen.monster.fragments.ExpendStatisFragment;
import com.daxen.monster.fragments.IncomeStatisFragment;

/**
 * @author  Gsy
 * @version 1.0
 * @date    2013-5-1
 * @description
 * 显示统计图表，通过fragment来显示不同的内容
 * 
 */
public class StatisticsActivity extends Activity implements ActionBar.TabListener {
	private static final String TAG = "StatisticsActivity";
	
	private Fragment mFrag;
	private FragmentManager mFragMgr;
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_statistics);
		mFragMgr = getFragmentManager();
		
		// Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        // For each of the sections in the activity, add a tab to the action bar.
        actionBar.addTab(actionBar.newTab().setText(R.string.statistics_tab_difference).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.statistics_tab_expend).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.statistics_tab_income).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.statistics_tab_account_balance).setTabListener(this));
        
        Log.v(TAG, "onCreate");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		switch (tab.getPosition()) {
		case 0:
			ShowDifferenceStatisFragment();
			break;
		case 1:
			ShowExpendStatisFragment();
			break;
		case 2:
			ShowImcomeStatisFragment();
			break;
		case 3:
			ShowAccountStatisFragment();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
	
	private void BackToHome() {
		finish();
	}
	
	private void ShowDifferenceStatisFragment() {
		mFrag = new DifferenceStatisFragment();
		FragmentTransaction transaction = mFragMgr.beginTransaction();
		transaction.replace(R.id.statis_fragment_container, mFrag);
		transaction.commit();
	}
	
	private void ShowExpendStatisFragment() {
		mFrag = new ExpendStatisFragment();
		FragmentTransaction transaction = mFragMgr.beginTransaction();
		transaction.replace(R.id.statis_fragment_container, mFrag);
		transaction.commit();
	}
	
	private void ShowImcomeStatisFragment() {
		mFrag = new IncomeStatisFragment();
		FragmentTransaction transaction = mFragMgr.beginTransaction();
		transaction.replace(R.id.statis_fragment_container, mFrag);
		transaction.commit();
	}
	
	private void ShowAccountStatisFragment() {
		
	}
}
