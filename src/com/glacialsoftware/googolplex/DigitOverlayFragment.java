package com.glacialsoftware.googolplex;

import java.math.BigInteger;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DigitOverlayFragment extends Fragment{
	
	private TextView textView;
	private DigitGridViewAdapter.ViewHolder viewHolder=null;
	private int position=0;
	
	public interface DigitOverlayCallbacks{
		public void dismissDigitOverlay();
	}
	
	private DigitOverlayCallbacks digitOverlayCallbacks;
	
	private class SetOverlayTextTask extends AsyncTask<Void,Void,String>{
		
		private TextView textView;
		private DigitGridViewAdapter.ViewHolder viewHolder;
		private BigInteger value;
		private boolean isComma;
		
		public SetOverlayTextTask(TextView textView,DigitGridViewAdapter.ViewHolder viewHolder,BigInteger value,boolean isComma){
			this.textView=textView;
			this.viewHolder=viewHolder;
			this.value=value;
			this.isComma=isComma;
		}

		@Override
		protected String doInBackground(Void... params) {
			String message = "This is the ";
			
			if (value.equals(DigitPositionController.zero)){
				message+="One. It is the first one. It is the only one. It is 1.";
			} else {
				message+= value.toString();
				String lastTwoDigits = message.substring(message.length()-2);
				
				if (lastTwoDigits.charAt(1)=='1' && !lastTwoDigits.equals("11")){
					message+="st";
				} else if (lastTwoDigits.charAt(1)=='2' && !lastTwoDigits.equals("12")){
					message+="nd";
				} else if (lastTwoDigits.charAt(1)=='3' && !lastTwoDigits.equals("13")){
					message+="rd";
				} else {
					message+="th";
				}
				
				if (isComma){
					message+=" comma.";
				} else {
					message+=" zero.";
				}
			}
			if (value.equals(DigitPositionController.max)){
				message+=" The End!!!";
			}
			
			return message;
		}
		
		
		@Override
		protected void onPostExecute(String message){
			textView.setText(message);
		}		
		
	}

	
	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_digit_overlay, container,false);
		

        return view;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		digitOverlayCallbacks = (DigitOverlayCallbacks) activity;
	}
	
	@Override
	public void onStart (){
		super.onStart();
		
		textView= (TextView) getView().findViewById(R.id.digitIdDisplay); 

		textView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				digitOverlayCallbacks.dismissDigitOverlay();
			}
		});
		
	
	}
	
	public void setText(BigInteger value, boolean isComma, View view, int position){
		if (viewHolder!=null){
			viewHolder.critical=false;
		}
		
		this.position=position;
		viewHolder=(DigitGridViewAdapter.ViewHolder)view.getTag();
		viewHolder.critical=true;
		
		SetOverlayTextTask setOverlayTextTask = new SetOverlayTextTask(textView,viewHolder,value, isComma);
		setOverlayTextTask.execute();
	}
	
	public int getCurrentPosition(){
		return position;
	}
	
}
