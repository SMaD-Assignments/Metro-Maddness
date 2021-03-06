package com.unimelb.swen30006.metromadness.trains;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerHandler;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.tracks.Track;

/** SWEN30006 Software Modeling and Design
Train Class
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Represents the trains in the simulation
*/
public class Train {
	
	// Logger
	private static Logger logger = LogManager.getLogger();
	// The state that a train can be in 
	public enum State {
		IN_STATION, READY_DEPART, ON_ROUTE, WAITING_ENTRY, FROM_DEPOT
	}

	// Constants
	protected static final int MAX_TRIPS=4;
	protected static final Color FORWARD_COLOUR = Color.ORANGE;
	protected static final Color BACKWARD_COLOUR = Color.VIOLET;
	protected static final float TRAIN_WIDTH=4;
	protected static final float TRAIN_LENGTH = 6;
	protected static final float TRAIN_SPEED=50f;
	
	// The train's name
	protected String name;

	// The line that this is traveling on
	protected Line trainLine;

	// Passenger Information
	protected ArrayList<Passenger> passengers;
	protected float departureTimer;
	protected int maxPassengers;
	
	// Station and track and position information
	protected Station station; 
	protected Track track;
	protected Point2D.Float pos;

	// Direction and direction
	protected boolean forward;
	protected State state;

	// State variables
	protected int numTrips;
	protected boolean disembarked;
	
	
	protected State previousState = null;

	/**
	 * Constructor
	 * @param trainLine - line train takes
	 * @param start - beginning station
	 * @param forward - direction of movement
	 * @param name - name of train
	 * @param size - passenger capacity
	 */
	public Train(Line trainLine, Station start, boolean forward, String name, int size){
		this.trainLine = trainLine;
		this.station = start;
		this.state = State.FROM_DEPOT;
		this.forward = forward;
		this.passengers = new ArrayList<Passenger>();
		this.name = name;
		this.maxPassengers = size;
	}

	/**
	 * Update the train and handles movement and calls PassengerHandler for passenger interactions
	 * @param delta - time since last update
	 */
	public void update(float delta){
		// Update all passengers
		PassengerHandler.update(passengers, delta);
		
		boolean hasChanged = false;
		if(previousState == null || previousState != this.state){
			previousState = this.state;
			hasChanged = true;
		}
		
		// Update the state
		switch(this.state) {
		case FROM_DEPOT:
			if(hasChanged){
				logger.info(this.name+ " is travelling from the depot: "+this.station.getName()+" Station...");
			}
			
			// We have our station initialized we just need to retrieve the next track, enter the
			// current station officially and mark as in station
			try {
				if(this.station.canEnter(this.trainLine)){
					
					this.station.enter(this);
					this.pos = (Point2D.Float) this.station.getPosition().clone();
					this.state = State.IN_STATION;
					this.disembarked = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		case IN_STATION:
			if(hasChanged){
				logger.info(this.name+" is in "+this.station.getName()+" Station.");
				// If the train should not stop at the station continue through
				if (isExpress()) {
					try {
						depart();
						break;
					} catch (Exception e){
						// Massive error.
						return;
					}
				}
			}
			
			// When in station we want to disembark passengers 
			// and wait 10 seconds for incoming passengers
			if(!this.disembarked){
				PassengerHandler.handleStation(station, this);
				this.departureTimer = this.station.getDepartureTime();
				this.disembarked = true;
			} else {
				// Count down if departure timer. 
				if(this.departureTimer>0){
					this.departureTimer -= delta;
				} else {
					// We are ready to depart, find the next track and wait until we can enter 
					try {
						depart();
						break;
					} catch (Exception e){
						// Massive error.
						return;
					}
				}
			}
			break;
		case READY_DEPART:
			if(hasChanged){
				logger.info(this.name+ " is ready to depart for "+this.station.getName()+" Station!");
			}
			
			// When ready to depart, check that the track is clear and if
			// so, then occupy it if possible.
			if(this.track.canEnter(this.forward)){
				try {
					// Find the next
					Station next = this.trainLine.nextStation(this.station, this.forward);
					// Depart our current station
					this.station.depart(this);
					this.station = next;

				} catch (Exception e) {
//					e.printStackTrace();
				}
				this.track.enter(this);
				this.state = State.ON_ROUTE;
			}		
			break;
		case ON_ROUTE:
			if(hasChanged){
				logger.info(this.name+ " enroute to "+this.station.getName()+" Station!");
			}
			
			// Checkout if we have reached the new station
			if(this.pos.distance(this.station.getPosition()) < 10 ){
				this.state = State.WAITING_ENTRY;
			} else {
				move(delta);
			}
			break;
		case WAITING_ENTRY:
			if(hasChanged){
				logger.info(this.name+ " is awaiting entry "+this.station.getName()+" Station..!");
			}
			
			// Waiting to enter, we need to check the station has room and if so
			// then we need to enter, otherwise we just wait
			try {
				if(this.station.canEnter(this.trainLine)){
					this.track.leave(this);
					this.pos = (Point2D.Float) this.station.getPosition().clone();
					this.station.enter(this);
					this.state = State.IN_STATION;
					this.disembarked = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}


	}

	/**
	 * Move the train
	 * @param delta - time since last step
	 */
	public void move(float delta){
		// Work out where we're going
		float angle = angleAlongLine(this.pos.x,this.pos.y,this.station.getPosition().x,this.station.getPosition().y);
		float newX = this.pos.x + (float)( Math.cos(angle) * delta * TRAIN_SPEED);
		float newY = this.pos.y + (float)( Math.sin(angle) * delta * TRAIN_SPEED);
		this.pos.setLocation(newX, newY);
	}

	/**
	 * Adds given passenger to train
	 * @throws Exception - if train is full
	 */
	public void embark(Passenger p) throws Exception {
		if(this.passengers.size() > maxPassengers){
			throw new Exception();
		}
		this.passengers.add(p);
	}
	
	/**
	 * removes given passenger
	 */
	public void disembark(Passenger p) {
		passengers.remove(p);
	}

	/**
	 * returns train status as a string
	 */
	@Override
	public String toString() {
		return "Train [line=" + this.trainLine.getName() +", departureTimer=" + departureTimer + ", pos=" + pos + ", forward=" + forward + ", state=" + state
				+ ", numTrips=" + numTrips + ", disembarked=" + disembarked + "]";
	}

	/**
	 * determines if train is in a station
	 */
	public boolean inStation(){
		return (this.state == State.IN_STATION || this.state == State.READY_DEPART);
	}
	
	/**
	 * Used to calculate angle for moving along a line in render
	 */
	public float angleAlongLine(float x1, float y1, float x2, float y2){	
		return (float) Math.atan2((y2-y1),(x2-x1));
	}

	/**
	 * Render the train
	 * @param renderer - renderer from libGDX
	 */
	public void render(ShapeRenderer renderer){
		if(!this.inStation()){
			float percentage = 0f;
			Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
			if (maxPassengers == 0) {
				renderer.setColor(col);
			} else if (maxPassengers < 50) {
				percentage = this.passengers.size()/10f;
				renderer.setColor(col.cpy().lerp(Color.MAROON, percentage));
			} else {
				percentage = this.passengers.size()/20f;
				renderer.setColor(col.cpy().lerp(Color.LIGHT_GRAY, percentage));
			}
			renderer.circle(this.pos.x, this.pos.y, TRAIN_WIDTH*(1+percentage));
		}
	}
	
	/**
	 * checks if the train is full
	 * @return
	 */
	public boolean isFull() {
		return (maxPassengers <= passengers.size());
	}
	
	public ArrayList<Passenger> getPassengers() { return passengers; }
	
	/**
	 * Normal train is never express
	 */
	protected boolean isExpress() { return false; }
	
	/**
	 * Prepares train to depart station
	 */
	protected void depart() throws Exception {
		boolean endOfLine = this.trainLine.endOfLine(this.station);
		if(endOfLine){
			this.forward = !this.forward;
		}
		this.track = this.trainLine.nextTrack(this.station, this.forward);
		this.state = State.READY_DEPART;
	}
	
	public ArrayList<Station> getStops() { return trainLine.getStations(); }
	public boolean getForward(){ return forward; }
}
