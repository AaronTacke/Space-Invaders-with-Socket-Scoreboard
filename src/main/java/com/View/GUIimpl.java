package com.View;


import com.Controller.KeyboardController;
import com.Model.Entity.Entity;
import com.Model.GameBoard.GameBoard;
import com.Model.GameSession.GameSession;
import com.View.Controllers.GameBoardController;
import com.View.Controllers.StatisticsController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.List;

public class GUIimpl implements GUI {
    //are initialized when the join / host button is pressed
    private GameSession gameSession;
    private GameBoard gameBoard;
    private KeyboardController keyboardController;
    private Stage stage;
    private FXMLLoader startUpLoader;
    private FXMLLoader gameBoardLoader;
    private FXMLLoader statisticsLoader;

    public GUIimpl(Stage stage, FXMLLoader startUpLoader, FXMLLoader gameBoardLoader, FXMLLoader statisticsLoader) {
        this.stage = stage;
        this.startUpLoader = startUpLoader;
        this.gameBoardLoader = gameBoardLoader;
        this.statisticsLoader = statisticsLoader;
    }

    public void displayStartUp() {
        try {
            stage.setTitle("TUM Space Invaders v1.0");
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    Platform.exit();
                    System.exit(0);
                }
            });
            stage.setScene(new Scene(startUpLoader.load()));
            stage.show();
        } catch (IOException e) {
            System.out.println("Could not read startUpFXML!");
        }
    }

    public void displayGameBoard(String sessionID){
        keyboardController = new KeyboardController();
        gameBoard = new GameBoard(this, keyboardController, 600, 500);

        gameBoard.setSessionID(sessionID);

        Thread gameBoardThread = new Thread(gameBoard);
        gameBoardThread.start();
        try {
            Scene gameBoardScene = new Scene(gameBoardLoader.load());
            gameBoardScene.setOnKeyPressed(keyboardController::keyPressed);
            gameBoardScene.setOnKeyReleased(keyboardController::keyReleased);
            stage.setScene(gameBoardScene);
        } catch (IOException e) {
            System.out.println("Could not read gameBoardFXML!");
        }
    }

    public void displayStatistics(){
        gameSession = new GameSession(this, 123); //TODO change port
        Thread gameSessionThread = new Thread(gameSession);
        gameSessionThread.start();
        try {
            stage.setScene(new Scene(statisticsLoader.load()));
            ((StatisticsController)statisticsLoader.getController()).updateSessionID(gameSession.getSessionID());
        } catch (IOException e) {
            System.out.println("Could not read statisticsFXML!");
        }
    }

    public void updateFrame(List<Entity> entityList, int score, boolean isConnected, String leaderboard){
        //is needed because a non-Application thread cannot update the UI
        if (entityList == null) ((GameBoardController)gameBoardLoader.getController()).displayLostScreen();
        else {
            String[] leaders = leaderboard.split(":");
            StringBuilder bd = new StringBuilder();
            boolean foundYourself = false;
            String[] numbers = new String[]{"1st","2nd","3rd"};
            for (int i = 0; i < leaders.length; i++) {
                if(!leaders[i].equals("")) {
                    bd.append(numbers[i]);
                    if (Math.abs(Integer.parseInt(leaders[i]) - score) <= 1 && !foundYourself) {
                        foundYourself = true;
                        bd.append(" (YOU)");
                    }
                    bd.append(": " + leaders[i] + "\n");
                }
            }
            String leaderLabelString = bd.toString();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ((GameBoardController) gameBoardLoader.getController()).updateFrame(entityList, score, isConnected, leaderLabelString);
                }
            });
        }
    }

    public void updateStatistics(List<Integer> values){
        //is needed because a non-Application thread cannot update the UI
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ((StatisticsController)statisticsLoader.getController()).updateValues(values);
            }
        });
    }
}
