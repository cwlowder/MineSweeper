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
    private boolean BoardIsUngenerated;

    public Board ( int dimension , int numMines ) {
        this.dimension = dimension;
        this.numMines = numMines;
        BoardIsUngenerated = true;
    }

    /**
     *
     * @return whether or not the board is currently solved
     */
    public boolean checkSolved() {
        int numCovered = 0;

        // checks if the first click hasn't been made, thus board is not generated
        if(BoardIsUngenerated) {
            return false;
        }

        for ( int x = 0 ; x < dimension ; x ++ ) {
            for ( int y = 0 ; y < dimension ; y ++ ) {
                if ( getCellSpecific(x,y).isCovered() ){
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
     * Note: this method handles if the first spot dug is a mine as specified: http://www.techuser.net/mineclick.html
     * as well as making sure the neighbors aren't mines
     * @param x the x position to uncover
     * @param y the y position to uncover
     */
    public void uncoverLocation( int x , int y ) {

        /*
         * Handling the fact your first click can never be a mine
         * Reasoning behind this implementation can be found here: http://www.techuser.net/mineclick.html
         */
        if(BoardIsUngenerated) {
            // generate board with a hole
            generateBoard(numMines, x, y);
            BoardIsUngenerated = false;
        }

        board[x][y].reveal();
        ArrayList<Cell> neighbors = findNeighbors(x, y);

        // only empty cells should reveal neighbor cells
        if (board[x][y].getNeighborMines() == 0) {
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
     *  @param xPos the location to not fill with a mine
     *  @param yPos the location to not fill with a mine
     */
    private void generateBoard( int numMines, int xPos, int yPos ) {
        // Initalizes board to be dimension x dimension
        board = new Cell[dimension][dimension];

        // places all the mines needed
        placeMines( numMines , xPos, yPos);

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

        // figure out the number of neighbor mines
        calcNumNeighborMines();
    }

    /**
     *  Doesn't place empty elements onto the board, just mines
     *  @param numMines the number of mines to be placed on the board
     *  @param xHole the x position of the hole to not fill mines
     *  @param yHole the y position of the hole to not fill mines
     */
    private void placeMines( int numMines, int xHole, int yHole) {
        while (numMines > 0) {
            int xPos = randomInt(dimension);
            int yPos = randomInt(dimension);

            // accounts for the hole not to fill with mines
            if(Math.abs(xHole - xPos) <= 1 || Math.abs(yHole - yPos) <= 1) {
                continue;
            }

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
                        if ( getCellSpecific( i , j ) != null ) {
                            neighbors.add( getCellSpecific(i, j) );
                        }
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
     * @return the cell which matches the x,y position, note a representation will be returned if the location is uncovered
     */
    public Cell getCell( int x , int y ) {
        // checks if the board hasn't been instantiated
        if(BoardIsUngenerated) {
            return null;
        }

        Cell cell = getCellSpecific(x, y);

        if(!cell.isCovered()) {
            return board[x][y];
        }
        // return a representation if position is covered
        else {
            return new Cell(x,y, false);
        }
    }

    /**
     * note: no null will be returned for covered cells
     * @param x the x position of the cell to return
     * @param y the y position of the cell to return
     * @return the cell which matches the x,y position
     */
    private Cell getCellSpecific( int x , int y ) {
        if ( x >= 0 && y >= 0 ) {
            if ( x < dimension && y < dimension ) {
                return board[x][y];
            }
        }
        return null;
    }

    /**
     * Helper function for printing out board
     * X and Y used if a mine has been hit
     * @param drawMines should mines be drawn?
     * @return a string representation of the board with the x, y position highlighted
     */
    private String getBoardString(boolean drawMines) {
        String boardString ="";
        boardString += "\\ ";
        // Drawing X axis
        // checking if the dimension is too large to fit in one row
        if(dimension > 10) {
            boardString += " ";
            for ( int i = 0 ; i < dimension ; i++ ) {
                // only draw first digit if not 0
                if(i/10!=0) {
                    // divide by ten to get first digit
                    boardString += i/10;
                }
                // otherwise add blank space
                else {
                    boardString += " ";
                }
            }
            // sets up next line, blank space needed to help align
            boardString += "\n \\ ";
        }
        for ( int i = 0 ; i < dimension ; i++ ) {
            // modulus i by 10 to get last digit
            boardString += i%10;
        }
        // sets up next line
        boardString += "\n";

        for ( int y = 0 ; y < dimension ; y++ ) {
            // checks if a blank space should be added to align columns
            if(dimension > 10 && y < 10 ) {
                boardString += " ";
            }
            boardString += y + "|";
            // running through the elements on a line
            for ( int x = 0 ; x < dimension ; x++ ) {
                if ( getCellSpecific(x,y).isCovered() ) {
                    if( drawMines && getCellSpecific(x, y).isMined() ) {
                        boardString += "M";
                    }
                    else {
                        boardString += "X";
                    }
                }
                else {
                    if ( drawMines && board[x][y].isMined() ) {
                        // is highlighted because this position has an exploded mine
                        boardString += Colorer.highlight("M");
                    }
                    else if ( board[x][y].getNeighborMines() > 0 ) {
                        boardString += Colorer.color(board[x][y].getNeighborMines());
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
     *
     * @returns string representation of the board to be printed out later
     */
    public String toString() {
        // mines should not be drawn
        return getBoardString(false);
    }

    public String stringFail() {
        // mines should be drawn
        return getBoardString(true);
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
     * note: returns a representation of a cell
     * @param cellStr the string representation of the target cell
     * @return the cell object correlated with that cellStr
     */
    public Cell getCellStr(String cellStr) {
        String[] coords= cellStr.split(",");
        return new Cell(Integer.parseInt( coords[0] ) , Integer.parseInt( coords[1] ), false);
    }

    /**
     * note: will always return true if the board has not been generated
     * @param x the x position to be dug
     * @param y the y position to be dug
     * @return the whether digging a spot will kill you
     */
    public boolean isDangerous(int x, int y) {
        boolean ret;
        if(BoardIsUngenerated) {
            // if the board has not been generated yet
            ret = true;
        }
        else if ( getCellSpecific( x , y ).isMined() ) {
            ret = true;
        }
        else {
            ret = false;
        }

        return ret;
    }

    /**
     * This method should only be used on the first click of the game
     * @param xPos the x position of the mine to move
     * @param yPos the y position of the mine to move
     */
    private void moveMineSafelyAway( int xPos, int yPos ) {
        boolean isMoved = false;

        // short circuit if out of bounds
        if( xPos >= dimension || xPos < 0 || yPos >= dimension || yPos < 0 ) {
            return;
        }
        // short circuit if pos is not mined
        if(!getCellSpecific(xPos, yPos).isMined()) {
            return;
        }

        // finds first open spot with no mine
        for ( int y = 0 ; y < dimension ; y++ ) {
            for ( int x = 0 ; x < dimension ; x++ ) {
                if ( x != xPos && y != yPos && ! isMoved ) {
                    if ( ! getCellSpecific(x, y).isMined() ) {
                        board[x][y] = new Cell( xPos , yPos , true);
                        board[xPos][yPos] = new Cell( x , y , false);
                        isMoved = true;
                    }
                }
            }
        }
    }

    /**
     *
     * @return the dimension of the board
     */
    public int getDimension() {
        return dimension;
    }
}
