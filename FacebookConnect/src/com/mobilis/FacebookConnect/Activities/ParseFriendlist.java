package com.mobilis.FacebookConnect.Activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

class Amigos{
	String name;
	long uid;
	Amigos(){}
}
class Container {
    List<Amigos> data;
    Container(){}
}
	
public class ParseFriendlist extends AsyncTask<String, Void, List<String> >{

	private ListView listaDeAmigos;
	private Context mContext;
	private ProgressDialog progress;
	private List<String> friendsname = new ArrayList<String>();
	private List<Long> friendsuid = new ArrayList<Long>(); 

	ParseFriendlist(Context contexto, ListView lista, ProgressDialog progress)
	{
		this.listaDeAmigos = lista;
		this.mContext = contexto;
		this.progress = progress;

		
	}
	@Override
	protected List<String> doInBackground(String... arg0) {
    	Gson gson = new Gson();
    	Container container = gson.fromJson(arg0[0],Container.class);
    	
        for (Amigos s : container.data){
        	this.friendsname.add(s.name);
        	Log.i("TESTESNAME", Long.toString(s.uid));
        	this.friendsuid.add(s.uid);
        }
        Log.i("Testa",friendsname.get(0));
		return friendsname;
	}
    protected void onPostExecute(List<String> result) {
    	MyProfile_adapter adapter = new MyProfile_adapter(mContext, result);
    	listaDeAmigos.setAdapter(adapter);
    	listaDeAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				long UID = ParseFriendlist.this.friendsuid.get(arg2);
				Log.i("UIDTESTE", Long.toString(UID));
				Intent intent = new Intent(ParseFriendlist.this.mContext, FriendX.class);
				intent.putExtra("friendUID", UID);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);

			}
    		
		});
    	this.progress.dismiss();
    }

}
