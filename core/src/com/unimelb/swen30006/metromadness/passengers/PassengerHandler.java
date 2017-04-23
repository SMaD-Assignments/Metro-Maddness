package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.trains.CargoTrain;
import com.unimelb.swen30006.metromadness.trains.Train;

public class PassengerHandler {
	
	private static Logger logger = LogManager.getLogger();
	private static ArrayList<Passenger> arrived = new ArrayList<Passenger>();
	
	public static void handleStation(Station s,Train t) {
		
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
	
	public static void update(ArrayList<Passenger> passengers, float delta) {
		for(Passenger p: passengers){
			p.update(delta);
		}
	}
	
}
