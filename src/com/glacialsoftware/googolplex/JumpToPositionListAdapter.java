package com.glacialsoftware.googolplex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JumpToPositionListAdapter extends BaseAdapter{
	
	Context context;
	int resource;
	
	static class ViewHolder{
		TextView exponent;
		TextView ten;
	}
	
	public JumpToPositionListAdapter(Context context, int resource){
		this.context=context;
		this.resource=resource;
	}

	@Override
	public int getCount() {
		return 101;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		
		if (convertView==null){
			convertView= LayoutInflater.from(context).inflate(resource, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.exponent=(TextView) convertView.findViewById(R.id.exponent);
			viewHolder.ten=(TextView) convertView.findViewById(R.id.ten);
			viewHolder.ten.setText("10");
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		viewHolder.exponent.setText(Integer.toString(position));
		
		return convertView;
	}

}
