package com.unimelb.swen30006.metromadness.stations;
import com.unimelb.swen30006.metromadness.passengers.CargoGenerator;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;

public class CargoStation extends Station {

	public CargoStation(float x, float y, String name, float maxPas, boolean active) {
		super(x, y, name, maxPas, active);
	}
	
	@Override
	protected PassengerGenerator createGen() {
		return new CargoGenerator(this);
	}

}
