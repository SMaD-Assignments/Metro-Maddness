package com.unimelb.swen30006.metromadness.passengers;

import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.routers.RouterAdapter;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.trains.Train;

public class Passenger {

	final private int id;
	private Station beginning;
	private Station destination;
	private float travelTime;
	private boolean reachedDestination;
	private RouterAdapter router;
	
	
	public Passenger(int id, Station start, Station end){
		this.id = id;
		this.beginning = start;
		this.destination = end;
		this.reachedDestination = false;
		this.travelTime = 0;
		this.router = getRouter();
	}
	
	public void update(float time){
		if(!this.reachedDestination){
			this.travelTime += time;
		}
	}
	
	protected RouterAdapter getRouter() { return new PassengerRouter(); }
	
	public boolean shouldDisembark(Station current) {
		return router.shouldDisembark(current, this);
	}
	
	public boolean shouldEmbark(Train train) {
		return router.shouldEmbark(train, this);
	}

	public int getWeight() {
		return 0;
	}
	
	public final int getId(){
		return id;
	}
	
	public Station getDestination(){
		return destination;
	}
	
	public Station getBeginning(){
		return beginning;
	}
	
	public float getTravelTime(){
		return travelTime;
	}
		
}
