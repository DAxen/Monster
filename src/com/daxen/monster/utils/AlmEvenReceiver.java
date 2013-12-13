/**
 * 
 */
package com.daxen.monster.utils;

import com.daxen.monster.AddRecordActivity;
import com.daxen.monster.R;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author Ê¥Ñô
 *
 */
public class AlmEvenReceiver extends BroadcastReceiver {
	private static final String TAG = "AlmEvenReceiver";
	
	static Intent[] makeMessageIntentStack(Context context) {
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
        intents[0].putExtra("period", "evening");
        intents[0].putExtra("method", "notification");

        return intents;
    }
	
	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, "onReceive");
		
		// get NotificationMananger
		NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        
		// get PendingIntent
        PendingIntent contentIntent = PendingIntent.getActivities(context, 0,
                      makeMessageIntentStack(context), PendingIntent.FLAG_CANCEL_CURRENT);
        
        // get information
        String contentTitle = context.getString(R.string.notif_evening_content_title);
        String contentText  = context.getString(R.string.notif_evening_content_text);
        String tickerText   = context.getString(R.string.notif_ticker_text);
        
        // build notification
        Notification notif = new Notification.Builder(context).setContentIntent(contentIntent)
        		                 .setSmallIcon(R.drawable.ic_launcher)
        		                 .setTicker(tickerText)
        		                 .setContentTitle(contentTitle)
        		                 .setContentText(contentText)
        		                 .setDefaults(Notification.DEFAULT_ALL)
        		                 .build();
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // notify
        nm.notify(R.string.notif_ticker_text, notif);
	}

}
