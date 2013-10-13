package com.sparkythefish.drawing;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class GameBoard extends View{
	
	private Paint p;
	
	private Background background = null;
	private Ufo ufo = null;
    private GameInfo gameInfo = null;
    private ArrayList<Level> levels = null;
    private int currentLevelNum = 0;
    private Level currentLevel = null;
    
    private boolean collisionDetected = false;
    private Point lastCollision = new Point(-1,-1);
    
    synchronized public Background getBoardBackground() {
    	return background;
    }
	
    synchronized public Ufo getUfo() {
    	return ufo;
    }
    
    synchronized public void setGameInfo(GameInfo gameInfo) {
    	this.gameInfo = gameInfo;
    }


	//return the point of the last collision
	synchronized public Point getLastCollision() {
		return lastCollision;
	}
	//return the collision flag
	synchronized public boolean wasCollisionDetected() {
		return collisionDetected;
	}
	
	synchronized public void setCurrentLevelNum(int levelNum) {
		this.currentLevelNum = levelNum;
	}
	
	synchronized public int getCurrentLevelNum() {
		return this.currentLevelNum;
	}
	
	synchronized public Level getCurrentLevel() {
		return this.currentLevel;
	}
	
	public GameBoard(Context context, AttributeSet aSet) throws Exception {
		super(context, aSet);
		p = new Paint();
		background = new Background(context);
		ufo = new Ufo(context);
		levels = new ArrayList<Level>();
	}
	
	@Override
	synchronized public void onDraw(Canvas canvas) {
		//Now we draw our sprites.  Items drawn in this function are stacked.
		//The items drawn at the top of the loop are on the bottom of the z-order.
		background.draw(canvas);
		
		if (currentLevel != null) {
			ufo.draw(canvas);
			currentLevel.draw(canvas);
			//The last order of business is to check for a collision
			lastCollision = getCurrentLevel().checkForCollision(ufo);
			collisionDetected = lastCollision != null;
			if (collisionDetected ) {
				//if there is one lets draw a red X
				p.setColor(Color.RED);
				p.setAlpha(255);
				p.setStrokeWidth(5);
				canvas.drawLine(lastCollision.x - 5, lastCollision.y - 5,
						lastCollision.x + 5, lastCollision.y + 5, p);
				canvas.drawLine(lastCollision.x + 5, lastCollision.y - 5,
						lastCollision.x - 5, lastCollision.y + 5, p);
			}
		}
	}
	
	synchronized public void initGame(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
		
		for (LevelInfo levelInfo : gameInfo.getLevels()) {
			Level level = new Level(levelInfo, this.getContext());
			levels.add(level);
		}

		this.setCurrentLevelNum(0);
		this.currentLevel = this.levels.get(this.currentLevelNum);
		
		Point origin = new Point((getWidth() / 2) - ufo.getBounds().width() / 2,
								 (getHeight() / 2) - ufo.getBounds().height() / 2);
		ufo.setLocation(origin.x, origin.y);
	}
	
    
	synchronized public void reset() {
		collisionDetected = false;
		background.reset();
		currentLevel.reset();
		ufo.reset();
	}
}
