package Resources;

import java.awt.image.BufferedImage;

/**
 * Created by HP PC on 26.02.2017.
 */
public class Texture {

    private int height;
    private int width;
    private BufferedImage[][] textures;
    private BufferedImage texture;

    public Texture(int width, int height, String path) {
        this.height = height;
        this.width = width;
        texture = ImageLoader.loadImage(path);
        loadTextureImage();
    }

    private void loadTextureImage(){
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
