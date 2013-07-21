package com.mobilis.FacebookConnect.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.mobilis.FacebookConnect.R;

public class FriendX extends Activity {
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_x);
		Intent intent = getIntent();
		Long UID = intent.getExtras().getLong("friendUID");
		
		//Dialogo de progresso para melhorar a experiencia do usuario
		progressDialog = new ProgressDialog(FriendX.this);
		progressDialog.setTitle("Carregando dados");
		progressDialog.setMessage("Por favor, aguarde...");
		progressDialog.setCancelable(false);
		progressDialog.show(); 

		
        Session session = Session.getActiveSession();

		
        //Tratamento dos dados do perfil do amigo como nome e imagem do perfil
		String userQuery = "SELECT name,pic FROM user WHERE uid=" + Long.toString(UID);
        Bundle params1 = new Bundle();
        params1.putString("q", userQuery);
        Request request1 = new Request(session,
        		"/fql",
        		params1,
        		HttpMethod.GET,
        		new Request.Callback(){

					@Override
					public void onCompleted(Response response) {
						TextView nome = (TextView) findViewById(R.id.nome_amigo);
						ImageView imagem_profile = (ImageView) findViewById(R.id.imagem_perfil_amigo);
                    	String jsonString2 = response.getGraphObject().getInnerJSONObject().toString();
                    	new ParseProfileInfo(nome, imagem_profile,progressDialog).execute(jsonString2);						
					}
        	
        		});
        Request.executeBatchAsync(request1);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_x, menu);
		return true;
	}

}