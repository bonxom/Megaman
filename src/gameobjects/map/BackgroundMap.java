package gameobjects.map;

import effect.CacheDataLoader;
import gameobjects.Camera;
import gameobjects.GameObject;
import gameobjects.GameWorld;
import userinterface.GameFrame;

import java.awt.*;

public class BackgroundMap extends GameObject {
    public int[][] map;
    private int tileSize;

    public BackgroundMap(float posX, float posY, GameWorld gameWorld) {
        super(posX, posY, gameWorld);
        this.tileSize = 30;
        this.map = CacheDataLoader.getInstance().getBackground_map();
    }

    @Override
    public void Update() {}

    public void draw(Graphics2D g2){
        Camera camera = getGameWorld().getCamera();

        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[0].length; j++){
                int limitX = (int)(j*tileSize - camera.getPosX());
                int limitY = (int)(i*tileSize - camera.getPosY());
                if (limitX > -30 && limitX < GameFrame.SCREEN_WIDTH &&
                    limitY > -30 && limitY < GameFrame.SCREEN_HEIGHT){

                    g2.drawImage(CacheDataLoader.getInstance().getFrameImage("tiled" + map[i][j]).getImage(),
                            (int)(getPosX() + j*tileSize - camera.getPosX()),
                            (int)(getPosY() + i*tileSize - camera.getPosY()), null);
                }
            }
        }
    }
}
