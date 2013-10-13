package com.sparkythefish.relativity;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.sparkythefish.relativity.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparkythefish.drawing.GameBoard;
import com.sparkythefish.drawing.GameInfo;

public class Main extends Activity implements OnClickListener{

	private Handler frame = new Handler();
	//Divide the frame by 1000 to calculate how many times per second the screen will update.
	private static final int FRAME_RATE = 20; //50 frames per second
	//Velocity includes the speed and the direction of our sprite motion
	private boolean isAccelerating = false;
	private Point touchPos = null;
	private GameBoard gameBoard = null;
	private boolean initialized = false;

	@Override
	synchronized public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			isAccelerating = true;
			if (gameBoard.wasCollisionDetected()) {
				initGfx();
			}
			touchPos = new Point((int)ev.getX(), (int)ev.getY());
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			isAccelerating = false;
			break;
		case MotionEvent.ACTION_MOVE:
			touchPos = new Point((int)ev.getX(), (int)ev.getY());
		}
		return true;
	}

	//Increase the velocity towards five or decrease
	//back to one depending on state
	private void updateVelocity() {
		if (isAccelerating) {
			if (initialized) {
				gameBoard.getUfo().updateVelocity(touchPos);
			}
		}
	}

	private void updateLocations() {
		if (initialized) {
			Point ufoVel = gameBoard.getUfo().getVelocity();
			gameBoard.getCurrentLevel().updateLocations(ufoVel);
			gameBoard.getBoardBackground().updateLocation(ufoVel);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Handler h = new Handler();
		gameBoard = (GameBoard)findViewById(R.id.the_canvas);
		((Button)findViewById(R.id.the_button)).setOnClickListener(this);
		//We can't initialize the graphics immediately because the layout manager
		//needs to run first, thus call back in a sec.
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				init();
				initGfx();
			}
		}, 1000);
	}

	/**
	 * Meant to be called after the layout manager is run.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	private void init() {
		try {
			AssetManager assetManager = getAssets();
			ObjectMapper mapper = new ObjectMapper();
			GameInfo gameInfo = mapper.readValue(assetManager.open("gameInfo.json"), GameInfo.class);
			gameBoard.initGame(gameInfo);
			initialized = true;
		} catch (Exception e) {
			Log.d("Main.java", "Failed to init the game config: " + e.getCause());
			Log.e("Main.java", "exception", e);
		}
	}

	synchronized public void initGfx() {
		gameBoard.reset();
		((Button)findViewById(R.id.the_button)).setEnabled(true);
		frame.removeCallbacks(frameUpdate);
		frame.postDelayed(frameUpdate, FRAME_RATE);
	}

	@Override
	synchronized public void onClick(View v) {
		initGfx();
	}

	private Runnable frameUpdate = new Runnable() {

		@Override
		synchronized public void run() {
			//Before we do anything else check for a collision
			if (gameBoard.wasCollisionDetected()) {
				//turn off the animation until reset gets pressed
				return;
			}

			frame.removeCallbacks(frameUpdate);

			//Add our call to increase or decrease velocity
			updateVelocity();
			updateLocations();

			//then invoke the on draw by invalidating the canvas
			gameBoard.invalidate();
			frame.postDelayed(frameUpdate, FRAME_RATE);
		}

	};
}