package Objects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;

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

    public void render(Graphics g) {
        if(position) g.drawImage(texture, (int)x,(int)y, null);
        else g.drawImage(mirror(texture), (int)x,(int)y, null);

//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setColor(Color.BLUE);
//        g2d.draw(getBounds());
//        g2d.draw(getBoundsRight());
//        g2d.draw(getBoundsLeft());
//        g2d.draw(getBoundsKickRight());
//        g2d.draw(getBoundsKickLeft());
//        g2d.draw(getBoundsLegKickRight());
//        g2d.draw(getBoundsLegKickLeft());
    }

    protected BufferedImage mirror(BufferedImage image){
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
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

    protected void animation(){

        if(dead){
            diedAnimation();
            return;
        }

        if(kicked){
            kickedAnimation();
            return;
        }

        if(velX == 0 && velY == 0){
            if(kick) handKickAnimation();
            else if(legKick) legKickAnimation();
            else texture = animationTextures.get("walking")[0][0];
        }

        if(abs(velX) > 0 && velY == 0){
            walkAnimation();
        }
    }

    protected abstract void walkAnimation();
    protected abstract void handKickAnimation();
    protected abstract void legKickAnimation();
    protected abstract void kickedAnimation();
    protected abstract void kickedFirst();
    protected abstract void kickedSecond();
    protected abstract void kickedFinish();
    protected abstract void diedAnimation();

    public abstract Rectangle getBoundsLeft();
    public abstract Rectangle getBoundsRight();
    public abstract Rectangle getBoundsTop();

    protected void kickParameter(AnimatedObject object){
        object.setHealth(object.getHealth() - 20);

        if(object.getHealth() <= 0){
            if(object.getClass() == Chinese.class) Player.increaseKillCount();
            object.setDead(true);
            object.setFalling(true);
            if(position){
                object.setVelX(6);
            }
            else {
                object.setVelX(-6);
            }
            object.setVelY(-2);
        }
        else {
            if(object.getKickedCount() == 3){
                if(object.getClass() == Player.class){
                    if(position){
                        object.setVelX(1);
                    }
                    else {
                        object.setVelX(-1);
                    }
                }
                if(object.getClass() == Chinese.class){
                    if(position){
                        object.setVelX(6);
                    }
                    else {
                        object.setVelX(-6);
                    }
                    object.setVelY(-5);
                    object.setFalling(true);
                }
            }
            else object.setVelX(0);
        }

        object.setKicked(true);
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

    public void setPosition(boolean position) {
        this.position = position;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
