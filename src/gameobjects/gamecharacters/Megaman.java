package gameobjects.gamecharacters;

import effect.Animation;
import effect.CacheDataLoader;
import gameobjects.GameWorld;
import gameobjects.bullets.BlueFire;
import gameobjects.bullets.Bullet;
import gameobjects.bullets.UltimateShoot;
import gameobjects.bullets.YellowBullet;

import javax.sound.sampled.Clip;
import java.awt.*;

public class Megaman extends Human {

    public static final int RUNSPEED = 3;

    private Animation runForwardAnim, runBackwardAnim, runShootingForwardAnim, runShootingBackwardAnim;
    private Animation idleForwardAnim, idleBackwardAnim, idleShootingForwardAnim, idleShootingBackwardAnim;
    private Animation dickForwardAnim, dickBackwardAnim;
    private Animation flyForwardAnim, flyBackwardAnim, flyShootingForwardAnim, flyShootingBackwardAnim;
    private Animation landingForwardAnim, landingBackwardAnim;
    private Animation climbForwardAnim, climbBackwardAnim;

    private long lastShootingTime;
    private boolean isShooting = false;

    private Clip hurtingSound;
    private Clip shooting1;

    public Megaman(float posX, float posY, GameWorld gameWorld) {
        super(posX, posY, 70, 90, 0.3f, 100, gameWorld);

        shooting1 = CacheDataLoader.getInstance().getSound("bluefireshooting");
        hurtingSound = CacheDataLoader.getInstance().getSound("megamanhurt");

        setTeamType(PLAYER);
        setTimeImmunity(2000*100000);

        runForwardAnim = CacheDataLoader.getInstance().getAnimation("run");
        runBackwardAnim = new Animation(runForwardAnim);
        runBackwardAnim.flipAllImage();

        idleForwardAnim = CacheDataLoader.getInstance().getAnimation("idle");
        idleBackwardAnim = CacheDataLoader.getInstance().getAnimation("idle");
        idleBackwardAnim.flipAllImage();

        runShootingForwardAnim = CacheDataLoader.getInstance().getAnimation("runshoot");
        runShootingBackwardAnim = CacheDataLoader.getInstance().getAnimation("runshoot");
        runShootingBackwardAnim.flipAllImage();

        idleShootingForwardAnim = CacheDataLoader.getInstance().getAnimation("idleshoot");
        idleShootingBackwardAnim = CacheDataLoader.getInstance().getAnimation("idleshoot");
        idleShootingBackwardAnim.flipAllImage();

        dickForwardAnim = CacheDataLoader.getInstance().getAnimation("dick");
        dickBackwardAnim = CacheDataLoader.getInstance().getAnimation("dick");
        dickBackwardAnim.flipAllImage();

        flyForwardAnim = CacheDataLoader.getInstance().getAnimation("flyingup");
        flyBackwardAnim = CacheDataLoader.getInstance().getAnimation("flyingup");
        flyBackwardAnim.flipAllImage();

        flyShootingForwardAnim = CacheDataLoader.getInstance().getAnimation("flyingupshoot");
        flyShootingBackwardAnim = CacheDataLoader.getInstance().getAnimation("flyingupshoot");
        flyShootingBackwardAnim.flipAllImage();

        landingForwardAnim = CacheDataLoader.getInstance().getAnimation("landing");
        landingBackwardAnim = CacheDataLoader.getInstance().getAnimation("landing");
        landingBackwardAnim.flipAllImage();

        climbForwardAnim = CacheDataLoader.getInstance().getAnimation("clim_wall");
        climbBackwardAnim = CacheDataLoader.getInstance().getAnimation("clim_wall");
        climbBackwardAnim.flipAllImage();

        behurtBeforeAnim = CacheDataLoader.getInstance().getAnimation("hurting");
        behurtAfterAnim = CacheDataLoader.getInstance().getAnimation("hurting");
        behurtAfterAnim.flipAllImage();

    }




    public Rectangle getBoundForCollisionWithMap(){
        Rectangle bound = new Rectangle();
        bound.x = (int) (getPosX() - getWidth()/2);
        bound.y = (int) (getPosY() - getHeight()/2);
        bound.width = (int) getWidth();
        bound.height = (int) getHeight();
        return bound;
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        Rectangle rect = getBoundForCollisionWithMap();

        if (getIsDicking()){
            rect.x = (int)getPosX() - 22;
            rect.y = (int)getPosY() - 20;
            rect.width = 44;
            rect.height = 65;
        }
        else{
            rect.x = (int) getPosX() - 22;
            rect.y = (int) getPosY() - 40;
            rect.width = 44;
            rect.height = 80;
        }
        return rect;
    }


    public void draw(Graphics2D g2){

        switch (getState()){
            case ALIVE:
            case IMMUNITY:
                if (getState() == IMMUNITY && (System.nanoTime()/100000000) % 2 != 1){
                    System.out.println("Plash....");
                }
                else{
                    if (getIsLanding()){
                        if (getDirection() == RIGHT_DIR){
                            landingForwardAnim.setCurrentFrame(landingBackwardAnim.getCurrentFrame());
                            landingForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                    (int)(getPosY() - getGameWorld().getCamera().getPosY() + (getBoundForCollisionWithMap().height/2 - landingForwardAnim.getCurrentImage().getHeight()/2)));
                        }
                        else{
                            landingBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                    (int)(getPosY() - getGameWorld().getCamera().getPosY() + (getBoundForCollisionWithMap().height/2 - landingBackwardAnim.getCurrentImage().getHeight()/2)));
                        }
                    }

                    else if (getIsJumping()){
                        if (getDirection() == RIGHT_DIR){
                            flyForwardAnim.Update(System.nanoTime());
                            if (isShooting){
                                flyShootingForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX() + 10),
                                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                            }
                            else{
                                flyForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                            }
                        }
                        else{
                            flyBackwardAnim.Update(System.nanoTime());
                            if (isShooting){
                                flyShootingBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX() - 10),
                                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                            }
                            else{
                                flyBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                            }
                        }
                    }

                    else if (getIsDicking()){
                        if (getDirection() == RIGHT_DIR){
                            dickForwardAnim.Update(System.nanoTime());
                            dickForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                    (int) getPosY() - (int) getGameWorld().getCamera().getPosY() + (getBoundForCollisionWithMap().height/2 - dickForwardAnim.getCurrentImage().getHeight()/2));
                        }
                        else{
                            dickBackwardAnim.Update(System.nanoTime());
                            dickBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                    (int) getPosY() - (int) getGameWorld().getCamera().getPosY() + (getBoundForCollisionWithMap().height/2 - dickBackwardAnim.getCurrentImage().getHeight()/2));
                        }
                    }
                    else{
                        if (getSpeedX() > 0){
                            runForwardAnim.Update(System.nanoTime());
                            if (isShooting){
                                runShootingForwardAnim.setCurrentFrame(runForwardAnim.getCurrentFrame() - 1);
                                runShootingForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                            }
                            else{
                                runForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                            }
                            if (runForwardAnim.getCurrentFrame() == 1) runForwardAnim.setIgnoreFrame(0);
                        }

                        else if (getSpeedX() < 0){
                            runBackwardAnim.Update(System.nanoTime());
                            if (isShooting){
                                runShootingBackwardAnim.setCurrentFrame(runShootingBackwardAnim.getCurrentFrame() - 1);
                                runShootingBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                            }
                            else{
                                runBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                        (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                            }
                            if (runBackwardAnim.getCurrentFrame() == 1) runBackwardAnim.setIgnoreFrame(0);
                        }

                        else{
                            if (getDirection() == RIGHT_DIR){
                                if (isShooting){
                                    idleShootingForwardAnim.Update(System.nanoTime());
                                    idleShootingForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                            (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                                }
                                else{
                                    idleForwardAnim.Update(System.nanoTime());
                                    idleForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                            (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                                }
                            }
                            else{
                                if (isShooting){
                                    idleShootingBackwardAnim.Update(System.nanoTime());
                                    idleShootingBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                            (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                                }
                                else{
                                    idleBackwardAnim.Update(System.nanoTime());
                                    idleBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                                            (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                                }
                            }
                        }
                    }
                }

                break;
            case BEHURT:
                if (getDirection() == RIGHT_DIR){
                    behurtBeforeAnim.Update(System.nanoTime());
                    behurtBeforeAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                            (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                }
                else{
                    behurtAfterAnim.Update(System.nanoTime());
                    //behurtAfterAnim.setCurrentFrame(behurtBeforeAnim.getCurrentFrame());
                    behurtAfterAnim.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                            (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                }
                break;
            case FEY:
            case DEATH:
                break;
        }





        drawBoundForCollisionWithMap(g2);
        drawBoundForCollisionWithEnemy(g2);


    }


    @Override
    public void Update() {

        super.Update();

        if(isShooting){
            if(System.nanoTime() - lastShootingTime > 500*1000000){
                isShooting = false;
            }
        }

        if(getIsLanding()){
            landingBackwardAnim.Update(System.nanoTime());
            if(landingBackwardAnim.isLastFrame()) {
                setIsLanding(false);
                landingBackwardAnim.reset();
                runForwardAnim.reset();
                runBackwardAnim.reset();
            }
        }

    }

    @Override
    public void run() {
        if (getDirection() == LEFT_DIR){
            setSpeedX(-3);
        }
        else setSpeedX(3);
    }

    @Override
    public void jump() {
        if (!getIsJumping()){
            setIsJumping(true);
            setSpeedY(-10.0f);
            flyBackwardAnim.reset();
            flyForwardAnim.reset();
        }
        else{//climb wall
            Rectangle rectRight = getBoundForCollisionWithMap();
            Rectangle rectLeft = getBoundForCollisionWithMap();
            rectRight.x += 1;
            rectLeft.x -= 1;
            if (getGameWorld().getPhysicalMap().haveCollisionWithRightWall(rectRight) != null && getSpeedX() > 0){
                setSpeedY(-10.0f);
                flyBackwardAnim.reset();
                flyForwardAnim.reset();
            }
            else if (getGameWorld().getPhysicalMap().haveCollisionWithLeftWall(rectLeft) != null && getSpeedX() < 0){
                setSpeedY(-10.0f);
                flyBackwardAnim.reset();
                flyForwardAnim.reset();
            }
        }
    }

    @Override
    public void dick() {
        if (!getIsJumping()) setIsDicking(true);
    }

    @Override
    public void standUp() {
        setIsDicking(false);
        idleForwardAnim.reset();
        idleBackwardAnim.reset();
        dickForwardAnim.reset();
        dickBackwardAnim.reset();
    }

    @Override
    public void stopRun() {
        setSpeedX(0);
        runForwardAnim.reset();
        runBackwardAnim.reset();
        runForwardAnim.unIgnoreFrame(0);
        runBackwardAnim.unIgnoreFrame(0);
    }

    @Override
    public void attack() {
        if (!isShooting && !getIsDicking()){
            shooting1.start();
            shooting1.setFramePosition(0);

            Bullet bullet = new BlueFire(getPosX(), getPosY(), getGameWorld());
            if (getDirection() == RIGHT_DIR){
                bullet.setSpeedX(10);
                bullet.setPosX(bullet.getPosX() + 40);
                if (getSpeedX() != 0 && getSpeedY() == 0){
                    bullet.setPosX(bullet.getPosX() + 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }
            else{
                bullet.setSpeedX(-10);
                bullet.setPosX(bullet.getPosX() - 40);
                if (getSpeedX() != 0 && getSpeedY() == 0){
                    bullet.setPosX(bullet.getPosX() - 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }
            if (getIsJumping()){
                bullet.setPosY(bullet.getPosY() - 20);
            }

            bullet.setTeamType(getTeamType());
            getGameWorld().getBulletManger().addObject(bullet);

            lastShootingTime = System.nanoTime();
            isShooting = true;
        }
    }

    public void attack1() {
        if (!isShooting && !getIsDicking()){
            shooting1.start();
            shooting1.setFramePosition(0);

            Bullet bullet = new YellowBullet(getPosX(), getPosY(), getGameWorld());
            if (getDirection() == RIGHT_DIR){
                bullet.setSpeedX(10);
                bullet.setPosX(bullet.getPosX() + 40);
                if (getSpeedX() != 0 && getSpeedY() == 0){
                    bullet.setPosX(bullet.getPosX() + 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }
            else{
                bullet.setSpeedX(-10);
                bullet.setPosX(bullet.getPosX() - 40);
                if (getSpeedX() != 0 && getSpeedY() == 0){
                    bullet.setPosX(bullet.getPosX() - 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }
            if (getIsJumping()){
                bullet.setPosY(bullet.getPosY() - 20);
            }

            bullet.setTeamType(getTeamType());
            getGameWorld().getBulletManger().addObject(bullet);

            lastShootingTime = System.nanoTime();
            isShooting = true;
        }
    }

    public void attack2() {
        if (!isShooting && !getIsDicking()){
            shooting1.start();
            shooting1.setFramePosition(0);

            Bullet bullet = new UltimateShoot(getPosX(), getPosY(), getGameWorld());
            if (getDirection() == RIGHT_DIR){
                bullet.setSpeedX(10);
                bullet.setPosX(bullet.getPosX() + 40);
                if (getSpeedX() != 0 && getSpeedY() == 0){
                    bullet.setPosX(bullet.getPosX() + 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }
            else{
                bullet.setSpeedX(-10);
                bullet.setPosX(bullet.getPosX() - 40);
                if (getSpeedX() != 0 && getSpeedY() == 0){
                    bullet.setPosX(bullet.getPosX() - 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }
            if (getIsJumping()){
                bullet.setPosY(bullet.getPosY() - 20);
            }

            bullet.setTeamType(getTeamType());
            getGameWorld().getBulletManger().addObject(bullet);

            lastShootingTime = System.nanoTime();
            isShooting = true;
        }
    }

    @Override
    public void hurtingCallback() {
        System.out.println("is Hurting");
        hurtingSound.start();
        hurtingSound.setFramePosition(0);
    }
}
