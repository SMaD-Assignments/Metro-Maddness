package com.unimelb.swen30006.metromadness.reader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

// Imports for parsing XML files
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

// The things we are generating
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.stations.CargoStation;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.CargoTrain;
import com.unimelb.swen30006.metromadness.trains.Train;

/** SWEN30006 Software Modeling and Design
MapReader class
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Reads and interprets a .xml map
*/
public class MapReader implements MapReaderAdapter {

	// Constants for train size
	private final int SMALL_TRAIN_SIZE = 10;
	private final int BIG_TRAIN_SIZE = 80;
	private final int SMALL_CARGO_SIZE = 200;
	private final int BIG_CARGO_SIZE = 1000;
	
	// Collections for created items
	private ArrayList<Train> trains;
	private HashMap<String, Station> stations;
	private HashMap<String, Line> lines;

	// Boolean to check if file has been processed
	private boolean processed;
	
	// file to read
	private String filename;

	/**
	 * Constructor sets up environment but does not process
	 * @param filename - destination of map file
	 */
	public MapReader(String filename){
		this.trains = new ArrayList<Train>();
		this.stations = new HashMap<String, Station>();
		this.lines = new HashMap<String, Line>();
		this.filename = filename;
		this.processed = false;
	}

	/**
	 * Private process method called the first time a getter is used
	 */
	private void process(){
		try {
			// Build the doc factory
			FileHandle file = Gdx.files.internal(filename);
			XmlReader reader = new XmlReader();
			Element root = reader.parse(file);
			
			// Process stations
			Element stations = root.getChildByName("stations");
			Array<Element> stationList = stations.getChildrenByName("station");
			for(Element e : stationList){
				Station s = processStation(e);
				this.stations.put(s.getName(), s);
			}
			
			// Process Lines
			Element lines = root.getChildByName("lines");
			Array<Element> lineList = lines.getChildrenByName("line");
			for(Element e : lineList){
				Line l = processLine(e);
				this.lines.put(l.getName(), l);
			}

			// Process Trains
			Element trains = root.getChildByName("trains");
			Array<Element> trainList = trains.getChildrenByName("train");
			for(Element e : trainList){
				Train t = processTrain(e);
				this.trains.add(t);
			}
			
			this.processed = true;
			
		} catch (Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Interface method used to get collection of trains
	 */
	@Override
	public Collection<Train> getTrains(){
		if(!this.processed) { this.process(); }
		return this.trains;
	}
	
	/**
	 * Interface method used to get collection of lines
	 */
	@Override
	public Collection<Line> getLines(){
		if(!this.processed) { this.process(); }
		return this.lines.values();
	}
	
	/**
	 * Interface method used to get collection of stations
	 */
	@Override
	public Collection<Station> getStations(){
		if(!this.processed) { this.process(); }
		return this.stations.values();
	}

	/**
	 * process the section of file
	 * @param e - element of XML with info about a train
	 * @return - created train
	 */
	private Train processTrain(Element e){
		// Retrieve the values
		String type = e.get("type");
		String line = e.get("line");
		String start = e.get("start");
		String name = e.get("name");
		boolean dir = e.getBoolean("direction");

		// Retrieve the lines and stations
		Line l = this.lines.get(line);
		Station s = this.stations.get(start);
		
		// Make the train
		if(type.equals("BigPassenger")){
			return new Train(l,s,dir,name, BIG_TRAIN_SIZE);
		} else if (type.equals("SmallPassenger")){
			return new Train(l,s,dir,name, SMALL_TRAIN_SIZE);
		} else if (type.equals("SmallCargo")){
			return new CargoTrain(l,s,dir,name, SMALL_TRAIN_SIZE, SMALL_CARGO_SIZE);
		} else if (type.equals("BigCargo")){
			return new CargoTrain(l,s,dir,name, BIG_TRAIN_SIZE, BIG_CARGO_SIZE);
		} else {
			return new Train(l, s, dir,name, 0);
		}
	}
	
	/**
	 * process the section of file
	 * @param e - element of XML with info about a station
	 * @return - created station
	 */
	private Station processStation(Element e){
		String type = e.get("type");
		String name = e.get("name");
		String router = e.get("router");
		int x_loc = e.getInt("x_loc")/8;
		int y_loc = e.getInt("y_loc")/8;
		if(type.equals("Active")){
			int maxPax = e.getInt("max_passengers");
			if (router.equals("cargo")) {
				return new CargoStation(x_loc, y_loc, name, maxPax, true);
			} else {
				return new Station(x_loc, y_loc, name, maxPax, true);
			}
		} else if (type.equals("Passive")){
			int maxPax = e.getInt("max_passengers");
			if (router.equals("cargo")) {
				return new CargoStation(x_loc, y_loc, name, maxPax, true);
			} else {
				return new Station(x_loc, y_loc, name, maxPax, true);
			}
		} else{
			return new Station(x_loc,y_loc,name, 0, false);
		}
	}

	/**
	 * process the section of file
	 * @param e - element of XML with info about a line
	 * @return - created line
	 */
	private Line processLine(Element e){
		Color stationCol = extractColour(e.getChildByName("station_colour"));
		Color lineCol = extractColour(e.getChildByName("line_colour"));
		String name = e.get("name");
		Line l = new Line(stationCol, lineCol, name);
		
		Array<Element> stations = e.getChildrenByNameRecursively("station");
		for(Element s: stations){
			Station station = this.stations.get(s.get("name"));
			boolean twoWay = s.getBoolean("double");
			l.addStation(station, twoWay);
		}
		
		return l;
	}
	
	/**
	 * process the section of file
	 * @param e - element of XML with info about colour to use
	 * @return - found colour
	 */
	private Color extractColour(Element e){
		float red = e.getFloat("red")/255f;
		float green = e.getFloat("green")/255f;
		float blue = e.getFloat("blue")/255f;
		return new Color(red, green, blue, 1f);
	}

}
