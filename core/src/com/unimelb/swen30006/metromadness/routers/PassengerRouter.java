package com.unimelb.swen30006.metromadness.routers;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.trains.Train;

/** SWEN30006 Software Modeling and Design
PassengerRouter
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Tells Passengers how to navigate the system
*/
public class PassengerRouter implements RouterAdapter {

	/**
	 * Determines if given passenger should board given train
	 */
	@Override
	public boolean shouldEmbark(Train train, Passenger p) {
		boolean isOnLine = false;
		
		// Check if the the train will take the passenger to their station
		for( Station s : train.getStops()) {
			if (s.equals(p.getDestination())) {
				isOnLine = true;
				break;
			}
		}
		
		// Check if the passenger can fit on the train then return if the passenger should board
		return (isOnLine && !train.isFull());
	}

	/**
	 * Checks if given passenger should get of at current station
	 */
	@Override
	public boolean shouldDisembark(Station current, Passenger p) {
		return current.equals(p.getDestination());
	}

}
