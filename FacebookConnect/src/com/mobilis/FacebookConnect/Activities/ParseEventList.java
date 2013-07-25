package com.mobilis.FacebookConnect.Activities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.google.gson.Gson;

@SuppressLint("SimpleDateFormat")
public class ParseEventList extends AsyncTask<String, Void, Integer> {

	class Event{
		String name;
		String start_time;
		Event(){}
	}
	class Container {
	    List<Event> data;
	    Container(){}
	}

	private ListView eventListView;
	private Context mContext;
	private List<String> eventList = new ArrayList<String>();
	private List<String> eventDateList = new ArrayList<String>(); 

	public ParseEventList(Context context, ListView list) {		
		this.eventListView = list;
		this.mContext = context;
	}
	@Override
	protected Integer doInBackground(String... arg0) {
    	Gson gson = new Gson();
    	Container container = gson.fromJson(arg0[0],Container.class);
	    SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy EEEEEE HH:mm:ss");  

        for (Event s : container.data){
        	this.eventList.add(s.name);
        	try {
       			this.eventDateList.add(out.format(ISO8601DateParser.parse(s.start_time)).toString());
			} catch (ParseException e) {
			    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			    SimpleDateFormat out2 = new SimpleDateFormat("dd/MM/yyyy EEEEEE");  
			    try {
					this.eventDateList.add(out2.format(formatter.parse(s.start_time)).toString());
				} catch (ParseException e1) {
					this.eventDateList.add("Unknown");
					e1.printStackTrace();
				}  
				e.printStackTrace();
			}
        }
		return 0;
	}
    protected void onPostExecute(Integer result) {
    	if (result==0){
    	FriendXEventListAdapter adapter = new FriendXEventListAdapter(mContext, this.eventList,this.eventDateList);
    	this.eventListView.setAdapter(adapter);
    	}
    }


}
