package com.unimelb.swen30006.metromadness.tracks;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.stations.Station;

/** SWEN30006 Software Modeling and Design
Line class
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Represents the train lines in the simulation
*/
public class Line {
	
	// The colour of this line
	private Color lineColour;
	private Color trackColour;
	
	// The name of this line
	private String name;
	// The stations on this line
	private ArrayList<Station> stations;
	// The tracks on this line between stations
	private ArrayList<Track> tracks;
		
	/**
	 * Constructor
	 * @param stationColour - colour for rendering
	 * @param lineColour - colour for lines
	 * @param name - line name
	 */
	public Line(Color stationColour, Color lineColour, String name){
		// Set the line colour
		this.lineColour = stationColour;
		this.trackColour = lineColour;
		this.name = name;
		
		// Create the data structures
		this.stations = new ArrayList<Station>();
		this.tracks = new ArrayList<Track>();
	}
	
	
	/**
	 * Add a station to the line
	 * @param s - station to add
	 * @param two_way - if the line can have a train each way or not
	 */
	public void addStation(Station s, Boolean two_way){
		// We need to build the track if this is adding to existing stations
		if(this.stations.size() > 0){
			// Get the last station
			Station last = this.stations.get(this.stations.size()-1);
			
			// Generate a new track
			Track t;
			if(two_way){
				t = new DualTrack(last.getPosition(), s.getPosition(), this.trackColour);
			} else {
				t = new Track(last.getPosition(), s.getPosition(), this.trackColour);
			}
			this.tracks.add(t);
		}
		
		// Add the station
		s.registerLine(this);
		this.stations.add(s);
	}
	
	/**
	 * Returns line status as a string
	 */
	@Override
	public String toString() {
		return "Line [lineColour=" + lineColour + ", trackColour=" + trackColour + ", name=" + name + "]";
	}

	/**
	 * Determines if given station is the last of the line
	 * @param s - station to check
	 * @throws Exception - if station is not on the line
	 */
	public boolean endOfLine(Station s) throws Exception{
		if(this.stations.contains(s)){
			int index = this.stations.indexOf(s);
			return (index==0 || index==this.stations.size()-1);
		} else {
			throw new Exception();
		}
	}

	/**
	 * Returns the next track along the line
	 * @param currentStation - starting point
	 * @param forward - direction of travel
	 * @throws Exception - if station is not on line
	 */
	public Track nextTrack(Station currentStation, boolean forward) throws Exception {
		if(this.stations.contains(currentStation)){
			// Determine the track index
			int curIndex = this.stations.lastIndexOf(currentStation);
			// Increment to retrieve
			if(!forward){ curIndex -=1;}
			
			// Check index is within range
			if((curIndex < 0) || (curIndex > this.tracks.size()-1)){
				throw new Exception();
			} else {
				return this.tracks.get(curIndex);
			}
			
		} else {
			throw new Exception();
		}
	}
	
	/**
	 * Returns next station along line
	 * @param s - starting point
	 * @param forward - direction of travel
	 * @throws Exception - if station not on line
	 */
	public Station nextStation(Station s, boolean forward) throws Exception{
		if(this.stations.contains(s)){
			int curIndex = this.stations.lastIndexOf(s);
			if(forward){ curIndex+=1;}else{ curIndex -=1;}
			
			// Check index is within range
			if((curIndex < 0) || (curIndex > this.stations.size()-1)){
				throw new Exception();
			} else {
				return this.stations.get(curIndex);
			}
		} else {
			throw new Exception();
		}
	}
	
	/**
	 * Render the line
	 * @param renderer - renderer from libGDX
	 */
	public void render(ShapeRenderer renderer){
		// Set the color to our line
		renderer.setColor(trackColour);
	
		// Draw all the track sections
		for(Track t: this.tracks){
			t.render(renderer);
		}	
	}
	
	public ArrayList<Station> getStations() { return stations; }
	public ArrayList<Track> getTracks(){ return tracks; }
	public Color getLineColour(){ return lineColour; }
	public Color getTrackColour(){ return trackColour; }
	public String getName(){ return name; }
}
