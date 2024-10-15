package userinterface;

import gameobjects.GameWorld;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements Runnable, KeyListener{
    private Thread thread;
    private boolean isRunning;
    private InputManager inputManager;

    private Graphics2D bufG2D;

    private GameWorld gameWorld;

    public GamePanel(){
        this.setBounds(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);

        gameWorld = new GameWorld();
        inputManager = new InputManager(gameWorld);

    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(gameWorld.getBufferedImage(), 0, 0, this);
    }

    public void UpdateGame(){
        gameWorld.Update();
    }

    public void RenderGame(){
        gameWorld.Render();
    }

    public void startGame(){
        if (thread == null){
            thread = new Thread(this);
            thread.start();
            isRunning = true;
        }
    }

    @Override
    public void run() {
        long FPS = 80;
        long period = 1000000000 / FPS;

        long beginTime = System.nanoTime();
        while (isRunning){//Repeat khung hinh de tao animation

            UpdateGame();
            RenderGame();
            repaint();//ve lai paint o moi buoc lap

            long deltaTime = System.nanoTime() - beginTime;
            long sleepTime = period - deltaTime;


            try{
                if (sleepTime > 0) Thread.sleep(sleepTime/1000000);
                else Thread.sleep(period/2000000);
            }catch(InterruptedException ex){};
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        inputManager.processKeyPressed(e.getKeyCode());

    }

    @Override
    public void keyReleased(KeyEvent e) {
        inputManager.processKeyReleased(e.getKeyCode());
    }

}
