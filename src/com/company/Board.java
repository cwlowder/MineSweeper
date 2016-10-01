package com.company;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.awt.*;
import java.util.Random;

public class Board {
    private int dimension;
    private Element[][] board;

    public Board ( int dimension , int numMines ) {
        this.dimension = dimension;
        generateBoard( dimension , numMines );
        calcNeighbors();
    }

    public void drawBoard() {
        for ( int y = 0 ; y < dimension ; y++ ) {
            for ( int x = 0 ; x < dimension ; x++ ) {
                if ( board[x][y].isCovered() ) {
                    System.out.print("◼");
                }
                else {
                    if ( board[x][y].isMine() ) {
                        System.out.print("M");
                    }
                    else {
                        System.out.print(".");
                    }
                }
            }
            System.out.println();
        }
    }

    /*
     *  @param dimension the dimensions of the board
     *  @param numMines the number of mines to be placed
     */
    private void generateBoard( int dimension, int numMines ) {
        // Initalizes board to be dimension x dimension
        board = new Element[dimension][dimension];

        // places all the mines needed
        while ( numMines > 0 ) {
            int xPos = randomInt( dimension );
            int yPos = randomInt( dimension );

            // Checks to see if position on board is empty
            if ( board[xPos][yPos] == null ) {
                Element newMine = new Element( xPos , yPos , true );
                board[newMine.getX()][newMine.getY()] = newMine;

                // removes the mines that remain to be placed
                numMines--;
            }
        }

        // fills the rest of the board with none mines
        for ( int x = 0 ; x < dimension ; x++ ) {
            for ( int y = 0 ; y < dimension ; y++ ) {
                // Checks if the location is empty
                if ( board[x][y] == null ) {
                    Element emptyCell = new Element( x , y , false );
                    board[x][y] = emptyCell;
                }
            }
        }
    }

    private void calcNeighbors() {
        for ( int x = 0 ; x < dimension ; x++ ) {
            for ( int y = 0 ; y < dimension ; y++ ) {
                // Checks to see if a position doesn't contains a mine
                if ( ! board[x][y].isMine() ) {

                }
            }
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
