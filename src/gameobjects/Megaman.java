package gameobjects;

import effect.Animation;
import effect.CacheDataLoader;

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

//    private AudioClip hurtingSound;
//    private AudioClip shooting1;

    private Animation anim;


    public Megaman(float posX, float posY, GameWorld gameWorld) {
        super(posX, posY, 70, 90, 0.1f, 100, gameWorld);

//        shooting1 = CacheDataLoader.getInstance().getSound("bluefireshooting");
//        hurtingSound = CacheDataLoader.getInstance().getSound("megamanhurt");

        setTeamType(PLAYER);
        setTimeImmunity(2000*100000);

        runForwardAnim = CacheDataLoader.getInstance().getAnimation("run");
        runBackwardAnim = CacheDataLoader.getInstance().getAnimation("run");
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
        flyShootingForwardAnim.flipAllImage();

        landingForwardAnim = CacheDataLoader.getInstance().getAnimation("landing");
        landingBackwardAnim = CacheDataLoader.getInstance().getAnimation("landing");
        landingBackwardAnim.flipAllImage();

        climbForwardAnim = CacheDataLoader.getInstance().getAnimation("clim_wall");
        climbBackwardAnim = CacheDataLoader.getInstance().getAnimation("clim_wall");
        climbBackwardAnim.flipAllImage();

        anim = CacheDataLoader.getInstance().getAnimation("run");
        anim.flipAllImage();
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
    /*
        switch (getState()){
            case ALIVE:
            case IMMUNITY:
                if ((System.nanoTime()/100000000) % 2 != 1){
                    System.out.println("Plash....");
                }
                else{
                    if (getIsLanding()){
                        if (getDirection() == RIGHT_DIR){
                            landingForwardAnim.setCurrentFrame(landingBackwardAnim.getCurrentFrame());
                            landingForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                    (int)(getPosY() - getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - landingForwardAnim.getCurrentImage().getHeight()/2)));
                        }
                        else{
                            landingBackwardAnim.draw((int)(getPosX() - getGameWorld().camera.getPosX()),
                                    (int)(getPosY() - getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - landingBackwardAnim.getCurrentImage().getHeight()/2)));
                        }
                    }

                    else if (getIsJumping()){
                        if (getDirection() == RIGHT_DIR){
                            flyForwardAnim.Update(System.nanoTime());
                            if (isShooting){
                                flyShootingForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX() + 10),
                                        (int)(getPosY() - getGameWorld().camera.getPosY()));
                            }
                            else{
                                flyForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                        (int)(getPosY() - getGameWorld().camera.getPosY()));
                            }
                        }
                        else{
                            flyBackwardAnim.Update(System.nanoTime());
                            if (isShooting){
                                flyShootingBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX() - 10),
                                        (int)(getPosY() - getGameWorld().camera.getPosY()));
                            }
                            else{
                                flyBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                        (int)(getPosY() - getGameWorld().camera.getPosY()));
                            }
                        }
                    }

                    else if (getIsDicking()){
                        if (getDirection() == RIGHT_DIR){
                            dickForwardAnim.Update(System.nanoTime());
                            dickForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - dickForwardAnim.getCurrentImage().getHeight()/2));
                        }
                        else{
                            dickBackwardAnim.Update(System.nanoTime());
                            dickBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - dickBackwardAnim.getCurrentImage().getHeight()/2));
                        }
                    }
                    else{
                        if (getSpeedX() > 0){
                            runForwardAnim.Update(System.nanoTime());
                            if (isShooting){
                                runShootingForwardAnim.setCurrentFrame(runForwardAnim.getCurrentFrame() - 1);
                                runShootingForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                        (int)(getPosY() - getGameWorld().camera.getPosY()));
                            }
                            else{
                                runForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                        (int)(getPosY() - getGameWorld().camera.getPosY()));
                            }
                            if (runForwardAnim.getCurrentFrame() == 1) runForwardAnim.setIgnoreFrame(0);
                        }

                        else if (getSpeedX() < 0){
                            runBackwardAnim.Update(System.nanoTime());
                            if (isShooting){
                                runShootingBackwardAnim.setCurrentFrame(runShootingBackwardAnim.getCurrentFrame() - 1);
                                runShootingBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                        (int)(getPosY() - getGameWorld().camera.getPosY()));
                            }
                            else{
                                runShootingBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                        (int)(getPosY() - getGameWorld().camera.getPosY()));
                            }
                            if (runShootingBackwardAnim.getCurrentFrame() == 1) runShootingBackwardAnim.setIgnoreFrame(0);
                        }

                        else{
                            if (getDirection() == RIGHT_DIR){
                                if (isShooting){
                                    idleShootingForwardAnim.Update(System.nanoTime());
                                    idleShootingForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                            (int)(getPosY() - getGameWorld().camera.getPosY));
                                }
                                else{
                                    idleForwardAnim.Update(System.nanoTime());
                                    idleForwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                            (int)(getPosY() - getGameWorld().camera.getPosY));
                                }
                            }
                            else{
                                if (isShooting){
                                    idleShootingBackwardAnim.Update(System.nanoTime());
                                    idleShootingBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                            (int)(getPosY() - getGameWorld().camera.getPosY));
                                }
                                else{
                                    idleBackwardAnim.Update(System.nanoTime());
                                    idleBackwardAnim.draw(g2, (int)(getPosX() - getGameWorld().camera.getPosX()),
                                            (int)(getPosY() - getGameWorld().camera.getPosY));
                                }
                            }
                        }
                    }
                }

                break;
            case BEHURT:
            case FEY:
            case DEATH:
        }
        */

        g2.setColor(Color.RED);
        g2.fillRect((int)(getPosX() - getWidth()/2), (int)(getPosY() - getHeight()/2), (int)getWidth(), (int)getHeight());
        g2.setColor(Color.BLACK);
        g2.fillRect((int)getPosX(), (int)getPosY(), 2, 2);


        anim.draw(g2, (int)(getPosX()), (int)(getPosY()));
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
            setSpeedY(-5.0f);
            flyBackwardAnim.reset();
            flyForwardAnim.reset();
        }
        else{//climb wall
            Rectangle rectRight = getBoundForCollisionWithMap();
            Rectangle rectLeft = getBoundForCollisionWithMap();
            rectRight.x += 1;
            rectLeft.x -= 1;
            if (getGameWorld().getPhysicalMap().haveCollisionWithRightWall(rectRight) != null && getSpeedX() > 0){
                setSpeedY(-5.0f);
                flyBackwardAnim.reset();
                flyForwardAnim.reset();
            }
            else if (getGameWorld().getPhysicalMap().haveCollisionWithLeftWall(rectLeft) != null && getSpeedX() < 0){
                setSpeedY(-5.0f);
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

    }

    @Override
    public void hurtingCallback() {}
}
