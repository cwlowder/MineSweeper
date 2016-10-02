package com.company;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        int dimension = getValueConsole( "What dimension should the board be?" , Integer.MAX_VALUE );
        int numMines = getValueConsole( "How many mines should be placed?" , dimension*dimension );
        //Board board = new Board(dimension, numMines);
        //System.out.println(board.toString());

        int numWins = 0;
        int numTrails = 100000;

        for ( int i = 0 ; i < numTrails ; i++ ) {
            Board board = new Board( dimension , numMines );
            Solver solve = new Solver( board , numMines );

            boolean isSolved = solve.solve();
            if ( isSolved ) {
                numWins++;
            }

        }
        System.out.println( "WINS: " + numWins + "/" + numTrails );
        System.out.println( "WIN%: " + 100*( (float) numWins / (float) numTrails) + "%" );
        /*while (true) {
            int xPos = getValueConsole( "What X position should be digged?" , dimension - 1 );
            int yPos = getValueConsole( "What Y position should be digged?" , dimension - 1 );

            board.uncoverLocation( xPos , yPos );

            System.out.println(board.toString());
        }*/
    }

    private static int getValueConsole( String Message , int max ) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( System.in ) );
        boolean badInput = true;

        int value = 0;
        while ( badInput ) {
            System.out.println(Message);
            String in = null;

            try {
                in = bufferedReader.readLine();
                value = Integer.parseInt(in);
            } catch (Exception e) {
                System.out.println( "Sorry, please enter a valid positive integer.");
                continue;
            }

            if ( max < value || value < 0 ) {
                System.out.println( "Sorry, please enter a valid positive integer" );
                badInput = true;
            }
            else {
                badInput = false;
            }
        }
        return value;
    }
}
