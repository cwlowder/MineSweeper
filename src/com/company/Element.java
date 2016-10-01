package com.company;

public class Element {
    private int x;
    private int y;
    private boolean isCovered = false;
    private boolean isMine;
    private int neighborMines = 0;

    public Element( int x, int y , boolean isMine ) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
    }

    public void reveal() {
        isCovered = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isCovered() {
        return isCovered;
    }

    public boolean isMine() {
        return isMine;
    }

    private int numNeighborMines() {
        return neighborMines;
    }
}
