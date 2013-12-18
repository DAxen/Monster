/**
 * 
 */
package com.daxen.monster.utils;

import com.daxen.monster.AddRecordActivity;
import com.daxen.monster.R;
import com.daxen.monster.fragments.SmsParser;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * @author 圣阳
 * 截获用户短信
 */
public class SmsReveiver extends BroadcastReceiver {
    private static final String TAG = "SmsReveiver";
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, "onReceive");
		if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
			SmsMessage[] messages = getMessagesFromIntent(intent);
			for (SmsMessage message : messages) {
				Log.i(TAG, message.getOriginatingAddress() + " : " +
	                  message.getDisplayOriginatingAddress() + " : " +
	                  message.getDisplayMessageBody() + " : " +
	                  message.getTimestampMillis());
				TallyRaw tally = SmsParser.ParseTally(message.getDisplayMessageBody());
				if (null != tally) {
					// notify
					NotifyAddRecord(context, tally); 
				}
			}
		}
	}
	
	public final SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }

        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];

        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }

        return msgs;
    }
	
	static Intent[] makeMessageIntentStack(Context context, TallyRaw tally) {
        // A typical convention for notifications is to launch the user deeply
        // into an application representing the data in the notification; to
        // accomplish this, we can build an array of intents to insert the back
        // stack stack history above the item being displayed.
        Intent[] intents = new Intent[1];

        // First: root activity of ApiDemos.
        // This is a convenient way to make the proper Intent to launch and
        // reset an application's task.
        intents[0] = Intent.makeRestartActivityTask(new ComponentName(context,
                     AddRecordActivity.class));
        intents[0].putExtra("intent_type", AddRecordActivity.ADD_RECORD_INTENT_SMS);
        intents[0].putExtra("mode",    tally.mTallyMode);
        intents[0].putExtra("account", tally.mAccount);
        intents[0].putExtra("time",    tally.mTime);
        intents[0].putExtra("amount",  tally.mAmount);

        return intents;
    }
	
	@SuppressLint("NewApi")
	private void NotifyAddRecord(Context context, TallyRaw tally) {
		// get NotificationMananger
		NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

		// get PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivities(context, 0,
				makeMessageIntentStack(context, tally), PendingIntent.FLAG_CANCEL_CURRENT);

		// get information
		String contentTitle = context.getString(R.string.notif_sms_content_title);
		String contentText  = context.getString(R.string.notif_sms_content_text);
		String tickerText   = context.getString(R.string.notif_sms_ticker_text);

		// build notification
		Notification notif = new Notification.Builder(context).setContentIntent(contentIntent)
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker(tickerText)
				.setContentTitle(contentTitle)
				.setContentText(contentText)
				.setDefaults(Notification.DEFAULT_ALL)
				.setSound(null)
				.setVibrate(null)
				.build();
		
		notif.flags |= Notification.FLAG_AUTO_CANCEL;

		// notify
		nm.notify(R.string.notif_sms_ticker_text, notif);
	}

}
