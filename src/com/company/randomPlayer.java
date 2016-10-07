package com.company;

/**
 * Created by Badtoasters on 10/7/2016.
 */
public class randomPlayer extends AbstractPlayer {

    public randomPlayer( Board board , int numMines) {
        setBoard( board );
        setDimension( board.getBoard().length );
        setNumMines( numMines );
    }

    /**
     * @return a true if the board was solved, a false otherwise
     */
    @Override
    public boolean solve() {
        while ( ! checkSolved() ) {
            int xPos = randomInt( getDimension() );
            int yPos = randomInt( getDimension() );
            if ( getBoard().getCell( xPos , yPos ).isCovered() ) {
                if ( getBoard().getCell(xPos, yPos).isMined() ) {
                    return false;
                } else {
                    getBoard().uncoverLocation(xPos, yPos);
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
}
