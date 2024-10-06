package gameobjects;

import effect.Animation;
import effect.CacheDataLoader;

import java.awt.*;

public class GameWorld {
    private Megaman megaman;
    private Animation anim;
    private PhysicalMap physicalMap;

    public GameWorld(){
        megaman = new Megaman(300, 300, this);
        anim = CacheDataLoader.getInstance().getAnimation("run");
        physicalMap = new PhysicalMap(0, 0, this);
    }

    public void Update(){
        megaman.Update();
        anim.Update(System.nanoTime());
    }

    public void Render(Graphics2D g2){
        physicalMap.draw(g2);
        megaman.draw(g2);
        anim.draw(g2, 300, 300);

    }

    public Megaman getMegaman() {
        return megaman;
    }

    public void setMegaman(Megaman megaman) {
        this.megaman = megaman;
    }

    public Animation getAnim() {
        return anim;
    }

    public void setAnim(Animation anim) {
        this.anim = anim;
    }

    public PhysicalMap getPhysicalMap() {
        return physicalMap;
    }

    public void setPhysicalMap(PhysicalMap physicalMap) {
        this.physicalMap = physicalMap;
    }
}
