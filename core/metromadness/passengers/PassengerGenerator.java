package com.unimelb.swen30006.metromadness.passengers;

import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class PassengerGenerator {
	
	// Random number generator
	static final protected Random random = new Random(30006);
	// Passenger id generator
	static protected int idGen = 1;
	// The station that passengers are getting on
	public Station s;
	
	public PassengerGenerator(Station s){
		this.s = s;
	}
	
	public Passenger[] generatePassengers(){
		int count = random.nextInt(4)+1;
		Passenger[] passengers = new Passenger[count];
		for(int i=0; i<count; i++){
			passengers[i] = generatePassenger(random);
		}
		return passengers;
	}
	
	public Passenger generatePassenger(Random random){
		// Pick a random station from the line
		Line l = s.getRandLine(random);
		int current_station = l.getStations().indexOf(this.s);
		boolean forward = random.nextBoolean();
		
		// If we are the end of the line then set our direction forward or backward
		if(current_station == 0){
			forward = true;
		} else if (current_station == l.getStations().size()-1){
			forward = false;
		}
		
		// Find the station
		int index = 0;
		
		if (forward){
			index = random.nextInt(l.getStations().size()-1-current_station) + current_station + 1;
		} else {
			index = current_station - 1 - random.nextInt(current_station);
		}
		Station s = l.getStations().get(index);
		
		return new Passenger(idGen++, this.s, s);
	}
	
}
