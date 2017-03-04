package Objects;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by HP PC on 25.02.2017.
 */
public abstract class GameObject {
    protected float x;
    protected float y;

    protected GameObjectId id;

    protected int width;
    protected int height;

    protected int velX;
    protected int velY;

    protected BufferedImage texture;

    public GameObject(float x, float y, int width, int height, GameObjectId id, BufferedImage texture){
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.id = id;
    }

    public abstract void render(Graphics g);
    public abstract void update();

    public abstract Rectangle getBounds();

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVelX() {
        return velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public GameObjectId getId() {
        return id;
    }
}
