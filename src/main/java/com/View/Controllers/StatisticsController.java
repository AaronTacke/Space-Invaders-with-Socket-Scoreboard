package com.View.Controllers;


import com.View.GUI;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.util.List;

public class StatisticsController {
    private GUI gui;
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public Label sessionID;
    public BarChart<String, Number> chart;
    private XYChart.Series<String, Number> dataSeries;

    public void updateValues(List<Integer> values){
        if(dataSeries==null) {
            dataSeries = new XYChart.Series<>();
        }
        dataSeries.getData().clear();
        for(int i = 0; i < values.size(); i++){
            dataSeries.getData().add(new XYChart.Data<String, Number>
                    ((-(values.size()-i)*10)+" sek", values.get(i)));
        }
        chart.getData().clear();
        chart.getData().add(dataSeries);
    }

    public void updateSessionID(String sessionID) {this.sessionID.setText("SessionID: "+sessionID);}
}
