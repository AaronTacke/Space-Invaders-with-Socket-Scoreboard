package com;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

import com.Controller.KeyboardController;
import com.Model.GameSession.GameSession;
import com.Model.GameSession.SessionID;
import com.Model.Observer;
import com.View.GUI;
import org.easymock.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@RunWith(EasyMockRunner.class)
public class GameSessionTest {

    @TestSubject
    static GameSession gameSession;
    @Mock
    static GUI gui;

    @BeforeClass
    public static void instantiateGameSession(){
        gameSession = new GameSession(gui,123);
    }

    @Test
    public void testGameSession() throws InterruptedException {
        Capture<List<Integer>> capturedArgument = Capture.newInstance();
        gui.updateStatistics(EasyMock.capture(capturedArgument));
        replay(gui);
        new Thread(gameSession).start();
        Thread.sleep(500);
        List<Integer> expected = new LinkedList<>();
        for(int i = 0; i < 15; i++) expected.add(0);
        assertEquals(expected,capturedArgument.getValue());
    }

    @Test
    public void testSessionID(){
        for(int i = 0; i < 100; i++){
            String ip = getRandomIP();
            int port = getRandomPort();
            SessionID encrypt = new SessionID(ip, port);
            SessionID decrypt = new SessionID(encrypt.getSessionID());
            assertEquals(ip,decrypt.getIP());
            assertEquals(port,decrypt.getPort());
        }
    }

    private String getRandomIP(){
        Random r = new Random();
        return r.nextInt(256)+"."+r.nextInt(256)+"."+r.nextInt(256)+"."+r.nextInt(256);
    }

    private int getRandomPort(){
        return (int)(Math.random()*65536);
    }
}
