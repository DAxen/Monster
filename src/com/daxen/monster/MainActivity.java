package com.daxen.monster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.daxen.monster.utils.ErrorCode;
import com.daxen.monster.utils.OutputManager;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
			
	private TextView    mTextIncomeValue;
	private TextView    mTextExpendValue;
	private TextView    mTextDiffValue;
	private ImageView   mImageSummary;
	private Button      mButtonStatistic;
	private Button      mButtonDetail;
	private Button      mButtonAdd;
	private TableLayout mTableLatestRecord;
	private TableRow    mRowNoRecord;
	private FrameLayout mImageFrame;
	private OutputManager mOutputMgr;
	
	// 查看统计按钮监听器
	class ButtonStatisticClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			OpenStatisticsPage();
		}
	}
	
	// 查看流水账按钮监听器
	class ButtonDetailClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			OpenListPage();
		}
	}
	
	// 快速添加按钮监听器
	class ButtonAddClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			OpenAddPage();
		}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取输出管理对象
        mOutputMgr = OutputManager.Instance(this);
        //Hide the Ugly title bar!!!
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 加载布局文件
        setContentView(R.layout.activity_main);
        
        // 获取布局控件
        mTextIncomeValue   = (TextView)findViewById(R.id.home_row_text_income_value);
        mTextExpendValue   = (TextView)findViewById(R.id.home_row_text_expend_value);
        mTextDiffValue     = (TextView)findViewById(R.id.home_row_text_diff_value);
        mImageSummary      = (ImageView)findViewById(R.id.home_image_summary);
        mButtonStatistic   = (Button)findViewById(R.id.home_button_viewstat);
        mButtonDetail      = (Button)findViewById(R.id.home_button_viewdetails);
        mButtonAdd         = (Button)findViewById(R.id.home_button_addrecord);
        mTableLatestRecord = (TableLayout)findViewById(R.id.home_table_latestrecords);
        mRowNoRecord       = (TableRow)findViewById(R.id.home_row_variable);
        mImageFrame        = (FrameLayout)findViewById(R.id.home_layout_summary);
        
        // 为按钮设置好监听器
        mButtonStatistic.setOnClickListener(new ButtonStatisticClickListener());
        mButtonDetail.setOnClickListener(new ButtonDetailClickListener());
        mButtonAdd.setOnClickListener(new ButtonAddClickListener());
        
        // 动态内容呈现
        // ContentPresent();
        
        Log.v(TAG, "onCreate success!");
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
		ContentPresent();
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

	// 负责初始化呈现主界面动态内容
    private void ContentPresent() {
    	long ret = ErrorCode.SUCCESS;
    	// 1.显示本月统计概览
    	ret |= mOutputMgr.SetHomeTextIncome(mTextIncomeValue);
    	ret |= mOutputMgr.SetHomeTextExpend(mTextExpendValue);
    	ret |= mOutputMgr.SetHomeTextDifference(mTextDiffValue);
    	
    	// 2.绘制统计图表
/*    	ret |= mOutputMgr.SetHomeImageSummary(mImageSummary, 
    			                              GetImageHeight(),
                                              GetImageWidth());*/
    	
    	// 3.填充最近消费记录，最多3条
    	ret |= mOutputMgr.SetHomeTableLatestRecord(mTableLatestRecord, mRowNoRecord);
    	if (ErrorCode.SUCCESS != ret) {
    		Log.e(TAG, "ContentPresent ret["+ret+"]");
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	private void OpenAddPage() {
    	Intent i = new Intent(this, AddRecordActivity.class);
    	startActivity(i);
    }
    
    private void OpenListPage() {
    	Intent i = new Intent(this, TallyListActivity.class);
    	startActivity(i);
    }
    
    private void OpenStatisticsPage() {
    	Intent i = new Intent(this, StatisticsActivity.class);
    	startActivity(i);
    }
    
    // 返回pixels
    private int GetImageHeight() {
    	DisplayMetrics metric = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(metric);
    	// actionBar height = 40dp
    	int actionBarHeight = OutputManager.GetDp2Px(this, 40);
    	int contentHeight   = metric.heightPixels - actionBarHeight;
    	int marginHeight    = OutputManager.GetDp2Px(this, 10);
    	return (int)(contentHeight/3-marginHeight);
    }
    
    // 返回pixels
    private int GetImageWidth() {
    	DisplayMetrics metric = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(metric);
    	int marginWidth  = OutputManager.GetDp2Px(this, 10);
    	// 横向黄金分割
    	return metric.widthPixels-marginWidth*2;
    }
}
