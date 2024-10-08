package gameobjects;

import effect.Animation;

import java.awt.*;

public abstract class ParticularObject extends GameObject {

    public static final int PLAYER = 1;
    public static final int ENEMY = 2;

    public static final int LEFT_DIR = 0;
    public static final int RIGHT_DIR = 1;

    public static final int ALIVE = 0;
    public static final int BEHURT = 1;
    public static final int FEY = 2;//gan toang
    public static final int DEATH = 3;
    public static final int IMMUNITY = 4;

    private int state = ALIVE;

    private float width;
    private float height;
    private float mass;
    private float speedX;
    private float speedY;
    private float blood;

    private int damage;
    private int direction;

    protected Animation behurtBeforeAnim, behurtAfterAnim;

    private int teamType;

    private long startImmunity;
    private long timeImmunity;

    public ParticularObject(float posX, float posY, float width, float height, float mass, int blood, GameWorld gameWorld) {
        super(posX, posY, gameWorld);
        this.width = width;
        this.height = height;
        this.mass = mass;
        this.blood = blood;

        direction = RIGHT_DIR;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getBlood() {
        return blood;
    }

    public void setBlood(float blood) {
        this.blood = blood;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getTeamType() {
        return teamType;
    }

    public void setTeamType(int teamType) {
        this.teamType = teamType;
    }

    public long getTimeImmunity() {
        return timeImmunity;
    }

    public void setTimeImmunity(long timeImmunity) {
        this.timeImmunity = timeImmunity;
    }

    public long getStartImmunity() {
        return startImmunity;
    }

    public void setStartImmunity(long startImmunity) {
        this.startImmunity = startImmunity;
    }


    public Rectangle getBoundForCollisionWithMap() {
        Rectangle bound = new Rectangle();
        bound.x = (int) (getPosX() - width / 2);
        bound.y = (int) (getPosX() - height / 2);
        bound.width = (int) width;
        bound.height = (int) height;
        return bound;
    }

    public void beHurt(int damageGet) {
        setBlood(getBlood() - damageGet);
        state = BEHURT;
        hurtingCallback();
    }

    @Override
    public void Update() {
        switch (state) {
            case ALIVE:

                break;
            case BEHURT:
                if (behurtAfterAnim == null) {//ko co animation cho trang thai bi tan cong
                    state = IMMUNITY;
                    startImmunity = System.nanoTime();
                    if (getBlood() == 0) state = FEY;
                } else {
                    behurtAfterAnim.Update(System.nanoTime());
                    if (behurtBeforeAnim.isLastFrame()) {
                        behurtBeforeAnim.reset();
                        state = IMMUNITY;
                        startImmunity = System.nanoTime();
                        if (getBlood() == 0) state = FEY;
                    }
                }
                break;
            case FEY:// gan chet
                state = DEATH;
                break;
            case DEATH:
                break;
            case IMMUNITY:
                System.out.printf("state = immunity");
                if (System.nanoTime() - startImmunity > timeImmunity) state = ALIVE;
                break;
        }
    }

    public void drawBoundForCollisionWithMap(Graphics2D g2) {
        Rectangle rect = getBoundForCollisionWithMap();
        g2.setColor(Color.BLUE);
        g2.drawRect(rect.x - (int)getGameWorld().getCamera().getPosX(),
                rect.y - (int)getGameWorld().getCamera().getPosY(), rect.width, rect.height);
    }

    public void drawBoundForCollisionWithEnemy(Graphics2D g2) {
        Rectangle rect = getBoundForCollisionWithEnemy();
        g2.setColor(Color.GREEN);
        g2.drawRect(rect.x - (int)getGameWorld().getCamera().getPosX(),
                rect.y - (int)getGameWorld().getCamera().getPosY(), rect.width, rect.height);
    }

    public abstract void attack();

    public abstract Rectangle getBoundForCollisionWithEnemy();

    public abstract void draw(Graphics2D g2);

    public void hurtingCallback() {}
}
