package hk.ust.comp4521.UasT;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GcmIntentService extends IntentService {
	static final String TAG = "UasT.GcmIntentService";
	public static final int NOTIFICATION_ID = 1;
	static final long[] vibraPattern = { 0, 500, 250, 500 };

	final ArrayList<String> messages = new ArrayList<String>();
	final Map<String, String> senders = new HashMap<String, String>();

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				Log.i(TAG, "Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				Log.i(TAG, "Deleted messages on server: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				sendNotification(extras);
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(Bundle data) {
		String sender = data.getString("sender");
		String senderKey = data.getString("senderKey");
		String chatId = data.getString("chatId");
		String message = data.getString("message");

		if (sender == null || message == null)
			return;

		NotificationManager mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		messages.add(message);
		senders.put(sender, null);

		Intent startActivity = new Intent(this, MainActivity.class);
		startActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity.putExtra("type", "chat");
		startActivity.putExtra("sender", senderKey);
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				startActivity, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this);
		
		mBuilder.setSmallIcon(R.drawable.ic_stat_gcm);
		mBuilder.setAutoCancel(true);
		mBuilder.setVibrate(vibraPattern);
		mBuilder.setLights(0xFFFFFF00, 1000, 1000);
		
		mBuilder.setContentTitle(sender);
		mBuilder.setContentText(message);
		
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mBuilder.setSound(alarmSound);

		if (messages.size() > 1) {
			String senderStr;
			if (senders.size() == 1)
				senderStr = sender;
			else
				senderStr = senders.size() + " contacts";
			mBuilder.setContentTitle(messages.size() + " messages from "
					+ senderStr);
			
			NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
			
			int startIdx = Math.max(0, messages.size() - 5);
			for (int i = startIdx; i < messages.size(); i++) {
				inboxStyle.addLine(messages.get(i));
			}
			
			mBuilder.setStyle(inboxStyle);
		}

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		
		if(main != null)
			main.refreshChat(chatId);
	}

	public class LocalBinder extends Binder {
		GcmIntentService getService() {
			return GcmIntentService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final IBinder mBinder = new LocalBinder();

	public void clearNotifications() {
		NotificationManager mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.cancel(NOTIFICATION_ID);
		messages.clear();
		senders.clear();
	}
	
	MainActivity main;
	public void setCallback(MainActivity main) {
		this.main = main;
	}
}


