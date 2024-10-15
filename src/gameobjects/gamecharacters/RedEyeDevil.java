package gameobjects.gamecharacters;

import effect.Animation;
import effect.CacheDataLoader;
import gameobjects.GameWorld;
import gameobjects.ParticularObject;
import gameobjects.bullets.Bullet;
import gameobjects.bullets.RedEyeBullet;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RedEyeDevil extends ParticularObject {

    private Animation forwardAnim;
    private Animation backwardAnim;
    private Clip shooting;
    private long startTimeToShoot;

    public RedEyeDevil(float posX, float posY, GameWorld gameWorld) {
        super(posX, posY, 127, 89, 0, 100, gameWorld);

        setDamage(10);
        setTimeImmunity(300000000);
        startTimeToShoot = 0;

        backwardAnim = CacheDataLoader.getInstance().getAnimation("redeye");
        forwardAnim = CacheDataLoader.getInstance().getAnimation("redeye");
        forwardAnim.flipAllImage();
        shooting = CacheDataLoader.getInstance().getSound("redeyeshooting");
    }

    @Override
    public void attack() {
        shooting.start();
        shooting.setFramePosition(0);

        Bullet bullet = new RedEyeBullet(getPosX(), getPosY(), getGameWorld());
        if (getDirection() == LEFT_DIR) bullet.setSpeedX(-8);
        else bullet.setSpeedX(8);
        bullet.setTeamType(getTeamType());
        getGameWorld().getBulletManger().addObject(bullet);
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        Rectangle rect = getBoundForCollisionWithMap();
        rect.x += 20;
        rect.width -= 40;
        return rect;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!isObjectOutOfCameraView()){
            if (getState() == IMMUNITY && (System.nanoTime()/10000000) % 2 != 1){
                //Can attack red eye
            }
            else{
                if (getDirection() == LEFT_DIR){
                    backwardAnim.Update(System.nanoTime());
                    backwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                            (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                }
                else{
                    if (getDirection() == LEFT_DIR) {
                        forwardAnim.Update(System.nanoTime());
                        forwardAnim.draw(g2, (int) (getPosX() - getGameWorld().getCamera().getPosX()),
                                (int) (getPosY() - getGameWorld().getCamera().getPosY()));
                    }
                }
            }
        }
    }

    public void Update(){
        super.Update();
        if (System.nanoTime() - startTimeToShoot > 2000000000){
            attack();
            System.out.println("Red Eye Attack");
            startTimeToShoot = System.nanoTime();
        }
    }
}
