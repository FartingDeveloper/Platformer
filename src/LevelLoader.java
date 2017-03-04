import Objects.Block;
import Objects.GameObject;
import Objects.GameObjectId;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP PC on 25.02.2017.
 */
public class LevelLoader {

    private BufferedImage levelImage;
    private Texture texture;
    private BufferedImage backgroundImage;
    private List<GameObject> objects = new ArrayList<>();

    public LevelLoader(String path, String backgroundPath,Texture texture){
        loadImage(path);
        loadBack(backgroundPath);
        this.texture = texture;
    }

    private void loadImage(String path){
        try {
            File file = new File(path);
            levelImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBack(String path){
        try {
            File file = new File(path);
            backgroundImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLevel(){
        for(int x = 0; x < levelImage.getWidth(); x++){
            for(int y = 0; y < levelImage.getHeight(); y++){
                int pix = levelImage.getRGB(x, y);

                int red = (pix >> 16) & 0xff;
                int green = (pix >> 8) & 0xff;
                int blue = pix & 0xff;

                if((red == 255) && (green == 255) && (blue == 255)){
                    objects.add(new Block(x*32 ,y*32, 32, 32, GameObjectId.Block, texture.getTextures()[0][0]));
                }
            }
        }
    }
    public BufferedImage getBackground(){
        return backgroundImage;
    }

    public List<GameObject> getObjects(){
        return objects;
    }
}

