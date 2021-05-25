package com.Model.GameBoard;

import com.Controller.KeyboardController;
import com.Model.Entity.*;
import com.Model.GameSession.SessionID;
import com.View.GUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard implements Runnable {
    private List<Entity> entityList;
    private ArrayList<ArrayList<Entity>> enemyList;
    private List<Projectile> newProjectiles;
    private Random random = new Random();

    private SessionID sessionID;
    private KeyboardController keyboardController;
    private GUI gui;
    private int height;
    private int width;
    public int gameScore = 0;
    private String leaderboard;

    private int anonymousIdentificationNumber;

    private boolean connected;
    public boolean gameOver = false;

    public GameBoard(GUI gui, KeyboardController keyboardController, int width, int height){
        this.gui = gui;
        this.width = width;
        this.height = height;
        anonymousIdentificationNumber = new Random().nextInt();
        this.keyboardController = keyboardController;
        enemyList = new ArrayList<>();
        entityList = new ArrayList<>();
        newProjectiles = new ArrayList<>();
        leaderboard = "";
        for (int i = 0; i < width - 3; i++) {
            enemyList.add(new ArrayList<>());
        }
    }

    public List<Entity> getEntityList(){
        return entityList;
    }

    //If a new Projectile is Created it has to be added to the GameBoard
    public synchronized void addProjectile(Projectile projectile){
        newProjectiles.add(projectile);
    }

    public void setSessionID(String sessionID){
        this.sessionID = new SessionID(sessionID);
    }

    public KeyboardController getKeyboardController() {
        return keyboardController;
    }

    //Creates Entities
    public void startGame(){
        transmitData(0); //Log In in SessionID-GameSession
        Player player = new Player(width/2,height-50,this);
        keyboardController.addObserver(player);
        entityList.add(player);
        createEnemies();
    }
    private void createEnemies(){
        Enemy enemy;
        for (int i = 0; i < height-200; i+=50) {
            for (int j = 0; j < width-100; j+=50) {
                enemy = new Enemy(j, i, this);
                enemyList.get(j).add(enemy);
                entityList.add(enemy);
            }
        }
    }

    /**
     * Goes through the entityList and sets the positions
     */
    public void updateGame(){
        int frameCounter = 0;
        int sendNotificationCounter = 0;
        while (true) {
            //Move entities
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).move();
            }
            changeEnemyDirection();

            detectCollision();

            manageProjectiles();

            if (frameCounter > 30){
                frameCounter = 0;
                letEnemyShoot();
                gui.updateFrame(entityList, gameScore, connected, leaderboard);
            }
            if(sendNotificationCounter>5000){
                sendNotificationCounter = 0;
                transmitData(0);
            }
            try {
                Thread.sleep(20);
                frameCounter += 30;
                sendNotificationCounter += 30;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkGameFinish();
        }
    }

    private synchronized void manageProjectiles(){
        entityList.removeIf(entity -> (entity instanceof Projectile &&
                (entity.getyPosition() < 1 || entity.getyPosition() > height-1)));
        entityList.addAll(newProjectiles);
        newProjectiles.clear();
    }

    private void changeEnemyDirection(){
        if(entityList.stream().filter(entity -> (entity instanceof Enemy)).anyMatch(entity -> (entity.getyPosition()>height-50)))
                gameOver = true;
        if (entityList.stream().filter(entity -> (entity instanceof Enemy)).anyMatch(entity -> (entity.getxPosition() < 1))) {
            entityList.stream().filter(entity -> (entity instanceof Enemy)).forEach(entity -> {
                entity.setDirection(Direction.RIGHT);
                entity.setYPosition(entity.getyPosition()+25);
            });
        }
        if (entityList.stream().filter(entity -> (entity instanceof Enemy)).anyMatch(entity -> (entity.getxPosition() >= width - 50))) {
            entityList.stream().filter(entit -> (entit instanceof Enemy)).forEach(entity -> {
                entity.setDirection(Direction.LEFT);
                entity.setYPosition(entity.getyPosition()+25);
            });
        }
    }

    private void checkGameFinish(){
        //Game lost:
        if(gameOver){
            gui.updateFrame(null,gameScore,connected,leaderboard);
            keyboardController.clearObservers();
            gameOver = false;
            gameScore = 0;
            for(int k = 0; k < enemyList.size(); k++){
                enemyList.get(k).clear();
            }
            entityList.clear();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startGame();
        //Game won:
        }else if(enemyList.stream().allMatch(List::isEmpty)){
            keyboardController.clearObservers();
            for(int k = 0; k < enemyList.size(); k++){
                enemyList.get(k).clear();
            }
            entityList.clear();
            startGame();
        }
    }

    private void letEnemyShoot(){
        if(enemyList.size()>0) {
            int indexOfEnemyShooting = random.nextInt(enemyList.size());
            ArrayList<Entity> enemiesShooting = enemyList.get(indexOfEnemyShooting);
            if(enemiesShooting.size()>0) {
                ((Enemy) enemiesShooting.get(enemiesShooting.size() - 1)).shoot();
            }
        }
    }

    public void increasePoints(int points){
        gameScore += points; //;)
        transmitData(points);
    }

    @Override
    public void run() {
        startGame();
        updateGame();
    }

    /**
     * Checks for the collisions and updates the game.
     */
    public void detectCollision(){
        for (int i = 0; i < entityList.size(); i++) {
            Entity entity1 = entityList.get(i);
            for (int j = 0; j < entityList.size(); j++) {
                Entity entity2 = entityList.get(j);

                if(!entity1.equals(entity2) && detectCollision(entity1, entity2)){
                    //Collision detected
                    //Projectile and Projectile
                    if((entity1 instanceof Projectile) && (entity2 instanceof Projectile)){
                        entityList.remove(entity1);
                        entityList.remove(entity2);
                        //Only gives points if the projectiles aren't both from player or enemy.
                        if(entity1.getDirection()!=entity2.getDirection()) {
                            increasePoints(10); //;)
                        }
                    }
                    //Projectile and Enemy
                    if((entity1 instanceof Projectile) && (entity2 instanceof Enemy) || (entity1 instanceof Enemy) && (entity2 instanceof Projectile)){
                        entityList.remove(entity1);
                        entityList.remove(entity2);
                        Enemy enemy = (Enemy) ((entity1 instanceof Enemy) ? entity1 : entity2);
                        deleteEnemyFromEnemyList(enemy);
                        increasePoints(1);
                    }
                    if(entity1 instanceof Barricade){
                        ((Barricade)entity1).hit();
                        entityList.remove(entity2);
                        if(entity2 instanceof Enemy){
                            deleteEnemyFromEnemyList((Enemy)entity2);
                        }
                        if(((Barricade) entity1).getHealth()<=0) entityList.remove(entity1);
                    }
                    if(entity2 instanceof Barricade){
                        ((Barricade)entity2).hit();
                        entityList.remove(entity1);
                        if(entity1 instanceof Enemy){
                            deleteEnemyFromEnemyList((Enemy)entity1);
                        }
                        if(((Barricade) entity2).getHealth()<=0) entityList.remove(entity2);
                    }
                    //Projectile and Player
                    if(entity2 instanceof Player || entity1 instanceof Player){
                        gameOver = true;
                    }
                }
            }
        }
    }

    private void deleteEnemyFromEnemyList(Enemy enemy) {
        for (int i = 0; i < enemyList.size(); i++) {
            if (enemyList.get(i).remove(enemy)){
                //If the last enemy is deleted from the columnList
                if (enemyList.get(i).size() == 0) {
                    enemyList.remove(i);
                }
                return;
            }
        }
    }


    public boolean detectCollision(Entity entity1, Entity entity2) {
        if(entity1 instanceof Projectile && entity2.equals(((Projectile)entity1).getShooter())) return false;
        if(entity2 instanceof Projectile && entity1.equals(((Projectile)entity2).getShooter())) return false;
        int distance = 25;
        if((entity1 instanceof Player && entity2 instanceof Enemy)||(entity1 instanceof Enemy && entity2 instanceof Player)) distance = 50;
        return Math.abs(entity1.getxPosition() - entity2.getxPosition())<distance && Math.abs(entity1.getyPosition() - entity2.getyPosition())<distance ;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth() {
        return width;
    }

    //Connects to GameSession and transmits data (Using the IP and Port in sessionID)
    public void transmitData(int data){
        if(sessionID==null) connected = false;
        else new Thread(new Transmitter(data)).start();
    }

    //Creates a Socket, connects to ServerSocket according to SessionID and transmits data.
    private class Transmitter implements Runnable{
        private String message;

        public Transmitter(int information){
            message = anonymousIdentificationNumber+":"+information+":"+gameScore+"\n";
        }

        @Override
        public void run() {
            //Connect to Serversocket and transmit data
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(sessionID.getIP(),sessionID.getPort()), 100);
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                printWriter.print(message);
                printWriter.flush();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!br.ready()) ;
                //Receive Data and store it.
                leaderboard = br.readLine();
                connected = true;
            } catch (Exception e) {
                connected = false;
            }
        }
    }


}
