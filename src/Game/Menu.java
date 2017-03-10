package Game;

import Resources.ImageLoader;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import static java.lang.System.exit;

/**
 * Created by HP PC on 09.03.2017.
 */
public class Menu implements MouseListener {

    private int width;
    private int height;

    private boolean firstTime = true;
    private int index = 0;

    private BufferedImage[] menu;
    private int count;
    private Rectangle start = new Rectangle(200, 250, 210, 75);
    private Rectangle exit = new Rectangle(200, 350, 200, 75);

    public Menu(int width, int height, String[] path) {
        this.width = width;
        this.height = height;
        menu = ImageLoader.loadImages(path);
    }

    public void render(Graphics g, int camera) {
        index++;

        if(firstTime || count < 2){
            menuAnimation();
        }

        g.drawImage(menu[count], camera, 0, width, height, null);
        start.x = camera + 300;
        exit.x = camera + 300;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.draw(start);
        g2d.draw(exit);
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
        int x = e.getX();
        int y = e.getY();

        if (x >= start.getX() && x <= (start.getX() + start.getWidth()) && y >= start.getY() && y <= (start.getY() + start.getHeight())) {
                Game.setPause(false);
                firstTime = false;
        }
        if (x >= exit.getX() && x <= (exit.getX() + exit.getWidth()) && y >= exit.getY() && y <= (exit.getY() + exit.getHeight())) {
            exit(0);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

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
