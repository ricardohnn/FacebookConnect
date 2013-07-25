package com.mobilis.FacebookConnect.Activities;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.mobilis.FacebookConnect.R;

public class FriendXBuilder extends AsyncTask<Long, Void, Integer> {

	Activity context;
	ProgressDialog progress;
	
	public FriendXBuilder(Activity activity, ProgressDialog progress) {
		this.context = activity;
		this.progress = progress;
	}
	@Override
	protected Integer doInBackground(Long... params) {
		
		Long UID = params[0];
        Session session = Session.getActiveSession();

		
        //Profile data mining
		String userQuery = "SELECT name,pic FROM user WHERE uid=" + Long.toString(UID);
        Bundle params1 = new Bundle();
        params1.putString("q", userQuery);
        Request request1 = new Request(session,
        		"/fql",
        		params1,
        		HttpMethod.GET);
        Response response = Request.executeAndWait(request1);
        
		TextView nome = (TextView) context.findViewById(R.id.friend_name);
		ImageView imagem_profile = (ImageView) context.findViewById(R.id.friend_profile_image);
    	String jsonString2 = response.getGraphObject().getInnerJSONObject().toString();
    	
    	try {
			new ParseProfileInfo(nome, imagem_profile).execute(jsonString2).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}						

        
        //get event list
        String eventListQuery = "SELECT name,start_time FROM event where eid IN (SELECT eid FROM event_member WHERE uid=" + Long.toString(UID)+" )";
        Bundle params2 = new Bundle();
        params2.putString("q", eventListQuery);
        Request request2 = new Request(session,
        		"/fql",
        		params2,
        		HttpMethod.GET);
        
        response = Request.executeAndWait(request2);
        
		ListView list = (ListView) context.findViewById(R.id.event_list);
    	jsonString2 = response.getGraphObject().getInnerJSONObject().toString();
    	
    	try {
			new ParseEventList(context.getApplication(), list).execute(jsonString2).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						


        return 0;
	}

	@Override
	protected void onPostExecute(Integer result) {
		progress.dismiss();
	}

}
