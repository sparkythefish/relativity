package com.sparkythefish.drawing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Canvas.EdgeType;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class Background extends View {

    private static final int STAR_FIELD_SIZE = 5000;
    private static final int NUM_OF_STARS = (int)Math.sqrt(STAR_FIELD_SIZE) * 50;
	private List<Point> starField = null;
	private int starAlpha = 80;
	private int starFade = 2;
	private Paint paint;
	
	public Background(Context context) {
		super(context);
		paint = new Paint();
		reset();
	}

	@Override
	synchronized public void onDraw(Canvas canvas) {
		//create a black canvas
		paint.setColor(Color.BLACK);
		paint.setAlpha(255);
	    paint.setStrokeWidth(1);
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
	
		//draw the stars
		paint.setColor(Color.CYAN);
		paint.setAlpha(starAlpha+=starFade);
		//fade them in and out
		if (starAlpha>=252 || starAlpha <=80) starFade=starFade*-1;
		paint.setStrokeWidth(5);
		for (Point star : starField) {
			if (inBounds(star, canvas.getWidth(), canvas.getHeight())) {
				canvas.drawPoint(star.x, star.y, paint);
			}
		}
	}
	
	synchronized public void updateLocation(Point ufoVelocity) {
		for (Point star : starField) {
			// inifite scrolling:
			// if the star reaches the outer edge of the star field, 
			// wrap it to the other side. This re-uses the random initial field, infinitely,
			// without needing to create millions of stars, etc..
			if (star.y > STAR_FIELD_SIZE) {
				star.y -= STAR_FIELD_SIZE;
			}
			if (star.x > STAR_FIELD_SIZE) {
				star.x -= STAR_FIELD_SIZE;
			}
			if (star.y < 0) {
				star.y += STAR_FIELD_SIZE;
			}
			if (star.x < 0) {
				star.x += STAR_FIELD_SIZE;
			}
			
			// move the stars slower than the foreground (planets n such.)
			star.x -= ufoVelocity.x / 2;
			star.y -= ufoVelocity.y / 2;
		}
	}
	
	private void initializeStars(int maxX, int maxY) {
		starField = new ArrayList<Point>();
		for (int i=0; i<NUM_OF_STARS; i++) {
			 Random r = new Random();
			 int x = r.nextInt(maxX-5+1)+5;
			 int y = r.nextInt(maxY-5+1)+5;
			 starField.add(new Point (x,y)); 
		}
	}
	
	synchronized public void reset() {
		initializeStars(STAR_FIELD_SIZE, STAR_FIELD_SIZE);
	}
	
	private boolean inBounds(Point point, int maxX, int maxY) {
		return point.x > 0 && point.x < maxX
		    && point.y > 0 && point.y < maxY;
	}

}
