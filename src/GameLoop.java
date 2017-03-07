import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import Objects.*;
import javafx.scene.layout.Background;
import sun.audio.AudioPlayer;


/**
 * Created by HP PC on 24.02.2017.
 */
public class GameLoop extends JComponent {

    private Player player;
    private List<GameObject> objects;
    private BufferedImage background;
    private BufferedImage[] backgroundArr;
    private int backgroundCount;

    private Font font = new Font("Impact", Font.PLAIN, 24);
    private Color color = new Color(1, 1, 1, 0.65f);
    private int hour = 10;
    private int minute = 13;

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

                if(System.currentTimeMillis() - timer > 1000){
                    background = backgroundArr[backgroundCount];
                    backgroundCount = new Random().nextInt(backgroundArr.length);
                    timer += 1000;
                    if(timer % 6000 == 0){
                        minute++;
                    }
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
        String[] backPath = {"C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\city2_VHS.jpg"};
//        String[] backPath = {"C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\city2_1.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\city2_2.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\city2_3.jpg"};

        String[] texturePath = {"C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\stone.jpg"};

        String musicPath = "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\theme.wav";

        LevelLoader loader = new LevelLoader("C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\map.png", backPath, texturePath, musicPath);
        loader.loadLevel();

        player = loader.getPlayer();
        this.addKeyListener(player);

        objects = loader.getObjects();

        backgroundArr = loader.getBackground();
        background = backgroundArr[backgroundCount];
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

            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.setFont(font);
            g2d.drawString("PM " + hour + ":" + minute, player.getX() - player.getWidth()*2, 500);
            g2d.drawString("MAR. 03 1984", player.getX() - player.getWidth()*2, 530);

            g.dispose();
        }
    }

}
