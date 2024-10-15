package gameobjects.bullets;

import effect.Animation;
import effect.CacheDataLoader;
import gameobjects.GameWorld;

import java.awt.*;

public class RedEyeBullet extends Bullet{
    private Animation forwardBulletAnim;
    private Animation backwardBulletAnim;

    public RedEyeBullet(float posX, float posY, GameWorld gameWorld) {
        super(posX, posY, 30, 30, 1.0f, 10, gameWorld);

        backwardBulletAnim = CacheDataLoader.getInstance().getAnimation("redeyebullet");
        forwardBulletAnim = CacheDataLoader.getInstance().getAnimation("redeyebullet");
        forwardBulletAnim.flipAllImage();
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
            forwardBulletAnim.Update(System.nanoTime());
            forwardBulletAnim.draw(g2,(int)(getPosX() - getGameWorld().getCamera().getPosX()),
                    (int)(getPosY() - getGameWorld().getCamera().getPosY()));
        }
        else{
            backwardBulletAnim.Update(System.nanoTime());
            backwardBulletAnim.draw(g2,(int)(getPosX() - getGameWorld().getCamera().getPosX()),
                    (int)(getPosY() - getGameWorld().getCamera().getPosY()));
        }

        drawBoundForCollisionWithEnemy(g2);

    }

    @Override
    public void Update(){
        super.Update();
    }
}
