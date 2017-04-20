package com.unimelb.swen30006.metromadness.passengers;

import java.util.Random;

import com.unimelb.swen30006.metromadness.routers.CargoRouter;
import com.unimelb.swen30006.metromadness.routers.RouterAdapter;
import com.unimelb.swen30006.metromadness.stations.Station;

public class CargoPassenger extends Passenger {

	public int cargoWeight;
	
	public CargoPassenger(int id, Random random, Station start, Station end) {
		super(id, start, end);
		this.cargoWeight = random.nextInt(51);
	}
	
	@Override
	public int getWeight() {
		return cargoWeight;
	}
	
	@Override
	protected RouterAdapter getRouter() { return new CargoRouter(); }

}
