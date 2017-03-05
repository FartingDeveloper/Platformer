import Objects.Block;
import Objects.GameObject;
import Objects.GameObjectId;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HP PC on 25.02.2017.
 */
public class LevelLoader {

    private BufferedImage levelImage;
    private BufferedImage[] backgroundImages;

    private List<GameObject> objects = new ArrayList<>();
    private HashMap<String, Texture> textures = new HashMap<>();

    public LevelLoader(String path, String[] backgroundPath, String[] textures){
        backgroundImages = new BufferedImage[backgroundPath.length];

        loadImage(path);
        loadBack(backgroundPath);
        loadTextures(textures);
    }

    private void loadImage(String path){
        try {
            File file = new File(path);
            levelImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBack(String[] path){
        for(int i = 0; i < path.length; i++){
            try {
                File file = new File(path[i]);
                backgroundImages[i] = ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadTextures(String[] path){
        for(int i = 0; i < path.length; i++){
            File file = new File(path[i]);

            String name = nameConverter(file.getName());
            Texture texture = textureFabric(name, path[i]);

            textures.put(name, texture);
        }
    }

    private Texture textureFabric(String name, String path){
        Texture texture = null;
        switch (name){
            case "stone":{
                texture = new Texture(400,320, path);
                break;
            }
            case "enemy":{

            }
        }
        return texture;
    }

    private String nameConverter(String fileName){
        String name = fileName;
        int index = name.lastIndexOf("\\");
        name = name.substring(index+1);

        index = name.indexOf(".");
        name = name.substring(0, index);

        return name;
    }

    public void loadLevel(){
        for(int x = 0; x < levelImage.getWidth(); x++){
            for(int y = 0; y < levelImage.getHeight(); y++){
                int pix = levelImage.getRGB(x, y);

                int red = (pix >> 16) & 0xff;
                int green = (pix >> 8) & 0xff;
                int blue = pix & 0xff;

                if((red == 255) && (green == 255) && (blue == 255)){
                    objects.add(new Block(x*32 ,y*32, 32, 32, GameObjectId.Block, textures.get("stone").getTextures()[0][0]));
                }
            }
        }
    }

    public BufferedImage[] getBackground(){
        return backgroundImages;
    }

    public List<GameObject> getObjects(){
        return objects;
    }
}

