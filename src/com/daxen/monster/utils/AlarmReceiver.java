/**
 * 
 */
package com.daxen.monster.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.daxen.monster.AddRecordActivity;
import com.daxen.monster.R;

/**
 * @author ʥ��
 *
 */
public class AlarmReceiver extends BroadcastReceiver {
	//private static final String TAG = "AlarmReceiver";
	
	
	static Intent[] makeMessageIntentStack(Context context, CharSequence from,
            CharSequence msg) {
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

        // "App"
        //intents[1] = new Intent(context, com.example.android.apis.ApiDemos.class);
        //intents[1].putExtra("com.example.android.apis.Path", "App");
        
        // "App/Notification"
        //intents[2] = new Intent(context, com.example.android.apis.ApiDemos.class);
        //intents[2].putExtra("com.example.android.apis.Path", "App/Notification");

        // Now the activity to display to the user.  Also fill in the data it
        // should display.
        //intents[3] = new Intent(context, IncomingMessageView.class);
        //intents[3].putExtra(IncomingMessageView.KEY_FROM, from);
        //intents[3].putExtra(IncomingMessageView.KEY_MESSAGE, msg);

        return intents;
    }
	
	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence from = "Tally Reminder";
        CharSequence message = "It is time to Take a tally";
        
        PendingIntent contentIntent = PendingIntent.getActivities(context, 0,
                      makeMessageIntentStack(context, from, message), PendingIntent.FLAG_CANCEL_CURRENT);
        
        String tickerText = context.getString(com.daxen.monster.R.string.notif_ticker_text, message);
        
        // old method
        //Notification notif = new Notification(com.daxen.monster.R.drawable.ic_launcher, tickerText,
        //                     System.currentTimeMillis());
        Notification notif = new Notification.Builder(context).setContentIntent(contentIntent)
        		                 .setSmallIcon(R.drawable.ic_launcher)
        		                 .setTicker(tickerText)
        		                 .setContentTitle(from)
        		                 .setContentText(message)
        		                 .setContentInfo("content info")
        		                 .setDefaults(Notification.DEFAULT_ALL)
        		                 .build();
        
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        
        nm.notify(R.string.notif_ticker_text, notif);
	}
}
