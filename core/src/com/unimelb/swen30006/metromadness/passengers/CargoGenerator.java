package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

/** SWEN30006 Software Modeling and Design
CargoGenerator class
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Extension of passenger generator that creates CargoPassengers instead
*/
public class CargoGenerator extends PassengerGenerator {

	/**
	 * Constructor same as PassengerGenerator
	 * @param s - station that this generator is linked with
	 */
	public CargoGenerator(Station s) {
		super(s);
	}
	
	/**
	 * Override that produces a cargoPassenger instead of a standard one
	 */
	@Override
	public Passenger generatePassenger(Random random){
		// Pick a random station from the line that has multiple cargo stations on it
		ArrayList<Integer> possible;
		Line l;
		do {
		l = getStation().getRandLine(random);
		possible = new ArrayList<Integer>();
		
		for (Station station : l.getStations()) {
			if (station instanceof CargoStation) {
				possible.add(l.getStations().indexOf(station));
			}
		}
		} while (possible.size() < 2);
		
		// Get the index of the station
		int current_station = l.getStations().indexOf(this.getStation());
		boolean forward = random.nextBoolean();

		// If we are last cargo stop on the line set our direction forward or backward
		if(possible.indexOf(current_station) == 0){
			forward = true;
		} else if (possible.indexOf(current_station) == possible.size()-1){
			forward = false;
		}
		
		// Find the a random cargo station along the line
		int index = 0;
		
		if (forward){
			index = random.nextInt(possible.size()-1-possible.indexOf(current_station)) + possible.indexOf(current_station) + 1;
		} else {
			index = possible.indexOf(current_station) - 1 - random.nextInt(possible.indexOf(current_station));
		}
		Station s = l.getStations().get(possible.get(index));
		
		// Generate and return the new passenger
		return new CargoPassenger(idGen++, random, this.getStation(), s);
	}

}
