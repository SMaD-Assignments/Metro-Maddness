package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.trains.CargoTrain;
import com.unimelb.swen30006.metromadness.trains.Train;

/** SWEN30006 Software Modeling and Design
PassengerHandler
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Static class that collects much of the passenger logic in one place
*/
public class PassengerHandler {
	
	/*
	 *  Logger used to print state of passengers, and ArrayList to hold arrived passengers
	 *  for later analysis
	 */
	private static Logger logger = LogManager.getLogger();
	private static ArrayList<Passenger> arrived = new ArrayList<Passenger>();
	
	/**
	 * Method used by train to deal with all passenger interactions when pulling into a station
	 * @param s - Station being arrived at
	 * @param t - arriving train
	 */
	public static void handleStation(Station s,Train t) {
		
		// As long as it is not a normal Train pulling into a CargoStation
		if(!(s instanceof CargoStation && !(t instanceof CargoTrain))) {
			// Add the waiting passengers
			Iterator<Passenger> pIter = s.getWaiting().iterator();
			while(pIter.hasNext()){
				Passenger p = pIter.next();
				try {
					if(p.shouldEmbark(t)) {
						logger.info("Passenger "+p.getId()+" carrying "+p.getWeight() +" kg cargo embarking at " +s.getName() +" heading to "+p.getDestination().getName());
						t.embark(p);
						pIter.remove();
					}
				} catch (Exception e){
					// Should never get here
					System.out.println("ERROR: Should never try to board a full train...");
					break;
				}
			}

			// If platform full do not generate passengers
			if (s.getMaxVolume() < s.getWaiting().size()) {
				return;
			}

			// Generate a random number of passengers
			Passenger[] ps = s.getPasGen().generatePassengers();
			for(Passenger p: ps){
				try {
					if(p.shouldEmbark(t)) {
						logger.info("Passenger "+p.getId()+" carrying "+ p.getWeight() +" kg cargo embarking at " +s.getName() +" heading to "+p.getDestination().getName());
						t.embark(p);
					} else {
						s.getWaiting().add(p);
					}
				} catch(Exception e){
					// Should never get here
					System.out.println("ERROR: Should never try to board a full train...");
				}
			}
		}
		
		// Disembark passengers and save in arrived array
		int currentDep = arrived.size();
		for (Passenger p : t.getPassengers()){
			if (p.shouldDisembark(s)) {
				logger.info("Passenger "+p.getId()+" is disembarking at "+s.getName());
				arrived.add(p);
			}
		}
		
		for (; currentDep < arrived.size(); currentDep++) {
			t.disembark(arrived.get(currentDep));
		}
		
	}
	
	
	/**
	 * Update all timers on the passengers provided
	 * @param passengers - passengers to be updated
	 * @param delta - time since last update
	 */
	public static void update(ArrayList<Passenger> passengers, float delta) {
		for(Passenger p: passengers){
			p.update(delta);
		}
	}
	
}
