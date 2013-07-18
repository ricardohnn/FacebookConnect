package com.mobilis.FacebookConnect.Activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.google.gson.Gson;

class Amigos{
	String name;
	Amigos(){}
}
class Container {
    List<Amigos> data;
    Container(){}
}
	
public class ParseFriendlist extends AsyncTask<String, Void, List<String>>{

	private ListView listaDeAmigos;
	private Context mContext;
	ParseFriendlist(Context contexto, ListView lista)
	{
		this.listaDeAmigos = lista;
		this.mContext = contexto;
		
	}
	@Override
	protected List<String> doInBackground(String... arg0) {
    	Gson gson = new Gson();
    	Container container = gson.fromJson(arg0[0],Container.class);
        List<String> friends = new ArrayList<String>();
        for (Amigos s : container.data){
        	friends.add(s.name);
        }
		return friends;
	}
    protected void onPostExecute(List<String> result) {
    	MyProfile_adapter adapter = new MyProfile_adapter(mContext, result);
    	listaDeAmigos.setAdapter(adapter);
    }

}
