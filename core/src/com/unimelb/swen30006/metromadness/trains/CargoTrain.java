package com.unimelb.swen30006.metromadness.trains;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

/** SWEN30006 Software Modeling and Design
CargoTrain class
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Extends Train to deal with cargo
*/
public class CargoTrain extends Train {
	
	// New variables to store cargo info
	protected int cargoCapacity;
	protected int currentCargo;
	
	/**
	 * Constructor largely the same other than capacity
	 * @param capacity - cargo capacity of train
	 */
	public CargoTrain(Line trainLine, Station start, boolean forward, String name, int size, int capacity) {
		super(trainLine, start, forward, name, size);
		cargoCapacity = capacity;
		currentCargo = 0;
	}
	
	/**
	 * Determines if the train should express through a station because it does not take cargo
	 */
	@Override
	protected boolean isExpress() {
		return !(station instanceof CargoStation);
	}
	
	/**
	 * Adds given passenger to train
	 * @throws Exception - if train is full
	 */
	@Override
	public void embark(Passenger p) throws Exception {
		if(this.passengers.size() > maxPassengers || p.getWeight()+currentCargo > cargoCapacity){
			throw new Exception();
		}
		this.passengers.add(p);
		currentCargo += p.getWeight();
	}
	
	/**
	 * removes given passenger with cargo
	 */
	@Override
	public void disembark(Passenger p) {
		passengers.remove(p);
		currentCargo -= p.getWeight();
	}
	
	/**
	 * Determines if cargo can be added to train
	 * @param add - weight of cargo
	 */
	public boolean hasCargoSpace(int add) {
		if(add + currentCargo > cargoCapacity) {
			return false;
		}
		return true;
	}

}
