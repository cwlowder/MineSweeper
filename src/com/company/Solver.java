package com.company;

import java.util.Random;

public class Solver {
    private Board board;
    private int numMines;
    private int dimension;
    public Solver( Board board , int numMines ) {
        dimension = board.getBoard().length;
        this.board = board;
        this.numMines = numMines;
    }

    public boolean solve() {
        int test = 0;
        while ( ! checkSolved() ) {
            test ++;
            int xPos = randomInt( dimension );
            int yPos = randomInt( dimension );
            if ( board.getBoard()[xPos][yPos].isMined() ) {
                return false;
            }
            else {
                board.uncoverLocation( xPos , yPos );
            }
        }
        if(test>20){
            System.out.println("LOL");
        }
        if ( checkSolved() ) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkSolved() {
        int numCovered = 0;
        for ( int x = 0 ; x < dimension ; x ++ ) {
            for ( int y = 0 ; y < dimension ; y ++ ) {
                if ( board.getElement(x,y).isCovered() ){
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

    /*
     *  @param maxValue The max integer that will be randomly generated
     *  @returns a random value from 0(inclusive) to max value(exclusive)
     */
    private int randomInt( int maxValue ) {
        Random gen = new Random();
        return gen.nextInt( maxValue );
    }
}
