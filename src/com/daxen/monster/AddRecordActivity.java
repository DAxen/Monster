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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.daxen.monster.fragments.AddExpendFragment;
import com.daxen.monster.fragments.AddIncomeFragment;
import com.daxen.monster.fragments.AddRecordFragment;
import com.daxen.monster.fragments.SmsParser;
import com.daxen.monster.utils.ErrorCode;
import com.daxen.monster.utils.InputManager;
import com.daxen.monster.utils.TallyRaw;

/**
 * @author  Gsy
 * @version 1.0
 * @date    2013-4-29
 * @description
 * ��Ӽ�¼�Ľ�����
 * 
 */
public class AddRecordActivity extends Activity implements ActionBar.TabListener  {
	public  static int ADD_RECORD_INTENT_SMS   = 0;
	public  static int ADD_RECORD_INTENT_EDIT = 1;
	private static final String TAG = "AddRecordActivity";
	
	private InputManager    mInputMgr;
	private FragmentManager mFragMgr;
	private Button          mButtonSave;
	private Fragment        mFrag;
	private String          mTime;
	
	private class ButtonSaveClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			SaveCurItem();
			
			// ����ϴ���д�����ݣ�׼����ʼ�µ�һ��
			AddRecordFragment fragment = (AddRecordFragment)mFragMgr.findFragmentById(R.id.add_record_frag_container);
			fragment.ClearContent();
		}
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_record);
		
        mInputMgr = InputManager.Instance(this);
        mFragMgr  = getFragmentManager();
        mButtonSave     = (Button)findViewById(R.id.add_record_button_save);
        
        mButtonSave.setOnClickListener(new ButtonSaveClickListener());
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        
        // For each of the sections in the activity, add a tab to the action bar.
        actionBar.addTab(actionBar.newTab().setText(R.string.add_record_mode_expend).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(R.string.add_record_mode_income).setTabListener(this));
        
        Bundle extras = getIntent().getExtras();
        mTime = extras != null ? extras.getString("date_time") : null;
        Log.v(TAG, "extras time:"+mTime);
        
        Log.v(TAG, "onCreate success!");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.v(TAG, "onPause");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.v(TAG, "onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
	}

	@Override
	protected void onStart() {
		super.onStart();
		SmsAddRecord();
        EditRecord();
		Log.v(TAG, "onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_add_record_nav, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			BackToHome();
			break;
		//case R.id.menu_add_record_save:
			//SaveCurItem();
		case R.id.menu_add_record_list:
			OpenListPage();
			break;
		case R.id.menu_add_record_statistics:
			OpenStatisticsPage();
			break;
		case R.id.menu_add_record_preference:
			OpenPreferencePage();
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		Log.v(TAG, "onTabReselected:"+(tab.getPosition()));
		switch (tab.getPosition()) {
		case 0:
			ShowExpendFragment();
			break;
		case 1:
			ShowIncomeFragment();
			break;
		default:
			break;
		}
		Log.v(TAG, "onTabReselected");
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.v(TAG, "onTabSelected:"+(tab.getPosition()));
		switch (tab.getPosition()) {
		case 0:
			ShowExpendFragment();
			break;
		case 1:
			ShowIncomeFragment();
			break;
		default:
			break;
		}
		Log.v(TAG, "onTabSelected");
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		Log.v(TAG, "onTabUnselected");
	}
	
	@Override
	public void onBackPressed() {
		// ���·��ؼ���ʱ����������ص�
		super.onBackPressed();
		Log.v(TAG, "onBackPressed");
	}
	
	

	private void BackToHome() {
		/*Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
		finish();
	}
	
	private void ShowExpendFragment() {
		mFrag = new AddExpendFragment();
		FragmentTransaction transaction = mFragMgr.beginTransaction();
		transaction.replace(R.id.add_record_frag_container, mFrag);
		transaction.commit();
	}
	
	private void ShowIncomeFragment() {
		mFrag = new AddIncomeFragment();
		FragmentTransaction transaction = mFragMgr.beginTransaction();
		transaction.replace(R.id.add_record_frag_container, mFrag);
		transaction.commit();
	}
	
	private void SaveCurItem() {
		if (null == mFrag) {
			Log.v(TAG, "fragment is null!");
			return;
		}
		
		AddRecordFragment fragment = (AddRecordFragment)mFrag;
		TallyRaw tally = fragment.GetTallyRaw();
		if (null == tally)
		{
			Log.v(TAG, "fragment GetTallyRaw null!");
			return;
		}

		long ret = mInputMgr.AddTallyItem(tally);
		if (ErrorCode.SUCCESS != ret) {
			Toast.makeText(getApplicationContext(), R.string.add_record_check_amount_toast, Toast.LENGTH_SHORT).show();
			Log.e(TAG, "addtally return["+ret+"]");
			return;
		}
		
		// Toast
		Toast.makeText(getApplicationContext(), R.string.add_record_save_toast, Toast.LENGTH_SHORT).show();
	}
	
	private void OpenListPage() {
    	Intent i = new Intent(this, TallyListActivity.class);
    	startActivity(i);
    }
	
	private void OpenStatisticsPage() {
    	Intent i = new Intent(this, StatisticsActivity.class);
    	startActivity(i);
    }
	
	private void OpenPreferencePage() {
		Intent i = new Intent(this, PreferActivity.class);
		startActivity(i);
	}
	
	private void SmsAddRecord() {
		Bundle extras = getIntent().getExtras();
		int intentType = extras != null ? extras.getInt("intent_type",	-1) : -1;
        
        if (ADD_RECORD_INTENT_SMS != intentType) {
        	return;
        }
        
        TallyRaw tally = new TallyRaw();
        tally.mId        = extras.getInt("id", -1);
        tally.mTallyMode = extras.getInt("mode", -1);
        tally.mAmount    = extras.getDouble("amount", -1);
        tally.mType      = extras.getInt("type", -1);
        tally.mAccount   = extras.getInt("account", -1);
        tally.mTime      = extras.getString("time", null);
        tally.mRemark    = extras.getString("remark", null);
        
        final ActionBar actionBar = getActionBar();
        actionBar.setSelectedNavigationItem(1-tally.mTallyMode);
        
        AddRecordFragment fragment = (AddRecordFragment)mFragMgr.findFragmentById(R.id.add_record_frag_container);
        fragment.SetContent(tally);
	}
	
	private void EditRecord() {
		Log.v(TAG, "EditRecord");
		Bundle extras = getIntent().getExtras();
		int intentType = extras != null ? extras.getInt("intent_type",	-1) : -1;
        
        if (ADD_RECORD_INTENT_EDIT != intentType) {
        	return;
        }
        
        TallyRaw tally = new TallyRaw();
        tally.mId        = extras.getInt("id", -1);
        tally.mTallyMode = extras.getInt("mode", -1);
        tally.mAmount    = extras.getDouble("amount", -1);
        tally.mType      = extras.getInt("type", -1);
        tally.mAccount   = extras.getInt("account", -1);
        tally.mTime      = extras.getString("time", null);
        tally.mRemark    = extras.getString("remark", null);
        
        final ActionBar actionBar = getActionBar();
        actionBar.setSelectedNavigationItem(1-tally.mTallyMode);
        Log.v(TAG, "setSelectedNavigationItem:"+(1-tally.mTallyMode));
        
        AddRecordFragment fragment = (AddRecordFragment)mFrag;
        if (null != fragment) {
        	fragment.SetContent(tally);
        	Log.v(TAG, "frag SetContent");
        }
	}
}
