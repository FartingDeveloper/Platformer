package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import Objects.*;
import Resources.LevelLoader;

/**
 * Created by HP PC on 24.02.2017.
 */
public class Game extends JComponent implements Runnable{
    private Menu menu;

    private Player player;
    private List<GameObject> objects;
    private BufferedImage background;
    private BufferedImage[] backgroundArr;
    private int backgroundCount = 0;

    private Font font = new Font("Impact", Font.PLAIN, 32);
    private Color color = new Color(1, 1, 1, 0.65f);
    private int hour = 10;
    private int minute = 13;

    private static boolean pause = false;

    private Thread thread = new Thread(this);

    public Game(){
        String[] menuPath ={"C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game.Game\\res\\start.png"};

        menu = new Menu(menuPath);
        this.addMouseListener(menu);

        String[] backPath = {"C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game.Game\\res\\city2_VHS.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game.Game\\res\\city2_VHS_2.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game.Game\\res\\city2_VHS_3.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game.Game\\res\\tv_screen.png"};

        String[] texturePath = {"C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game.Game\\res\\stone.jpg"};

        LevelLoader loader = new LevelLoader("C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game.Game\\res\\map.png", backPath, texturePath);
        loader.loadLevel();

        player = loader.getPlayer();
        this.addKeyListener(player);

        objects = loader.getObjects();

        backgroundArr = loader.getBackground();
        background = backgroundArr[0];
    }

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
                timer += 1000;
                backgroundCount = new Random().nextInt(backgroundArr.length - 1);
                background = backgroundArr[backgroundCount++];
                if(backgroundCount == backgroundArr.length - 1) backgroundCount = 0;
                if(timer % 6000 == 0){
                    minute++;
                }
            }

            currentTime = now;
        }
    }

    public void start(){
        thread.start();
    }

    private void updateLogic(){
        if(!pause){
            for(GameObject object : objects) object.update();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!pause){
            setFocusable(true);
            renderGame(g);
        }
        else {
            setFocusable(false);
            renderMenu(g);
        }

        g.drawImage(backgroundArr[backgroundArr.length - 1], (int) player.getX() - player.getWidth()*4, 0 , getWidth(), getHeight(), null);// Рамка

    }

    private void renderGame(Graphics g){
        g.translate(-(int) player.getX() + player.getWidth()*4, 0);

        for(int x = -background.getWidth(); x < background.getWidth()*100; x += background.getWidth()){
            g.drawImage(background, x, 0, null);
        }

        for(GameObject object : objects){
            object.render(g);
        }

        if(player.getHealth() > 0){
            g.setColor(Color.RED);
            g.fillRect((int)player.getX() - player.getWidth()*2, 50, player.getHealth(), 10);
        }


        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.setFont(font);
        g2d.drawString("PM " + hour + ":" + minute, player.getX() - player.getWidth()*2, 480);
        g2d.drawString("MAR. 03 1984", player.getX() - player.getWidth()*2, 510);
        g2d.drawString("" + Player.getKillCount(), player.getX() + player.getWidth()*4, 60);

        g.dispose();
    }

    private void renderMenu(Graphics g){

    }

    public static boolean isPause() {
        return pause;
    }

    public static void setPause(boolean pause) {
        Game.pause = pause;
    }
}