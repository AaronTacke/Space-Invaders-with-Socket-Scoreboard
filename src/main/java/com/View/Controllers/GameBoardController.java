package com.View.Controllers;


import com.Model.Entity.Enemy;
import com.Model.Entity.Entity;
import com.Model.Entity.Player;
import com.View.GUI;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class GameBoardController {
    private GUI gui;
    public Canvas canvas;
    public Label connected;
    public Label score;
    public Label leaderLabel;
    private Image background;
    private Image player;
    private Image enemy;
    private Image projectile;

    public GameBoardController(){
        try {
            background = new Image(getClass().getResource("/images/background.png").toURI().toURL().toString());
            player = new Image(getClass().getResource("/images/player50p.png").toURI().toURL().toString());
            enemy = new Image(getClass().getResource("/images/alien50p.png").toURI().toURL().toString());
            projectile = new Image(getClass().getResource("/images/projectile50p.png").toURI().toURL().toString());
        } catch (MalformedURLException e) {
            System.out.println("Images could not be loaded, Malformed URL!");
        } catch (URISyntaxException e) {
            System.out.println("Images could not be loaded, URI Syntax Exception!");
        }
    }

    public void displayLostScreen(){
        Platform.runLater(() -> {
            Alert lost = new Alert(Alert.AlertType.INFORMATION);
            lost.setTitle("Oh no...");
            lost.setContentText("You've lost!");
            lost.show();
        });
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void updateFrame(List<Entity> entityList, int score, boolean isConnected, String leaderLabelString){
        //clears before new entity is drawn
        canvas.getGraphicsContext2D().clearRect(0, 0, 1000, 1000);
        canvas.getGraphicsContext2D().drawImage(background, 0, 0);
        entityList.forEach(entity -> {
            Image toDraw;
            if (entity instanceof Player) toDraw = player;
            else if (entity instanceof Enemy) toDraw = enemy;
            else toDraw = projectile;
            canvas.getGraphicsContext2D().drawImage(
                    toDraw,entity.getxPosition(),entity.getyPosition()
            );
        });
        this.score.setText(Integer.toString(score));
        if (isConnected) connected.setText("Yes");
        else connected.setText("No");
        leaderLabel.setText(leaderLabelString);
    }
}
