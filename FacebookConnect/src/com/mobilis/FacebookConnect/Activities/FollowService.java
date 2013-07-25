package com.mobilis.FacebookConnect.Activities;

import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FollowService extends Service{
	private static final String TAG = "com.mobilis.testes";
	private static HashMap<Long, Void> running = null;	
	
	public static HashMap<Long, Void> getRunning() {
		return running;
	}

	@Override
	  public void onCreate() {
		Log.i(TAG, "Service onCreate");
		//HashMap was chosen because of faster index search
		running = new HashMap<Long, Void>();
	}
	
	//Unfollow certain UID. Return TRUE if the operation was successful.
	static boolean unfollowUID(long UID)
	{
		if(running.remove(UID) == null)
			return false;
		else
			return true;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.i(TAG, "Service onStartCommand " + startId);
		long UID = intent.getExtras().getLong("UID");

		//If there is already a thread running that UID, this service does nothing.
		if(running.containsKey(UID))
			return Service.START_STICKY;

		//Put this UID on the running list (You are now following that friend)
		running.put(UID, null);
		
		VerifyFriendEventState thread = new VerifyFriendEventState();
		thread.execute(UID);
		
		return Service.START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG, "Service onBind");
		return null;
	}
	
	@Override
	  public void onDestroy() {
		Log.i(TAG, "Service onDestroy");
	}
}
