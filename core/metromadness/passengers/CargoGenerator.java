package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class CargoGenerator extends PassengerGenerator {

	public CargoGenerator(Station s) {
		super(s);
	}
	
	@Override
	public Passenger generatePassenger(Random random){
		// Pick a random station from the line that has multiple cargo stations on it
		ArrayList<Integer> possible;
		Line l;
		do {
		l = s.getRandLine(random);
		possible = new ArrayList<Integer>();
		
		for (Station station : l.getStations()) {
			if (station instanceof CargoStation) {
				possible.add(l.getStations().indexOf(station));
			}
		}
		} while (possible.size() < 2);
		
		int current_station = l.getStations().indexOf(this.s);
		boolean forward = random.nextBoolean();

		// If we are the end of the line then set our direction forward or backward
		System.out.println("index: " + possible.indexOf(current_station) + "   size: " + possible.size() + "   Station: " + s.name);
		if(possible.indexOf(current_station) == 0){
			forward = true;
		} else if (possible.indexOf(current_station) == possible.size()-1){
			forward = false;
		}
		
		// Find the station
		int index = 0;
		
		if (forward){
			index = random.nextInt(possible.size()-1-possible.indexOf(current_station)) + possible.indexOf(current_station) + 1;
		} else {
			index = possible.indexOf(current_station) - 1 - random.nextInt(possible.indexOf(current_station));
		}
		Station s = l.getStations().get(possible.get(index));
		
		return new CargoPassenger(idGen++, random, this.s, s);
	}

}
