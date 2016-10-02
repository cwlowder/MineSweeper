package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private int dimension;
    private Element[][] board;

    public Board ( int dimension , int numMines ) {
        this.dimension = dimension;
        generateBoard( numMines );
        calcNumNeighborMines();
    }

    public String toString() {
        String boardString ="";
        for ( int y = 0 ; y < dimension ; y++ ) {
            for ( int x = 0 ; x < dimension ; x++ ) {
                if ( board[x][y].isCovered() ) {
                    boardString += "◼";
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
     *  @param dimension the dimensions of the board
     *  @param numMines the number of mines to be placed
     */
    private void generateBoard( int numMines ) {
        // Initalizes board to be dimension x dimension
        board = new Element[dimension][dimension];

        // places all the mines needed
        placeMines( numMines );

        // fills the rest of the board with none mines
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                // Checks if the location is empty
                if (board[x][y] == null) {
                    Element emptyCell = new Element(x, y, false);
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
                Element newMine = new Element(xPos, yPos, true);
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
                    ArrayList<Element> neighbors = findNeighbors( x , y );

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

    /*
     *  @param x the x position of the target location to be calculated
     *  @param y the y position of the target location to be calculated
     *  @returns Array of the neighbors for the location x,y
     */
    public ArrayList<Element> findNeighbors(int x, int y) {
        ArrayList<Element> neighbors = new ArrayList<>();

        // Loops through possible neighbors in each of the directions
        for ( int i = x - 1 ; i <= x + 1 ; i ++ ) {
            for ( int j = y - 1 ; j <= y + 1 ; j ++ ) {
                boolean shouldAdd = true;
                // Tests if the point is the same as the target location
                if ( i == x && j == y ) {
                    shouldAdd = false;
                }
                // Tests if point is out of bounds
                else if ( x < 0 || y < 0 ) {
                    shouldAdd = false;
                }
                // Tests if point is out of bounds
                else if ( x > dimension || y > dimension ) {
                    shouldAdd = false;
                }

                if ( shouldAdd ) {
                    neighbors.add(getElement(i, j));
                }
            }
        }

        return neighbors;
    }

    public Element getElement( int x , int y ) {
        if ( x >= 0 && y >= 0 ) {
            if ( x < dimension && y < dimension ) {
                return board[x][y];
            }
        }
        return null;
    }

    /*
     *  @param maxValue The max integer that will be randomly generated
     *  @returns a random value from 0(inclusive) to max value(exclusive)
     */
    private int randomInt( int maxValue ) {
        Random gen = new Random();
        return gen.nextInt( maxValue );
    }
}
