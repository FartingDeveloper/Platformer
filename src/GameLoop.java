import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import Objects.*;
import javafx.scene.layout.Background;


/**
 * Created by HP PC on 24.02.2017.
 */
public class GameLoop extends JComponent {

    private Player player;
    private List<GameObject> objects;
    private BufferedImage background;
    private BufferedImage[] backgroundArr;
    private int backgroundCount;

    private boolean initialize = false;

    private Thread thread = new Thread(){
        @Override
        public void run() {
            double frapTime = 1000000000 / 60; //Количество времени необходимое на обработку 1 кадра
            long currentTime = System.nanoTime();
            double delta = 0;
            long timer = System.currentTimeMillis();
            while(true || !player.isDead()){
                long now = System.nanoTime();
                delta += (now - currentTime) / frapTime;
                while(delta >= 1){
                    updateLogic();
                    delta--;
                }

                repaint();

                if(System.currentTimeMillis() - timer > 500){
                    background = backgroundArr[backgroundCount++];
                    if(backgroundCount == backgroundArr.length) backgroundCount = 0;
                    timer += 500;
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

        String[] backPath = {"C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\city2_1.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\city2_2.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\city2_3.jpg"};

        String[] texturePath = {"C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\stone.jpg"};

        LevelLoader loader = new LevelLoader("C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\map.png", backPath, texturePath);
        loader.loadLevel();
        backgroundArr = loader.getBackground();
        background = backgroundArr[backgroundCount];
        objects = loader.getObjects();

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
        Texture playerDiedUp = new Texture(72, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\died_up.png");
        playerTextures.put("died_up", playerDiedUp.getTextures());
        Texture playerDiedDown = new Texture(83, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\died_down.png");
        playerTextures.put("died_down", playerDiedDown.getTextures());
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
