package gameobjects.bullets;

import effect.Animation;
import effect.CacheDataLoader;
import gameobjects.GameWorld;

import java.awt.*;

public class YellowBullet extends Bullet{
    private Animation forwardAnim;
    private Animation backwardAnim;

    public YellowBullet(float posX, float posY, GameWorld gameWorld) {
        super(posX, posY, 30, 30, 0.1f, 10, gameWorld);

        forwardAnim = CacheDataLoader.getInstance().getAnimation("yellow_flower_bullet");
        backwardAnim = CacheDataLoader.getInstance().getAnimation("yellow_flower_bullet");
        backwardAnim.flipAllImage();
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
            forwardAnim.Update(System.nanoTime());
            forwardAnim.draw(g2,(int)(getPosX() - getGameWorld().getCamera().getPosX()),
                    (int)(getPosY() - getGameWorld().getCamera().getPosY()));
        }
        else{
            backwardAnim.Update(System.nanoTime());
            backwardAnim.draw(g2,(int)(getPosX() - getGameWorld().getCamera().getPosX()),
                    (int)(getPosY() - getGameWorld().getCamera().getPosY()));
        }

        drawBoundForCollisionWithEnemy(g2);

    }

    @Override
    public void Update(){
        super.Update();
    }
}
