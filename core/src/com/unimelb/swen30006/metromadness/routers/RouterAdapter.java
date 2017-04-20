/**
 * 
 */
package com.unimelb.swen30006.metromadness.routers;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.trains.Train;

public interface RouterAdapter {

	public boolean shouldEmbark(Train train, Passenger p);
	public boolean shouldDisembark(Station current, Passenger p);
	
}
