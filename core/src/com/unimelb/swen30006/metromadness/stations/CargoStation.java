package com.unimelb.swen30006.metromadness.stations;
import com.unimelb.swen30006.metromadness.passengers.CargoGenerator;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;

/** SWEN30006 Software Modeling and Design
CargoStation class
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Extension of station to deal with cargo
*/
public class CargoStation extends Station {

	/**
	 * Constructor is the same as normal station
	 */
	public CargoStation(float x, float y, String name, float maxPas, boolean active) {
		super(x, y, name, maxPas, active);
	}
	
	/**
	 * Override to change the type of passenger generator
	 */
	@Override
	protected PassengerGenerator createGen() {
		return new CargoGenerator(this);
	}

}
