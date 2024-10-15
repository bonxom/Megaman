package gameobjects;

import effect.CacheDataLoader;
import effect.FrameImage;
import gameobjects.bullets.BulletManager;
import gameobjects.gamecharacters.FinalBoss;
import gameobjects.gamecharacters.Megaman;
import gameobjects.gamecharacters.RedEyeDevil;
import gameobjects.map.BackgroundMap;
import gameobjects.map.PhysicalMap;
import userinterface.GameFrame;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameWorld {

    private BufferedImage bufferedImage;
    private int lastState;

    private ParticularObjectManager particularObjectManager;
    private Megaman megaman;
    private PhysicalMap physicalMap;
    private Camera camera;
    private BulletManager bulletManger;
    private BackgroundMap backgroundMap;
    public Clip bgMusic;

    public static final int finalBossX = 3600;
    public static final int PAUSEGAME = 0;
    public static final int TUTORIAL = 1;
    public static final int GAMEPLAY = 2;
    public static final int GAMEOVER = 3;
    public static final int GAMEWIN = 4;

    public static final int INTROGAME = 0;
    public static final int MEETFINALBOSS = 1;

    public int openIntroGameY = 0;
    public int state = PAUSEGAME;
    public int previousState = state;
    public int tutorialState = INTROGAME;

    public int storyTutorial = 0;
    public String[] texts1 = new String[4];

    public String textTutorial;
    public int currentSize = 1;

    private boolean finalBossTrigger = true;
    ParticularObject boss;

    FrameImage avt = CacheDataLoader.getInstance().getFrameImage("avatar");

    private int numberOfLife = 3;

    public GameWorld(){

        texts1[0] = "We are heros, and our mission is protecting our Home\nEarth....";
        texts1[1] = "There was a Monster from University on Earth in 10 years\n"
                + "and we lived in the scare in that 10 years....";
        texts1[2] = "Now is the time for us, kill it and get freedom!....";
        texts1[3] = "      LET'S GO!.....";
        textTutorial = texts1[0];

        bufferedImage = new BufferedImage(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        megaman = new Megaman(300, 300, this);
        physicalMap = new PhysicalMap(0, 0, this);
        backgroundMap = new BackgroundMap(0, 0, this);
        camera = new Camera(0, 0, this, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
        bulletManger = new BulletManager(this);

        particularObjectManager = new ParticularObjectManager(this);
        particularObjectManager.addObject(megaman);

        initEnemies();

        bgMusic = CacheDataLoader.getInstance().getSound("bgmusic");
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void TutorialUpdate(){
        switch (tutorialState){
            case INTROGAME:
                if (storyTutorial == 0){
                    if (openIntroGameY < 450){
                        openIntroGameY += 4;
                    }
                    else storyTutorial++;
                }
                else{
                    if (currentSize < textTutorial.length()) currentSize++;
                }
                break;
            case MEETFINALBOSS:
                if (storyTutorial == 0){
                    if (openIntroGameY >= 450){
                        openIntroGameY -= 1;
                    }
                    if (camera.getPosX() < finalBossX){
                        camera.setPosX(camera.getPosX() + 2);
                    }
                    if (megaman.getPosX() < finalBossX + 150){
                        megaman.setDirection(ParticularObject.RIGHT_DIR);
                        megaman.run();
                        megaman.Update();
                    }
                    else{
                        megaman.stopRun();
                    }

                    if (openIntroGameY < 450 && camera.getPosX() >= finalBossX && megaman.getPosX() >= finalBossX + 150){
                        camera.Lock();
                        storyTutorial++;
                        megaman.stopRun();

                        physicalMap.phys_map[14][120] = 1;
                        physicalMap.phys_map[15][120] = 1;
                        physicalMap.phys_map[16][120] = 1;
                        physicalMap.phys_map[17][120] = 1;

                        backgroundMap.map[14][120] = 17;
                        backgroundMap.map[15][120] = 17;
                        backgroundMap.map[16][120] = 17;
                        backgroundMap.map[17][120] = 17;
                    }
                }
                else{
                    if (currentSize < textTutorial.length()) currentSize++;
                }
                break;
        }
    }

    private void drawString(Graphics2D g2, String text, int x, int y){
        for (String str : text.split("\n")){
            g2.drawString(str, x, y += g2.getFontMetrics().getHeight());
        }
    }

    private void TutorialRender (Graphics2D g2){
        switch (tutorialState){
            case INTROGAME:{
                int yMid = GameFrame.SCREEN_HEIGHT/2 - 15;
                int y1 = yMid - GameFrame.SCREEN_HEIGHT/2 - openIntroGameY/2;
                int y2 = yMid + openIntroGameY/2;

                g2.setColor(Color.BLACK);
                g2.fillRect(0, y1, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT/2);
                g2.fillRect(0, y2, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT/2);

                if (storyTutorial >= 1){
                    g2.drawImage(avt.getImage(), 600, 350, null);
                    g2.setColor(Color.BLUE);
                    g2.fillRect(280, 450, 350, 80);
                    g2.setColor(Color.WHITE);
                    String text = textTutorial.substring(0, currentSize - 1);
                    drawString(g2, text, 290, 480);
                }
                break;
            }
            case MEETFINALBOSS: {
                int yMid = GameFrame.SCREEN_HEIGHT / 2 - 15;
                int y1 = yMid - GameFrame.SCREEN_HEIGHT / 2 - openIntroGameY / 2;
                int y2 = yMid + openIntroGameY / 2;

                g2.setColor(Color.BLACK);
                g2.fillRect(0, y1, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT/2);
                g2.fillRect(0, y2, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT/2);
                break;
            }
        }
    }

    public void Update(){
        switch (state){
            case PAUSEGAME:
                break;
            case TUTORIAL:
                TutorialUpdate();
                break;
            case GAMEPLAY:
                particularObjectManager.UpdateObjects();
                bulletManger.UpdateObjects();
                physicalMap.Update();
                camera.Update();

                if (megaman.getPosX() > finalBossX && finalBossTrigger){
                    finalBossTrigger = false;
                    switchState(TUTORIAL);
                    tutorialState = MEETFINALBOSS;
                    storyTutorial = 0;

                    openIntroGameY = 500;

                    boss = new FinalBoss(finalBossX + 700, 460, this);
                    boss.setTeamType(ParticularObject.ENEMY);
                    boss.setDirection(ParticularObject.LEFT_DIR);
                    particularObjectManager.addObject(boss);
                }

                if (megaman.getState() == ParticularObject.DEATH){
                    numberOfLife--;
                    if (numberOfLife >= 0){
                        megaman.setBlood(100);
                        megaman.setPosY(megaman.getPosY() - 50);
                        megaman.setState(ParticularObject.IMMUNITY);
                        particularObjectManager.addObject(megaman);
                    }
                    else{
                        switchState(GAMEOVER);
                        bgMusic.stop();
                    }

                }

                if (!finalBossTrigger && boss.getState() == ParticularObject.DEATH){
                    switchState(GAMEWIN);
                }
            case GAMEOVER:
                break;
            case GAMEWIN:
                break;
        }
    }

    public void Render(){
        Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();

        if (g2 != null){
            switch (state){
                case PAUSEGAME:
                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
                    g2.setColor(Color.WHITE);
                    g2.drawString("PRESS ENTER TO CONTINUE", 400, 300);
                    break;
                case TUTORIAL:
                    backgroundMap.draw(g2);
                    if (tutorialState == MEETFINALBOSS){
                        particularObjectManager.draw(g2);
                    }
                    TutorialRender(g2);
                    break;
                case GAMEWIN:
                case GAMEPLAY:
                    backgroundMap.draw(g2);
                    particularObjectManager.draw(g2);
                    bulletManger.draw(g2);

                    g2.setColor(Color.GRAY);
                    g2.fillRect(19, 59, 102, 22);
                    g2.setColor(Color.RED);
                    g2.fillRect(20, 60, (int)megaman.getBlood(), 20);

                    for (int i = 0; i < numberOfLife; i++){
                        g2.drawImage(CacheDataLoader.getInstance().getFrameImage("hearth").getImage(),
                                20 + i*40, 18, null);

                    }

                    if (state == GAMEWIN){
                        g2.drawImage(CacheDataLoader.getInstance().getFrameImage("gamewin").getImage(),
                                300, 300, null);
                    }
                    break;
                case GAMEOVER:
                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
                    g2.setColor(Color.WHITE);
                    g2.drawString("GAME OVER!", 450, 300);
                    break;
            }
        }
    }

    private void initEnemies(){
        ParticularObject redeye = new RedEyeDevil(1250, 410, this);
        redeye.setDirection(ParticularObject.LEFT_DIR);
        redeye.setTeamType(ParticularObject.ENEMY);
        particularObjectManager.addObject(redeye);
    }

    public void switchState(int state){
        previousState = this.state;
        this.state = state;
    }

    public ParticularObjectManager getParticularObjectManager() {
        return particularObjectManager;
    }

    public Megaman getMegaman() {
        return megaman;
    }

    public PhysicalMap getPhysicalMap() {
        return physicalMap;
    }

    public Camera getCamera() {
        return camera;
    }

    public BulletManager getBulletManger() {
        return bulletManger;
    }

}
