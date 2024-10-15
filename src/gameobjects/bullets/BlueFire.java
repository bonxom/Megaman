package gameobjects.bullets;

import effect.Animation;
import effect.CacheDataLoader;
import gameobjects.GameWorld;

import java.awt.*;

public class BlueFire extends Bullet{

    private Animation forwardBulletAnim;
    private Animation backwardBulletAnim;

    public BlueFire(float posX, float posY,GameWorld gameWorld) {
        super(posX, posY, 60, 30, 1.0f, 10, gameWorld);
        forwardBulletAnim = CacheDataLoader.getInstance().getAnimation("bluefire");
        backwardBulletAnim = CacheDataLoader.getInstance().getAnimation("bluefire");
        backwardBulletAnim.flipAllImage();
    }

    @Override
    public void attack() {

    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return getBoundForCollisionWithMap();
    }

    @Override
    public void draw(Graphics2D g2) {
        if (getSpeedX() > 0){
            if (!forwardBulletAnim.isIgnoreFrame(0) && forwardBulletAnim.getCurrentFrame() == 3){ //start shooting state
                forwardBulletAnim.setIgnoreFrame(0);
                forwardBulletAnim.setIgnoreFrame(1);
                forwardBulletAnim.setIgnoreFrame(2);
            }

            forwardBulletAnim.Update(System.nanoTime());
            forwardBulletAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                    (int)(getPosY() - getGameWorld().getCamera().getPosY()));
        }
        else{
            if (!backwardBulletAnim.isIgnoreFrame(0) && backwardBulletAnim.getCurrentFrame() == 3){ //start shooting state
                backwardBulletAnim.setIgnoreFrame(0);
                backwardBulletAnim.setIgnoreFrame(1);
                backwardBulletAnim.setIgnoreFrame(2);
            }

            backwardBulletAnim.Update(System.nanoTime());
            backwardBulletAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                    (int)(getPosY() - getGameWorld().getCamera().getPosY()));
        }
    }

    @Override
    public void Update(){
        if (forwardBulletAnim.isIgnoreFrame(0) || backwardBulletAnim.isIgnoreFrame(0)){
            setPosX(getPosX() + getSpeedX());
        }
    }
}
