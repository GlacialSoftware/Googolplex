package com.glacialsoftware.googolplex;


import java.math.BigInteger;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class GoogolplexDisplayActivity extends ActionBarActivity implements GoogolplexDisplayFragment.GoogolplexDisplayCallbacks,
																																		    DigitOverlayFragment.DigitOverlayCallbacks,
																																		    DigitGridViewAdapter.DigitGridViewAdapterCallbacks,
																																		    JumpToPositionDialogFragment.JumpToPositionCallbacks,
																																		    GoogolplexPreferenceFragment.PreferenceCallbacks{
	
	private GoogolplexDisplayFragment googolplexDisplayFragment;
	private DigitOverlayFragment digitOverlayFragment;
	private GoogolplexPreferenceFragment googolplexPreferenceFragment;
	private int firstVisiblePosition=0;
	//private boolean created=false;
	private boolean overlayShowing=false;
	private boolean preferencesShowing=false;
	
	public static String currentTheme="Light";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String theme=PreferenceManager.getDefaultSharedPreferences(this).getString("theme_select", "Light");
		if (theme.equals("Light")){
			setTheme(R.style.lightTheme);
			currentTheme=theme;
		} else {
			setTheme(R.style.darkTheme);
			currentTheme=theme;
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_googolplex_display);
		//created=true;
		
		if (savedInstanceState==null){
			Intent intent = getIntent();
			savedInstanceState=intent.getBundleExtra("com.glacialsoftware.googolplex.manualSavedInstanceState");
		}
		
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("tilt_lock", false)){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}
		
		firstVisiblePosition=0;
		overlayShowing=false;
		preferencesShowing=false;
		int overlayPosition=0;
		if (savedInstanceState!=null){
			firstVisiblePosition=savedInstanceState.getInt("firstVisiblePosition");
			overlayShowing=savedInstanceState.getBoolean("overlayShowing");
			overlayPosition=savedInstanceState.getInt("overlayPosition");
			preferencesShowing=savedInstanceState.getBoolean("preferencesShowing");
		}
		
		googolplexDisplayFragment=new GoogolplexDisplayFragment(overlayShowing,overlayPosition);
		digitOverlayFragment=new DigitOverlayFragment();
		googolplexPreferenceFragment=new GoogolplexPreferenceFragment();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.googolplex_display_frame, googolplexDisplayFragment);
		fragmentTransaction.add(R.id.googolplex_display_frame, digitOverlayFragment);
		fragmentTransaction.add(R.id.googolplex_display_frame, googolplexPreferenceFragment);
		fragmentTransaction.hide(digitOverlayFragment);
		fragmentTransaction.hide(googolplexPreferenceFragment);
		fragmentTransaction.commit();
	}
	
	/*
	@Override
	protected void onStart(){
		super.onStart();
		if (created){
			created=false;
		} else{
			boolean preferencesShowing=googolplexPreferenceFragment.isVisible();
			
			if (preferencesShowing){
			    FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
			    transaction0.hide(googolplexPreferenceFragment);
			    transaction0.commit();
				getSupportFragmentManager().popBackStackImmediate();
			}
			
		    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.remove(googolplexPreferenceFragment);
			googolplexPreferenceFragment=new GoogolplexPreferenceFragment();
			fragmentTransaction.add(R.id.googolplex_display_frame, googolplexPreferenceFragment);
			fragmentTransaction.hide(googolplexPreferenceFragment);			
			fragmentTransaction.commit();
			
			if (preferencesShowing){
				FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
				transaction1.show(googolplexPreferenceFragment);
				transaction1.addToBackStack(null);
				transaction1.commit();
			}
		}
	}*/
	
	@Override
	protected void onResumeFragments(){
		super.onResumeFragments();
		
		
		if (overlayShowing && !digitOverlayFragment.isVisible()){
			FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
			transaction0.show(digitOverlayFragment);
			transaction0.addToBackStack(null);
			transaction0.commit();
		}
		
		if (preferencesShowing && !googolplexPreferenceFragment.isVisible()){
			FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
			transaction1.show(googolplexPreferenceFragment);
			transaction1.addToBackStack(null);
			transaction1.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.googolplex_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			if (!googolplexPreferenceFragment.isVisible()){
			    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			    fragmentTransaction.show(googolplexPreferenceFragment);
			    fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
			return true;
		} else if (id==R.id.action_jump){
			Fragment fragment = getSupportFragmentManager().findFragmentByTag("jumpToPositionDialogFragment");
			if (fragment!=null){
				DialogFragment dialogFragment = (DialogFragment) fragment;
				dialogFragment.dismiss();
			}
			
			JumpToPositionDialogFragment jumpToPositionDialogFragment = new JumpToPositionDialogFragment();
			jumpToPositionDialogFragment.show(getSupportFragmentManager(), "jumpToPositionDialogFragment");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void saveInstanceState(Bundle savedInstanceState){
		overlayShowing=digitOverlayFragment.isVisible();
		preferencesShowing=googolplexPreferenceFragment.isVisible();
		
		if (preferencesShowing){
			getSupportFragmentManager().popBackStackImmediate();
		}
		
		if (overlayShowing){
			getSupportFragmentManager().popBackStackImmediate();
		}
		
		savedInstanceState.putBoolean("overlayShowing", overlayShowing);
		savedInstanceState.putInt("firstVisiblePosition", googolplexDisplayFragment.getFirstVisiblePosition());
		savedInstanceState.putInt("overlayPosition",digitOverlayFragment.getCurrentPosition());
		savedInstanceState.putBoolean("preferencesShowing", preferencesShowing);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		saveInstanceState(savedInstanceState);

		super.onSaveInstanceState(savedInstanceState);
	}

	public void digitSelected(BigInteger value, boolean isComma, View view, int position) {

		digitOverlayFragment.setText(value, isComma, view,position);		
		if (!digitOverlayFragment.isVisible()){
		    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		    fragmentTransaction.show(digitOverlayFragment);
		    fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}

	@Override
	public void dismissDigitOverlay() {

	    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
	    fragmentTransaction.hide(digitOverlayFragment);
		fragmentTransaction.commit();
		getSupportFragmentManager().popBackStackImmediate();
	}

	@Override
	public void summonOverlay(int position, View view) {
	    	 DigitGridViewAdapter.ViewHolder viewHolder = (DigitGridViewAdapter.ViewHolder)view.getTag();
   	    	 
	    	 BigInteger value;
	    	 
	    	 if (viewHolder.isComma){
	    		value=DigitPositionController.getCurrentCommaAbs(DigitPositionController.getCurrentAbs(position+1));
	    	 } else {
	    		 value=DigitPositionController.getCurrentAbs(position);
	    	 }
	    	 
	    	digitSelected(value, viewHolder.isComma,view,position);
	}

	@Override
	public int getFirstVisiblePosition() {
		return firstVisiblePosition;
	}

	@Override
	public void jumpToPosition(BigInteger target) {
		DigitPositionController.jumpToPosition(target);
		
		Fragment fragment = getSupportFragmentManager().findFragmentByTag("jumpToPositionDialogFragment");
		if (fragment!=null){
			DialogFragment dialogFragment = (DialogFragment) fragment;
			dialogFragment.dismiss();
		}
		if (digitOverlayFragment.isVisible()){
			dismissDigitOverlay();
		}
	}

	@Override
	public void updateOrientation(Boolean newValue) {
		if (newValue){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}	
	}

	@Override
	public void showLicenseFragment() {
    	LicenseDialogFragment licenseDialogFragment = new LicenseDialogFragment();
    	licenseDialogFragment.setRetainInstance(true);
    	licenseDialogFragment.show(getSupportFragmentManager(),"licenseDialog");
	}

	@Override
	public void doRecreateActivity() {
		
		Bundle savedInstanceState = new Bundle();
		saveInstanceState(savedInstanceState);
		
		Intent intent = getIntent();
		intent.putExtra("com.glacialsoftware.googolplex.manualSavedInstanceState", savedInstanceState);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		
		startActivity(intent);		
		overridePendingTransition(0, 0);
	}
	
}
