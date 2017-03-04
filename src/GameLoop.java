import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import Objects.*;


/**
 * Created by HP PC on 24.02.2017.
 */
public class GameLoop extends JComponent {

    private Player player;
    private boolean initialize = false;
    private List<GameObject> objects;
    private BufferedImage background;

    private Thread thread = new Thread(){
        @Override
        public void run() {
            double frapTime = 1000000000 / 60; //Количество времени необходимое на обработку 1 кадра
            long currentTime = System.nanoTime();
            double delta = 0;
            int frames = 0;
            long timer = System.currentTimeMillis();
            while(true){
                long now = System.nanoTime();
                delta += (now - currentTime) / frapTime;
                while(delta >= 1){
                    updateLogic();
                    delta--;
                    frames++;
                }

                repaint();

                if(System.currentTimeMillis() - timer > 1000){
                    System.out.println("FPS:" + frames);
                    timer += 1000;
                    frames = 0;
                }

                currentTime = now;
            }
        }
    };

    public void start(){
        init();
        thread.start();
    }

    private void init(){
        initialize = true;

        Texture texture = new Texture(400,320,"C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\stone.jpg");
        LevelLoader loader = new LevelLoader("C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\map.png", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\city2.jpg", texture);
        loader.loadLevel();
        background = loader.getBackground();
        objects = loader.getObjects();

//        Texture playerTextureRight = new Texture(155, 311, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\a.png");
//        Texture playerTextureLeft = new Texture(155, 311, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\b.png");
//        Texture playerTextureRightKick = new Texture(212, 311, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\kick_right.png");
//        Texture playerTextureLeftKick = new Texture(212, 311, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\kick_left.png");
//        BufferedImage[][][] playerTextures = {playerTextureRight.getTextures(), playerTextureLeft.getTextures(), playerTextureRightKick.getTextures(), playerTextureLeftKick.getTextures()};
//
//        Texture enTex = new Texture(155, 311, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\a.png");
//        Texture enKick = new Texture(212, 311, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\kick_right.png");
//        BufferedImage[][][] en = {enTex.getTextures(), enKick.getTextures()};
//        Enemy enemi = new Enemy(100, 100, 155, 311, objects, GameObjectId.Enemy, enTex.getTextures()[0][3], en);
//        objects.add(enemi);

        HashMap<String, BufferedImage[][]> playerTextures = new HashMap<>();
        Texture playerTextureStandingRight = new Texture(48, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\standing.png");
        playerTextures.put("standing", playerTextureStandingRight.getTextures());
        Texture playerTextureWalkingRight = new Texture(42, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\walking.png");
        playerTextures.put("walking", playerTextureWalkingRight.getTextures());
        Texture playerTextureKickRight= new Texture(60, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\kick.png");
        playerTextures.put("kick", playerTextureKickRight.getTextures());
        Texture playerTextureKickRight2 = new Texture(58, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\kick_2.png");
        playerTextures.put("kick_2", playerTextureKickRight2.getTextures());
        Texture playerTextureLegKickRight = new Texture(57, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\leg_kick.png");
        playerTextures.put("leg_kick", playerTextureLegKickRight.getTextures());
        Texture playerTextureKickedFinal1 = new Texture(47, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\kicked_final_1.png");
        playerTextures.put("kicked_final_1", playerTextureKickedFinal1.getTextures());
        Texture playerTextureKickedFinal2 = new Texture(47, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\kicked_final_2.png");
        playerTextures.put("kicked_final_2", playerTextureKickedFinal2.getTextures());
        Texture playerTextureKickedFirst = new Texture(56, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\kicked_first.png");
        playerTextures.put("kicked_first", playerTextureKickedFirst.getTextures());
        Texture playerTextureKickedSecond = new Texture(57, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\kicked_second.png");
        playerTextures.put("kicked_second", playerTextureKickedSecond.getTextures());
        player = new Player(0, 298, 48,  105, objects, GameObjectId.Player, playerTextureStandingRight.getTextures()[0][0], playerTextures);
//        player = new Player(400, 100, 155, 311, objects, GameObjectId.Player, playerTextureRight.getTextures()[0][3], playerTextures);
        this.addKeyListener(player);
        Enemy enemy = new Enemy(300, 298, 48,  105, objects, GameObjectId.Enemy, playerTextureStandingRight.getTextures()[0][0], playerTextures);
        objects.add(enemy);
        objects.add(player);
    }

    private void updateLogic(){
        for(GameObject object : objects) object.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(initialize){

            g.translate(-(int) player.getX() + player.getWidth()*4, 0);

            for(int x = -background.getWidth(); x < background.getWidth()*20; x += background.getWidth()) g.drawImage(background, x, 0, null);

            for(GameObject object : objects) object.render(g);


            if(player.getHealth() > 0){
                g.setColor(Color.RED);
                g.fillRect((int)player.getX() - player.getWidth()*2, 30, player.getHealth(), 10);
            }

            g.dispose();
        }
    }

}
