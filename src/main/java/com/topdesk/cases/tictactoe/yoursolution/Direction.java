package com.topdesk.cases.tictactoe.yoursolution;

public enum Direction {

	RIGHT(1, 0),
	DOWN(0, 1),
	RIGHT_DOWN(1, 1),
	RIGHT_UP(1, -1);

	public final int x;
	public final int y;

	Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
