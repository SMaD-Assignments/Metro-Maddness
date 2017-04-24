package com.unimelb.swen30006.metromadness.routers;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.trains.Train;

/** SWEN30006 Software Modeling and Design
RouterAdapter interface
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Adapter to allow for easy implementation of new types of passenger logic
*/
public interface RouterAdapter {

	public boolean shouldEmbark(Train train, Passenger p);
	public boolean shouldDisembark(Station current, Passenger p);
	
}
