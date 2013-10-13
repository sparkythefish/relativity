package com.sparkythefish.drawing;

import com.sparkythefish.relativity.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

public class Planet extends View {
	private Point location;
	private Point origLocation;
	private Rect bounds = new Rect(0,0,0,0);
	private Bitmap bitmap = null;
	private Matrix matrix = null;
	private int rotation = 0;
	
	synchronized public void setLocation(int x, int y) {
		location = new Point(x, y);
	}
	
	synchronized public Point getLocation() {
		return location;
	}
	
	synchronized public Rect getBounds() {
		return bounds;
	}
	
	synchronized public Bitmap getBitmap() {
		return bitmap;
	}
	
	public Planet(Context context) {
		super(context);
	}
	
	public Planet(Point location, Context context) {
		this(context);
		this.location = location;
		origLocation = location;
		matrix = new Matrix();
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.asteroid);
		bounds = new Rect(0,0, bitmap.getWidth(), bitmap.getHeight());
	}
	
	@Override
	synchronized public void onDraw(Canvas canvas) {
		if (location.x + bounds.width() >=0) {
			matrix.reset();
			matrix.postTranslate((float)(location.x), (float)(location.y));
			matrix.postRotate(rotation,
					(float)(location.x+bounds.width()/2.0),
					(float)(location.y+bounds.width()/2.0));
			canvas.drawBitmap(bitmap, matrix, null);
			rotation+=5;
			if (rotation >= 360) rotation=0;
		}
	}
	
	/**
	 * Updates the location of planet based on the ufo velocity.
	 */
	synchronized public void updateLocation(Point ufoVelocity) {
		setLocation(location.x - ufoVelocity.x, location.y - ufoVelocity.y);
	}
	
	synchronized public Point checkForCollision(Ufo ufo) {
		Point collisionPoint = null;
		
		Point ufoPos = ufo.getLocation();
		if (location.x<0 && ufoPos.x<0 && location.y<0 && ufoPos.y<0) return null;
		
		Rect r1 = new Rect(location.x, location.y, location.x
				+ bounds.width(),  location.y + bounds.height());
		Rect r2 = new Rect(ufoPos.x, ufoPos.y, ufoPos.x +
				ufo.getBounds().width(), ufoPos.y + ufo.getBounds().height());
		Rect r3 = new Rect(r1);
		if(r1.intersect(r2)) {
			for (int i = r1.left; i<r1.right; i++) {
				for (int j = r1.top; j<r1.bottom; j++) {
					if (bitmap.getPixel(i-r3.left, j-r3.top)!=Color.TRANSPARENT) {
						if (ufo.getBitmap().getPixel(i-r2.left, j-r2.top) !=Color.TRANSPARENT) {
							collisionPoint = new Point(ufoPos.x +i-r2.left, ufoPos.y + j-r2.top);
							return collisionPoint;
						}
					}
				}
			}
		}
		return collisionPoint;
	}
	
	synchronized public void reset() {
		this.location = this.origLocation;
	}

}
