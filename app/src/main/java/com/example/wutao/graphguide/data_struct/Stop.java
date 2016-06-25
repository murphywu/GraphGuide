package com.example.wutao.graphguide.data_struct;

public class Stop {

	private final char mStopName;

	private final int mX;

	private final int mY;

	private Track mFirstTrack;
	
	public Stop(char stopName, int x, int y){
		mStopName = stopName;
		mX = x;
		mY = y;
	}
	
	public char getStopName() {
		return mStopName;
	}

	public Track getFirstTrack() {
		return mFirstTrack;
	}
	
	public void setFirstTrack(Track firstTrack) {
		mFirstTrack = firstTrack;
	}

	public int getX(){
		return mX;
	}

	public int getY(){
		return mY;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj instanceof Stop){
			return mStopName == ((Stop)obj).mStopName;
		}
		return false;
	}

}
