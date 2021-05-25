package com.View.Controllers;


import com.View.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StartUpController {
    private GUI gui;
    private Stage stage;
    private FXMLLoader gameBoardLoader;
    private FXMLLoader statisticsLoader;

    public TextField textfield;

    public void setGui(GUI gui){
        this.gui = gui;
    }

    public StartUpController(Stage stage, FXMLLoader gameBoardLoader, FXMLLoader statisticsLoader) {
        this.stage = stage;
        this.gameBoardLoader = gameBoardLoader;
        this.statisticsLoader = statisticsLoader;
    }

    public void displayGameBoardScene(ActionEvent event) throws IOException {
        gui.displayGameBoard(textfield.getText());
    }
    public void displayStatisticsScene(ActionEvent event) throws IOException {
        gui.displayStatistics();
    }
}
