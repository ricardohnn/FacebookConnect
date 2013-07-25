package com.mobilis.FacebookConnect.Activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import com.facebook.Session;
import com.mobilis.FacebookConnect.R;

public class FriendX extends Activity {
	ProgressDialog progressDialog;
	Session session;
	Long UID;
	Button btnFollowUnfollow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_x);
		Intent intent = getIntent();
		this.UID = intent.getExtras().getLong("friendUID");
		
		//ProgressDialog for user experience improvement.
		progressDialog = new ProgressDialog(FriendX.this);
		progressDialog.setTitle("Carregando dados");
		progressDialog.setMessage("Por favor, aguarde...");
		progressDialog.setCancelable(false);
		progressDialog.show(); 

		new FriendXBuilder(this, progressDialog).execute(UID);


        
        btnFollowUnfollow = (Button) findViewById(R.id.follow_friend);
		if(FollowService.getRunning() != null && FollowService.getRunning().containsKey(UID) == true)
			btnFollowUnfollow.setText("Unfollow");

        btnFollowUnfollow.setOnClickListener(new View.OnClickListener() {

        	@Override
        	public void onClick(View arg0) {
        		if(FollowService.getRunning() == null || FollowService.getRunning().containsKey(UID) == false){
        			FriendX.this.btnFollowUnfollow.setText("Unfollow");
        			Intent intent = new Intent(getApplicationContext(), FollowService.class);
        			intent.putExtra("UID", FriendX.this.UID);
        			startService(intent);
        		}
        		else
        		{
        			FollowService.unfollowUID(FriendX.this.UID);
        			FriendX.this.btnFollowUnfollow.setText("Follow");
        		}
        	}
        });


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_x, menu);
		return true;
	}

}
