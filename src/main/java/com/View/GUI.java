package com.View;


import com.Model.Entity.Entity;

import java.util.List;

public interface GUI {
        void displayStartUp();
    void displayGameBoard(String sessionID);
   void displayStatistics();
     void updateFrame(List<Entity> entityList, int score, boolean isConnected, String leaderboard);
    void updateStatistics(List<Integer> values);
}
