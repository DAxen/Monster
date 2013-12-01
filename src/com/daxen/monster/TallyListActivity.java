package com.daxen.monster;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.daxen.monster.utils.ErrorCode;
import com.daxen.monster.utils.InputManager;
import com.daxen.monster.utils.OutputManager;
import com.daxen.monster.utils.TallyRaw;

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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * @author  Gsy
 * @version 1.0
 * @date    2013-5-1
 * @description
 * 流水账列表，按月显示
 * 
 */
public class TallyListActivity extends ListActivity {
	private static final String TAG = "TallyListActivity";
	private static final int CONTEXT_MENU_DEL  = Menu.FIRST;
	private static final int CONTEXT_MENU_EDIT = Menu.FIRST + 1;
	
	private OutputManager mOutputMgr;
	private InputManager  mInputMgr;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_list_record);
		mOutputMgr = OutputManager.Instance(this);
		mInputMgr  =InputManager.Instance(this);
		
		//action bar
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
		// 初始化列表内容，默认是本月的
		InitContent();
		
		// 注册list view的context menu
		registerForContextMenu(getListView());
		
		Log.v(TAG, "onCreate!");
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, TallyDetailActivity.class);
		i.putExtra("position", position);
		startActivity(i);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.menu_list_record_add:
			finish();
			break;
		case R.id.menu_list_record_statistics:
			OpenStatisticsPage();
			break;
		case R.id.menu_list_record_preference:
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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
		switch (item.getItemId()) {
		case CONTEXT_MENU_DEL:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			TallyRaw tally = mOutputMgr.GetTallyInfo((int)info.id);
			long ret = mInputMgr.DelTallyItem(tally);
			if (ret != ErrorCode.SUCCESS) {
				Log.v(TAG, "DelTallyItem return["+ret+"]");
			}
			Toast.makeText(getApplicationContext(), R.string.tally_list_del_toast, Toast.LENGTH_SHORT).show();
			InitContent();
			break;
		case CONTEXT_MENU_EDIT:
			// TODO: 打开编辑界面
			break;
		}
		
		Log.v(TAG, "onContextItemSelected");
		
		return super.onContextItemSelected(item);
	}

	private void InitContent() {
		GregorianCalendar  curcal = new GregorianCalendar();
		setListAdapter(mOutputMgr.GetListAdapter(curcal.get(Calendar.MONTH)));
	}
	
	private void OpenStatisticsPage() {
    	Intent i = new Intent(this, StatisticsActivity.class);
    	startActivity(i);
    }
}
