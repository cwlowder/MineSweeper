package com.company;

import java.util.Random;

/**
 * Created by Badtoasters on 10/7/2016.
 */
public abstract class AbstractPlayer {
    private Board board;
    private int numMines;
    private int dimension;


    /**
     *
     * @return a true if the board was solved, a false otherwise
     */
    public abstract boolean solve();

    /**
     *
     * @return whether or not the board is currently solved
     */
    public boolean checkSolved() {
        int numCovered = 0;
        for ( int x = 0 ; x < dimension ; x ++ ) {
            for ( int y = 0 ; y < dimension ; y ++ ) {
                if ( board.getCell(x,y).isCovered() ){
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
     *  @param maxValue The max integer that will be randomly generated
     *  @returns a random value from 0(inclusive) to max value(exclusive)
     */
    public int randomInt( int maxValue ) {
        Random gen = new Random();
        return gen.nextInt( maxValue );
    }

    /**
     *
     * @return the board for the game
     */
    public Board getBoard() {
        return board;
    }

    /**
     *
     * @return the number of mines on the board
     */
    public int getNumMines() {
        return numMines;
    }

    /**
     *
     * @return the dimension of the board
     */
    public int getDimension() {
        return dimension;
    }

    /**
     *
     * @param board the player is using
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     *
     * @param numMines in the board
     */
    public void setNumMines(int numMines) {
        this.numMines = numMines;
    }

    /**
     *
     * @param dimension dimension of the board
     */
    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

}
