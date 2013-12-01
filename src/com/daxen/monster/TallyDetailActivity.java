
package com.daxen.monster;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.daxen.monster.utils.ErrorCode;
import com.daxen.monster.utils.OutputManager;

/**
 * @author  Gsy
 * @version 1.0
 * @date    2013-5-1
 * @description
 * 显示账目的详细信息，并可对账目进行编辑，删除等操作
 * 
 */
public class TallyDetailActivity extends Activity {
	private static final String TAG = "TallyDetailActivity";
	
	private TextView mTextTallyMode;
	private TextView mTextAmount;
	private TextView mTextType;
	private TextView mTextAccount;
	private TextView mTextTime;
	private TextView mTextRemark;
	
	private Integer mPosition; // 这里应该保存的是output manager里面的sorted list的位置
	private OutputManager mOutputMgr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        //load layout files
        setContentView(R.layout.activity_tally_detail);
        //action bar
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        //get view
        mTextTallyMode = (TextView)findViewById(R.id.detail_tallymode_title);
        mTextAmount    = (TextView)findViewById(R.id.detail_amount_content);
        mTextType      = (TextView)findViewById(R.id.detail_type_content);
        mTextAccount   = (TextView)findViewById(R.id.detail_account_content);
		mTextTime      = (TextView)findViewById(R.id.detail_time_content);
		mTextRemark    = (TextView)findViewById(R.id.detail_remark_content);
        
		mOutputMgr = OutputManager.Instance(this);
		
		mPosition = (savedInstanceState == null) ? null :
            (Integer) savedInstanceState.getSerializable("position");
		if (mPosition == null) {
			Bundle extras = getIntent().getExtras();
			mPosition = extras != null ? extras.getInt("position") : null;
		}
		
		InitContent();
		
		Log.v(TAG, "onCreate!");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");
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
		Log.v(TAG, "onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_tally_detail, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.menu_detail_edit:
			//跳转至编辑页面
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void InitContent() {
		if (null == mPosition) {
			mTextTallyMode.setText(R.string.public_text_error);
			return;
		}
		long ret = ErrorCode.SUCCESS;
		ret |= mOutputMgr.SetDetailTextMode(mTextTallyMode, mPosition);
		ret |= mOutputMgr.SetDetailTextAmount(mTextAmount, mPosition);
		ret |= mOutputMgr.SetDetailTextType(mTextType, mPosition);
		ret |= mOutputMgr.SetDetailTextAccount(mTextAccount, mPosition);
		ret |= mOutputMgr.SetDetailTextTime(mTextTime, mPosition);
		ret |= mOutputMgr.SetDetailTextRemark(mTextRemark, mPosition);
		if (ErrorCode.SUCCESS != ret) {
			Log.e(TAG, "InitContent return["+ret+"]");
		}
	}
	
}
