package com.daxen.monster;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.daxen.monster.utils.InputManager;
import com.daxen.monster.utils.OutputManager;
import com.daxen.monster.utils.TallyRaw;

// TODO 记账类目管理
public class TypeMngActivity extends ListActivity {
	private static final String TAG = "TypeMngActivity";
	private static final int CONTEXT_MENU_DEL  = Menu.FIRST;
	private static final int CONTEXT_MENU_EDIT = Menu.FIRST + 1;
	
	private OutputManager mOutputMgr;
	private InputManager  mInputMgr;
	
	@Override
	protected void onDestroy() {
		Log.v(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mOutputMgr = OutputManager.Instance(this);
		mInputMgr  = InputManager.Instance(this);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.activity_type_manage);
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_type_mng, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.menu_type_add:
			
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

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
			
			break;
		case CONTEXT_MENU_EDIT:
			
			break;
		}
		
		Log.v(TAG, "onContextItemSelected id="+info.id);
		return super.onContextItemSelected(item);
	}
	
	private void InitContent() {
		setListAdapter(mOutputMgr.GetTypeListAdapter(TallyRaw.TALLY_MODE_EXPEND));
	}
}
