package com.sparkythefish.drawing;

import java.util.ArrayList;

import android.graphics.Point;

public class GameInfo {
	
	ArrayList<LevelInfo> levels;
	synchronized public ArrayList<LevelInfo> getLevels() {
		return levels;
	}
	synchronized public void setLevelInfos(ArrayList<LevelInfo> levels) {
		this.levels = levels;
	}
}
