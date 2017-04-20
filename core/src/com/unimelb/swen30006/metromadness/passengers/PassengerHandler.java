package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.trains.Train;

public class PassengerHandler {
	
	private static Logger logger = LogManager.getLogger();
	private static ArrayList<Passenger> arrived = new ArrayList<Passenger>();
	
	public static void handleStation(Station s,Train t) {
		
		// Add the waiting passengers
		Iterator<Passenger> pIter = s.getWaiting().iterator();
		while(pIter.hasNext()){
			Passenger p = pIter.next();
			try {
				if(p.shouldEmbark(t)) {
					logger.info("Passenger "+p.id+" carrying "+p.getWeight() +" kg cargo embarking at " +s.getName() +" heading to "+p.destination.name);
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
					logger.info("Passenger "+p.id+" carrying "+p.getWeight() +" kg cargo embarking at " +s.getName() +" heading to "+p.destination.name);
					t.embark(p);
				} else {
					s.getWaiting().add(p);
				}
			} catch(Exception e){
				// Should never get here
				System.out.println("ERROR: Should never try to board a full train...");
			}
		}
		
		// Disembark passengers and save in arrived array
		pIter = t.getPassengers().iterator();
		while(pIter.hasNext()){
			Passenger p = pIter.next();
			if (p.shouldDisembark(s)) {
				logger.info("Passenger "+p.id+" is disembarking at "+s.getName());
				pIter.remove();
				arrived.add(p);
			}
		}
		
	}
	
}
