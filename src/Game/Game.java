package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import Objects.*;
import Loaders.ImageLoader;
import Loaders.LevelLoader;

import static java.lang.System.exit;

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

    private static boolean pause = true;

    private Thread thread = new Thread(this);

    public Game(){
        String[] menuPath ={"opening/opening.jpg", "opening/opening_second.jpg", "opening/opening_retry.jpg", "opening/opening_continue.jpg"};

        menu = new Menu(menuPath);
        this.addMouseListener(menu);

        String[] backPath = {"city2_VHS.jpg", "city2_VHS_2.jpg", "city2_VHS_3.jpg", "tv_screen.png"};

        String[] texturePath = {"stone.jpg"};

        loader = new LevelLoader(800, 540, backPath, texturePath);
//        loader.loadLevel("res\\map.png");

        objects = loader.loadLevel();

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
            if(getMouseListeners() == null) addMouseListener(menu);
            if(!menu.isFirstTime()){
                if(player.isDead()){
                    menu.setMenu(2);
                }
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
                        enemy.setX(camera + getWidth() + new Random().nextInt(200));
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

    public static void setPause(boolean pause) {
        Game.pause = pause;
    }

    private class Menu implements MouseListener {

        private boolean firstTime = true;
        private int index = 0;

        private BufferedImage[] menu;
        private int count;
        private Rectangle start = new Rectangle(200, 250, 210, 75);
        private Rectangle exit = new Rectangle(200, 350, 200, 75);

        public Menu(String[] path) {
            menu = ImageLoader.loadImages(path);
        }

        public void render(Graphics g, int camera) {
            index++;

            if(firstTime && count < 2){
                menuAnimation();
            }

            g.drawImage(menu[count], camera, 0, getWidth(), getHeight(), null);
            start.x = camera + 300;
            exit.x = camera + 300;

//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setColor(Color.BLUE);
//        g2d.draw(start);
//        g2d.draw(exit);
        }

        private void menuAnimation(){
            count = 0;
            if(index > 50){
                count = 1 - count;
                index = 45;
            }
        }

        public void setMenu(int i){
            count = i;
            firstTime = false;
        }

        public boolean isFirstTime() {
            return firstTime;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (x >= start.getX() && x <= (start.getX() + start.getWidth()) && y >= start.getY() && y <= (start.getY() + start.getHeight())) {
                if(count == 2){
                    objects = loader.loadLevel();
                    player.setKillCount(0);
                    player = loader.getPlayer();
                    addKeyListener(player);
                }
                pause = false;
                firstTime = false;
                addMouseListener(null);
            }
            if (x >= exit.getX() && x <= (exit.getX() + exit.getWidth()) && y >= exit.getY() && y <= (exit.getY() + exit.getHeight())) {
                exit(0);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }
}
