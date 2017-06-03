package com.company;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Curtis Lowder on 10/2/2016.
 */
public class ProbabilisticPlayer extends AbstractPlayer {

    private TreeMap< String , Double > probMap = new TreeMap<>();

    public ProbabilisticPlayer(Board board , int numMines ) {
        setBoard( board );
        setDimension( board.getBoard().length );
        setNumMines( numMines );
    }

    /**
     * @return a true if the board was solved, a false otherwise
     */
    @Override
    public boolean solve() {
        while ( ! getBoard().checkSolved() ) {
            if ( probMap.size() > 0 ) {
                Cell lowestProb = lowestProbLocation();
                if ( getBoard().isDangerous( lowestProb.getX() , lowestProb.getY() ) ) {
                    return false;
                }
                else {
                    getBoard().uncoverLocation(lowestProb.getX(), lowestProb.getY());
                    computeProbabilities();
                    probMap.remove(lowestProb.toString());
                }
            }
            else {
                Cell randomLocation = randomLocation();
                if ( getBoard().isDangerous( randomLocation.getX() , randomLocation.getY() ) ) {
                    return false;
                }
                else {
                    getBoard().uncoverLocation( randomLocation.getX() , randomLocation.getY() );
                    computeProbabilities();
                }
            }
        }
        if ( getBoard().checkSolved() )
            return true;
        else {
            return false;
        }
    }

    /**
     *
     * @return the cell in the probMap with the lowest probability of being a mine
     */
    private Cell lowestProbLocation() {
        Cell smallestProbCell = null;
        double minValue = Double.MAX_VALUE;

        for ( String cellStr : probMap.keySet() ) {
            double value = probMap.get( cellStr );
            // cell must have smaller probability and be covered
            if ( value < minValue && getBoard().getCellStr( cellStr ).isCovered() ) {
                minValue = value;
                smallestProbCell = getBoard().getCellStr(cellStr);
            }
        }

        // makes sure that the pointer to the cell is correct
        if ( smallestProbCell != null) {
            return getBoard().getCell(smallestProbCell.getX(), smallestProbCell.getY());
        }
        else {
            return null;
        }
    }

    /**
     * computes the probabilities for the uncovered cells
     */
    private void computeProbabilities() {
        for ( int x = 0 ; x < getDimension() ; x++ ) {
            for ( int y = 0 ; y < getDimension() ; y++ ) {
                if ( ! getBoard().getCell( x , y ).isCovered() ) {
                    computeProbabilityNeighborMines( getBoard().getCell( x , y ) );
                }
            }
        }
    }

    /**
     * input must be uncovered and have at least one neighbor mine
     *
     * @param cell the uncovered cell which will have its neighbors probabilities of being a mine calculated
     */
    private void computeProbabilityNeighborMines( Cell cell ) {
        if ( cell.getNeighborMines() > 0 ) {
            ArrayList<Cell> neighbors = getBoard().findNeighbors( cell.getX() , cell.getY() );
            int numCoveredNeighbors = 0;

            for ( Cell neighbor : neighbors ) {
                if ( neighbor.isCovered() ) {
                    numCoveredNeighbors++;
                }
            }

            // don't continue if no covered neighbors exist
            if ( numCoveredNeighbors == 0 ) {
                return;
            }

            // the probability any neighbor cell is a mine
            double probability = cell.getNeighborMines() / numCoveredNeighbors;

            for ( Cell neighbor : neighbors ) {
                if ( neighbor.isCovered() ) {
                    if (!probMap.containsKey(neighbor.toString())) {
                        probMap.put(neighbor.toString(), probability);
                    } else {
                        // adds existing probability with new probability
                        probMap.put(neighbor.toString(), probability + probMap.get(neighbor.toString() ) );
                    }
                }
            }
        }
    }

    /**
     *
     * @return a random cell that is covered
     */
    private Cell randomLocation() {
        Cell randomCell = null;
        while ( randomCell == null || ! randomCell.isCovered() ) {
            int x = randomInt(getDimension());
            int y = randomInt(getDimension());
            randomCell = getBoard().getCell(x, y);
        }
        return randomCell;
    }
}