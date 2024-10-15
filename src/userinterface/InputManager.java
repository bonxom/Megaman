package userinterface;

import gameobjects.GameWorld;
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
            case KeyEvent.VK_J:
                gameWorld.getMegaman().attack();
                break;
            case KeyEvent.VK_K:
                gameWorld.getMegaman().attack1();
                break;
            case KeyEvent.VK_L:
                gameWorld.getMegaman().attack2();
                break;
            case KeyEvent.VK_ENTER:
                if (gameWorld.state == GameWorld.PAUSEGAME){
                    if (gameWorld.previousState == GameWorld.GAMEPLAY){
                        gameWorld.switchState(GameWorld.GAMEPLAY);
                    }
                    else gameWorld.switchState(GameWorld.TUTORIAL);

                    gameWorld.bgMusic.start();

                }

                if (gameWorld.state == GameWorld.TUTORIAL && gameWorld.storyTutorial >= 1){
                    if (gameWorld.storyTutorial <= 3){
                        gameWorld.storyTutorial++;
                        gameWorld.currentSize = 1;
                        gameWorld.textTutorial = gameWorld.texts1[gameWorld.storyTutorial - 1];
                    }
                    else{
                        gameWorld.switchState(GameWorld.GAMEPLAY);
                    }

                    if (gameWorld.tutorialState == GameWorld.MEETFINALBOSS){
                        gameWorld.switchState(GameWorld.GAMEPLAY);
                    }
                }
        }
    }

    public void processKeyReleased(int KeyCode){
        switch (KeyCode) {
            case KeyEvent.VK_A:
                if (gameWorld.getMegaman().getSpeedX() < 0){
                    gameWorld.getMegaman().stopRun();
                }
                break;
            case KeyEvent.VK_S:
                gameWorld.getMegaman().standUp();
                break;

            case KeyEvent.VK_D:
                if (gameWorld.getMegaman().getSpeedX() > 0){
                    gameWorld.getMegaman().stopRun();
                }
                break;
            case KeyEvent.VK_W:
                break;
            case KeyEvent.VK_ENTER:
                break;
            case KeyEvent.VK_J:
                break;
            case KeyEvent.VK_K:
                break;
            case KeyEvent.VK_L:
                break;
        }
    }
}
