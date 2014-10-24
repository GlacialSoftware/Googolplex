package com.glacialsoftware.googolplex;

import java.math.BigInteger;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class JumpToPositionDialogFragment extends DialogFragment{
	
	public interface JumpToPositionCallbacks{
		public void jumpToPosition(BigInteger target);
	}
	
	JumpToPositionCallbacks jumpToPositionCallbacks;

	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	getDialog().setTitle("Jump to position");
    	
        View view = inflater.inflate(R.layout.fragment_jump_to_position_dialog, container, false);
        
        EditText editText = (EditText)view.findViewById(R.id.customJumpEntry);
        GridView gridView = (GridView)view.findViewById(R.id.jumpGridView);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        	
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                	CharSequence uncheckedTarget = view.getText();
                	if (uncheckedTarget==null || uncheckedTarget.equals("")){
                		return handled;
                	}
                	String target = DigitPositionController.validateTargetString(uncheckedTarget.toString());
                	if (target.equals("-1")){
                		view.setError("Invalid input - must be a positive integer");
                		view.setText("");
                		return handled;
                	} else if (target.equals("-2")){
                		view.setError("Out of range - must be between 0 and 10^100");
                		view.setText("");
                		return handled;
                	}
                	jumpToPositionCallbacks.jumpToPosition(new BigInteger(target));
                    handled = true;
                }
                return handled;
            }
        });
        
        JumpToPositionListAdapter jumpToPositionListAdapter = new JumpToPositionListAdapter(getActivity(),R.layout.item_jump_to_position_grid);
        gridView.setAdapter(jumpToPositionListAdapter);
        
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

   	     public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
   	                             long id) {
   	    	 BigInteger ten = new BigInteger("10");
   	    	 
   	    	jumpToPositionCallbacks.jumpToPosition(ten.pow(position));
   	     }
       });
        

        return view;
    }
    
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		jumpToPositionCallbacks = (JumpToPositionCallbacks) activity;
	}
	
    @Override
    public void onDestroyView() {
      if (getDialog() != null && getRetainInstance())
        getDialog().setDismissMessage(null);
      super.onDestroyView();
    }
}
