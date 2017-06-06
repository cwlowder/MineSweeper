package com.company;

/**
 * Created by Curtis Lowder on 10/2/2016.
 */
public class Cell {
	private int x;
	private int y;
	private boolean isCovered = true;
	private boolean isMine;
	private int neighborMines = 0;

	public Cell(int x, int y, boolean isMine) {
		this.x = x;
		this.y = y;
		this.isMine = isMine;
	}

	public void reveal() {
		isCovered = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isCovered() {
		return isCovered;
	}

	public boolean isMined() {
		return isMine;
	}

	public int getNeighborMines() {
		return neighborMines;
	}

	public void setNeighborMines(int numMines) {
		neighborMines = numMines;
	}

	public String toString() {
		return x + "," + y;
	}
}
