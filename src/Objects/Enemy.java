package Objects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.*;

import static java.lang.Math.abs;

/**
 * Created by HP PC on 27.02.2017.
 */
public abstract class Enemy extends AnimatedObject {

    protected boolean triggered = false;

    protected int length = 0;

    protected boolean kickNumber = true;
    protected Player player;

    public Enemy(float x, float y, int width, int height, java.util.List<GameObject> objects, GameObjectId id, BufferedImage texture, HashMap<String, BufferedImage[][]> textures) {
        super(x, y, width, height, objects, id, texture, textures);
        velX = 1;
    }

    @Override
    public void update() {

        x += velX;
        y += velY;
        length++;

        if(falling){
            velY += gravity;
        }

        if(!triggered && length == 150 && !dead){
            velX = -velX;
            position = !position;
            length = 0;
        }

        collision();

        index++;
        animation();
    }

    @Override
    protected void collision() {
        for(GameObject object : objects) {
            switch (object.getId()) {
                case Block:
                    blockCollision(object);
                    break;
                case Player:
                    Player player = (Player) object;
                    if(!dead){
                        if ((player.getX() - x) < 150 && (player.getX() - x) > 0 && !player.isKicked() && !legKick && !kick && !kicked) {
                            position = true;
                            velX = 1;
                            triggered = true;
                        } else if ((player.getX() - x) > -150 && (player.getX() - x) < 0 && !player.isKicked() && !legKick && !kick && !kicked) {
                            position = false;
                            velX = -1;
                            triggered = true;
                        } else {
                            triggered = false;
//                        if(velX == 0) turnVelOn();
                            return;
                        }

                        if (!player.isKicked() && triggered && !player.isDead()) {
                            if (!player.isKick() && !kick){
                                boolean kickCount = new Random().nextBoolean();
                                if (kickCount) {
                                    if (getBoundsKickLeft().intersects(player.getBoundsRight())) {
                                        kick = true;
                                        position = false;
                                        count = 0;
                                        velX = 0;
                                        this.player = player;
                                    }
                                    if (getBoundsKickRight().intersects(player.getBoundsLeft())) {
                                        kick = true;
                                        position = true;
                                        count = 0;
                                        velX = 0;
                                        this.player = player;
                                    }
                                }
                                else {
                                    if (getBoundsLegKickLeft().intersects(player.getBoundsRight())){
                                        legKick = true;
                                        position = false;
                                        count = 0;
                                        velX = 0;
                                        this.player = player;
                                    }
                                    if(getBoundsLegKickRight().intersects(player.getBoundsLeft())){
                                        legKick = true;
                                        position = true;
                                        count = 0;
                                        velX = 0;
                                        this.player = player;
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    private void turnVelOn(){
        if(position) velX = 1;
        else velX = -1;
        position = !position;
    }

    protected void checkPlayer(){
        if(player != null){
            kickParameter(player);
            player = null;
        }
    }

    public Rectangle getBounds(){
        return new Rectangle((int) x + width/4, (int) y + height*5/6, width/2, height/10);
    }

    public Rectangle getBoundsLeft(){
        return new Rectangle((int) x + width*1/4, (int) y, width/8, height*5/6);
    }

    public Rectangle getBoundsRight(){
        return new Rectangle((int) x + width*3/4 - 6, (int) y, width/8, height*5/6);
    }

    public Rectangle getBoundsTop(){
        return new Rectangle((int) x + width/4, (int) y, width/2, height/10);
    }

    public Rectangle getBoundsKickRight(){
        return new Rectangle((int) x + width*3/4, (int) y + height*1/6, width/2 - 2, height*1/6);
    }

    public Rectangle getBoundsKickLeft(){
        return new Rectangle((int) x - 7, (int) y + height*1/6, width/2 - 2, height*1/6);
    }

    public Rectangle getBoundsLegKickRight(){
        return new Rectangle((int) x + width*3/4, (int) y + height*2/6, width/2 - 2, height*1/6);
    }

    public Rectangle getBoundsLegKickLeft(){
        return new Rectangle((int) x - 7, (int) y + height*2/6, width/2 - 2, height*1/6);
    }

}
