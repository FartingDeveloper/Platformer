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
public class Enemy extends AnimatedObject {

    private boolean triggered = false;

    private int length = 0;

    private boolean kickNumber = true;
    private Player player;

    public Enemy(float x, float y, int width, int height, java.util.List<GameObject> objects, GameObjectId id, BufferedImage texture, HashMap<String, BufferedImage[][]> textures) {
        super(x, y, width, height, objects, id, texture, textures);
        velX = 1;
    }

    @Override
    public void update() {
        if(!dead){
            x += velX;
            y += velY;
            length++;

            if(falling){
                velY += gravity;
            }

            if(!triggered && length == 150){
                velX = -velX;
                position = !position;
                length = 0;
            }

            index++;
            collision();
            animation();
        }
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

                    if (!player.isKicked() && triggered) {
                        boolean kickCount = hit();
                        if (kickCount) {
                            if (!player.isKick() && !kick) {
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
                            } else {
                                if ((getBoundsLegKickLeft().intersects(player.getBoundsRight()) || getBoundsLegKickRight().intersects(player.getBoundsLeft())) && !player.isKick() && !player.isKicked()) {
                                    legKick = !kickCount;
                                    velX = 0;
                                    this.player = player;
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

    private boolean hit(){
        Random rand = new Random();
        return rand.nextBoolean();
    }

    private void checkPlayer(){
        if(player != null){
            kickParameter(player);
            player = null;
        }
    }

    protected void animation(){
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

    private void walkAnimation(){
        if(index > speed) {
            index = 0;
            texture = animationTextures.get("walking")[0][count++];
            if (count == 5) count = 0;
        }
    }

    private void handKickAnimation(){
        if(index > speed - 7){
            index = 0;
            if(kickNumber) {
                texture = animationTextures.get("kick")[0][count++];
                if (count == 4) {
                    count = 0;
                    kick = false;
                    kickNumber = !kickNumber;
                    checkPlayer();
                }
            }
            else {
                texture = animationTextures.get("kick_2")[0][count++];
                if(count == 3){
                    count = 0;
                    kick = false;
                    kickNumber = !kickNumber;
                    checkPlayer();
                }
            }
        }
    }

    private void legKickAnimation(){
        if(index > speed - 5){
            index = 0;
            texture = animationTextures.get("leg_kick")[0][count++];
            if(count == 3){
                count = 0;
                legKick = false;
                checkPlayer();
            }
        }
    }

    private void kickedAnimation(){
        switch (kickedCount){
            case 1:{
                kickedFirst();
                break;
            }
            case 2:{
                kickedSecond();
                break;
            }
            case 3:{
                kickedFinish();
                break;
            }
        }
    }

    private void kickedFirst(){
        if(index > speed +5) {
            index = 0;
            texture = animationTextures.get("kicked_first")[0][count++];
            if (count == 2) {
                velX = 0;
                kicked = false;
                kickedCount++;
                count = 0;
            }
        }
    }

    private void kickedSecond(){
        if(index > speed + 5) {
            index = 0;
            texture = animationTextures.get("kicked_second")[0][count++];
            if(count == 2){
                velX = 0;
                kicked = false;
                kickedCount++;
                count = 0;
            }
        }
    }

    private void kickedFinish(){
        if(index > speed) {
            index = 0;
            if(count < 3){
                texture = animationTextures.get("kicked_final_1")[0][count++];
            }
            if(count >= 3 && count < 7){
                texture = animationTextures.get("kicked_final_2")[0][-3 + count++];
            }
            if (count == 7){
                kickedCount = 1;
                velX = 0;
                kicked = false;
                count = 0;
            }
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
        return new Rectangle((int) x + width*3/4, (int) y + height*2/6, width/2, height*1/6);
    }

    public Rectangle getBoundsKickLeft(){
        return new Rectangle((int) x - 10, (int) y + height*2/6, width/2, height*1/6);
    }

    public Rectangle getBoundsLegKickRight(){
        return new Rectangle((int) x + width*3/4, (int) y + height*2/6, width/2, height*1/6);
    }

    public Rectangle getBoundsLegKickLeft(){
        return new Rectangle((int) x - 10, (int) y + height*2/6, width/2, height*1/6);
    }

}
