package com.unimelb.swen30006.metromadness.passengers;

import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.routers.RouterAdapter;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.trains.Train;

/** SWEN30006 Software Modeling and Design
Passenger class
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Represents the passengers in the simulation
*/
public class Passenger {

	final private int id;
	private Station beginning;
	private Station destination;
	private float travelTime;
	private boolean reachedDestination;
	
	// Router stored here to allow for passengers to navigate for themselves
	private RouterAdapter router;
	
	/**
	 * Constructor, utilizes getRouter for greater re-usability
	 * @param id - Passenger id
	 * @param start - Current station
	 * @param end - Destination station
	 */
	public Passenger(int id, Station start, Station end){
		this.id = id;
		this.beginning = start;
		this.destination = end;
		this.reachedDestination = false;
		this.travelTime = 0;
		this.router = getRouter();
	}
	
	/**
	 * Keep track of how long the passenger has been traveling
	 * @param time - time since last update
	 */
	public void update(float time){
		if(!this.reachedDestination){
			this.travelTime += time;
		}
	}
	
	/**
	 * Used in constructor to allow for greater re-use in extended classes
	 */
	protected RouterAdapter getRouter() { return new PassengerRouter(); }
	
	/**
	 * Asks the passenger if it should get off at this station
	 * @param current - current station
	 * @return - if passenger should disembark
	 */
	public boolean shouldDisembark(Station current) {
		return router.shouldDisembark(current, this);
	}
	
	/**
	 * Asks the passenger if it should get on this train
	 * @param train - the train in question
	 * @return - if passenger should board
	 */
	public boolean shouldEmbark(Train train) {
		return router.shouldEmbark(train, this);
	}
	
	/**
	 * Always returns 0 as normal passengers have negligible cargo
	 */
	public int getWeight() { return 0; }
	
	public final int getId(){ return id; }
	public Station getDestination(){ return destination; }
	public Station getBeginning(){ return beginning; }
	public float getTravelTime(){ return travelTime; }
}
