/**
 * 
 */
package com.daxen.monster;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daxen.monster.utils.AccountInfo;
import com.daxen.monster.utils.AccountManager;
import com.daxen.monster.utils.ErrorCode;
import com.daxen.monster.utils.InputManager;

/**
 * @author  Gsy
 * @version 1.0
 * @date    2013-7-28
 * @description
 * 编辑账户信息
 */
public class AccountDialogActivity extends Activity {
	private static final String TAG = "AccountDialogActivity";
	private Integer mPosition;
	private AccountManager mAccountMgr;
	private InputManager   mInputMgr;
	private EditText mName;
	private EditText mBase;
	private EditText mCard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_dialog);

        Button button = (Button)findViewById(R.id.account_dialog_ok);
        button.setOnClickListener(mOkButtonListener);
        button = (Button)findViewById(R.id.account_dialog_cancle);
        button.setOnClickListener(mCancleButtonListener);
        
        mName = (EditText)findViewById(R.id.account_dialog_name_edit);
        mBase = (EditText)findViewById(R.id.account_dialog_base_edit);
        mCard = (EditText)findViewById(R.id.account_dialog_card_edit);
        
        mAccountMgr = AccountManager.Instance(AccountDialogActivity.this);
        mInputMgr   = InputManager.Instance(AccountDialogActivity.this);
        
        // 获得传递进来的position，如果没有传递，则认为是新创建
        Bundle extras = getIntent().getExtras();
        if (null == extras) {
        	mPosition = null;
        }
        else {
        	mPosition = extras.getInt("position");
        }
		
		
		InitContent();
    }
	
    private OnClickListener mOkButtonListener = new OnClickListener() {
        public void onClick(View v) {
        	long ret = ErrorCode.SUCCESS;
        	String name = mName.getText().toString();
        	double base = 0;
        	String card = mCard.getText().toString();
        	try {
        		base = Double.parseDouble(mBase.getText().toString());
        	}
        	catch (NumberFormatException e) {
        		Toast.makeText(getApplicationContext(), R.string.add_record_check_amount_toast, Toast.LENGTH_SHORT).show();
        		return;
        	}
        	
        	if (null == mPosition) {
        		// 新创建
        		ret = mInputMgr.AddAccount(name, base, card);
        		if (ErrorCode.SUCCESS != ret) {
        			Log.e(TAG, "AddAccount return["+ret+"]");
        		}
        	}
        	else {
        		AccountInfo account = mAccountMgr.GetAccount(mPosition);
        		if (null == account) {
        			Log.e(TAG, "account not exist! position="+mPosition);
        			return;
        		}
        		// 修改已有
        		ret = mInputMgr.ModiAccount(account.GetId(), name, base, card);
        		if (ErrorCode.SUCCESS != ret) {
        			Log.e(TAG, "ModiAccount return["+ret+"]");
        		}
        	}
        	
        	AccountDialogActivity.this.finish();
        	
        }
    };

    private OnClickListener mCancleButtonListener = new OnClickListener() {
        public void onClick(View v) {
        	AccountDialogActivity.this.finish();
        }
    };
    
    private void InitContent() {
    	if (null == mPosition) {
    		return;
    	}
    	
    	AccountInfo account = mAccountMgr.GetAccount(mPosition);
    	if (null == account) {
    		return;
    	}
    	
    	mName.setText(account.GetName());
    	if (0 <= mPosition && mPosition <= 4) {
    		mName.setEnabled(false);
    	}
    	DecimalFormat format = new DecimalFormat("#0.0");
    	mBase.setText(format.format(account.GetBase()));
    	mCard.setText(account.GetCard());
    }
}
