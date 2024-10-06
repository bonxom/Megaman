package userinterface;

import effect.CacheDataLoader;

import javax.swing.JFrame;
import java.io.IOException;

public class GameFrame extends JFrame{
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 600;
    private GamePanel gamePanel;


    public GameFrame(){
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            CacheDataLoader.getInstance().LoadData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        gamePanel = new GamePanel();
        this.add(gamePanel);

        this.addKeyListener(gamePanel);
    }

    public void startGame(){
        gamePanel.startGame();
    }

    public static void main(String[] args) {
        GameFrame gameframe = new GameFrame();
        gameframe.setVisible(true);
        gameframe.startGame();
    }
}
