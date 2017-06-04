package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Curtis Lowder on 10/2/2016.
 */
public class Main {
    // After a dimension of 77 the program will crash due to StackOverflowError
    private static final int MAXDIMENSION = 77;
    private static final String YES = "YES";
    private static final String NO = "NO";
    private static final float MAXPERCENTMINES = .90f;

    public static void main(String[] args) {
        Board b = new Board(5 , 2);
        Cell[][] template = {
                { new Cell(0, 0, false), new Cell( 1, 0, true), new Cell(2, 0, false), new Cell(3, 0, false)},
                { new Cell(0, 1, false), new Cell( 1, 1, false), new Cell(2, 1, false), new Cell(3, 1, false)},
                { new Cell(0, 2, true), new Cell( 1, 2, false), new Cell(2, 2, false), new Cell(3, 2, false)},
                { new Cell(0, 3, false), new Cell( 1, 3, false), new Cell(2, 3, false), new Cell(3, 3, false)}
        };
        b.setBoard(template);
        b.uncoverLocation(3,3);
        //System.out.println(b.toString());
        AbstractPlayer player = new FilterPlayer(b, 1);
        player.solve();
        System.out.println("END:");
        System.out.println(b.stringFail());
        //
        boolean aiPlay = getYesNoConsole("Should AI play(YES/NO)?");

        int dimension = getValueConsole( "What dimension should the board be?" , MAXDIMENSION );
        int numMines = getValueConsole( "How many mines should be placed?" , (int)(MAXPERCENTMINES*dimension*dimension) );

        if ( aiPlay ) {
            int numTrails = getValueConsole( "How many trails of the AIs should be run?" , Integer.MAX_VALUE );

            // probabilistic model
            /*
            System.out.println("Probabilistic Model:");
            Long startTime = System.currentTimeMillis();
            trailRunAIProbabilistic(dimension, numMines, numTrails);
            Long endTime = System.currentTimeMillis();
            System.out.println("time passed(millisec):" + (endTime - startTime));
            */
            // Separator
            System.out.println("------------");

            // filter model
            System.out.println("Filter Model:");
            Long startTime = System.currentTimeMillis();
            trailRunAIFilter(dimension, numMines, numTrails);
            Long endTime = System.currentTimeMillis();
            System.out.println("time passed(millisec):" + (endTime - startTime));
            // Separator
            System.out.println("------------");

            // random model
            System.out.println("Random Model:");
            startTime = System.currentTimeMillis();
            trailRunAIRandom(dimension, numMines, numTrails);
            endTime = System.currentTimeMillis();
            System.out.println("time passed(millisec):" + (endTime - startTime));
        }
        else {
            playHuman(dimension, numMines);
        }
    }

    /**
     * plays a number of trails using the ProbabilisticPlayer class
     * prints out the success rate afterwards
     * also prints out the progress of the AI
     * @param dimension dimension for the board to be tested
     * @param numMines the number of mines to place on the board
     * @param numTrails the number of trails that should be done
     */
    private static void trailRunAIProbabilistic(int dimension, int numMines, int numTrails) {
        int numWins = 0;

        for ( int i = 0 ; i < numTrails ; i++ ) {
            System.out.print("\rProgress:" + 100 * ( (float) i / (float) numTrails) + "%");

            Board board = new Board( dimension , numMines );
            AbstractPlayer player = new ProbabilisticPlayer( board , numMines );

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
     * plays a number of trails using the RandomPlayer class
     * prints out the success rate afterwards
     * also prints out the progress of the AI
     * @param dimension dimension for the board to be tested
     * @param numMines the number of mines to place on the board
     * @param numTrails the number of trails that should be done
     */
    private static void trailRunAIRandom(int dimension, int numMines, int numTrails) {
        int numWins = 0;

        for ( int i = 0 ; i < numTrails ; i++ ) {
            System.out.print("\rProgress:" + 100 * ( (float) i / (float) numTrails) + "%");

            Board board = new Board( dimension , numMines );
            AbstractPlayer player = new RandomPlayer( board , numMines );

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
     * plays a number of trails using the FilterPlayer class
     * prints out the success rate afterwards
     * also prints out the progress of the AI
     * @param dimension dimension for the board to be tested
     * @param numMines the number of mines to place on the board
     * @param numTrails the number of trails that should be done
     */
    private static void trailRunAIFilter(int dimension, int numMines, int numTrails) {
        int numWins = 0;

        for ( int i = 0 ; i < numTrails ; i++ ) {
            System.out.print("\rProgress:" + 100 * ( (float) i / (float) numTrails) + "%");

            Board board = new Board( dimension , numMines );
            AbstractPlayer player = new FilterPlayer( board , numMines );

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
     * This allows a human to play the game of MineSweeper
     * @param dimension dimension for the board to be used
     * @param numMines number of mines to be placed on board
     */
    private static void playHuman(int dimension, int numMines) {
        Board board = new Board(dimension, numMines);
        while ( ! board.checkSolved() ) {
            int xPos = getValueConsole("What X position should be digged?", dimension - 1);
            int yPos = getValueConsole("What Y position should be digged?", dimension - 1);

            // most uncover location and then check if it is dangerous
            board.uncoverLocation(xPos, yPos);

            if ( board.isDangerous( xPos, yPos) ) {
                System.out.println("Boom! You have exploded");
                System.out.println(board.stringFail());
                return;
            }

            System.out.println(board);
        }
        System.out.println("Congratulations! you have won the game!");
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
                System.out.println( "Sorry, please enter a valid positive integer less than " + max + ".");
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

    /**
     *
     * @param message the message to be displayed
     * @return a boolean answering the question in the message
     */
    private static boolean getYesNoConsole(String message) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( System.in ) );
        boolean badInput = true;

        while ( badInput ) {
            System.out.println(message);
            String in = null;

            try {
                in = bufferedReader.readLine();
            } catch (Exception e) {
                System.out.println( "Sorry, please enter either yes or no.");
                continue;
            }

            if ( in.equalsIgnoreCase(YES) ) {
                return true;
            }
            else if ( in.equalsIgnoreCase(NO) ){
                return false;
            }
            else {
                System.out.println( "Sorry, please enter a valid positive integer" );
                badInput = true;
            }
        }
        return false;
    }
}
