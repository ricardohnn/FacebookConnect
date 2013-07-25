package com.mobilis.FacebookConnect.Activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;

import com.mobilis.FacebookConnect.R;

public class MyProfile extends Activity {
	ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);
		
		//Dialogo de progresso para melhorar a experiencia do usuario
		progressDialog = new ProgressDialog(MyProfile.this);
		progressDialog.setTitle("Carregando dados");
		progressDialog.setMessage("Por favor, aguarde...");
		progressDialog.setCancelable(false);
		progressDialog.show(); 
		
		new MyProfileBuilder(this, progressDialog).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_profile, menu);
		return true;
	}

}
