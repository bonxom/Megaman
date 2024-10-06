package userinterface;

import effect.Animation;
import effect.CacheDataLoader;
import effect.FrameImage;
import gameobjects.GameWorld;
import gameobjects.Megaman;
import gameobjects.PhysicalMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements Runnable, KeyListener{
    private Thread thread;
    private boolean isRunning;
    private InputManager inputManager;

    private BufferedImage bufferedImage;
    private Graphics2D bufG2D;

    private GameWorld gameWorld;

    public GamePanel(){
        this.setBounds(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);

        gameWorld = new GameWorld();
        inputManager = new InputManager(gameWorld);
        bufferedImage = new BufferedImage(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);

    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, this);
    }

    public void UpdateGame(){
        gameWorld.Update();
    }

    public void RenderGame(){
        if (bufferedImage == null){
            bufferedImage = new BufferedImage(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        }

        if (bufferedImage != null){
            bufG2D = (Graphics2D) bufferedImage.getGraphics();
        }

        if (bufG2D != null){
            bufG2D.setColor(Color.WHITE);
            bufG2D.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);

            gameWorld.Render(bufG2D);
        }

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
        System.out.println("HELLO");
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
        System.out.println("Press");

        inputManager.processKeyPressed(e.getKeyCode());

    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Release");
        inputManager.processKeyReleased(e.getKeyCode());
    }

}
