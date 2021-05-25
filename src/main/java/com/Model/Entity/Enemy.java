package com.Model.Entity;


import com.Model.GameBoard.GameBoard;

public class Enemy extends Entity {

    public Enemy(int xPosition, int yPosition, GameBoard gameBoard) {
        super(xPosition, yPosition,1,Direction.RIGHT,gameBoard);
    }

    //Creates a new projectile on the GameBoard
    public void shoot(){
        gameBoard.addProjectile(new Projectile(getxPosition(), getyPosition() + 5, Direction.DOWN, gameBoard, this));
    }

    public void setDirection(Direction direction){
        super.direction = direction;
    }

}
