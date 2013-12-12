/**
 * 
 */
package com.daxen.monster;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author 圣阳
 *
 */
public class BugReportActivity extends Activity {
    private static final String TAG = "BugReportActivity";
	private EditText mTxtEmail;
	private EditText mTxtContent;
	private Button   mBtnCommit;
    
	private class CommitButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO: 线程这里一知半解，最好深入了解一下
			new Thread() {
				public void run() {
					Looper.prepare();
					BugReport();
				}
			}.start();
		}
	}
	
	// 需要起一个新的线程，否则会抛出异常NetworkOnMainThreadException
	void BugReport() {
		// URL
		String httpUrl = "http://192.168.2.100/Monster/BugReport.php";
		// HttpPost对象
		HttpPost httpRequest = new HttpPost(httpUrl);
		// POST参数
		List<NameValuePair> para = GetPostInfo();
		
		try {
			// 字符集，这里会抛出异常
			HttpEntity httpEntity = new UrlEncodedFormEntity(para, "utf-8");
			httpRequest.setEntity(httpEntity);
			// HttpClient对象
			HttpClient httpClient = new DefaultHttpClient();
			try {
				// 取得HttpResponse
				HttpResponse httpResp = httpClient.execute(httpRequest);
				if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// POST请求成功，获取返回的字符串
					String strResult = EntityUtils.toString(httpResp.getEntity());
					Log.v(TAG, "Result:"+strResult);
				    Toast.makeText(getApplicationContext(), R.string.bug_report_toast, Toast.LENGTH_SHORT).show();
					finish();
				}
				else {
					Toast.makeText(getApplicationContext(), R.string.bug_report_toast_fail, Toast.LENGTH_SHORT).show();
				}
			}
			catch (IOException e) {
				Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
				return;
			}
		} 
		catch (UnsupportedEncodingException e) {
			Toast.makeText(getApplicationContext(), "UnsupportedEncodingException", Toast.LENGTH_SHORT).show();
			return;
		}
	}
	
	List<NameValuePair> GetPostInfo() {
		TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE); 
		DisplayMetrics dm = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		List<NameValuePair> para = new ArrayList<NameValuePair>();
		para.add(new BasicNameValuePair("email", mTxtEmail.getText().toString()));
		para.add(new BasicNameValuePair("content", mTxtContent.getText().toString()));
		
		// 获取设备信息
		// IP
		String ipAddr = GetLocalIpAddress();
		Log.v(TAG, "IP: "+ipAddr);
		para.add(new BasicNameValuePair("user_ip", ipAddr));
		
		// Device Model
		Log.v(TAG, "Model:"+android.os.Build.MODEL);
		para.add(new BasicNameValuePair("device_model", android.os.Build.MODEL));
		
		// Device Version
		Log.v(TAG, "Version.Release:"+android.os.Build.VERSION.RELEASE);
		para.add(new BasicNameValuePair("device_android_ver", android.os.Build.VERSION.RELEASE));
				
		// Device ID
		String devID = tm.getDeviceId();
		Log.v(TAG, "DevId: "+devID);
		para.add(new BasicNameValuePair("device_id", devID));
		
		// PhoneNumber
		String phoneNum = tm.getLine1Number();
		Log.v(TAG, "Number: "+phoneNum);
		para.add(new BasicNameValuePair("device_phone_num", phoneNum));
		
		// Resolution
		String resolution = dm.heightPixels+"*"+dm.widthPixels;
		Log.v(TAG, "Resolution: "+resolution);
		para.add(new BasicNameValuePair("device_resolu", resolution));
		
		// Density
		Log.v(TAG, "Density: "+dm.densityDpi);
		para.add(new BasicNameValuePair("device_density", ""+dm.densityDpi));
		
		return para;
	}
	
	// 获取IP地址
	public String GetLocalIpAddress() {  
		String ipaddress="";

		try {  
			for (Enumeration<NetworkInterface> en = NetworkInterface  
					.getNetworkInterfaces(); en.hasMoreElements();) {  
				NetworkInterface intf = en.nextElement();  
				for (Enumeration<InetAddress> enumIpAddr = intf  
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
					InetAddress inetAddress = enumIpAddr.nextElement();  
					if (!inetAddress.isLoopbackAddress()) {  
						ipaddress=ipaddress+";"+ inetAddress.getHostAddress().toString();  
					}  
				}  
			}  
		} catch (SocketException ex) {  
			Log.e("WifiPreference IpAddress", ex.toString());  
		}  
		return ipaddress; 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        // 配置布局文件
		setContentView(R.layout.activity_bug_report);
		
		//action bar
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        // 获取控件
        mTxtEmail   = (EditText)findViewById(R.id.bug_report_email_text);
        mTxtContent = (EditText)findViewById(R.id.bug_report_content_text);
        mBtnCommit  = (Button)findViewById(R.id.bug_report_button);
        
        mBtnCommit.setOnClickListener(new CommitButtonListener());
        
        Log.v(TAG, "onCreate");
	}

	@Override
	public void onBackPressed() {
		finish();
		Log.v(TAG, "onBackPressed");
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
			finish();
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
}
