package gameobjects.gamecharacters;

import gameobjects.GameWorld;
import gameobjects.ParticularObject;

import java.awt.*;

public abstract class Human extends ParticularObject {
    private boolean isJumping;
    private boolean isDicking; //dang ngoi
    private boolean isLanding;

    public Human(float posX, float posY, float width, float height, float mass, int blood, GameWorld gameWorld) {
        super(posX, posY, width, height, mass, blood, gameWorld);
        setState(ALIVE);
    }

    public abstract void run();
    public abstract void jump();
    public abstract void dick();
    public abstract void standUp();
    public abstract void stopRun();

    public boolean getIsJumping() {
        return isJumping;
    }

    public void setIsJumping(boolean jumping) {
        isJumping = jumping;
    }

    public boolean getIsDicking() {
        return isDicking;
    }

    public void setIsDicking(boolean dicking) {
        isDicking = dicking;
    }

    public boolean getIsLanding() {
        return isLanding;
    }

    public void setIsLanding(boolean landing) {
        isLanding = landing;
    }

    @Override
    public void Update(){
        super.Update();
        if (getState() == ALIVE || getState() == IMMUNITY){
            if (!isLanding){
                setPosX(getPosX() + getSpeedX());
                if (getDirection() == LEFT_DIR &&
                        getGameWorld().getPhysicalMap().haveCollisionWithLeftWall(getBoundForCollisionWithMap()) != null){
                    Rectangle rectLeft = getGameWorld().getPhysicalMap().haveCollisionWithLeftWall(getBoundForCollisionWithMap());
                    setPosX(rectLeft.x + rectLeft.width + getWidth()/2);
                }

                if (getDirection() == RIGHT_DIR &&
                        getGameWorld().getPhysicalMap().haveCollisionWithRightWall(getBoundForCollisionWithMap()) != null){
                    Rectangle rectRight = getGameWorld().getPhysicalMap().haveCollisionWithRightWall(getBoundForCollisionWithMap());
                    setPosX(rectRight.x - getWidth()/2);
                }


                Rectangle boundForCollisionWithMapFuture = getBoundForCollisionWithMap();
                if (getSpeedY() != 0){
                    boundForCollisionWithMapFuture.y += (int)getSpeedY();
                }
                else{
                    boundForCollisionWithMapFuture.y += 2;
                }

                Rectangle rectLand = getGameWorld().getPhysicalMap().haveCollisionWithLand(boundForCollisionWithMapFuture);
                Rectangle rectTop = getGameWorld().getPhysicalMap().haveCollisionWithTop(boundForCollisionWithMapFuture);

                if (rectTop != null){
                    setSpeedY(0);
                    setPosY(rectTop.y + rectTop.height + getHeight()/2);
                }
                else if (rectLand != null){
                    setIsJumping(false);
                    if (getSpeedY() > 0) setIsLanding(true);
                    setSpeedY(0);
                    setPosY(rectLand.y - getHeight()/2 - 1);
                }
                else{
                    setIsJumping(true);
                    setSpeedY(getSpeedY() + getMass());
                    setPosY(getPosY() + getSpeedY());
                }
            }
        }
    }
}
