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

/** SWEN30006 Software Modeling and Design
Station class
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Represents stations in the simulation
*/
public class Station {
	
	protected static Logger logger = LogManager.getLogger();
	protected static final int PLATFORMS=2;
	protected Point2D.Float position;
	protected static final float RADIUS=6;
	protected static final int NUM_CIRCLE_STATMENTS=100;
	protected static final int MAX_LINES=3;
	protected String name;
	protected ArrayList<Line> lines;
	protected ArrayList<Train> trains;
	protected static final float DEPARTURE_TIME = 2;
	protected ArrayList<Passenger> waiting;
	protected float maxVolume;
	protected PassengerGenerator g;
	
	// Boolean to used to check if trains should stop at the station
	protected boolean isActive;

	/**
	 * Constructor
	 * @param x - x coordinate for rendering
	 * @param y - y coordinate for rendering
	 * @param name - station name
	 * @param maxPas - maximum passengers that can wait at the station
	 * @param active - if the station should begin as active
	 */
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
	
	/**
	 * Creates the passenger generator, used for greater extendability
	 */
	protected PassengerGenerator createGen() {
		return new PassengerGenerator(this);
	}
	
	/**
	 * Adds a line to the station
	 */
	public void registerLine(Line l){
		this.lines.add(l);
	}
	
	/**
	 * Renders the station
	 * @param renderer - renderer form libGDX
	 */
	public void render(ShapeRenderer renderer){
		float radius = RADIUS;
		for(int i=0; (i<this.lines.size() && i<MAX_LINES); i++){
			Line l = this.lines.get(i);
			renderer.setColor(l.getLineColour());
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
	
	/**
	 * Puts a train into the station
	 * @param t - train to enter
	 * @throws Exception - if station is full
	 */
	public void enter(Train t) throws Exception {
		if(trains.size() >= PLATFORMS){
			throw new Exception();
		} else {
			this.trains.add(t);
		}
	}
	
	/**
	 * Removes a train from the station
	 * @param t - train to remove
	 * @throws Exception - if train is not there
	 */
	public void depart(Train t) throws Exception {
		if(this.trains.contains(t)){
			this.trains.remove(t);
		} else {
			throw new Exception();
		}
	}
	
	/**
	 * Determines if a train can enter the station from given line
	 * @param l - line to enter from
	 */
	public boolean canEnter(Line l) throws Exception {
		return trains.size() < PLATFORMS;
	}

	/**
	 *  Returns departure time in seconds
	 */
	public float getDepartureTime() {
		return DEPARTURE_TIME;
	}

	/**
	 * Returns station stats as a string
	 */
	@Override
	public String toString() {
		return "Station [position=" + position + ", name=" + name + ", trains=" + trains.size()
				 + "]";
	}
	
	/**
	 * Returns a random line registered to the station
	 * @param random -  randomizer to use
	 */
	public Line getRandLine(Random random) {
		return lines.get(random.nextInt(lines.size()));
	}
	
	public boolean checkActive() {return isActive;}
	public ArrayList<Passenger> getWaiting() { return waiting; }
	public String getName() { return name; }
	public float getMaxVolume() { return maxVolume; }
	public PassengerGenerator getPasGen() { return g; }
	public Point2D.Float getPosition(){ return position; }
	
}
