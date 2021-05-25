package com;

import com.Controller.KeyboardController;
import com.Model.Entity.*;
import com.Model.GameBoard.GameBoard;

import com.Model.Observer;
import com.View.GUI;
import org.easymock.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static junit.framework.TestCase.assertEquals;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

@RunWith(EasyMockRunner.class)
public class GameBoardTest {

    @TestSubject
    static GameBoard gameBoard;
    @Mock
    static Barricade barricade;


    @BeforeClass
    public static void instantiateGameBoard(){
        gameBoard = new GameBoard(null,null,600,500);
    }

    @Test
    public void barricadeCollisionTest() throws InterruptedException {

        final AtomicBoolean wasHit = new AtomicBoolean();
        barricade.hit();
        expectLastCall().andAnswer((IAnswer<Void>) () -> {
            wasHit.set(true);
            return null;
        });


        expect(barricade.getxPosition()).andReturn(550).anyTimes();
        expect(barricade.getyPosition()).andReturn(450).anyTimes();
        expect(barricade.getHealth()).andReturn(1).anyTimes();
        barricade.move();
        expectLastCall().anyTimes();

        replay(barricade);

        gameBoard.getEntityList().clear();
        gameBoard.getEntityList().add(barricade);


        Projectile projectile = new Projectile(550,450, Direction.DOWN,gameBoard,null);
        gameBoard.getEntityList().add(projectile);


        //new Thread(gameBoard).start();
        gameBoard.detectCollision();
        //Thread.sleep(1000);
        assert(wasHit.get());
    }

    @Test
    public void projectileCollisionTestSameDirection(){
        gameBoard.getEntityList().clear();
        gameBoard.getEntityList().add(new Projectile(200,200,Direction.DOWN,null,null));
        gameBoard.getEntityList().add(new Projectile(200,200,Direction.DOWN,null,null));
        int oldScore = gameBoard.gameScore;
        gameBoard.detectCollision();
        //The points should be the same because they fly in the same direction (otherwise player could cheat):
        assertEquals(oldScore,gameBoard.gameScore);
        //Still the overlapping Projectiles should not exist anymore:
        assert(gameBoard.getEntityList().isEmpty());
    }

   @Test
    public void projectileCollisionTestDifferentDirection(){
        gameBoard.getEntityList().clear();
        gameBoard.getEntityList().add(new Projectile(200,200,Direction.UP,null,null));
        gameBoard.getEntityList().add(new Projectile(200,200,Direction.DOWN,null,null));
        int oldScore = gameBoard.gameScore;
        gameBoard.detectCollision();
        //The projectiles should hit each other:
        assert(gameBoard.getEntityList().isEmpty());
        //The score should be higher than before.
        assert(gameBoard.gameScore>oldScore);
    }


}
