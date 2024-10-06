package effect;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;

public class CacheDataLoader {
    private static CacheDataLoader instance = null;

    private String framefile = "data/frame.txt";
    private String animationfile = "data/animation.txt";
    private String physmapfile = "data/phys_map.txt";
    private String backgroundmapfile = "data/background_map.txt";
    private String soundfile = "data/sounds.txt";

    private Hashtable<String, FrameImage> frameImages;
    private Hashtable<String, Animation> animations;


    private int[][] phys_map;

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

        for (int i = 0; i < numRows; i++){
            for (int j = 0; j < numColumns; j++){
                System.out.print(instance.phys_map[i][j] + " ");
            }
            System.out.println();
        }
    }

    public FrameImage getFrameImage(String name) {
        FrameImage frameImage = new FrameImage(instance.frameImages.get(name));
        return frameImage;
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
        br.close(); //dong file
    }

    public Animation getAnimation(String name){
        Animation animation = new Animation(instance.animations.get(name));
        return animation;
    }

}
