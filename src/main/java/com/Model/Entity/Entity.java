package com.Model.Entity;

import com.Model.GameBoard.GameBoard;
import javafx.scene.image.Image;

public abstract class Entity {
    protected GameBoard gameBoard;
    private int xPosition; //top left corner
    Direction direction;
    private int speed;
    protected int yPosition;  //top left corner
    private int height = 50;
    private int width = 50;

    public Entity(int xPosition, int yPosition, int speed, Direction direction, GameBoard gameBoard) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.speed = speed;
        this.direction = direction;
        this.gameBoard = gameBoard;
    }

    //moves in the direction (set either by KeyboardController or GameBoard) and with specified speed
    public void move() {
        switch (direction) {
            case DOWN:
                yPosition = calculatePosition(yPosition + speed, gameBoard.getHeight());
                return;
            case UP:
                yPosition = calculatePosition(yPosition - speed, gameBoard.getHeight());
                return;
            case LEFT:
                xPosition = calculatePosition(xPosition - speed, gameBoard.getWidth());
                return;
            case RIGHT:
                xPosition = calculatePosition(xPosition + speed, gameBoard.getWidth() - width);
        }
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    private int calculatePosition(int newPosition, int maxPosition) {
        if (newPosition < 0) {
            return 0;
        }
        if (newPosition > maxPosition) {
            return maxPosition;
        }
        return newPosition;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setYPosition(int y){
        yPosition = y;
    }
}