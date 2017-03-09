package Objects;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HP PC on 06.03.2017.
 */
public class Chinese extends Enemy {

    public Chinese(float x, float y, int width, int height, List<GameObject> objects, GameObjectId id, BufferedImage texture, HashMap<String, BufferedImage[][]> textures) {
        super(x, y, width, height, objects, id, texture, textures);
    }

    protected void walkAnimation(){
        if(index > speed) {
            index = 0;
            texture = animationTextures.get("walking")[0][count++];
            if (count == 5) count = 0;
        }
    }

    protected void handKickAnimation(){
        if(index > speed - 6){
            index = 0;
            if(kickNumber) {
                texture = animationTextures.get("kick")[0][count++];
                if (count == 2) {
                    count = 0;
                    kick = false;
                    kickNumber = !kickNumber;
                    checkPlayer();
                }
            }
            else {
                texture = animationTextures.get("kick_second")[0][count++];
                if(count == 3){
                    count = 0;
                    kick = false;
                    kickNumber = !kickNumber;
                    checkPlayer();
                }
            }
        }
    }

    protected void legKickAnimation(){
        if(index > speed - 7){
            index = 0;
            texture = animationTextures.get("leg_kick")[0][count++];
            if(count == 2){
                count = 0;
                legKick = false;
                checkPlayer();
            }
        }
    }

    protected void kickedAnimation(){
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

    protected void kickedFirst(){
        if(index > speed - 5) {
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

    protected void kickedSecond(){
        if(index > speed - 5) {
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

    protected void kickedFinish(){
        if(index > speed - 5){
            index = 0;
            if(count < 4){
                texture = animationTextures.get("died_up")[0][count++];
            }
            if(count >= 4 && count < 6){
                texture = animationTextures.get("died_down")[0][count++ - 4];
            }
            if(count >= 6){
                texture = animationTextures.get("kicked_final")[0][count++ - 6];
                if(count == 11){
                    count = 0;
                    kicked = false;
                    velX = 0;
                    kickedCount = 1;
                }
            }

        }
    }

    protected void diedAnimation(){
        if(index > speed - 4){
            index = 0;
            if(count < 4){
                texture = animationTextures.get("died_up")[0][count++];
            }
            if(count >= 4){
                texture = animationTextures.get("died_down")[0][count++ - 4];
                if(count == 6){
                    kicked = false;
                    count = 5;
                    texture = animationTextures.get("died_down")[0][1];
                    velX = 0;
                }
            }
        }
    }
}
