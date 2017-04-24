package com.unimelb.swen30006.metromadness.tracks;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.trains.Train;

/** SWEN30006 Software Modeling and Design
DualTrack class
George Juliff - 624946
David Murges - 657384
Thomas Miles - 626263

Extends Track to allow for a train to travel in each direction
*/
public class DualTrack extends Track {

	// Alternative boolean for each direction
	private boolean forwardOccupied;
	private boolean backwardOccupied;
	
	/**
	 * Constructor
	 * @param start - start coordinates
	 * @param end - end coordinates
	 * @param col - colour for rendering
	 */
	public DualTrack(Point2D.Float start, Point2D.Float end, Color col) {
		super(start, end, col);
		this.forwardOccupied = false;
		this.backwardOccupied = false;
	}
	
	/**
	 * Render method
	 */
	public void render(ShapeRenderer renderer){
		renderer.rectLine(getStartPos().x, getStartPos().y, getEndPos().x, getEndPos().y, getLineWidth());
		renderer.setColor(new Color(245f/255f,245f/255f,245f/255f,0.5f).lerp(this.getTrackColour(), 0.5f));
		renderer.rectLine(getStartPos().x, getStartPos().y, getEndPos().x, getEndPos().y, getLineWidth()/3);
		renderer.setColor(this.getTrackColour());
	}
	
	/**
	 * Puts a train into the line
	 */
	@Override
	public void enter(Train t){
		if(t.getForward()){
			this.forwardOccupied = true;
		} else {
			this.backwardOccupied = true;
		}
	}

	/**
	 * Checks if a train can enter the line
	 */
	@Override
	public boolean canEnter(boolean forward) {
		if(forward){
			return !this.forwardOccupied;
		} else {
			return !this.backwardOccupied;
		}
	}

	/**
	 * Removes train from line
	 */
	@Override
	public void leave(Train t) {
		if(t.getForward()){
			this.forwardOccupied = false;
		} else {
			this.backwardOccupied = false;
		}
	}
}
