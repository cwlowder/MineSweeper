package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private int dimension;
    private Cell[][] board;

    public Board ( int dimension , int numMines ) {
        this.dimension = dimension;
        generateBoard( numMines );
        calcNumNeighborMines();
    }

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

    /*
     *  @param dimension the dimensions of the board
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

    /*
     *  Doesn't place empty elements onto the board, just mines
     *
     *  @param numMines the number of mines to be placed on the board
     *
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

    /*
     *  @param x the x position of the target location to be calculated
     *  @param y the y position of the target location to be calculated
     *  @returns Array of the neighbors for the location x,y
     */
    public ArrayList<Cell> findNeighbors(int x, int y) {
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

    public Cell getCell(int x , int y ) {
        if ( x >= 0 && y >= 0 ) {
            if ( x < dimension && y < dimension ) {
                return board[x][y];
            }
        }
        return null;
    }

    /*
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

        /*// Drawing divider
        for ( int i = 0 ; i < dimension + 1 ; i++ ) {
            boardString += "-";
        }
        // sets up next line
        boardString += "\n";*/

        for ( int y = 0 ; y < dimension ; y++ ) {
            boardString += y + "|";
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

    /*
     *  @param maxValue The max integer that will be randomly generated
     *  @returns a random value from 0(inclusive) to max value(exclusive)
     */
    private int randomInt( int maxValue ) {
        Random gen = new Random();
        return gen.nextInt( maxValue );
    }

    public Cell[][] getBoard() {
        return board;
    }

    public Cell getCellStr(String cellStr) {
        String[] coords= cellStr.split(",");
        return getCell( Integer.parseInt( coords[0] ) , Integer.parseInt( coords[1] ) );
    }
}
