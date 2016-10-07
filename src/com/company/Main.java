package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Curtis Lowder on 10/2/2016.
 */
public class Main {
    private static final int MAXDIMENSION = 77;
    public static void main(String[] args) {
        // After a dimension of 77 the program will crash due to StackOverflowError
        int dimension = getValueConsole( "What dimension should the board be?" , MAXDIMENSION );
        int numMines = getValueConsole( "How many mines should be placed?" , dimension*dimension );
        int numTrails = getValueConsole( "How many trails of the AIs should be run?" , Integer.MAX_VALUE );

        boolean shouldPlayAI = true;

        if ( shouldPlayAI ) {
            System.out.println("Probabilistic Model:");
            Long startTime = System.currentTimeMillis();
            playAIProbabilistic(dimension, numMines, numTrails);
            Long endTime = System.currentTimeMillis();
            System.out.println("time passed(millisec):" + (endTime - startTime));


            System.out.println("Random Model:");
            startTime = System.currentTimeMillis();
            playAIRandom(dimension, numMines, numTrails);
            endTime = System.currentTimeMillis();
            System.out.println("time passed(millisec):" + (endTime - startTime));
        }
        else {
            playHuman(dimension, numMines);
        }
    }

    /**
     * This allows a human to play the game of MineSweeper
     * @param dimension dimension for the board to be used
     * @param numMines number of mines to be placed on board
     */
    private static void playHuman(int dimension, int numMines) {
        Board board = new Board(dimension, numMines);
        while ( ! board.checkSolved() ) {
            int xPos = getValueConsole("What X position should be digged?", dimension - 1);
            int yPos = getValueConsole("What Y position should be digged?", dimension - 1);

            board.uncoverLocation(xPos, yPos);

            System.out.println(board.toString());

            if ( board.getCell( xPos , yPos ).isMined() ) {
                System.out.println("Boom! You have exploded");
                return;
            }
        }
        System.out.println("Congratulations! you have won the game!");
    }

    /**
     * plays a number of trails using the probabilisticPlayer class
     * prints out the success rate afterwards
     * also prints out the progress of the AI
     * @param dimension dimension for the board to be tested
     * @param numMines the number of mines to place on the board
     * @param numTrails the number of trails that should be done
     */
    private static void playAIProbabilistic(int dimension, int numMines, int numTrails) {
        int numWins = 0;

        for ( int i = 0 ; i < numTrails ; i++ ) {
            System.out.print("\rProgress:" + 100 * ( (float) i / (float) numTrails) + "%");

            Board board = new Board( dimension , numMines );
            AbstractPlayer player = new probabilisticPlayer( board , numMines );

            boolean isSolved = player.solve();

            if ( isSolved ) {
                numWins++;
            }
        }
        System.out.println("\rDone!");
        System.out.println( "WINS: " + numWins + "/" + numTrails );
        System.out.println( "WIN%: " + 100*( (float) numWins / (float) numTrails) + "%" );
    }

    /**
     * plays a number of trails using the randomPlayer class
     * prints out the success rate afterwards
     * also prints out the progress of the AI
     * @param dimension dimension for the board to be tested
     * @param numMines the number of mines to place on the board
     * @param numTrails the number of trails that should be done
     */
    private static void playAIRandom(int dimension, int numMines, int numTrails) {
        int numWins = 0;

        for ( int i = 0 ; i < numTrails ; i++ ) {
            System.out.print("\rProgress:" + 100 * ( (float) i / (float) numTrails) + "%");

            Board board = new Board( dimension , numMines );
            AbstractPlayer player = new randomPlayer( board , numMines );

            boolean isSolved = player.solve();

            if ( isSolved ) {
                numWins++;
            }
        }
        System.out.println("\rDone!");
        System.out.println( "WINS: " + numWins + "/" + numTrails );
        System.out.println( "WIN%: " + 100*( (float) numWins / (float) numTrails) + "%" );
    }

    /**
     *
     * @param Message the message that the user will be prompted with
     * @param max the maximum number that will be acceptable
     * @return an integer given by the user
     */
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
