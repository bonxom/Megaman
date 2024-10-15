package gameobjects.bullets;

import gameobjects.GameWorld;
import gameobjects.ParticularObject;

import java.awt.*;

public abstract class Bullet extends ParticularObject {

    public Bullet(float posX, float posY, float width, float height, float mass, int damage, GameWorld gameWorld) {
        super(posX, posY, width, height, mass, 1, gameWorld);
        setDamage(damage);
    }

    public abstract void draw (Graphics2D g2);

    public void Update(){
        super.Update();
        setPosX(getPosX() + getSpeedX());
        setPosY(getPosY() + getSpeedY());

        ParticularObject object = getGameWorld().getParticularObjectManager().getCollisionWidthEnemyObject(this);
        if(object!=null && object.getState() == ALIVE){
            setBlood(0);
            object.beHurt(getDamage());
            System.out.println("Bullet set behurt for enemy");
        }
    }
}
