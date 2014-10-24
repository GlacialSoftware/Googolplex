package com.glacialsoftware.googolplex;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class GoogolplexDisplayFragment extends Fragment{

	private DigitGridView digitGridView;
	private DigitGridViewAdapter digitGridViewAdapter;
	private boolean restoreOverlay=false;
	private int restoreTarget=0;
	
	public interface GoogolplexDisplayCallbacks{
		public void summonOverlay(int position, View view);
		public int getFirstVisiblePosition();
	}
	
	private GoogolplexDisplayCallbacks googolplexDisplayCallbacks;
	
	public GoogolplexDisplayFragment(){}
	
	public GoogolplexDisplayFragment(boolean restoreOverlay, int restoreTarget){
		this.restoreOverlay=restoreOverlay;
		this.restoreTarget=restoreTarget;
	}
	
	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_googolplex_display, container,false);
		
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            int backgroundColor = typedValue.data;
            view.setBackgroundColor(backgroundColor);
            
        } else {
            Drawable drawable = getActivity().getResources().getDrawable(typedValue.resourceId);
            view.setBackgroundDrawable(drawable);
        }

        return view;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		googolplexDisplayCallbacks = (GoogolplexDisplayCallbacks) activity;
	}
	
	@Override
	public void onStart (){
		super.onStart();
		
		digitGridView = (DigitGridView) getView().findViewById(R.id.digitGridView);
		
		digitGridViewAdapter=new DigitGridViewAdapter(getActivity(),R.layout.item_digit_grid,restoreOverlay,restoreTarget);
		
		digitGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

   	     public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
   	                             long id) {
   	    	 
   	    	googolplexDisplayCallbacks.summonOverlay(position,view);
   	     }
   	});
		
		digitGridView.setAdapter(digitGridViewAdapter);
		
		DigitPositionController.digitGridView=digitGridView;
		
		digitGridView.setSelection(googolplexDisplayCallbacks.getFirstVisiblePosition());
		digitGridView.setScrollPosition(DigitPositionController.scrollPosition);
	}
	
	public int getFirstVisiblePosition(){
		return digitGridView.getFirstVisiblePosition();
	}
	
	
}
