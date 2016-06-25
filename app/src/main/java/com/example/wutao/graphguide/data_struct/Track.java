package com.example.wutao.graphguide.data_struct;

public class Track {
	
	private int mAdjStop;
	
	private int mDistance;

	private Track mNext;
	
	public Track(){
		
	}
	
	public Track(int adjStop, int distance, Track next){
		mAdjStop = adjStop;
		mDistance = distance;
		mNext = next;
	}
	
	public int getAdjStop() {
		return mAdjStop;
	}

	public Track getNext() {
		return mNext;
	}
	
	public int getDistance(){
		return mDistance;
	}

	public void setAdjStop(int adjStop) {
		mAdjStop = adjStop;
	}

	public void setDistance(int distance) {
		mDistance = distance;
	}

	public void setNext(Track next) {
		mNext = next;
	}
	
}
