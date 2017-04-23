package com.unimelb.swen30006.metromadness.tracks;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.trains.Train;

public class Track {
	private static final float DRAW_RADIUS=10f;
	private static final int LINE_WIDTH=6;
	private Point2D.Float startPos;
	private Point2D.Float endPos;
	private Color trackColour;
	private boolean occupied;
	
	public Track(Point2D.Float start, Point2D.Float end, Color trackCol){
		this.startPos = start;
		this.endPos = end;
		this.trackColour = trackCol;
		this.occupied = false;
	}
	
	public void render(ShapeRenderer renderer){
		renderer.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, LINE_WIDTH);
	}
	
	public boolean canEnter(boolean forward){
		return !this.occupied;
	}
	
	public void enter(Train t){
		this.occupied = true;
	}
	
	@Override
	public String toString() {
		return "Track [startPos=" + startPos + ", endPos=" + endPos + ", trackColour=" + trackColour + ", occupied="
				+ occupied + "]";
	}

	public void leave(Train t){
		this.occupied = false;
	}
	
	public static final float getDrawRadius(){
		return DRAW_RADIUS;
	}
	public static final int getLineWidth(){
		return LINE_WIDTH;
	}
	
	public Point2D.Float getStartPos(){
		return startPos;
	}
	
	public Point2D.Float getEndPos(){
		return endPos;
	}
	
	public Color getTrackColour(){
		return trackColour;
	}
	
	public boolean getOccupied(){
		return occupied;
	}
	
	
	
	
	
}
