package com.sparkythefish.drawing;

import com.sparkythefish.relativity.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

public class Ufo extends View {
	
	private Point location;
	private Rect bounds = new Rect(0,0,0,0);
	private Bitmap bitmap = null;
	private Point velocity = null;
	
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
	
	public Ufo(Context context) {
		super(context);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ufo);
		location = new Point(-1, -1);
		bounds =  new Rect(0,0, bitmap.getWidth(), bitmap.getHeight());
		velocity = new Point(0,0);
	}
	
	@Override
	synchronized public void onDraw(Canvas canvas) {
		if (location.x>=0) {
			canvas.drawBitmap(bitmap, location.x, location.y, null);
		}
	}
	
	synchronized public Point getVelocity() {
		return velocity;
	}
	
	synchronized public void updateVelocity(Point touchPos){
		float spriteXCenter = location.x + bounds.width() / 2;
		float spriteYCenter = location.y + bounds.height() / 2;
		int newVX = (int)((touchPos.x - spriteXCenter) / 20);
		int newVY = (int)((touchPos.y - 100 - spriteYCenter) / 20);

		velocity.x = newVX;
		velocity.y = newVY;
	}
	
	synchronized public void reset() {
		velocity.x = 0;
		velocity.y = 0;
	}

}
