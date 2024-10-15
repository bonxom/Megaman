package effect;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

public class CacheDataLoader {
    private static CacheDataLoader instance = null;

    private String framefile = "data/frame.txt";
    private String animationfile = "data/animation.txt";
    private String physmapfile = "data/phys_map.txt";
    private String backgroundmapfile = "data/background_map.txt";
    private String soundfile = "data/sounds.txt";

    private Hashtable<String, FrameImage> frameImages;
    private Hashtable<String, Animation> animations;
    private Hashtable<String, Clip> sounds;


    private int[][] phys_map;
    private int[][] background_map;

    private CacheDataLoader(){}

    public static CacheDataLoader getInstance(){
        if (instance == null)
            instance = new CacheDataLoader();
        return instance;
    }

    public void LoadData() throws IOException {
        LoadFrame();
        LoadAnimation();
        LoadPhysMap();
        LoadBackgroundMap();
        LoadSounds();
    }


    public void LoadSounds() throws IOException {
        sounds = new Hashtable<>();

        FileReader fr = new FileReader(soundfile);
        BufferedReader bf = new BufferedReader(fr);

        String line = null;

        if (bf == null){
            System.out.println("No data");
            throw new IOException();
        }
        else{
            fr = new FileReader(soundfile);
            bf = new BufferedReader(fr);

            while((line = bf.readLine()).equals(""));
            int n = Integer.parseInt(line);

            for (int i = 0; i < n; i++){
                while((line = bf.readLine()).equals(""));
                String[] str = line.split("\\s+");

                String name = str[0];
                String path = str[1];
                Clip clip = null;

                try{
                    File soundFile = new File(path);
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);

                } catch (UnsupportedAudioFileException e) {
                    System.out.println("Unsupported audio file: " + path);
                } catch (LineUnavailableException e) {
                    System.out.println("Audio line unavailable");
                } catch (IOException e) {
                    System.out.println("Error loading sound file: " + path);
                }

                sounds.put(name, clip);
            }
        }
        bf.close();
    }

    public Clip getSound(String name){
        return instance.sounds.get(name);
    }

    public int[][] getPhysicalMap(){
        return instance.phys_map;
    }

    public void LoadPhysMap() throws IOException {
        FileReader fr = new FileReader(physmapfile);
        BufferedReader bf = new BufferedReader(fr);

        String line = null;

        line = bf.readLine();
        int numRows = Integer.parseInt(line);
        line = bf.readLine();
        int numColumns = Integer.parseInt(line);

        instance.phys_map = new int[numRows][numColumns];

        for (int i = 0; i < numRows; i++){
            line = bf.readLine();
            String[] tmp = line.split("\\s+");
            for (int j = 0; j < numColumns; j++){
                instance.phys_map[i][j] = Integer.parseInt(tmp[j]);
            }
        }

//        for (int i = 0; i < numRows; i++){
//            for (int j = 0; j < numColumns; j++){
//                System.out.print(instance.phys_map[i][j] + " ");
//            }
//            System.out.println();
//        }
    }



    public void LoadAnimation() throws IOException {
        animations = new Hashtable<>();

        FileReader fr = new FileReader(animationfile);
        BufferedReader br = new BufferedReader(fr);//con tro o dau file // con tro doc file

        String line = null;

        if (br.readLine() == null){
            System.out.println("No data");
            throw new IOException();
        }
        else{
            fr = new FileReader(animationfile);
            br = new BufferedReader(fr);

            while ((line = br.readLine()).equals(""));
            int n = Integer.parseInt(line);

            for (int i = 0; i < n; i++){
                Animation animation = new Animation();

                while ((line = br.readLine()).equals(""));
                animation.setName(line);

                while ((line = br.readLine()).equals(""));
                String[] tmp = line.trim().split("\\s+");

                for (int j = 0; j < tmp.length; j+=2) {
                    animation.add(getFrameImage(tmp[j]), Double.parseDouble(tmp[j + 1]));
                }

                instance.animations.put(animation.getName(), animation);
            }
        }

        Animation animation = new Animation();

        animation.setName("ultimate");

        for (int j = 1; j <= 3; j++) {
            animation.add(getFrameImage("until" + j), 30000000.0f);
        }

        instance.animations.put(animation.getName(), animation);

        br.close();
    }

    public void LoadFrame() throws IOException {
        frameImages = new Hashtable<>();

        FileReader fr = new FileReader(framefile);
        BufferedReader br = new BufferedReader(fr);//con tro o dau file // con tro doc file

        String line = null;

        if (br.readLine() == null){
            System.out.println("No data");
            throw new IOException();
        }
        else{
            fr = new FileReader(framefile);
            br = new BufferedReader(fr);  //this 2 lines move the pointer back to the head of file
            while ((line = br.readLine()).equals("")); // while space => nextLine;

            int n = Integer.parseInt(line);
            for (int i = 0; i < n; i++){
                FrameImage frame = new FrameImage();

                while ((line = br.readLine()).equals(""));
                frame.setName(line);

                while ((line = br.readLine()).equals(""));
                String[] tmp = line.trim().split("\\s+");
                String path = tmp[1];

                while ((line = br.readLine()).equals(""));
                tmp = line.trim().split("\\s++");
                int x = Integer.parseInt(tmp[1]);

                while ((line = br.readLine()).equals(""));
                tmp = line.trim().split("\\s++");
                int y = Integer.parseInt(tmp[1]);

                while ((line = br.readLine()).equals(""));
                tmp = line.trim().split("\\s++");
                int w = Integer.parseInt(tmp[1]);

                while ((line = br.readLine()).equals(""));
                tmp = line.trim().split("\\s++");
                int h = Integer.parseInt(tmp[1]);

                BufferedImage image = ImageIO.read(new File(path));
                BufferedImage subImage = image.getSubimage(x, y, w, h);
                frame.setImage(subImage);

                instance.frameImages.put(frame.getName(), frame);
            }
        }
        for (int i = 1; i <= 3; i++){
            String path = "data/until" + i + ".png";
            FrameImage frame = new FrameImage();
            BufferedImage image = ImageIO.read(new File(path));
            BufferedImage subImage = image.getSubimage(0, 0, 50, 35);
            frame.setImage(subImage);
            instance.frameImages.put("until" + i, frame);
        }

        br.close(); //dong file
    }

    public int[][] getBackground_map(){
        return instance.background_map;
    }

    public void LoadBackgroundMap() throws IOException {
        FileReader fr = new FileReader(backgroundmapfile);
        BufferedReader br = new BufferedReader(fr);

        String line = null;
        line = br.readLine();
        int numOfRows = Integer.parseInt(line);
        line = br.readLine();
        int numOfColumns = Integer.parseInt(line);

        instance.background_map = new int[numOfRows][numOfColumns];

        for (int i = 0; i < numOfRows; i++){
            line = br.readLine();
            String[] tmp = line.trim().split("\\s+");
            for (int j = 0; j < numOfColumns; j++){
                instance.background_map[i][j] = Integer.parseInt(tmp[j]);
            }
        }

//        for (int i = 0; i < numOfRows; i++){
//            for (int j = 0; j < numOfColumns; j++){
//                System.out.print(background_map[i][j] + " ");
//            }
//            System.out.println();
//        }
    }

    public FrameImage getFrameImage(String name) {
        FrameImage frameImage = new FrameImage(instance.frameImages.get(name));
        return frameImage;
    }

    public Animation getAnimation(String name){
        Animation animation = new Animation(instance.animations.get(name));
        return animation;
    }

}
