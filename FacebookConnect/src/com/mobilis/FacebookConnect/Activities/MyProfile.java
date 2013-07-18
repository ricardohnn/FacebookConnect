package com.mobilis.FacebookConnect.Activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.google.gson.Gson;
import com.mobilis.FacebookConnect.R;

public class MyProfile extends Activity {
	class Amigos{
		String name;
		Amigos(){}
	}
	class Container {
	    List<Amigos> data;
	    Container(){}
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);
        String fqlQuery = "SELECT name FROM user WHERE uid IN (SELECT uid1 FROM friend WHERE uid2=me())";
        Bundle params = new Bundle();
        params.putString("q", fqlQuery);
        Session session = Session.getActiveSession();
        Request request = new Request(session,
                "/fql",
                params,
                HttpMethod.GET,
                new Request.Callback(){
                    public void onCompleted(Response response) {                 	
                    	Gson gson = new Gson();
                    	String jsonString = response.getGraphObject().getInnerJSONObject().toString();
                    	Container container = gson.fromJson(jsonString,Container.class);
                        List<String> friends = new ArrayList<String>();
                        for (Amigos s : container.data){
                        	friends.add(s.name);
                        }
                    	Log.i("TEST", friends.get(0));
                    	ListView lista = (ListView) findViewById(R.id.listadeAmigos);
                    	MyProfile_adapter adapter = new MyProfile_adapter(getApplication(), friends);
                    	lista.setAdapter(adapter);               	
                    }
                });
        Request.executeBatchAsync(request);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_profile, menu);
		return true;
	}

}
