package com.glacialsoftware.googolplex;

import java.math.BigInteger;

import android.os.AsyncTask;

public class DigitPositionController {
	
	private static BigInteger lowerBound=new BigInteger("0");
	private static BigInteger upperBound=new BigInteger("1000000000");
	private static BigInteger min=new BigInteger("0");
	public static BigInteger max=new BigInteger("10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
	private static BigInteger halfFrameSize = new BigInteger("500000000");
	private static BigInteger frameSize = new BigInteger("1000000000");
	private static BigInteger inflatedFrameSize = new BigInteger("1333333333");
	private static BigInteger scrollSegment = new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
	public static  BigInteger zero =new BigInteger("0");
	private static BigInteger one=new BigInteger("1");
	private static BigInteger two=new BigInteger("2");
	private static BigInteger three=new BigInteger("3");
	
	private static boolean changingFrame=false;
	private static boolean isFirstFrame=true;
	private static boolean isFinalFrame=false;
	
	public static int scrollPosition=0;
	public static int firstPosition=1;
	public static int currentPosition=0;
	public static DigitGridView digitGridView;
	
	private static class IncrementFrameTask extends AsyncTask<Void,Void,Integer>{
		
		private BigInteger lowerBound;
		private BigInteger upperBound;
		private boolean isFinalFrame;
		private int firstPosition;
		private int scrollPosition;
		
		protected Integer doInBackground(Void...params){

			lowerBound=DigitPositionController.lowerBound.add(halfFrameSize);
			upperBound=DigitPositionController.upperBound.add(halfFrameSize);
			
			isFinalFrame=upperBound.equals(max);
			
			int commaOffset= ((max.subtract((lowerBound.add(one)))).mod(three)).intValue();
			if (commaOffset==0){
				firstPosition=1;
			} else if (commaOffset==2){
				firstPosition=3;
			} else {
				firstPosition=4;
			}
			
			scrollPosition=lowerBound.divide(scrollSegment).intValue();
			
			return 0;
		}
		
		protected void onPostExecute(Integer result){
			DigitPositionController.lowerBound=lowerBound;
			DigitPositionController.upperBound=upperBound;
			DigitPositionController.firstPosition=firstPosition;
			
			int setTo=digitGridView.getFirstVisiblePosition()-666666666;
			currentPosition=setTo;
			digitGridView.setSelection(setTo);
			digitGridView.setScrollPosition(scrollPosition);
			
			DigitPositionController.scrollPosition=scrollPosition;
			DigitPositionController.isFirstFrame=false;
			DigitPositionController.isFinalFrame=isFinalFrame;
			
			changingFrame=false;
		}
		
	}
	
	private static class DecrementFrameTask extends AsyncTask<Void,Void,Integer>{
		
		private BigInteger lowerBound;
		private BigInteger upperBound;
		private boolean isFirstFrame;
		private int firstPosition;
		private int scrollPosition;
		
		protected Integer doInBackground(Void...params){

			lowerBound=DigitPositionController.lowerBound.subtract(halfFrameSize);
			upperBound=DigitPositionController.upperBound.subtract(halfFrameSize);
			
			isFirstFrame=lowerBound.equals(min);
			
			int commaOffset= ((max.subtract((lowerBound.add(one)))).mod(three)).intValue();
			if (commaOffset==0){
				firstPosition=1;
			} else if (commaOffset==2){
				firstPosition=3;
			} else {
				firstPosition=4;
			}
			
			scrollPosition=lowerBound.divide(scrollSegment).intValue();
			
			return 0;
		}
		
		protected void onPostExecute(Integer result){
			DigitPositionController.lowerBound=lowerBound;
			DigitPositionController.upperBound=upperBound;
			DigitPositionController.firstPosition=firstPosition;
			
			int setTo=digitGridView.getFirstVisiblePosition()+666666666;
			currentPosition=setTo;
			digitGridView.setSelection(setTo);
			digitGridView.setScrollPosition(scrollPosition);
			
			DigitPositionController.scrollPosition=scrollPosition;
			DigitPositionController.isFirstFrame=isFirstFrame;
			DigitPositionController.isFinalFrame=false;
			
			changingFrame=false;
		}
		
	}
	
	private static class JumpToPositionTask extends AsyncTask<BigInteger,Void,Integer>{
		
		private BigInteger lowerBound;
		private BigInteger upperBound;
		private boolean isFirstFrame;
		private boolean isFinalFrame;
		private int firstPosition;
		private int scrollPosition;
		private int relativePosition;
		
		protected Integer doInBackground(BigInteger... absolutePosition){
			BigInteger adjustedAbsolutePosition;
			if (absolutePosition[0].equals(max)){
				adjustedAbsolutePosition=absolutePosition[0].subtract(one);
			} else {
				adjustedAbsolutePosition=absolutePosition[0];
			}
			
			lowerBound=(adjustedAbsolutePosition.divide(frameSize)).multiply(frameSize);
			upperBound=lowerBound.add(frameSize);
			
			relativePosition=(((adjustedAbsolutePosition.subtract(lowerBound)).multiply(inflatedFrameSize)).divide(frameSize)).intValue();
			
			isFirstFrame=lowerBound.equals(min);
			isFinalFrame=upperBound.equals(max);
			
			if (relativePosition <= 266666666 && !isFirstFrame){
				lowerBound=lowerBound.subtract(halfFrameSize);
				upperBound=upperBound.subtract(halfFrameSize);
				relativePosition+=666666666;
				isFirstFrame=lowerBound.equals(min);
				isFinalFrame=upperBound.equals(max);
			} else if (relativePosition>=1066666666 && !isFinalFrame){
				lowerBound=lowerBound.add(halfFrameSize);
				upperBound=upperBound.add(halfFrameSize);
				relativePosition-=666666666;
				isFirstFrame=lowerBound.equals(min);
				isFinalFrame=upperBound.equals(max);
			}
			
			if (relativePosition<0){
				relativePosition=0;
			} else if (relativePosition>=1333333334){
				relativePosition=1333333333;
			}
			
			int commaOffset= ((max.subtract((lowerBound.add(one)))).mod(three)).intValue();
			if (commaOffset==0){
				firstPosition=1;
			} else if (commaOffset==2){
				firstPosition=3;
			} else {
				firstPosition=4;
			}
			
			scrollPosition=lowerBound.divide(scrollSegment).intValue();
			
			return 0;
		}
		
		protected void onPostExecute(Integer result){
			DigitPositionController.lowerBound=lowerBound;
			DigitPositionController.upperBound=upperBound;
			DigitPositionController.firstPosition=firstPosition;
			
			currentPosition=relativePosition;
			digitGridView.setSelection(relativePosition);
			digitGridView.setScrollPosition(scrollPosition);
			
			DigitPositionController.scrollPosition=scrollPosition;
			DigitPositionController.isFirstFrame=isFirstFrame;
			DigitPositionController.isFinalFrame=isFinalFrame;
			
			changingFrame=false;
		}
		
	}
	
	
	public static void incrementFrame(int current){
		if (!isFinalFrame && !changingFrame){
			changingFrame=true;
			IncrementFrameTask incrementFrameTask=new IncrementFrameTask();
			incrementFrameTask.execute();
		}
	}
	
	public static void decrementFrame(int current){
		if (!isFirstFrame && !changingFrame){
			changingFrame=true;
			DecrementFrameTask decrementFrameTask=new DecrementFrameTask();
			decrementFrameTask.execute();
		}
	}
	
	public static void jumpToPosition(BigInteger absolutePosition){
		if (!changingFrame){
			changingFrame=true;
			JumpToPositionTask jumpToPositionTask = new JumpToPositionTask();
			jumpToPositionTask.execute(absolutePosition);
		}
	}
	
	public static boolean changePosition(int position){
		int diff = Math.abs(position-currentPosition);
		if (diff>1000000){
			return false;
		}
		currentPosition=position;
		return true;
	}
	
	public static BigInteger getCurrentAbs(int relativePosition){
		
		relativePosition=(relativePosition-firstPosition)-getPrecedingCommaCount(relativePosition);
		
		return lowerBound.add((new BigInteger(Integer.toString(relativePosition))).add(one));
	}
	
	public static int getPrecedingCommaCount(int index){
		int firstValidComma;
		if (firstPosition==1){
			firstValidComma=2;
		} else {
			firstValidComma=6;
		}
		
		if (index<firstValidComma){
			return 0;
		}
		
		int precedingCommaCount = ((index-firstValidComma)/4)+1;
		return precedingCommaCount;
	}
	
	public static BigInteger getCurrentCommaAbs(BigInteger nextZero){
		nextZero = nextZero.subtract(two);
		return (nextZero.divide(three)).add(one);
	}
	
	public static String validateTargetString(String target){
		String noCommas="";
		final int count=target.length();
		for (int i=0;i<count;++i){
			final char currentChar = target.charAt(i);
			if (currentChar==','){
				continue;
			} else if (!Character.isDigit(currentChar)){
				return "-1";
			} else{
				noCommas+=currentChar;
			}
		}
		
		BigInteger targetBigInteger;
		
		try{
			targetBigInteger = new BigInteger(noCommas);
		} catch (Exception e){
			return "-1";
		}
		
		if (targetBigInteger.equals(DigitPositionController.max) || targetBigInteger.equals(DigitPositionController.min)){
			return noCommas;
		}
		
		BigInteger max=targetBigInteger.max(DigitPositionController.max);
		BigInteger min=targetBigInteger.min(DigitPositionController.min);
		
		if (max.equals(targetBigInteger) || min.equals(targetBigInteger)){
			return "-2";
		}
		
		return noCommas;
	}

}
