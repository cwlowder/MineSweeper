package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by Curtis Lowder on 10/2/2016.
 */
public class ProbabilisticPlayer extends AbstractPlayer {

    private TreeMap< String , Double > probMap = new TreeMap<>();

    public ProbabilisticPlayer(Board board , int numMines ) {
        setBoard( board );
        setDimension( board.getDimension() );
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
                getBoard().uncoverLocation(lowestProb.getX(), lowestProb.getY());
                if ( getBoard().isDangerous( lowestProb.getX() , lowestProb.getY() ) ) {
                    //System.out.println(getBoard().stringFail());
                    return false;
                }
                else {
                    computeProbabilities();
                    probMap.remove(lowestProb.toString());
                }
            }
            else {
                // pick a random location
                Cell randomLocation = randomLocation();
                getBoard().uncoverLocation( randomLocation.getX() , randomLocation.getY() );
                // updated the probabilities
                computeProbabilities();
            }
            //System.out.println(getBoard());
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
            if ( value < minValue && getBoard().getCellStr( cellStr ) != null ) {
                minValue = value;
                smallestProbCell = getBoard().getCellStr(cellStr);
            }
        }

        return smallestProbCell;
    }

    /**
     * computes the probabilities for the uncovered cells
     */
    private void computeProbabilities() {
        for ( int x = 0 ; x < getDimension() ; x++ ) {
            for ( int y = 0 ; y < getDimension() ; y++ ) {
                //if ( getBoard().getCell( x , y ) != null ) {
                    computeProbabilityNeighborMines( getBoard().getCell( x , y ) );
                //}
            }
        }
    }

    /**
     * input must be uncovered and have at least one neighbor mine
     *
     * @param cell the uncovered cell which will have its neighbors probabilities of being a mine calculated
     */
    private void computeProbabilityNeighborMines( Cell cell ) {
        if(cell.isCovered()) {
            // set probability to being numMines/numCells
            setProbabilityCell(((double)getNumMines())/(double)(getDimension()*getDimension()), cell);
        }
        else if ( cell.getNeighborMines() > 0 ) {
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
                    setProbabilityCell(probability, neighbor);
                }
            }
        }
    }

    private void setProbabilityCell(double probability, Cell neighbor) {
        if (!probMap.containsKey(neighbor.toString())) {
            probMap.put(neighbor.toString(), probability);
        } else {
            // adds existing probability with new probability
            probMap.put(neighbor.toString(), probability + probMap.get(neighbor.toString() ) );
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
            // return a representation of the cell to be uncovered
            randomCell = new Cell(x, y, false);
        }
        return randomCell;
    }
}