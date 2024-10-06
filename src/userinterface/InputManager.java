package userinterface;

import gameobjects.GameWorld;
import gameobjects.Megaman;
import gameobjects.ParticularObject;

import java.awt.event.KeyEvent;

public class InputManager {
    private GameWorld gameWorld;

    public InputManager(GameWorld gameWorld){
        this.gameWorld = gameWorld;
    }
    public void processKeyPressed(int KeyCode){
        switch (KeyCode) {
            case KeyEvent.VK_A:
                gameWorld.getMegaman().setDirection(ParticularObject.LEFT_DIR);
                gameWorld.getMegaman().run();
                break;
            case KeyEvent.VK_S:
                gameWorld.getMegaman().dick();
                break;
            case KeyEvent.VK_D:
                gameWorld.getMegaman().setDirection(ParticularObject.RIGHT_DIR);
                gameWorld.getMegaman().run();
                break;
            case KeyEvent.VK_W:
                gameWorld.getMegaman().jump();
                break;
        }
        System.out.println((gameWorld.getMegaman().getDirection() == 1)?"RIGHT" : "LEFT");
    }

    public void processKeyReleased(int KeyCode){
        switch (KeyCode) {
            case KeyEvent.VK_A:
                gameWorld.getMegaman().setSpeedX(0);
                break;
            case KeyEvent.VK_S:
                break;
            case KeyEvent.VK_D:
                gameWorld.getMegaman().setSpeedX(0);
                break;
            case KeyEvent.VK_W:
                break;
//            case KeyEvent.VK_A:
//
//                break;
//            case KeyEvent.VK_A:
//
//                break;
//            case KeyEvent.VK_A:
//
//                break;
//            case KeyEvent.VK_A:
//
//                break;
//            case KeyEvent.VK_A:
//
//                break;
        }
    }
}
