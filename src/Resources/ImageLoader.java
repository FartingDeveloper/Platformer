package Resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by HP PC on 08.03.2017.
 */
public class ImageLoader {

    public static BufferedImage loadImage(String path){
        BufferedImage image = null;
        try {
            File file = new File(path);
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage[] loadImages(String[] path){
        BufferedImage[] images = new BufferedImage[path.length];
        for(int i = 0; i < path.length; i++){
            try {
                File file = new File(path[i]);
                images[i] = ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images;
    }

}
