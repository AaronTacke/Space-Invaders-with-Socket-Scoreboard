package com.Controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyboardController extends Observable {

    public void reactToPress(char key){
        //Starts the game (on GameBoard), creates a GameSession, or controls Player
        notifyObservers(key);
    }

    public void keyPressed(KeyEvent e) {
        KeyCode keyCode = e.getCode();
        if (keyCode == KeyCode.LEFT) {
            reactToPress('l');
        }else if(keyCode == KeyCode.RIGHT){
            reactToPress('r');
        }else if (keyCode == KeyCode.S || keyCode == KeyCode.UP || keyCode == KeyCode.SPACE) {
            reactToPress('s');
        }
    }

    public void keyReleased(KeyEvent e){
        KeyCode keyCode = e.getCode();
        if(keyCode == KeyCode.LEFT || keyCode == KeyCode.RIGHT){
            reactToPress('n');
        }
    }
}
