package com.mobilis.FacebookConnect.Activities;

import java.util.List;

import com.mobilis.FacebookConnect.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FriendXEventListAdapter extends BaseAdapter{
	private List<String> eventname;
	private List<String> eventdate;
	private LayoutInflater lf;
	
	public FriendXEventListAdapter(Context context, List<String> eventlist, List<String> eventdate) {
		super();
		this.eventname=eventlist;
		this.eventdate = eventdate;
		lf = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if(eventname != null)
			return eventname.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		try {
			return eventname.get(position);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
        if (convertView == null) {
            convertView = lf.inflate(R.layout.friendx_itemrow, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.event_name);
            holder.date = (TextView) convertView.findViewById(R.id.event_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(eventname.get(position));
       	holder.date.setText(eventdate.get(position));
       

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView date;        
    }


}
