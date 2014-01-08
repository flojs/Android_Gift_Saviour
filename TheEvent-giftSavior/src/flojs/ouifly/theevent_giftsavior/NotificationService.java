package flojs.ouifly.theevent_giftsavior;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationService extends IntentService {

	public NotificationService() {
		super("NotificationService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent notificationIntent) {
		
		int intentId = notificationIntent.getIntExtra(CalendarListActivity.EXTRA_MESSAGE_NOTIFY_ID,-1);
		String intentMessage = notificationIntent.getStringExtra(CalendarListActivity.EXTRA_MESSAGE_NOTIFY);
		//System.out.println("intentId: " + intentId + " | " + "intentMessage: " + intentMessage);
		createNotification(intentId, intentMessage);
		
		
	}

public void createNotification(int notifyId, String message){
		
		long when = System.currentTimeMillis();         // notification time
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_action_go_to_today)
		        .setContentTitle("Upcomming Event in 3 days!")
		        .setAutoCancel(true)
		        .setWhen(when)
		        .setContentText(message);
				
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, CalendarListActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(CalendarListActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(notifyId, mBuilder.build());
	}


}
