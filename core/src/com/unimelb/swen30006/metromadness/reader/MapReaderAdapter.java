package com.unimelb.swen30006.metromadness.reader;

import java.util.Collection;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

/** SWEN30006 Software Modeling and Design
MapReaderAdapter interface
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Provides an interface for easy adaptation to read new map formats
*/
public interface MapReaderAdapter {
	
	public Collection<Train> getTrains();
	public Collection<Line> getLines();
	public Collection<Station> getStations();
	
}
