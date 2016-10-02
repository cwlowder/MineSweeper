package com.company;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.Random;
import java.util.TreeMap;

public class Solver {
    private Board board;
    private int numMines;
    private int dimension;

    private TreeMap<String, Float> probMap = new TreeMap<>();

    public Solver( Board board , int numMines ) {
        dimension = board.getBoard().length;
        this.board = board;
        this.numMines = numMines;
    }

    public boolean solve() {
        ArrayList<String> likelyMines = new ArrayList<>();
        while ( ! checkSolved() ) {
            //System.out.println( board.toString() );
            //System.out.println( this.probDraw() );

            // if sufficient number of probabilities have been calculated
            Cell lowestProbCell = getSmallestProbCell();

            if ( lowestProbCell != null ) {
                float prob = probMap.get( lowestProbCell.toString() );
                if ( prob < 1 ) {
                    if (lowestProbCell.isMined()) {
                        Main.test0++;
                        return false;
                    }
                    board.uncoverLocation(lowestProbCell.getX(), lowestProbCell.getY());
                    computeProbability(lowestProbCell);
                }
                else if ( ! likelyMines.contains( lowestProbCell.toString() ) ) {
                    likelyMines.add( lowestProbCell.toString() );
                }
                probMap.remove( lowestProbCell.toString() );
            }
            else {
                Cell coveredCell = getRandomCoveredLocation();

                // Random position must not be in the probMap
                if ( ! likelyMines.contains( coveredCell ) ) { //probMap.containsKey( coveredCell.toString() )
                    if (coveredCell.isMined()) {
                        Main.test1++;
                        return false;
                    } else {
                        coveredCell.reveal();
                        computeProbability(coveredCell);
                    }
                }
                else {
                    Main.test2++;
                    return false;
                }
            }
        }

        return true;
    }


    private Cell getRandomCoveredLocation() {
        while (true) {
            int xPos = randomInt(dimension);
            int yPos = randomInt(dimension);
            if ( board.getCell(xPos, yPos).isCovered() ) {
                return board.getCell(xPos, yPos);
            }
        }
    }

    public void computeProbability( Cell cell ) {
        // cell must be uncovered to compute probabilities
        if ( ! cell.isCovered() ) {
            ArrayList<Cell> neighbors = board.findNeighbors( cell.getX() , cell.getY() );

            float numCovered = 0;
            // Find the number of covered neighbors
            for (Cell neighbor : neighbors) {
                if (neighbor.isCovered()) {
                    numCovered++;
                }
            }

            float probability = cell.getNeighborMines() / numCovered;
            probability *= choose( (int)numCovered , cell.getNeighborMines() );
            // set prob of neighbors if some of the neighbors are landmines
            if ( probability > 0 ) {
                for (Cell neighbor : neighbors) {
                    if ( ! probMap.containsKey( neighbor.toString() ) ) {
                        probMap.put( neighbor.toString() , probability );
                    }
                    // Probability of being a landmine is assumed to be the sum of the existing and new probabilities
                    else {
                        probMap.put( neighbor.toString() , probability + probMap.get( neighbor.toString() ) );
                    }
                }
            }
            // otherwise compute probabilities of empty neighbors
            else {
                probMap.put( cell.toString() , 0f );
                for ( Cell neighbor : neighbors ) {
                    if ( ! probMap.containsKey( neighbor.toString() ) ) {
                        computeProbability( neighbor );
                    }
                }
            }
        }
    }

    public static long choose(int total, int choose){
        if(total < choose)
            return 0;
        if(choose == 0 || choose == total)
            return 1;
        return choose(total-1,choose-1)+choose(total-1,choose);
    }

    public boolean randomlySolve() {
        while ( ! checkSolved() ) {
            int xPos = randomInt( dimension );
            int yPos = randomInt( dimension );
            if ( board.getCell( xPos , yPos ).isCovered() ) {
                if ( board.getCell(xPos, yPos).isMined() ) {
                    return false;
                } else {
                    board.uncoverLocation(xPos, yPos);
                }
            }
        }

        if ( checkSolved() ) {
            return true;
        }
        else {
            return false;
        }
    }

    public Cell getSmallestProbCell() {
        Cell smallestProbCell = null;
        float minValue = Float.MAX_VALUE;

        for ( String cellStr : probMap.keySet() ) {
            float value = probMap.get( cellStr );
            // cell must have smaller probability and be covered
            if ( value < minValue && board.getCellStr( cellStr ).isCovered() ) {
                minValue = value;
                smallestProbCell = board.getCellStr(cellStr);
            }
        }

        return smallestProbCell;
    }

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

    /*
     *  @param maxValue The max integer that will be randomly generated
     *  @returns a random value from 0(inclusive) to max value(exclusive)
     */
    private int randomInt( int maxValue ) {
        Random gen = new Random();
        return gen.nextInt( maxValue );
    }

    public String probDraw() {
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
                if ( board.getCell(x,y).isMined()) {
                    boardString +="!";
                }
                if ( board.getCell(x,y).isCovered() ) {
                    int num=0;
                    for ( Cell cell : board.findNeighbors(x,y) ) {
                        if ( cell.isCovered() )
                            num++;
                    }
                    boardString += "%"+probMap.get( x+","+y ) +" ";
                }
                else {
                    if ( board.getCell(x,y).isMined() ) {
                        boardString += "M ";
                    }
                    else if ( board.getCell(x,y).getNeighborMines() > 0 ) {
                        boardString += board.getCell(x,y).getNeighborMines() +" ";
                    }
                    else {
                        boardString += ". ";
                    }
                }
            }
            boardString += "\n";
        }
        return boardString;
    }
}
