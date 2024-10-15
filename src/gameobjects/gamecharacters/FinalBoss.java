package gameobjects.gamecharacters;

import effect.Animation;
import effect.CacheDataLoader;
import gameobjects.GameWorld;
import gameobjects.bullets.BossBullet;
import gameobjects.bullets.Bullet;

import java.awt.*;
import java.util.Hashtable;

public class FinalBoss extends Human{
    private Animation idleForward, idleBackward;
    private Animation shootingFoward, shootingBackward;
    private Animation slideForward, slideBackward;

    private long startTimeToAttack;

    private Hashtable<String, Long> timeAttack = new Hashtable<String, Long>();
    private String[] attackType = new String[4];
    private int attackIndex = 0;
    private long lastAttackTime;

    public FinalBoss(float posX, float posY, GameWorld gameWorld) {
        super(posX, posY, 110, 150, 0.1f, 100, gameWorld);

        idleBackward = CacheDataLoader.getInstance().getAnimation("boss_idle");
        idleForward = CacheDataLoader.getInstance().getAnimation("boss_idle");
        idleForward.flipAllImage();

        shootingBackward = CacheDataLoader.getInstance().getAnimation("boss_shooting");
        shootingFoward = CacheDataLoader.getInstance().getAnimation("boss_shooting");
        shootingFoward.flipAllImage();

        slideBackward = CacheDataLoader.getInstance().getAnimation("boss_slide");
        slideForward = CacheDataLoader.getInstance().getAnimation("boss_slide");
        slideForward.flipAllImage();

        setTimeImmunity(500*1000000);
        setDamage(10);

        attackType[0] = "NONE";
        attackType[1] = "shooting";
        attackType[2] = "NONE";
        attackType[3] = "slide";

        timeAttack.put("NONE", Long.valueOf(2000));
        timeAttack.put("shooting", Long.valueOf(500));
        timeAttack.put("slide", Long.valueOf(5000));
    }

    @Override
    public void Update(){
        super.Update();

        if (getGameWorld().getMegaman().getPosX() > getPosX()){
            setDirection(RIGHT_DIR);
        }
        else setDirection(LEFT_DIR);

        if (startTimeToAttack == 0){
            startTimeToAttack = System.currentTimeMillis();
        }
        else if (System.currentTimeMillis() - startTimeToAttack > 300){
            attack();
            startTimeToAttack = System.currentTimeMillis();
        }

        if (!attackType[attackIndex].equals("NONE")){
            if (attackType[attackIndex].equals("shooting")){

                Bullet bullet = new BossBullet(getPosX(), getPosY() - 50, getGameWorld());
                if (getDirection() == LEFT_DIR) bullet.setSpeedX(-4);
                else bullet.setSpeedX(4);
                bullet.setTeamType(getTeamType());
                getGameWorld().getBulletManger().addObject(bullet);
            }
            else if (attackType[attackIndex].equals("slide")){

                if (getGameWorld().getPhysicalMap().haveCollisionWithRightWall(getBoundForCollisionWithMap()) != null){
                    setSpeedX(-5);
                }
                else if (getGameWorld().getPhysicalMap().haveCollisionWithLeftWall(getBoundForCollisionWithMap()) != null){
                    setSpeedX(5);
                }

                setPosX(getPosX() + getSpeedX());
            }
        }
        else setSpeedX(0);
    }

    @Override
    public void run() {

    }

    @Override
    public void jump() {

    }

    @Override
    public void dick() {

    }

    @Override
    public void standUp() {

    }

    @Override
    public void stopRun() {

    }

    @Override
    public void attack() {
        if (System.currentTimeMillis() - lastAttackTime > timeAttack.get(attackType[attackIndex])){
            lastAttackTime = System.currentTimeMillis();

            attackIndex++;
            if (attackIndex >= attackType.length) attackIndex = 0;
            if (attackType[attackIndex].equals("slide")){
                if (getPosX() < getGameWorld().getMegaman().getPosX()) setSpeedX(5);
                else setSpeedX(-5);
            }
        }
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return null;
    }

    @Override
    public void draw(Graphics2D g2) {
        if(getState() == IMMUNITY && (System.nanoTime()/10000000) % 2 != 1){
            System.out.println("boss immunity");
        }
        else{
            if (attackType[attackIndex].equals("NONE")){
                if (getDirection() == RIGHT_DIR){
                    idleForward.Update(System.nanoTime());
                    idleForward.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                            (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                }
                else{
                    idleBackward.Update(System.nanoTime());
                    idleBackward.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                            (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                }
            }
            else if (attackType[attackIndex].equals("shooting")){
                if (getDirection() == RIGHT_DIR){
                    shootingFoward.Update(System.nanoTime());
                    shootingFoward.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                            (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                }
                else{
                    shootingBackward.Update(System.nanoTime());
                    shootingBackward.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                            (int)(getPosY() - getGameWorld().getCamera().getPosY()));
                }
            }

            else if (attackType[attackIndex].equals("slide")){
                if (getDirection() == RIGHT_DIR){
                    slideForward.Update(System.nanoTime());
                    slideForward.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                            (int)(getPosY() - getGameWorld().getCamera().getPosY())+ 50);
                }
                else{
                    slideBackward.Update(System.nanoTime());
                    slideBackward.draw(g2, (int)(getPosX() - getGameWorld().getCamera().getPosX()),
                            (int)(getPosY() - getGameWorld().getCamera().getPosY())+ 50);
                }
            }
        }

        g2.drawRect((int)(getPosX() - getWidth()/2 - getGameWorld().getCamera().getPosX()),
                (int)(getPosY() - getHeight()/2 - getGameWorld().getCamera().getPosY()), (int)getWidth(), (int)getHeight());
    }
}
