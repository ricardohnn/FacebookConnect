package com.mobilis.FacebookConnect.Activities;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobilis.FacebookConnect.R;

public class MyProfileAdapter extends BaseAdapter{
	private List<String> friends;
	private LayoutInflater lf;
	
	MyProfileAdapter(Context context, List<String> listaamigos)
	{
		super();
		friends=listaamigos;
		lf = LayoutInflater.from(context);
	}
	public void setItens(List<String> lista){
		this.friends = lista;
	}
	
	@Override
	public int getCount() {
		if(friends != null)
			return friends.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		try {
			return friends.get(position);
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
            convertView = lf.inflate(R.layout.myp_itemrow, null);
            holder = new ViewHolder();
            holder.word = (TextView) convertView.findViewById(R.id.friend_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.word.setText(friends.get(position).toString());
       

        return convertView;
    }

    static class ViewHolder {
        TextView word;
    }




}
