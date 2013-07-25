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

public class MyProfileBuilder extends AsyncTask<Void, Void, Integer>{

	Activity context;
	ProgressDialog progress;
	
	MyProfileBuilder(Activity activity, ProgressDialog progress){
		this.context = activity;
		this.progress = progress;
	}
	@Override
	protected Integer doInBackground(Void... arg0) {
        Session session = Session.getActiveSession();
        
        //Tratamento dos dados do perfil do usuario como nome e imagem do perfil
		String userQuery = "SELECT name,pic FROM user WHERE uid=me()";
        Bundle params1 = new Bundle();
        params1.putString("q", userQuery);
        Request request1 = new Request(session,
        		"/fql",
        		params1,
        		HttpMethod.GET);
        Response response = Request.executeAndWait(request1);
        
		TextView nome = (TextView) context.findViewById(R.id.profile_name);
		ImageView imagem_profile = (ImageView) context.findViewById(R.id.profile_image);
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


		//Pegando os nomes dos amigos
        String friendsQuery = "SELECT name,uid FROM user WHERE uid IN (SELECT uid1 FROM friend WHERE uid2=me())";
        Bundle params = new Bundle();
        params.putString("q", friendsQuery);
        Request request = new Request(session,
                "/fql",
                params,
                HttpMethod.GET);
        response = Request.executeAndWait(request);
        
    	String jsonString = response.getGraphObject().getInnerJSONObject().toString();
    	ListView lista = (ListView) context.findViewById(R.id.friend_list);
    	
    	try {
			new ParseFriendlist(context.getApplication(),lista).execute(jsonString).get();
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
		this.progress.dismiss();
	}
	

}
