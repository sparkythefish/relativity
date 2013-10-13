package com.sparkythefish.drawing;

import java.util.ArrayList;

import android.graphics.Point;

public class LevelInfo {
	
	private ArrayList<Point> planetPositions;
	synchronized public ArrayList<Point> getPlanetPositions() {
		return planetPositions;
	}
	synchronized public void setPlanetPositions(ArrayList<Point> planetPositions) {
		this.planetPositions = planetPositions;
	}
	
	private Point ufoPosition;
	synchronized public Point getUfoPosition() {
		return ufoPosition;
	}
	synchronized public void setUfoPosition(Point ufoPosition) {
		this.ufoPosition = ufoPosition;
	}
}
