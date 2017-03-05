package Objects;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static java.lang.Math.abs;

/**
 * Created by HP PC on 25.02.2017.
 */
public class Player extends AnimatedObject implements KeyListener{

    private boolean kickNumber = true;
    private Enemy enemy;

    public Player(float x, float y, int width, int height, java.util.List<GameObject> objects, GameObjectId id, BufferedImage texture, HashMap<String, BufferedImage[][]> textures) {
        super(x, y, width, height, objects, id,texture, textures);
    }

    @Override
    public void update() {
        x += velX;
        y += velY;

        if(falling){
            velY += gravity;
        }


        collision();


        index++;
        animation();
    }

    protected void collision(){

        for(GameObject object : objects) {
            switch (object.getId()){
                case Block:
                    blockCollision(object);
                    break;
                case Enemy:
                    Enemy enemy = (Enemy) object;
                    if(!kicked && !enemy.isKick() && !enemy.isKicked() && !enemy.isDead()){
                        if(kick){
                            if (getBoundsKickLeft().intersects(enemy.getBoundsRight()) || getBoundsKickRight().intersects(enemy.getBoundsLeft())) {
                                this.enemy = enemy;
                            }
                        }
                        if(legKick){
                            if (getBoundsLegKickLeft().intersects(enemy.getBoundsRight()) || getBoundsLegKickRight().intersects(enemy.getBoundsRight())) {
                                this.enemy = enemy;
                            }
                        }
                    }
            }
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
                    checkEnemy();
                }
            }
            else {
                texture = animationTextures.get("kick_2")[0][count++];
                if(count == 3){
                    count = 0;
                    kick = false;
                    kickNumber = !kickNumber;
                    checkEnemy();
                }
            }
        }
    }

    private void checkEnemy(){
        if(enemy != null){
            kickParameter(enemy);
            enemy = null;
        }
    }

    private void legKickAnimation(){
        if(index > speed - 5){
            index = 0;
            texture = animationTextures.get("leg_kick")[0][count++];
            if(count == 3){
                count = 0;
                legKick = false;
                checkEnemy();
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

    private void diedAnimation(){
        if(index > speed - 7){
            index = 0;
            if(count < 3){
                texture = animationTextures.get("died_up")[0][count++];
            }
            if(count >=3 && count < 6){
                texture = animationTextures.get("died_down")[0][count++ - 3];
            }
            if(count >= 6){
                texture = animationTextures.get("died_down")[0][2];
                velX = 0;
            }
        }
    }

//    private void walkAnimationLeft(){
//        if(index > speed){
//            index = 0;
//            texture = animationTextures.get("walking_left")[0][5 - count++];
//            if(6 - count == 0) count = 0;
//        }
//    }
//
//    private void handKickAnimationLeft(){
//        if(index > speed - 4){
//            index = 0;
////            if(kickNumber) {
////                texture = animationTextures.get("kick_left")[0][3 - count++];
////                if (count == 4) {
////                    count = 0;
////                    kick = false;
////                    kickNumber = !kickNumber;
////                }
////            }
////            else {
//                texture = animationTextures.get("kick_left_2")[0][2 - count++];
//                if(count == 3){
//                    count = 0;
//                    kick = false;
////                    kickNumber = !kickNumber;
//                }
////            }
//        }
//    }
//
//    private void legKickAnimationLeft(){
//        if(index > speed - 7){
//            index = 0;
//            if(count < 4){
//                texture = animationTextures.get("leg_kick_left")[0][3 - count++];
//                return;
//            }
//            if (count == 4) {
//                texture = animationTextures.get("leg_kick_finish_left")[0][0];
//                count++;
//                return;
//            }
//            if(count > 4){
//                texture = animationTextures.get("leg_kick_left")[0][count++ - 5];
//                if(count == 9){
//                    count = 0;
//                    legKick = false;
//                }
//            }
//        }
//    }

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
        return new Rectangle((int) x + width*3/4, (int) y + height*1/6, width/2, height*1/6);
    }

    public Rectangle getBoundsKickLeft(){
        return new Rectangle((int) x - 10, (int) y + height*1/6, width/2, height*1/6);
    }

    public Rectangle getBoundsLegKickRight(){
        return new Rectangle((int) x + width*3/4, (int) y + height*2/6, width/2, height*1/6);
    }

    public Rectangle getBoundsLegKickLeft(){
        return new Rectangle((int) x - 10, (int) y + height*2/6, width/2, height*1/6);
    }



    @Override
    public void keyTyped(KeyEvent e) {
        //NTD
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!kick && !legKick && !kicked){
            if(e.getKeyCode() == KeyEvent.VK_A){
                velX = -1;
                position = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_D){
                velX = 1;
                position = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_W){
                velY = -3;
                falling = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                kick = true;
                velX = 0;
                count = 0;
            }
            if(e.getKeyCode() == KeyEvent.VK_F){
                legKick = true;
                velX = 0;
                count = 0;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!kick && !legKick && !kicked){
            velX = 0;
            if(e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_F) count = 0;
        }
    }

}
