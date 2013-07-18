package com.mobilis.FacebookConnect.Activities;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobilis.FacebookConnect.R;

public class MyProfile_adapter extends BaseAdapter{
	private List<String> amigos;
	private LayoutInflater lf;
	
	MyProfile_adapter(Context context, List<String> listaamigos)
	{
		super();
		amigos=listaamigos;
		lf = LayoutInflater.from(context);
	}
	public void setItens(List<String> lista){
		this.amigos = lista;
	}
	
	@Override
	public int getCount() {
		if(amigos != null)
			return amigos.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		try {
			return amigos.get(position);
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
            holder.word = (TextView) convertView.findViewById(R.id.nome_amigo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.word.setText(amigos.get(position).toString());
       

        return convertView;
    }

    static class ViewHolder {
        TextView word;
    }




}
