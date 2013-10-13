package com.sparkythefish.drawing;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class Level extends View{
	
	private ArrayList<Planet> planets;
	
	synchronized public ArrayList<Planet> getPLanets() {
		return planets;
	}
	
	public Level(LevelInfo levelInfo, Context context){
		this(context);
		planets = new ArrayList<Planet>();
		for (Point planetPosition : levelInfo.getPlanetPositions()) {
			Planet planet = new Planet(planetPosition, context);
			planets.add(planet);
		}
	}

	public Level(Context context) {
		super(context);
	}
	
	synchronized public void draw(Canvas canvas) {
		for (Planet planet : planets) {
			planet.draw(canvas);
		}
	}
	
	synchronized public Point checkForCollision(Ufo ufo) {
		Point collisionPoint = null;
		for (Planet planet : planets) {
			collisionPoint = planet.checkForCollision(ufo);
			if(collisionPoint != null) { 
				return collisionPoint; 
			}
		}
		return collisionPoint;
	}
	
	synchronized public void updateLocations(Point ufoVel) {
		for (Planet planet : planets) {
			planet.updateLocation(ufoVel);
		}
	}
	
	synchronized public void reset() {
		for (Planet planet : planets) {
			planet.reset();
		}
	}
}
