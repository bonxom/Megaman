package gameobjects;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ParticularObjectManager {
    protected List<ParticularObject> particularObjects;
    private GameWorld gameWorld;

    public ParticularObjectManager(GameWorld gameWorld){
        this.gameWorld = gameWorld;
        particularObjects = Collections.synchronizedList(new LinkedList<>());//dong bo hoa(Synchronized)->push in Queue-> avoid function collision when call a same function
        //we have to use this because our program has 2 streams: Key event, and Game Stream
        //Example: Key event call attack(), which include shooting(). Moreover, shooting() is also calling by an Character
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void addObject(ParticularObject particularObject){
        synchronized (particularObjects){
            particularObjects.add(particularObject);
        }
    }

    public void removeObject(ParticularObject particularObject){
        synchronized (particularObjects){
            for (int i = 0; i < particularObjects.size(); i++){
                ParticularObject object = particularObjects.get(i);
                if (object == particularObject){
                    particularObjects.remove(i);
                }
            }
        }
    }

    public ParticularObject getCollisionWidthEnemyObject(ParticularObject object){
        synchronized (particularObjects){
            for (int i = 0; i < particularObjects.size(); i++){
                ParticularObject existedObject = particularObjects.get(i);



                if (existedObject.getTeamType() != object.getTeamType() &&
                        object.getBoundForCollisionWithEnemy().intersects(existedObject.getBoundForCollisionWithEnemy())) {
                    System.out.println("Shooteddddddddddddddddddddddddd");
                    return existedObject;
                }
            }
        }
        return null;
    }

    public void UpdateObjects(){
        synchronized (particularObjects){
            for (int i = 0; i < particularObjects.size(); i++){
                ParticularObject object = particularObjects.get(i);

                if (!object.isObjectOutOfCameraView()) object.Update();
                if (object.getState() == ParticularObject.DEATH) particularObjects.remove(i);
            }
        }
    }

    public void draw(Graphics2D g2){
        synchronized (particularObjects){
            for (ParticularObject x : particularObjects){
                if (!x.isObjectOutOfCameraView()) x.draw(g2);
            }
        }
    }
}
