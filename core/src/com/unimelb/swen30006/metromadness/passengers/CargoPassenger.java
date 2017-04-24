package com.unimelb.swen30006.metromadness.passengers;

import java.util.Random;

import com.unimelb.swen30006.metromadness.routers.CargoRouter;
import com.unimelb.swen30006.metromadness.routers.RouterAdapter;
import com.unimelb.swen30006.metromadness.stations.Station;

/** SWEN30006 Software Modeling and Design
CargoPassenger class
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Extension of Passenger class that implements passenger cargo weight
*/
public class CargoPassenger extends Passenger {

	// addition of cargoWeight parameter
	private int cargoWeight;
	
	/**
	 * Constructor, requires random to create the cargoWeight
	 * @param id - Passenger id
	 * @param random - Random number generator
	 * @param start - Current station
	 * @param end - Destination station
	 */
	public CargoPassenger(int id, Random random, Station start, Station end) {
		super(id, start, end);
		this.cargoWeight = random.nextInt(51);
	}
	
	@Override
	public int getWeight() { return cargoWeight; }
	
	/**
	 * Used in constructor to allow for greater re-use in extended classes
	 */
	@Override
	protected RouterAdapter getRouter() { return new CargoRouter(); }

}
