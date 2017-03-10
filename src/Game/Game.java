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
    private LevelLoader loader;

    private Player player;
    private List<GameObject> objects = new ArrayList<GameObject>();
    private BufferedImage background;
    private BufferedImage screen;
    private BufferedImage[] backgroundArr;
    private int backgroundCount = 0;

    private int camera;

    private Font font = new Font("Impact", Font.PLAIN, 32);
    private Color color = new Color(1, 1, 1, 0.65f);
    private int hour = 10;
    private int minute = 13;

    private static boolean pause = true;

    private Thread thread = new Thread(this);

    public Game(){
        String[] menuPath ={"C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\opening\\opening.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\opening\\opening_second.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\opening\\opening_retry.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\opening\\opening_continue.jpg"};

        menu = new Menu(800, 540, menuPath);
        this.addMouseListener(menu);

        String[] backPath = {"C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\city2_VHS.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\city2_VHS_2.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\city2_VHS_3.jpg", "C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\tv_screen.png"};

        String[] texturePath = {"C:\\Users\\HP PC\\IntelliJIDEAProjects\\Game\\res\\stone.jpg"};

        loader = new LevelLoader(800, 540, backPath, texturePath, objects);
//        loader.loadLevel("C:\\Users\\HP PC\\IntelliJIDEAProjects\Game\\res\\map.png");

        loader.loadLevel();

        player = loader.getPlayer();
        this.addKeyListener(player);

        backgroundArr = loader.getBackground();
        background = backgroundArr[0];
        screen = backgroundArr[backgroundArr.length - 1];

        camera = (int) player.getX() - player.getWidth()*4;
    }

    @Override
    public void run() {
        double frapTime = 1000000000 / 60; //Количество времени необходимое на обработку 1 кадра
        long currentTime = System.nanoTime();
        double delta = 0;
        long timer = System.currentTimeMillis();
        while(true){
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
            moveLevel();
            addEnemy();
        }
        else{
            if(!menu.isFirstTime()){
                if(player.isDead()) menu.setMenu(2);
                else menu.setMenu(3);
            }
        }
    }

    private void moveLevel(){
        for(GameObject object : objects){
            if (object.getId() == GameObjectId.Block){
                if((object.getX() + object.getWidth()) < camera){
                    Block lastBlock = loader.getLastBlock();
                    object.setX((int) (lastBlock.getX() + lastBlock.getWidth()));
                    continue;
                }
                if((object.getX() + object.getWidth()) > camera + getWidth()){
                    Block firstBlock = loader.getFirstBlock();
                    object.setX((int) (firstBlock.getX() - firstBlock.getWidth()));
                }
            }
        }
    }

    private void resurrectDead(){
        for(GameObject object : objects){
            if(object.getId() == GameObjectId.Enemy){
                Enemy enemy = (Enemy) object;
                if(enemy.isDead()){
                    if(enemy.getX() + enemy.getWidth() < camera || enemy.getX()+enemy.getWidth() > camera + getWidth()){
                        enemy.setDead(false);
                        enemy.setHealth(100);
                        enemy.setX(camera + getWidth()*2 - 200);
                        return;
                    }
                }
            }
        }
    }

    private void addEnemy(){
//        int i = 0;
//        for(GameObject object : objects){
//            if(object.getId() == GameObjectId.Enemy){
//                Enemy enemy = (Enemy) object;
//                if(!enemy.isDead()){
//                    if(enemy.getX() + enemy.getWidth() > camera && enemy.getX()+enemy.getWidth() < camera + getWidth()){
//                        i++;
//                    }
//                }
//            }
//        }

        resurrectDead();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        camera = (int) player.getX() - player.getWidth()*4;

        g.translate(-camera, 0);

        if(!pause) renderGame(g);

        else renderMenu(g);

        g.drawImage(screen, camera, 0 , getWidth(), getHeight(), null);// Рамка

        g.dispose();

    }

    private void renderGame(Graphics g){

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
//        g2d.drawString("PM " + hour + ":" + minute, player.getX() - player.getWidth()*2, 480);
//        g2d.drawString("MAR. 03 1984", player.getX() - player.getWidth()*2, 510);
        g2d.drawString("" + Player.getKillCount(), player.getX() + player.getWidth()*4, 60);

    }

    private void renderMenu(Graphics g){
        menu.render(g, camera);
    }

    public static boolean isPause() {
        return pause;
    }

    public static void setPause(boolean pause) {
        Game.pause = pause;
    }
}
