package com.Main;

import com.View.GUI;
import com.View.Controllers.*;
import com.View.GUIimpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        //launches JavaFX
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        //order is important here!
        //Not get confused about the KeyBoardController or the FXML related Controllers here, two different things!
        FXMLLoader startUpLoader = new FXMLLoader(getClass().getResource("/fxml/startUpScene.fxml"));
        FXMLLoader gameBoardLoader = new FXMLLoader(getClass().getResource("/fxml/gameBoardScene.fxml"));
        FXMLLoader statisticsLoader = new FXMLLoader(getClass().getResource("/fxml/statisticsScene.fxml"));

        GameBoardController gameBoardController = new GameBoardController();
        StatisticsController statisticsController = new StatisticsController();

        gameBoardLoader.setController(gameBoardController);
        statisticsLoader.setController(statisticsController);

        StartUpController startUpController = new StartUpController(stage, gameBoardLoader, statisticsLoader);
        startUpLoader.setController(startUpController);

        GUI gui = new GUIimpl(stage, startUpLoader, gameBoardLoader, statisticsLoader);

        startUpController.setGui(gui);

        gui.displayStartUp();
    }
}
