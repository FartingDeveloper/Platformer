package Objects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HP PC on 27.02.2017.
 */
public abstract class AnimatedObject extends GameObject {

    protected java.util.List<GameObject> objects;
    protected HashMap<String, BufferedImage[][]> animationTextures;

    protected int health = 100;
    protected boolean dead = false;

    protected boolean legKick = false;
    protected boolean falling = true;
    protected boolean kick = false;
    protected boolean kicked = false;
    protected int kickedCount = 1;

    protected int gravity = 1;
    protected int count = 0;
    protected int speed = 10;
    protected int index = 0;

    protected boolean position = true;

    public AnimatedObject(float x, float y, int width, int height, List<GameObject> objects, GameObjectId id, BufferedImage texture, HashMap<String, BufferedImage[][]> textures) {
        super(x, y, width, height, id, texture);
        this.objects = objects;
        this.animationTextures = textures;
    }

    @Override
    public void render(Graphics g) {
        if(health > 0){
            if(position) g.drawImage(texture, (int)x,(int)y, null);
            else g.drawImage(mirror(texture), (int)x,(int)y, null);
        }
    }

    private BufferedImage mirror(BufferedImage image){
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-texture.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        return image;
    }

    protected abstract void collision();

    protected void blockCollision(GameObject object){
        Block block = (Block) object;
        if (getBounds().intersects(block.getBounds())) {
            if(falling){
                velY = 0;
                velX = 0;
                y = block.getY() - height;
                falling = false;
            }
        }

        if (getBoundsLeft().intersects(block.getBounds())) {
            if(velY > 0){
                velY = gravity;
            }
            x = block.getX() + block.getWidth() + 1;
            kicked = false;
            count = 0;
        }

        if (getBoundsRight().intersects(block.getBounds())) {
            if(velY > 0){
                velY = gravity;
            }
            x = block.getX() - 1;
            kicked = false;
            count = 0;
        }

        if (getBoundsTop().intersects(block.getBounds())) {
            velY = 0;
            y = block.getY() - texture.getHeight();
            kicked = false;
            count = 0;
        }
    }

    protected abstract void animation();

    public abstract Rectangle getBoundsLeft();
    public abstract Rectangle getBoundsRight();
    public abstract Rectangle getBoundsTop();

    protected void kickParameter(AnimatedObject object){
        if(object.getKickedCount() == 3){
            if(position){
                object.setVelX(1);
            }
            else {
                object.setVelX(-1);
            }
        }
        else object.setVelX(0);

        object.setKicked(true);
        object.setHealth(object.getHealth() - 10);
        object.setCount(0);
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public boolean isKicked() {
        return kicked;
    }

    public void setKicked(boolean kicked) {
        this.kicked = kicked;
    }

    public boolean isKick() {
        return kick;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if(health <= 0) dead = true;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getKickedCount() {
        return kickedCount;
    }

    public boolean isPosition() {
        return position;
    }

    public boolean isDead() {
        return dead;
    }
}
