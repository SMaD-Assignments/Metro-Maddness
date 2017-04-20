package com.unimelb.swen30006.metromadness.passengers;

import java.util.Random;

import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.routers.RouterAdapter;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.trains.Train;

public class Passenger {

	final public int id;
	public Station beginning;
	public Station destination;
	public float travelTime;
	public boolean reachedDestination;
	public int cargoWeight;
	private RouterAdapter router;
	
	public Passenger(int id, Random random, Station start, Station end){
		this.id = id;
		this.beginning = start;
		this.destination = end;
		this.reachedDestination = false;
		this.travelTime = 0;
		this.cargoWeight = random.nextInt(51);
		this.router = new PassengerRouter();
	}
	
	public void update(float time){
		if(!this.reachedDestination){
			this.travelTime += time;
		}
	}
	public int getWeight() {
		return cargoWeight;
	}
	
	public boolean shouldDisembark(Station current) {
		return router.shouldDisembark(current, this);
	}
	
	public boolean shouldEmbark(Train train) {
		return router.shouldEmbark(train, this);
	}
}
