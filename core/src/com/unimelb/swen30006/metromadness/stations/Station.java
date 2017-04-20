package com.unimelb.swen30006.metromadness.stations;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

public class Station {
	
	protected static Logger logger = LogManager.getLogger();
	
	public static final int PLATFORMS=2;
	
	public Point2D.Float position;
	public static final float RADIUS=6;
	public static final int NUM_CIRCLE_STATMENTS=100;
	public static final int MAX_LINES=3;
	public String name;
	public ArrayList<Line> lines;
	public ArrayList<Train> trains;
	public static final float DEPARTURE_TIME = 2;
	public ArrayList<Passenger> waiting;
	public float maxVolume;
	protected PassengerGenerator g;
	protected boolean isActive;

	public Station(float x, float y, String name, float maxPas, boolean active){
		this.name = name;
		this.position = new Point2D.Float(x,y);
		this.lines = new ArrayList<Line>();
		this.trains = new ArrayList<Train>();
		this.waiting = new ArrayList<Passenger>();
		this.maxVolume = maxPas;
		g = createGen();
		isActive = active;
	}
	
	protected PassengerGenerator createGen() {
		return new PassengerGenerator(this);
	}
	
	public void registerLine(Line l){
		this.lines.add(l);
	}
	
	public void render(ShapeRenderer renderer){
		float radius = RADIUS;
		for(int i=0; (i<this.lines.size() && i<MAX_LINES); i++){
			Line l = this.lines.get(i);
			renderer.setColor(l.lineColour);
			renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
			radius = radius - 1;
		}
		
		// Calculate the percentage
		float t = this.trains.size()/(float)PLATFORMS;
		Color c = Color.WHITE.cpy().lerp(Color.DARK_GRAY, t);
		if(this.waiting.size() > 0){
		c = Color.RED;
		}
		renderer.setColor(c);
		renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
			
	}
	
	public void enter(Train t) throws Exception {
		if(trains.size() >= PLATFORMS){
			throw new Exception();
		} else {
			this.trains.add(t);
		}
	}
	
	
	public void depart(Train t) throws Exception {
		if(this.trains.contains(t)){
			this.trains.remove(t);
		} else {
			throw new Exception();
		}
	}
	
	public boolean canEnter(Line l) throws Exception {
		return trains.size() < PLATFORMS;
	}

	// Returns departure time in seconds
	public float getDepartureTime() {
		return DEPARTURE_TIME;
	}


	@Override
	public String toString() {
		return "Station [position=" + position + ", name=" + name + ", trains=" + trains.size()
				 + "]";
	}
	
	public boolean checkActive() {return isActive;}
	
	public Line getRandLine(Random random) {
		return lines.get(random.nextInt(lines.size()));
	}
	
	public ArrayList<Passenger> getWaiting() { return waiting; }
	
	public String getName() { return name; }
	
	public float getMaxVolume() { return maxVolume; }
	
	public PassengerGenerator getPasGen() { return g; }
	
}
