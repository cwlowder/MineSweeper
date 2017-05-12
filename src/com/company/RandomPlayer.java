package com.company;

/**
 * Created by Curtis Lowder on 10/7/2016.
 */
public class RandomPlayer extends AbstractPlayer {

    public RandomPlayer(Board board , int numMines) {
        setBoard( board );
        setDimension( board.getBoard().length );
        setNumMines( numMines );
    }

    /**
     * this algorithm simply picks random cells on the board until either winning or loosing
     * @return a true if the board was solved, a false otherwise
     */
    @Override
    public boolean solve() {
        while ( ! getBoard().checkSolved() ) {
            int xPos = randomInt( getDimension() );
            int yPos = randomInt( getDimension() );
            if ( getBoard().getCell( xPos , yPos ).isCovered() ) {
                if ( getBoard().isDangerous( xPos , yPos ) ) {
                    return false;
                } else {
                    getBoard().uncoverLocation(xPos, yPos);
                }
            }
        }

        if ( getBoard().checkSolved() ) {
            return true;
        }
        else {
            return false;
        }
    }
}
