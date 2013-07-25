package com.mobilis.FacebookConnect.Activities;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.google.gson.Gson;

public class VerifyFriendEventState extends AsyncTask<Long, Void, Void> {

	List<String> initialState = new ArrayList<String>();

	
	//Primitives for gson parse
	class Event{
		long eid;
		Event(){};
	}
	class FriendLocation{
		double latitude;
		double longitude;
		FriendLocation() {}
	}
	class FriendLocationContainer{
		List<FriendLocation> data;
		FriendLocationContainer() {}
	}
	class Venue{
		double latitude;
		double longitude;
		String city;
		String state;
		String country;
		long id;
		String street;
		String zip;
	}
	class EventLocation{
		Venue venue;
	}
	class EventLocationContainer{
		List<EventLocation> data;
		EventLocationContainer(){}
	}
	class EventDataContainer
	{
		List<Event> data;
		EventDataContainer(){}
	}
	class Status{
		String rsvp_status;
		Status(){};
	}
	class StatusDataContainer
	{
		List<Status> data;
		StatusDataContainer(){}
	}
	
		
	@Override
	protected Void doInBackground(Long... arg0) {
		Long uid = arg0[0];

		List<Event> eidList = getFriendEventList(uid);

		//Store initial state for each event
		int i;
		for(Event event : eidList){
			this.initialState.add(getFriendEventStatus(uid, event.eid));
		}
		
		while( FollowService.getRunning().containsKey(uid) )
		{
			
			i=0;
			
			//Compare status for each event
			for(Event event : eidList)
			{
				Log.i("com.mobilis", "EID: "+event.eid + " - " + getFriendEventStatus(uid, event.eid));

				if( getFriendEventStatus(uid,event.eid) != this.initialState.get(i) ){
					//TODO Should vibrate
				}
				
				i++;
			}
						
			//Sleep 30s
        	long endTime = System.currentTimeMillis() + 30*1000;
        	while (System.currentTimeMillis() < endTime) {
        		synchronized (this) {
        			try {
        				wait(endTime - 
                                 System.currentTimeMillis());
        			} catch (Exception e) {
        		      }
        	       }
           }		
		}
		return null;
	}
	
	protected List<Event> getFriendEventList(Long uid)
	{
		EventDataContainer container = null;
		//Get shared events
		//TODO Test query with shared events!
		String query = "SELECT eid FROM event_member WHERE uid="+Long.toString(uid);//+" AND uid=me()";
        Session session = Session.getActiveSession();
        Bundle params1 = new Bundle();
        params1.putString("q", query);
        Request request1 = new Request(session,
        		"/fql",
        		params1,
        		HttpMethod.GET);
        Response response = Request.executeAndWait(request1);
        
        
		Gson gson = new Gson();
    	String jsonString = response.getGraphObject().getInnerJSONObject().toString();
    	container = gson.fromJson(jsonString,EventDataContainer.class);                   	
        
        if(container!=null)
        	return container.data;
        else
        	return null;
	}
	
	protected String getFriendEventStatus(Long uid, Long eid)
	{		
		String state = null;
		String queryStatus="SELECT rsvp_status FROM event_member WHERE uid="+Long.toString(uid)+" AND eid="+Long.toString(eid);
		
		// Get facebook rsvp status from FQL
		Session session = Session.getActiveSession();
		Bundle params1 = new Bundle();
		params1.putString("q", queryStatus);
		Request request1 = new Request(session,
				"/fql",
				params1,
				HttpMethod.GET);
		Response statusResponse = Request.executeAndWait(request1);

		Gson gson = new Gson();
		String jsonString = statusResponse.getGraphObject().getInnerJSONObject().toString();
		
		StatusDataContainer container = gson.fromJson(jsonString,StatusDataContainer.class);
		if(container!=null)
			state = container.data.get(0).rsvp_status;

		//If presence is yet to be confirmed, return red color
		if(!state.contentEquals("attending"))
			return "red";
		
		//Getting friend_location(latitude and longitude) from location_post table
		String friendLocationQuery="SELECT latitude,longitude FROM location_post WHERE author_uid="+Long.toString(uid);
		//Getting latitude longitude of event
		String eventLocationQuery="SELECT venue FROM event WHERE eid="+Long.toString(eid);
		
		double friendLat,friendLong,eventLat,eventLong;
		
		//Friend query
		Bundle params2 = new Bundle();

		params2.putString("q", friendLocationQuery);
		Request request2 = new Request(session,
				"/fql",
				params2,
				HttpMethod.GET);
		Response friendLocationResponse = Request.executeAndWait(request2);
		

		String jsonString2 = friendLocationResponse.getGraphObject().getInnerJSONObject().toString();
		FriendLocationContainer locationContainer = gson.fromJson(jsonString2,FriendLocationContainer.class);
		
		//Get most recent checkin
		friendLat = locationContainer.data.get(0).latitude;
		friendLong = locationContainer.data.get(0).longitude;

		//Event query
		Bundle params3 = new Bundle();

		params3.putString("q", eventLocationQuery);
		Request request3 = new Request(session,
				"/fql",
				params3,
				HttpMethod.GET);
		Response eventLocationResponse = Request.executeBatchAndWait(request3).get(0);

		String jsonString3 = eventLocationResponse.getGraphObject().getInnerJSONObject().toString();
		EventLocationContainer locationEventContainer = gson.fromJson(jsonString3,EventLocationContainer.class);
		eventLat = locationEventContainer.data.get(0).venue.latitude;
		eventLong = locationEventContainer.data.get(0).venue.longitude;
		
		Location locationA = new Location("friend location");

		locationA.setLatitude(friendLat);
		locationA.setLongitude(friendLong);

		Location locationB = new Location("event location");

		locationB.setLatitude(eventLat);
		locationB.setLongitude(eventLong);

		float distance = locationA.distanceTo(locationB);
		
		//If distance between friend and event location is 1km or less, he is going. if
		if( distance < 100 )
			return "green";
		else if (distance < 1000 )
			return "yellow";
		else
			return "red";
	}

}
