package com.glacialsoftware.googolplex;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class DigitGridView extends GridView {
	
	private int scrollPosition=0;

	public DigitGridView(Context context) {
		super(context);
	}

	public DigitGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DigitGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected int computeVerticalScrollRange(){
		return 10000;
	}
	
	@Override
	protected int computeVerticalScrollOffset(){
		return scrollPosition;
	}
	
	@Override
	protected int computeVerticalScrollExtent(){
		return 10;
	}
	
	public void setScrollPosition(int scrollPosition){
		this.scrollPosition=scrollPosition;
	}
}
