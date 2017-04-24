package com.unimelb.swen30006.metromadness.tracks;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.trains.Train;

/** SWEN30006 Software Modeling and Design
Track class
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Represents the tracks in the simulation
*/
public class Track {
	private static final float DRAW_RADIUS=10f;
	private static final int LINE_WIDTH=6;
	private Point2D.Float startPos;
	private Point2D.Float endPos;
	private Color trackColour;
	private boolean occupied;
	
	/**
	 * Constructor
	 * @param start - start coordinates
	 * @param end - end coordinates
	 * @param col - colour for rendering
	 */
	public Track(Point2D.Float start, Point2D.Float end, Color trackCol){
		this.startPos = start;
		this.endPos = end;
		this.trackColour = trackCol;
		this.occupied = false;
	}
	
	/**
	 * Render method
	 */
	public void render(ShapeRenderer renderer){
		renderer.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, LINE_WIDTH);
	}
	
	/**
	 * Checks if a train can enter the line
	 */
	public boolean canEnter(boolean forward){
		return !this.occupied;
	}
	
	/**
	 * Puts a train into the line
	 */
	public void enter(Train t){
		this.occupied = true;
	}
	
	/**
	 * Returns status as a string
	 */
	@Override
	public String toString() {
		return "Track [startPos=" + startPos + ", endPos=" + endPos + ", trackColour=" + trackColour + ", occupied="
				+ occupied + "]";
	}

	/**
	 * Removes train from line
	 */
	public void leave(Train t){
		this.occupied = false;
	}
	
	public static final float getDrawRadius(){ return DRAW_RADIUS; }
	public static final int getLineWidth(){ return LINE_WIDTH; }
	public Point2D.Float getStartPos(){ return startPos; }
	public Point2D.Float getEndPos(){ return endPos; }
	public Color getTrackColour(){ return trackColour; }
	public boolean getOccupied(){ return occupied; }
}
