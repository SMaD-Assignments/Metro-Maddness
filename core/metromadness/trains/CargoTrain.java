package com.unimelb.swen30006.metromadness.trains;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class CargoTrain extends Train {
	
	protected int cargoCapacity;
	protected int currentCargo;
	
	public CargoTrain(Line trainLine, Station start, boolean forward, String name, int size, int capacity) {
		super(trainLine, start, forward, name, size);
		cargoCapacity = capacity;
		currentCargo = 0;
	}
	
	@Override
	protected boolean isExpress() {
		return !(station instanceof CargoStation);
	}
	
	@Override
	public void embark(Passenger p) throws Exception {
		if(this.passengers.size() > maxPassengers || p.getWeight()+currentCargo > cargoCapacity){
			throw new Exception();
		}
		this.passengers.add(p);
		currentCargo += p.getWeight();
	}
	
	@Override
	public void disembark(Passenger p) {
		passengers.remove(p);
		currentCargo -= p.getWeight();
	}
	
	public boolean hasCargoSpace(int add) {
		if(add + currentCargo > cargoCapacity) {
			return false;
		}
		return true;
	}

}
