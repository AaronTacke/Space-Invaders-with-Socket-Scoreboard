package com;

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

import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

@RunWith(EasyMockRunner.class)
public class KeyboardControllerTest {

    @TestSubject
    static KeyboardController keyboardController;
    @Mock
    static Observer observer;

    @BeforeClass
    public static void instantiateKeyboardController(){
        keyboardController = new KeyboardController();
    }

    @Test
    public void testKeyboardControllerAddObserver(){
        replay(observer);
        int oldSize = keyboardController.observerCount();
        keyboardController.addObserver(observer);
        assertEquals(oldSize+1,keyboardController.observerCount());
    }

    @Test
    public void testKeyboardControllerClearObserver(){
        replay(observer);
        keyboardController.addObserver(observer);
        keyboardController.clearObservers();
        assertEquals(0,keyboardController.observerCount());
    }

    @Test
    public void testKeyboardControllerUpdateObserver(){
        Capture<Character> capturedChar = Capture.newInstance();
        observer.update((EasyMock.captureChar(capturedChar)));
        replay(observer);
        keyboardController.addObserver(observer);
        keyboardController.reactToPress('s');
        assertEquals('s',(char)(capturedChar.getValue()));
    }
}
