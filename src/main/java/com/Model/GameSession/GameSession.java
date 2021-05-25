package com.Model.GameSession;

import com.View.GUI;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

public class GameSession implements Runnable{
    private ServerSocket serverSocket;
    private Map<Integer, Integer> data;
    private List<Integer> evaluatedData;
    private SessionID sessionID;
    private GUI gui;
    private Map<Integer, Integer> scoreMap;

    final private int sleepTime = 10000;

    public GameSession(GUI gui, int port) {
        //Fills Attributes
        this.gui = gui;
        data = new HashMap<>();
        scoreMap = new HashMap<>();
        evaluatedData = new LinkedList<>();
        for(int i = 0; i < 15; i++) evaluatedData.add(0);
        setSessionID(port);

        //Start DataListener to receive Data and show Statistics:
        new Thread(new DataListener()).start();
    }

    public String getSessionID(){
        return sessionID.getSessionID();
    }

    @Override
    public void run(){
        while(true){
            showStatistics();
        }
    }

    private void showStatistics(){
            //Later use evaluateStatistics, for now just the sum of the points.
        List<Integer> evaluate = evaluateStatistics();
            gui.updateStatistics(evaluate);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Still work in progress:
    //3 points per second per player equals 100% score (0<=score<=100)
    private List<Integer> evaluateStatistics(){
        evaluatedData.remove(0);
        evaluatedData.add((int)(data.values().stream().mapToDouble(i -> (i*33333.3/sleepTime)).map(i -> i<0?0:i).map(i -> i>100?100:i).average().orElse(0)));
        //Delete the player from Scoreboard that didnt play the last 10 seconds:
        scoreMap.keySet().stream().filter(key -> !data.containsKey(key))
                .collect(Collectors.toList()).forEach(scoreMap::remove);
        data.clear();
        return evaluatedData;
    }

    //Sets the sessionID using the IP address of the machine and the given port.
    private void setSessionID(int port) {
        try {
            sessionID= new SessionID(InetAddress.getLocalHost().getHostAddress(), port);
        } catch (UnknownHostException e) {
            throw new RuntimeException("The program has to access the IP address of the machine.");
        }
    }

    //Adds data to Map<String, Integer> that can later be evaluated by evaluateStatistics()
    private String addData(String message){
        Integer key = Integer.parseInt(message.split(":")[0]);
        Integer value = Integer.parseInt(message.split(":")[1]);
        Integer score = Integer.parseInt(message.split(":")[2]);
        if(data.containsKey(key)){
            data.put(key, value+data.get(key));
        }else{
            data.put(key,value);
        }
        scoreMap.put(key, score);
        return scoreMap.values().stream().sorted(Comparator.reverseOrder()).limit(3).map(i -> i+"").collect(Collectors.joining(":"));
    }

    //Opens a ServerSocket and waits for a connection to receive Data.
    private class DataListener implements Runnable {
        @Override
        public void run() {
            while (true) {
                checkServerSocket();
                try {
                    //Connect to new socket:
                    Socket client = serverSocket.accept();
                    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    while (!br.ready()) ;
                    //Receive Data and store it.
                    String returnString = addData(br.readLine());
                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
                    printWriter.print(returnString);
                    printWriter.flush();
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Makes a new serverSocket if the old one is closed or null
        private void checkServerSocket() {
            while (serverSocket == null || serverSocket.isClosed()) {
                try {
                    serverSocket = new ServerSocket(sessionID.getPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
