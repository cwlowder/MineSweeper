package com.company;

/**
 * Created by Curtis Lowder on 10/7/2016.
 */
public class RandomPlayer extends AbstractPlayer {

    public RandomPlayer(Board board , int numMines) {
        setBoard( board );
        setDimension( board.getDimension() );
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
            if ( getBoard().getCell( xPos , yPos ) == null ) {
                // must uncover a location
                getBoard().uncoverLocation(xPos, yPos);
                // then check if the location is a mine
                if ( getBoard().isDangerous( xPos , yPos ) ) {
                    return false;
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
