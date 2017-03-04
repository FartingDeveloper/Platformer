package Objects;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by HP PC on 25.02.2017.
 */
public class Block extends GameObject {

    public Block(float x, float y, int width, int height, GameObjectId id, BufferedImage texture) {
        super(x, y, width, height, id, texture);
    }

    @Override
    public void render(Graphics g) {
//        g.drawImage(texture, (int)x,(int) y, width, height, null);
    }

    @Override
    public void update() {
    }

    public Rectangle getBounds(){
        return new Rectangle((int) x, (int) y, width, height);
    }
}
