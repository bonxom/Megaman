package effect;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {
    private String name;
    private boolean isRepeated;
    private ArrayList<FrameImage> frameImages; //image for an annimation scene
    private int currentFrame;//idx for given list

    private ArrayList<Boolean>  ignoreFrames; //skip annimation if it is not necessary
    private ArrayList<Double> delayFrames; //delay time between 2 frames

    private long begintime;
    private boolean drawRectFrame; //draw rectange around image, just for keeping track of the image we used

    public Animation(){
        delayFrames = new ArrayList<>();
        begintime = 0;
        currentFrame = 0;
        ignoreFrames = new ArrayList<>();
        frameImages = new ArrayList<>();
        drawRectFrame = false;
        isRepeated = true;
    }

    public <T> ArrayList<T> clone(ArrayList<T> v) {
        return new ArrayList<>(v);
    }

    public Animation(Animation animation){//copy constructor
        begintime = animation.begintime;
        currentFrame = animation.currentFrame;
        drawRectFrame = animation.drawRectFrame;
        isRepeated = animation.isRepeated;

        delayFrames = clone(animation.delayFrames);
        ignoreFrames = clone(animation.ignoreFrames);
        frameImages = clone(animation.frameImages);
    }

    //getter setter here
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsRepeated() {
        return isRepeated;
    }

    public void setIsRepeated(boolean repeated) {
        isRepeated = repeated;
    }

    public ArrayList<FrameImage> getFrameImages() {
        return frameImages;
    }

    public void setFrameImages(ArrayList<FrameImage> frameImages) {
        this.frameImages = frameImages;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        if (currentFrame >= 0 && currentFrame < frameImages.size())
            this.currentFrame = currentFrame;
        else this.currentFrame = 0;
    }

    public ArrayList<Boolean> getIgnoreFrames() {
        return ignoreFrames;
    }

    public void setIgnoreFrames(ArrayList<Boolean> ignoreFrames) {
        this.ignoreFrames = ignoreFrames;
    }

    public ArrayList<Double> getDelayFrames() {
        return delayFrames;
    }

    public void setDelayFrames(ArrayList<Double> delayFrames) {
        this.delayFrames = delayFrames;
    }

    public long getBegintime() {
        return begintime;
    }

    public void setBegintime(long begintime) {
        this.begintime = begintime;
    }

    public boolean getDrawRectFrame() {
        return drawRectFrame;
    }

    public void setDrawRectFrame(boolean drawRectFrame) {
        this.drawRectFrame = drawRectFrame;
    }
    //end of original getter and setter

    public boolean isIgnoreFrame(int id){
        return ignoreFrames.get(id);
    }

    public void setIgnoreFrame(int id){
        if (id >= 0 && id < ignoreFrames.size())
            ignoreFrames.set(id, true);
    }

    public void unIgnoreFrame(int id){
        if (id >= 0 && id < ignoreFrames.size()){
            ignoreFrames.set(id, false);
        }
    }

    public void reset(){
        this.currentFrame = 0;
        this.begintime = 0;
        for (int i = 0; i < ignoreFrames.size(); i++){
            ignoreFrames.set(i, false);
        }
    }

    public void add(FrameImage frameImage, double timeToNextFrame){
        ignoreFrames.add(false);
        frameImages.add(frameImage);
        delayFrames.add(timeToNextFrame);
    }

    public BufferedImage getCurrentImage(){
        return frameImages.get(currentFrame).getImage();
    }

    private void nextframe(){

        if(currentFrame >= frameImages.size() - 1){

            if(isRepeated) currentFrame = 0;
        }
        else currentFrame++;

        if(ignoreFrames.get(currentFrame)) nextframe();

    }
    public void Update(long currentTime){
        if (begintime == 0) begintime = currentTime;
        else{
            if (currentTime - begintime > delayFrames.get(currentFrame)){
                nextframe();
                begintime = currentTime;
            }
        }
    }

    public boolean isLastFrame(){
        return currentFrame == frameImages.size()-1;
    }

    public void flipAllImage(){//Character change his direction (Ex: he's going forward, and then moving back -> flip)
        for (int i = 0; i < frameImages.size(); i++){
            BufferedImage image = frameImages.get(i).getImage();

            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(), 0);

            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            image = op.filter(image, null);

            frameImages.get(i).setImage(image);
        }
    }

    public void draw (Graphics2D g2, int x, int y){
        BufferedImage image = getCurrentImage();

        g2.drawImage(image, x - image.getWidth()/2, y - image.getHeight()/2, null);
        if (drawRectFrame){
            g2.drawRect(x - image.getWidth()/2, y - image.getHeight()/2, image.getWidth(), image.getHeight());
        }
    }
}
