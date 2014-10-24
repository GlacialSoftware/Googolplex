package com.glacialsoftware.googolplex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DigitGridViewAdapter extends BaseAdapter {
	
	private Context context;
	private int resource;
	private boolean restoreOverlay;
	private int restoreTarget;
	
	static class ViewHolder{
		TextView digit;
		boolean isComma;
		boolean isFirst;
		boolean critical;
	}
	
	public interface DigitGridViewAdapterCallbacks{
		public void dismissDigitOverlay();
		public void summonOverlay(int position, View view);
	}
	
	private DigitGridViewAdapterCallbacks digitGridViewAdapterCallbacks;
		
	
	public DigitGridViewAdapter(Context context, int resource,boolean restoreOverlay,int restoreTarget){
		this.context=context;
		this.resource=resource;
		this.digitGridViewAdapterCallbacks=(DigitGridViewAdapterCallbacks) context;
		this.restoreOverlay=restoreOverlay;
		this.restoreTarget=restoreTarget;
	}

	@Override
	public int getCount() {
		return 1333333334;
	}

	@Override
	public Object getItem(int position) {
		return "0";
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		
		if (DigitPositionController.changePosition(position)){
			if (position>=1066666666){
				DigitPositionController.incrementFrame(position);
			} else if (position <= 266666666){
				DigitPositionController.decrementFrame(position);
			}
		}
		
		if (convertView==null){
			convertView= LayoutInflater.from(context).inflate(resource, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.digit=(TextView) convertView.findViewById(R.id.digit);
			viewHolder.digit.setText("0");
			viewHolder.isComma=false;
			viewHolder.isFirst=false;
			viewHolder.critical=false;
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder=(ViewHolder) convertView.getTag();
			if (viewHolder.isFirst){
				viewHolder.digit.setText("0");
				viewHolder.isComma=false;
				viewHolder.isFirst=false;
			}
		}
		
		if (viewHolder.critical){
			digitGridViewAdapterCallbacks.dismissDigitOverlay();
			viewHolder.critical=false;
		} 
		
		if (position==0){
			viewHolder.digit.setText("1");
			viewHolder.isComma=false;
			viewHolder.isFirst=true;
		} else if ((position%4)==2){
			if (!viewHolder.isComma){
				viewHolder.digit.setText(",");
				viewHolder.isComma=true;
			}
		} else {
			if (viewHolder.isComma){
				viewHolder.digit.setText("0");
				viewHolder.isComma=false;
			}
		}
		
		if (restoreOverlay){
			if (position==restoreTarget){
				digitGridViewAdapterCallbacks.summonOverlay(position, convertView);
				restoreOverlay=false;
			}
		}
	
		return convertView;
	}
}
