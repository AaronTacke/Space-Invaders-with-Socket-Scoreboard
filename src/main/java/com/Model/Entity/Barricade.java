package com.Model.Entity;

import com.Model.GameBoard.GameBoard;

public abstract class Barricade extends Entity {
    int health;

    public Barricade(int xPosition, int yPosition, int speed, Direction direction, GameBoard gameBoard) {
        super(xPosition, yPosition, speed, direction, gameBoard);
        health = 5;
    }

    public abstract void hit();

    public abstract int getHealth();

}
