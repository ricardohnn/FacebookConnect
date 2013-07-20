package com.mobilis.FacebookConnect.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.mobilis.FacebookConnect.R;

public class FriendX extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_x);
		TextView teste = (TextView) findViewById(R.id.hello);
		Intent intent = getIntent();
		Long UID = intent.getExtras().getLong("friendUID");
		teste.setText(Long.toString(UID));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_x, menu);
		return true;
	}

}
