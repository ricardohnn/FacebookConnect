package com.mobilis.FacebookConnect.Activities;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.mobilis.FacebookConnect.R;

public class MyProfile extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);
        Session session = Session.getActiveSession();
        
		String userQuery = "SELECT name,pic_big FROM user WHERE uid=me()";
        Bundle params1 = new Bundle();
        params1.putString("q", userQuery);
        Request request1 = new Request(session,
        		"/fql",
        		params1,
        		HttpMethod.GET,
        		new Request.Callback(){

					@Override
					public void onCompleted(Response response) {
						TextView nome = (TextView) findViewById(R.id.nome_profile);
						ImageView imagem_profile = (ImageView) findViewById(R.id.imagem_perfil);
                    	String jsonString2 = response.getGraphObject().getInnerJSONObject().toString();
                    	new ParseProfileInfo(nome, imagem_profile).execute(jsonString2);						
					}
        	
        		});
        Request.executeBatchAsync(request1);


		
        String friendsQuery = "SELECT name FROM user WHERE uid IN (SELECT uid1 FROM friend WHERE uid2=me())";
        Bundle params = new Bundle();
        params.putString("q", friendsQuery);
        Request request = new Request(session,
                "/fql",
                params,
                HttpMethod.GET,
                new Request.Callback(){
                    public void onCompleted(Response response) {                 	
                    	String jsonString = response.getGraphObject().getInnerJSONObject().toString();
                    	ListView lista = (ListView) MyProfile.this.findViewById(R.id.listadeAmigos);
                    	new ParseFriendlist(MyProfile.this.getApplication(),lista).execute(jsonString);
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
