package com.company;

import org.omg.CORBA.OMGVMCID;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Badtoasters on 6/3/2017.
 */
public class FilterPlayer extends AbstractPlayer {
	// filters must be defined before static block
	private static List<Filter> filters = new ArrayList<>();

	static {
		filters.add(new Filter(
				FLAG.One, FLAG.One, FLAG.One, FLAG.Ign,
				FLAG.Cov, FLAG.Min, FLAG.One, FLAG.Ign,
				FLAG.Min, FLAG.Two, FLAG.One, FLAG.Ign,
				FLAG.One, FLAG.One, FLAG.Ign, FLAG.Ign
		));
		filters.add(new Filter(
				FLAG.Ign, FLAG.Cov, FLAG.One,
				FLAG.Cov, FLAG.Min, FLAG.One,
				FLAG.One, FLAG.One, FLAG.One
		));
		filters.add(new Filter(
				FLAG.Cov, FLAG.One, FLAG.One,
				FLAG.One, FLAG.Min, FLAG.One,
				FLAG.One, FLAG.One, FLAG.One
		));
		filters.add(new Filter(
				FLAG.One, FLAG.One, FLAG.One,
				FLAG.Cov, FLAG.Min, FLAG.One,
				FLAG.One, FLAG.One, FLAG.One
		));

		filters.add(new Filter(
				FLAG.Cov, FLAG.Two, FLAG.One,
				FLAG.Min, FLAG.One, FLAG.Emp,
				FLAG.Cov, FLAG.One, FLAG.Emp
		));
		filters.add(new Filter(
				FLAG.Cov, FLAG.Min, FLAG.One,
				FLAG.Two, FLAG.Two, FLAG.One,
				FLAG.Min, FLAG.One, FLAG.Emp
		));
		filters.add(new Filter(
				FLAG.Ign, FLAG.Cov, FLAG.One,
				FLAG.Cov, FLAG.Min, FLAG.One,
				FLAG.One, FLAG.One, FLAG.One
		));
		filters.add(new Filter(
				FLAG.Cov, FLAG.Min, FLAG.One, FLAG.Ign,
				FLAG.Two, FLAG.Cov, FLAG.Two, FLAG.Ign,
				FLAG.One, FLAG.Min, FLAG.One, FLAG.Ign,
				FLAG.One, FLAG.One, FLAG.One, FLAG.Ign
		));
	}

	public FilterPlayer(Board board, int numMines) {
		setBoard(board);
		setDimension(board.getDimension());
		setNumMines(numMines);
	}

	@Override
	public boolean solve() {
		List<Cell> mines = new ArrayList<>();

		//System.out.println(getBoard());
		//getBoard().uncoverLocation(3, 3);
		//System.out.println(getBoard());

		while (!getBoard().checkSolved()) {
			if (mines.isEmpty()) {
				//-filters.get(f).getSide()
				for (int f = 0; f < filters.size(); f++) {
					for (int x = -filters.get(f).getSide(); x < getDimension(); x++) {
						for (int y = -filters.get(f).getSide(); y < getDimension(); y++) {
							//System.out.println("FILTER NEW " + f + " @ x:" +x + " y:" + y);
							List<Cell> ret = filters.get(f).check(getBoard(), x, y);
							//ret.forEach(m -> System.out.println(m.getX() + ", " + m.getY()));
							if (ret != null && !ret.isEmpty()) {
								//System.out.println("ADDED NUM: " + ret.size());
								mines.addAll(ret);
							}
						}
					}
				}
				// if no mines are picked for digging
				if (mines.isEmpty()) {
					// uncover random location
					mines.add(randomLocation());
				}
			} else {
				// uncover a mine
				//mines.forEach((mine)->System.out.println("x: " + mine.getX() + " y: " + mine.getY() + " f:" + filters.size()));
				Cell mine = mines.get(0);
				mines.remove(0);
				getBoard().uncoverLocation(mine.getX(), mine.getY());
				//System.out.println("x: " + mine.getX() + " y: " + mine.getY() + " s: " + mines.size());
				if (getBoard().isDangerous(mine.getX(), mine.getY())) {
					return false;
				}
			}
			//System.out.println(getBoard());
		}

		//System.out.println(getBoard());
		if (getBoard().checkSolved())
			return true;
		else {
			return false;
		}
	}

	private boolean uncoverRandomLocation() {
		Cell randomLocation = randomLocation();
		getBoard().uncoverLocation(randomLocation.getX(), randomLocation.getY());
		if (getBoard().isDangerous(randomLocation.getX(), randomLocation.getY())) {
			//System.out.println(getBoard().stringFail());
			return true;
		}
		return false;
	}

	/**
	 * @return a random cell that is covered
	 */
	private Cell randomLocation() {
		Cell randomCell = null;
		while (randomCell == null || !randomCell.isCovered()) {
			int x = randomInt(getDimension());
			int y = randomInt(getDimension());
			// return a representation of the cell to be uncovered
			randomCell = new Cell(x, y, false);
		}
		return randomCell;
	}
}

/**
 * This class handles the filters
 */
class Filter {
	public static final int NUMDIM = 3;
	/**
	 * -1 is a mine
	 * <-1 doesn't matter
	 * >=0 is number of neighbors
	 */
	private FLAG[][][] filter;
	private int side;

	/**
	 * @param cells a varag of cells for the filter
	 */
	public Filter(FLAG... cells) {
		side = (int) Math.sqrt(cells.length);

		// if the sides aren't an exact square
		if (side * side != cells.length) {
			// throw an exception
			throw new IllegalArgumentException("Filter must be of a square size");
		}

		filter = new FLAG[4][side][side];

		// rotate 0
		for (int x = 0; x < side; x++) {
			for (int y = 0; y < side; y++) {
				filter[0][y][x] = cells[y * side + x];
			}
		}

		// rotate 1
		for (int x = 0; x < side; x++) {
			for (int y = 0; y < side; y++) {
				filter[1][x][side - y - 1] = cells[y * side + x];
			}
		}

		// rotate 2
		for (int x = 0; x < side; x++) {
			for (int y = 0; y < side; y++) {
				filter[2][side - y - 1][side - x - 1] = cells[y * side + x];
			}
		}

		// rotate 3
		for (int x = 0; x < side; x++) {
			for (int y = 0; y < side; y++) {
				filter[3][side - x - 1][y] = cells[y * side + x];
			}
		}
	}

	private void printFilter(int rot) {
		for (int i = 0; i < side; i++) {
			for (int j = 0; j < side; j++) {
				System.out.print(filter[rot][i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * @param board the board to check
	 * @param x     the x position to start from
	 * @param y     the y position to start from
	 * @return an array of cells with out mines
	 */
	public List<Cell> check(Board board, int x, int y) {

		return checkHelper(board, x, y);
	}

	private List<Cell> checkHelper(Board board, int x, int y) {
		if (board == null || x + side > board.getDimension() || y + side > board.getDimension()) {
			return null;
		}

		List<Cell> ret = new ArrayList<>();
		for (int r = 0; r < NUMDIM + 1; r++) {
			boolean works = true;
			List<Cell> retDim = new ArrayList<>();
			for (int xF = 0; works && xF < side; xF++) {
				for (int yF = 0; works && yF < side; yF++) {
					FLAG code = filter[r][yF][xF];
					if (!posMatch(code, board, x + xF, y + yF)) {
						works = false;
					} else if (code == FLAG.Cov) {
						Cell c = board.getCell(x + xF, y + yF);
						if (c != null) {
							if (board.getDimension() > x + xF && x + xF >= 0) {
								if (board.getDimension() > y + yF && y + yF >= 0) {
									retDim.add(c);
								}
							}
						}
					}
				}
			}
			if (works) {
				ret.addAll(retDim);
			}
		}
		return ret;
	}

	public int getSide() {
		return side;
	}

	/**
	 * @param code  the code on the filter
	 * @param board the board to check
	 * @param x     xPos
	 * @param y     yPos
	 * @return whether or not it matches
	 */
	private boolean posMatch(FLAG code, Board board, int x, int y) {
		Cell cell = board.getCell(x, y);

		if (0 > x || x >= board.getDimension()) {
			return true;
		}
		if (0 > y || y >= board.getDimension()) {
			return true;
		}

		if (cell == null) {
			return true;
		}

		int neigh = cell.getNeighborMines();
		switch (code) {
			case Ign:
				return true;
			case Cov:
				return (cell.isCovered());
			case Min:
				return (cell.isCovered());
			case Emp:
				return (!cell.isCovered()) && (neigh == 0);
			case One:
				return (!cell.isCovered()) && (neigh == 1);
			case Two:
				return (!cell.isCovered()) && (neigh == 2);
			case Thr:
				return (!cell.isCovered()) && (neigh == 3);
			case For:
				return (!cell.isCovered()) && (neigh == 4);
			case Fiv:
				return (!cell.isCovered()) && (neigh == 5);
			case Six:
				return (!cell.isCovered()) && (neigh == 6);
			case Sev:
				return (!cell.isCovered()) && (neigh == 7);
			case Eit:
				return (!cell.isCovered()) && (neigh == 8);
			case Num:
				return (!cell.isCovered());
		}
		return false;
	}
}

enum FLAG {
	Cov, Emp, One, Two, Thr, For, Fiv, Six, Sev, Eit, Min, Num, Ign;

	@Override
	public String toString() {
		switch (this) {
			case Cov:
				return "#";
			case Emp:
				return "_";
			case One:
				return "1";
			case Two:
				return "2";
			case Thr:
				return "3";
			case For:
				return "4";
			case Fiv:
				return "5";
			case Six:
				return "6";
			case Sev:
				return "7";
			case Eit:
				return "8";
			case Min:
				return "M";
			case Ign:
				return "i";
			default:
				return "?";
		}
	}
}