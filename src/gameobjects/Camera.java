package gameobjects;

public class Camera extends GameObject{
    private float widthView;
    private float heightView;

    private boolean isLocked = false;

    public Camera(float posX, float posY, GameWorld gameWorld, float widthView, float heightView) {
        super(posX, posY, gameWorld);
        this.widthView = widthView;
        this.heightView = heightView;
    }

    public void Lock(){
        isLocked = true;
    }

    public void unLock(){
        isLocked = false;
    }
    @Override
    public void Update() {
        if (!isLocked){
            Megaman mainChar = getGameWorld().getMegaman();

            if (mainChar.getPosX() - getPosX() > 400) setPosX(mainChar.getPosX() - 400);
            if (mainChar.getPosX() - getPosX() < 200) setPosX(mainChar.getPosX() - 200);

            if (mainChar.getPosY() - getPosY() > 400) setPosY(mainChar.getPosY() - 400);
            else if (mainChar.getPosY() - getPosY() < 250) setPosY(mainChar.getPosY() - 250);
        }
    }

    public float getWidthView() {
        return widthView;
    }

    public void setWidthView(float widthView) {
        this.widthView = widthView;
    }

    public float getHeightView() {
        return heightView;
    }

    public void setHeightView(float heightView) {
        this.heightView = heightView;
    }
}
