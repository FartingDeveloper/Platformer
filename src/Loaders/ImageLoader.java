package Loaders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by HP PC on 08.03.2017.
 */
 public class ImageLoader {

    public static BufferedImage loadImage(String path){
        ClassLoader cl = ImageLoader.class.getClassLoader();
        BufferedImage image = null;
        try {
            BufferedInputStream bi = new BufferedInputStream(cl.getResourceAsStream(path));
            image = ImageIO.read(bi);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage[] loadImages(String[] path){
        ClassLoader cl = ImageLoader.class.getClassLoader();
        // создать изображения
        BufferedImage[] images = new BufferedImage[path.length];
        for(int i = 0; i < path.length; i++){
            try {
                BufferedInputStream bi = new BufferedInputStream(cl.getResourceAsStream(path[i]));
                images[i] = ImageIO.read(bi);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images;
    }

}
