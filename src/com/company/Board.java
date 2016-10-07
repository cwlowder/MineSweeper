package com.company;

import java.util.ArrayList;
import java.util.Random;
/**
 * Created by Curtis Lowder on 10/1/2016.
 */
public class Board {
    private int dimension;
    private Cell[][] board;
    private int numMines;

    public Board ( int dimension , int numMines ) {
        this.dimension = dimension;
        this.numMines = numMines;
        generateBoard( numMines );
        calcNumNeighborMines();
    }

    /**
     *
     * @return whether or not the board is currently solved
     */
    public boolean checkSolved() {
        int numCovered = 0;
        for ( int x = 0 ; x < dimension ; x ++ ) {
            for ( int y = 0 ; y < dimension ; y ++ ) {
                if ( getCell(x,y).isCovered() ){
                    numCovered++;
                }
            }
        }
        if ( numCovered == numMines ) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @param x the x position to uncover
     * @param y the y position to uncover
     */
    public void uncoverLocation( int x , int y ) {
        board[x][y].reveal();
        ArrayList<Cell> neighbors = findNeighbors( x , y );

        // only empty cells should reveal neighbor cells
        if ( board[x][y].getNeighborMines() == 0 ) {
            for (Cell neighbor : neighbors) {
                // reveals neighbors that are covered and not mines
                if (neighbor.isCovered() && !neighbor.isMined()) {
                    uncoverLocation(neighbor.getX(), neighbor.getY());
                }
            }
        }
    }

    /**
     *
     *  @param numMines the number of mines to be placed
     */
    private void generateBoard( int numMines ) {
        // Initalizes board to be dimension x dimension
        board = new Cell[dimension][dimension];

        // places all the mines needed
        placeMines( numMines );

        // fills the rest of the board with none mines
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                // Checks if the location is empty
                if (board[x][y] == null) {
                    Cell emptyCell = new Cell(x, y, false);
                    board[x][y] = emptyCell;
                }
            }
        }
    }

    /**
     *  Doesn't place empty elements onto the board, just mines
     *  @param numMines the number of mines to be placed on the board
     */
    private void placeMines( int numMines) {
        while (numMines > 0) {
            int xPos = randomInt(dimension);
            int yPos = randomInt(dimension);

            // Checks to see if position on board is empty
            if (board[xPos][yPos] == null) {
                Cell newMine = new Cell(xPos, yPos, true);
                board[newMine.getX()][newMine.getY()] = newMine;

                // removes the mines that remain to be placed
                numMines--;
            }
        }
    }

    /**
     * calculates the number of neighbor mines for each cell on the grid
     */
    private void calcNumNeighborMines() {
        for ( int x = 0 ; x < dimension ; x++ ) {
            for ( int y = 0 ; y < dimension ; y++ ) {
                // Checks to see if a position doesn't contains a mine
                if ( ! board[x][y].isMined() ) {
                    int numMines = 0;
                    ArrayList<Cell> neighbors = findNeighbors( x , y );

                    for ( int n = 0 ; n < neighbors.size() ; n ++ ) {
                        if ( neighbors.get( n ) != null && neighbors.get( n ).isMined() ) {
                            numMines++;
                        }
                    }

                    board[x][y].setNeighborMines( numMines );
                }
            }
        }
    }

    /**
     *
     * @return the number of covered cells on the board left
     */
    public int numCoveredCells() {
        int numCovered = 0;
        for ( int x = 0 ; x < dimension ; x++ ) {
            for ( int y = 0 ; y < dimension ; y++ ) {
                if ( board[x][y].isCovered() ) {
                    numCovered++;
                }
            }
        }
        return numCovered;
    }

    /**
     *
     *  @param x the x position of the target location to be calculated
     *  @param y the y position of the target location to be calculated
     *  @returns Array of the neighbors for the location x,y
     */
    public ArrayList<Cell> findNeighbors( int x , int y ) {
        ArrayList<Cell> neighbors = new ArrayList<>();

        // Loops through possible neighbors in each of the directions
        for ( int i = x - 1 ; i <= x + 1 ; i ++ ) {
            for ( int j = y - 1 ; j <= y + 1 ; j ++ ) {
                boolean shouldAdd = true;
                // Tests if the point is the same as the target location
                if ( i == x && j == y ) {
                    shouldAdd = false;
                }
                // Tests if point is out of bounds
                else if ( i < 0 || j < 0 ) {
                    shouldAdd = false;
                }
                // Tests if point is out of bounds
                else if ( i >= dimension || j >= dimension ) {
                    shouldAdd = false;
                }

                if ( shouldAdd ) {
                    if (board[i][j] != null) {
                        neighbors.add(getCell(i, j));
                    }
                }
            }
        }

        return neighbors;
    }

    /**
     *
     * @param x the x position of the cell to return
     * @param y the y position of the cell to return
     * @return the cell which matches the x,y position
     */
    public Cell getCell( int x , int y ) {
        if ( x >= 0 && y >= 0 ) {
            if ( x < dimension && y < dimension ) {
                return board[x][y];
            }
        }
        return null;
    }

    /**
     *
     * @returns string representation of the board to be printed out later
     */
    public String toString() {
        String boardString ="";
        boardString += "\\ ";
        // Drawing X axis
        for ( int i = 0 ; i < dimension ; i++ ) {
            boardString += i;
        }
        // sets up next line
        boardString += "\n";

        for ( int y = 0 ; y < dimension ; y++ ) {
            boardString += y + "|";
            // running through the elements on a line
            for ( int x = 0 ; x < dimension ; x++ ) {
                if ( board[x][y].isCovered() ) {
                    boardString += "X";
                }
                else {
                    if ( board[x][y].isMined() ) {
                        boardString += "M";
                    }
                    else if ( board[x][y].getNeighborMines() > 0 ) {
                        boardString += board[x][y].getNeighborMines();
                    }
                    else {
                        boardString += ".";
                    }
                }
            }
            boardString += "\n";
        }
        return boardString;
    }

    /**
     *  @param maxValue The max integer that will be randomly generated
     *  @returns a random value from 0(inclusive) to max value(exclusive)
     */
    private int randomInt( int maxValue ) {
        Random gen = new Random();
        return gen.nextInt( maxValue );
    }

    /**
     *
     * @return a 2d array of the cells which represent the board
     */
    public Cell[][] getBoard() {
        return board;
    }

    /**
     *
     * @param cellStr the string representation of the target cell
     * @return the cell object correlated with that cellStr
     */
    public Cell getCellStr(String cellStr) {
        String[] coords= cellStr.split(",");
        return getCell( Integer.parseInt( coords[0] ) , Integer.parseInt( coords[1] ) );
    }
}
