package com.Model.Entity;

import com.Model.GameBoard.GameBoard;

public class BarricadeImpl extends Barricade{

    //We could easily have implemented this, but that would destroy the need of a mock object, right? :D

    public BarricadeImpl(int xPosition, int yPosition, int speed, Direction direction, GameBoard gameBoard) {
        super(xPosition, yPosition, speed, direction, gameBoard);
    }

    @Override
    public void hit() {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED.");
    }

    @Override
    public int getHealth() {
        throw new UnsupportedOperationException("NOT YET IMPLEMENTED.");
    }
}
