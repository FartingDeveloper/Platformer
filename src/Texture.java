
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by HP PC on 26.02.2017.
 */
public class Texture {

    private int height;
    private int width;
    private BufferedImage[][] textures;

    public Texture(int width, int height, String path){
        this.height = height;
        this.width = width;
        loadTextureImage(path);
    }

    private void loadTextureImage(String texturePath){
        BufferedImage texture = null;
        try {
            File file = new File(texturePath);
            texture = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        textures = new BufferedImage[texture.getHeight()/height+1][texture.getWidth()/width+1];

        for(int y = 1; y <= texture.getHeight()/height; y++){
            for(int x = 1; x <= texture.getWidth()/width; x++){
                textures[y-1][x-1] = texture.getSubimage(x*width - width, y*height - height, width, height);
            }
        }
    }

    public BufferedImage[][] getTextures() {
        return textures;
    }
}
