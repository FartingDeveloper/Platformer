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

        HashMap<String, BufferedImage[][]> chineseTextures = new HashMap<>();
        Texture chineseTextureStandingRight = new Texture(48, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\chinese\\standing.png");
        chineseTextures.put("standing", chineseTextureStandingRight.getTextures());
        chineseTextures.put("walking", new Texture(50, 99, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\chinese\\walking.png").getTextures());
        chineseTextures.put("kick", new Texture(59, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\chinese\\kick.png").getTextures());
        chineseTextures.put("kick_second", new Texture(55, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\chinese\\kick_second.png").getTextures());
        chineseTextures.put("leg_kick", new Texture(64, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\chinese\\leg_kick.png").getTextures());
        chineseTextures.put("kicked_first", new Texture(50, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\chinese\\kicked_first.png").getTextures());
        chineseTextures.put("kicked_second", new Texture(50, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\chinese\\kicked_second.png").getTextures());
        chineseTextures.put("kicked_final", new Texture(47, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\chinese\\kicked_final.png").getTextures());
        chineseTextures.put("died_down", new Texture(88, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\chinese\\died_down.png").getTextures());
        chineseTextures.put("died_up", new Texture(61, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\chinese\\died_up.png").getTextures());
        Enemy enemy = new Chinese(300, 298, 48,  105, objects, GameObjectId.Enemy, chineseTextureStandingRight.getTextures()[0][0], chineseTextures);
        objects.add(enemy);

        HashMap<String, BufferedImage[][]> playerTextures = new HashMap<>();
        Texture playerTextureStandingRight = new Texture(48, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\player\\standing.png");
        playerTextures.put("standing", playerTextureStandingRight.getTextures());
        playerTextures.put("walking", new Texture(42, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\player\\walking.png").getTextures());
        playerTextures.put("kick", new Texture(60, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\player\\kick.png").getTextures());
        playerTextures.put("kick_2", new Texture(58, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\player\\kick_2.png").getTextures());
        playerTextures.put("leg_kick", new Texture(57, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\player\\leg_kick.png").getTextures());
        playerTextures.put("kicked_final_1", new Texture(47, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\player\\kicked_final_1.png").getTextures());
        playerTextures.put("kicked_final_2", new Texture(47, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\player\\kicked_final_2.png").getTextures());
        playerTextures.put("kicked_first",new Texture(56, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\player\\kicked_first.png").getTextures());
        playerTextures.put("kicked_second", new Texture(57, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\player\\kicked_second.png").getTextures());
        playerTextures.put("died_up", new Texture(72, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\player\\died_up.png").getTextures());
        playerTextures.put("died_down", new Texture(83, 105, "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\player\\died_down.png").getTextures());
        player = new Player(0, 298, 48,  105, objects, GameObjectId.Player, playerTextureStandingRight.getTextures()[0][0], playerTextures);
//        player = new Player(400, 100, 155, 311, objects, GameObjectId.Player, playerTextureRight.getTextures()[0][3], playerTextures);
        this.addKeyListener(player);
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
