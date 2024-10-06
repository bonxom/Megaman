package gameobjects;

import effect.CacheDataLoader;

import java.awt.*;

public class PhysicalMap extends GameObject{
    public int[][] phys_map;
    private int tileSize;


    public PhysicalMap(float x, float y, GameWorld gameWorld){
        super(x, y, gameWorld);
        this.tileSize = 30;
        phys_map = CacheDataLoader.getInstance().getPhysicalMap();
    }

    @Override
    public void Update() {

    }

    public int getTileSize() {
        return tileSize;
    }

    public void draw(Graphics2D g2){

        for (int i = 0; i < phys_map.length; i++){
            for (int j = 0; j < phys_map[0].length; j++){
                if (phys_map[i][j] != 0){
                    g2.setColor(Color.GRAY);
                    g2.fillRect((int)getPosX() + j*tileSize, (int)getPosY() + i*tileSize, tileSize, tileSize);
                    g2.setColor(Color.BLACK);
                    g2.drawRect((int)getPosX() + j*tileSize, (int)getPosY() + i*tileSize, tileSize, tileSize);
                }
            }
        }
    }

    public Rectangle haveCollisionWithLand(Rectangle rect){
        int posX1 = rect.x/tileSize;
        posX1 -= 2;
        int posX2 = (rect.x + rect.width)/tileSize;
        posX2 += 2;

        int posY1 = (rect.y + rect.height)/tileSize;
        int posY2 = posY1 + 3;

        if (posX1 < 0) posX1 = 0;
        if (posX2 >= phys_map[0].length) posX2 = phys_map[0].length - 1;
        if (posY2 >= phys_map.length) posY2 = phys_map.length - 1;

        for (int y = posY1; y <= posY2; y++){
            for (int x = posX1; x <= posX2; x++){
                if (phys_map[y][x] == 1){
                    Rectangle r = new Rectangle((int)(getPosX() + x*tileSize), (int)(getPosY() + y*tileSize), tileSize, tileSize);
                    if (rect.intersects(r)){
                        return r;
                    }
                }
            }
        }
        return null;
    }

    public Rectangle haveCollisionWithTop(Rectangle rect){

        int posX1 = rect.x/tileSize;
        posX1 -= 2;
        int posX2 = (rect.x + rect.width)/tileSize;
        posX2 += 2;

        int posY2 = rect.y/tileSize;

        if(posX1 < 0) posX1 = 0;

        if(posX2 >= phys_map[0].length) posX2 = phys_map[0].length - 1;

        for(int y = posY2; y >= 0; y--){
            for(int x = posX1; x <= posX2; x++){

                if(phys_map[y][x] == 1){
                    Rectangle r = new Rectangle((int) getPosX() + x * tileSize, (int) getPosY() + y * tileSize, tileSize, tileSize);
                    if(rect.intersects(r))
                        return r;
                }
            }
        }
        return null;
    }

    public Rectangle haveCollisionWithRightWall(Rectangle rect){

        int posX1 = (rect.x + rect.width)/tileSize;
        int posX2 = posX1 + 3;
        posX1 -= 2;

        int posY1 = rect.y/tileSize;
        posY1 -= 2;
        int posY2 = (rect.y + rect.width)/tileSize;
        posY2 += 2;

        if (posX1 < 0) posX1 = 0;
        if (posX2 >= phys_map[0].length) posX2 = phys_map[0].length - 1;
        if (posY1 < 0) posY1 = 0;
        if (posY2 >= phys_map.length) posY2 = phys_map.length - 1;

        for (int x = posX1; x <= posX2; x++){
            for (int y = posY1; y <= posY2; y++){
                if(phys_map[y][x] == 1){
                    Rectangle r = new Rectangle((int)(getPosX() + x*tileSize), (int)(getPosY() + y*tileSize), tileSize, tileSize);
                    if (rect.intersects(r)) {
                        return r;
                    }
                }
            }
        }
        return null;
    }

    public Rectangle haveCollisionWithLeftWall(Rectangle rect){

        int posX2 = (rect.x)/tileSize;
        int posX1 = posX2 - 3;
        posX2 += 2;

        int posY1 = (rect.y)/tileSize;
        int posY2 = (rect.y + rect.height)/tileSize;

        if (posX2 >= phys_map[0].length) posX2 = phys_map[0].length - 1;
        if (posX1 < 0) posX1 = 0;
        if (posY2 >= phys_map.length) posY2 = phys_map.length - 1;

        for (int x = posX1; x <= posX2; x++){
            for (int y = posY1; y <= posY2; y++){
                if(phys_map[y][x] == 1){
                    Rectangle r = new Rectangle((int)(getPosX() + x*tileSize), (int)(getPosY() + y*tileSize), tileSize, tileSize);
                    if (rect.intersects(r)) {
                        return r;
                    }
                }
            }
        }
        return null;
    }
}
