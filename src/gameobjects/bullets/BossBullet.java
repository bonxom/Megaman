package gameobjects.bullets;

import effect.Animation;
import effect.CacheDataLoader;
import gameobjects.GameWorld;

import java.awt.*;

public class BossBullet extends Bullet{

    private Animation forwardAnim, forwardUpAnim, forwardDownAnim;
    private Animation backwardAnim, backwardUpAnim, backwardDownAnim;

    private long startTimeChangeSpeedY;

    public BossBullet(float posX, float posY, GameWorld gameWorld) {
        super(posX, posY, 30, 30, 1.0f, 10, gameWorld);

        backwardAnim = CacheDataLoader.getInstance().getAnimation("rocket");
        backwardUpAnim = CacheDataLoader.getInstance().getAnimation("rocketUp");
        backwardDownAnim = CacheDataLoader.getInstance().getAnimation("rocketDown");

        forwardAnim = CacheDataLoader.getInstance().getAnimation("rocket");
        forwardAnim.flipAllImage();
        forwardUpAnim = CacheDataLoader.getInstance().getAnimation("rocketUp");
        forwardUpAnim.flipAllImage();
        forwardDownAnim = CacheDataLoader.getInstance().getAnimation("rocketDown");
        forwardDownAnim.flipAllImage();
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
            if (getSpeedY() > 0){
                forwardDownAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
            }
            else if (getSpeedY() < 0){
                forwardUpAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
            }
            else{
                forwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
            }
        }

        else{
            if (getSpeedY() > 0){
                backwardDownAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
            }
            else if (getSpeedY() < 0){
                backwardUpAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
            }
            else{
                backwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
            }
        }
    }

    private void changeSpeedY(){
        if (System.nanoTime() % 3 == 0){
            setSpeedY(getSpeedX());
        }
        else if (System.nanoTime() % 3 == 1){
            setSpeedY(-getSpeedX());
        }
        else setSpeedY(0);
    }

    @Override
    public void Update(){
        super.Update();

        if (System.nanoTime() - startTimeChangeSpeedY > 500*1000000){
            startTimeChangeSpeedY = System.nanoTime();
            changeSpeedY();
        }
    }
}
