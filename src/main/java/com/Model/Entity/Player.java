package com.Model.Entity;


import com.Model.GameBoard.GameBoard;
import com.Model.Observer;

public class Player extends Entity implements Observer {

    public Player(int xPosition, int yPosition, GameBoard gameBoard){
        super(xPosition, yPosition, 10, Direction.NONE, gameBoard);
    }

    //Invoked by KeyboardController, Creates a new projectile on the GameBoard
    public void shoot(){
        gameBoard.addProjectile(new Projectile(getxPosition(), getyPosition() - 5, Direction.UP, gameBoard, this));
    }

    //Invoked by the KeyboardController when Arrow keys are pressed.
    public void setDirection(Direction direction){
        super.direction = direction;
    }

    /**
     *
     * @param c //s shoot l left r right n nothing (key released)
     */
    @Override
    public void update(char c) {
        if(c == 's'){
            shoot();
        }
        if(c == 'l'){
            setDirection(Direction.LEFT);
        }
        if(c == 'r'){
            setDirection(Direction.RIGHT);
        }
        if(c == 'n'){
            setDirection(Direction.NONE);
        }
    }
}
