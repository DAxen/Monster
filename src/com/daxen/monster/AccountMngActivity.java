package com.daxen.monster;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.daxen.monster.utils.AccountInfo;
import com.daxen.monster.utils.ErrorCode;
import com.daxen.monster.utils.InputManager;
import com.daxen.monster.utils.OutputManager;

// TODO 账户管理activity
public class AccountMngActivity extends ListActivity {
	private static final String TAG = "AccountMngActivity";
	private OutputManager mOutputMgr;
	private InputManager  mInputMgr;
	private static final int CONTEXT_MENU_DEL  = Menu.FIRST;
	private static final int CONTEXT_MENU_EDIT = Menu.FIRST + 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mOutputMgr = OutputManager.Instance(this);
		mInputMgr  = InputManager.Instance(this);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.activity_account_manage);
		
		// 注册list view的context menu
		registerForContextMenu(getListView());
		
		InitContent();
		
		Log.v(TAG, "onCreate");
	}

	@Override
	protected void onPause() {
		Log.v(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.v(TAG, "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.v(TAG, "onResume");
		// 从新初始化列表显示
		InitContent();
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.v(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.v(TAG, "onStop");
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		Log.v(TAG, "onDestroy");
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_account_mng, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.menu_account_add:
			OpenAccountDialog(0, false);
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		OpenAccountDialog(position, true);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CONTEXT_MENU_DEL,  0, R.string.list_record_context_menu_del);
		menu.add(0, CONTEXT_MENU_EDIT, 1, R.string.list_record_context_menu_edit);
		Log.v(TAG, "onCreateContextMenu");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case CONTEXT_MENU_DEL:
			AccountInfo account = mOutputMgr.GetAccount((int)info.id);
			long ret = mInputMgr.DelAccount(account.GetId());
			if (ret == ErrorCode.PARA_ERR) {
				Toast.makeText(getApplicationContext(), R.string.account_mng_delete_no_permit, Toast.LENGTH_SHORT).show();
			}
			else if (ret != ErrorCode.SUCCESS) {
				Log.v(TAG, "DelAccount return["+ret+"]");
			}
			else {
				Toast.makeText(getApplicationContext(), R.string.tally_list_del_toast, Toast.LENGTH_SHORT).show();
			}
			InitContent();
			break;
		case CONTEXT_MENU_EDIT:
			OpenAccountDialog((int)info.id, true);
			break;
		}
		
		Log.v(TAG, "onContextItemSelected id="+info.id);
		return super.onContextItemSelected(item);
	}

	private void InitContent() {
		setListAdapter(mOutputMgr.GetAccountListAdapter());
	}
	
	private void OpenAccountDialog(int position, boolean exist) {
		Intent i = new Intent(this, AccountDialogActivity.class);
		if (exist) {
			i.putExtra("position", position);
		}
		startActivity(i);
	}
}
