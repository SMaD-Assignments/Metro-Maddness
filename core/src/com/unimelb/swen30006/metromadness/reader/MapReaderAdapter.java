package com.unimelb.swen30006.metromadness.reader;

import java.util.Collection;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

/** Interface for the MapReader, allows for minimal adaptation to add a different file format */
public interface MapReaderAdapter {
	
	public Collection<Train> getTrains();
	public Collection<Line> getLines();
	public Collection<Station> getStations();
	
}
