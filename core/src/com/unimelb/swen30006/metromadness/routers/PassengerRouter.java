package com.unimelb.swen30006.metromadness.routers;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.trains.Train;

public class PassengerRouter implements RouterAdapter {

	@Override
	public boolean shouldEmbark(Train train, Passenger p) {
		boolean isOnLine = false;
		
		// Check if the the train will take the passenger to their station
		for( Station s : train.getStops()) {
			if (s.equals(p.destination)) {
				isOnLine = true;
				break;
			}
		}
		
		// Check if the passenger can fit on the train then return if the passenger should board
		return (isOnLine && !train.isFull());
	}

	@Override
	public boolean shouldDisembark(Station current, Passenger p) {
		return current.equals(p.destination);
	}

}
