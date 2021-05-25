package com.Model.Entity;


import com.Model.GameBoard.GameBoard;

public class Projectile extends Entity {

    private Entity shooter;

    public Projectile(int xPosition, int yPosition, Direction direction, GameBoard gameBoard, Entity shooter) {
        super(xPosition, yPosition,15, direction, gameBoard);
        this.shooter = shooter;
    }

    public Entity getShooter() {
        return shooter;
    }
}
