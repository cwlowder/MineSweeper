package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.SyncFailedException;

public class Main {
    public static int test0 = 0;
    public static int test1 = 0;
    public static int test2 = 0;
    public static void main(String[] args) {
        int dimension = getValueConsole( "What dimension should the board be?" , Integer.MAX_VALUE );
        int numMines = getValueConsole( "How many mines should be placed?" , dimension*dimension );

        Long startTime = System.currentTimeMillis();
        playAI(dimension, numMines );
        Long endTime = System.currentTimeMillis();

        System.out.println( "time passed(millisec):" + ( endTime - startTime ) );

        /*
        Board board = new Board(dimension, numMines);
        while (true) {
            int xPos = getValueConsole( "What X position should be digged?" , dimension - 1 );
            int yPos = getValueConsole( "What Y position should be digged?" , dimension - 1 );

            board.uncoverLocation( xPos , yPos );

            System.out.println(board.toString());
        }
        */
    }

    private static void playAI(int dimension, int numMines) {
        int numWins = 0;
        int numTrails = 100000;

        for ( int i = 0 ; i < numTrails ; i++ ) {
            //System.out.println( "TRIAL:" + i );
            Board board = new Board( dimension , numMines );
            //AbstractPlayer player = new probabilisticPlayer( board , numMines );
            AbstractPlayer player = new randomPlayer( board , numMines );

            boolean isSolved = player.solve();

            if ( isSolved ) {
                numWins++;
            }
        }
        System.out.println( "WINS: " + numWins + "/" + numTrails );
        System.out.println( "WIN%: " + 100*( (float) numWins / (float) numTrails) + "%" );
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
