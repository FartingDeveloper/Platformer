import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * Created by HP PC on 08.03.2017.
 */
public class Menu implements MouseListener {

    private BufferedImage menu;
    private Rectangle startGame = new Rectangle(200, 200, 200, 200);

    public Menu(String path) {
        menu = ImageLoader.loadImage(path);
    }

    public void render(Graphics g) {
        g.drawImage(menu, (int)startGame.getX(), (int)startGame.getY(), null);

        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(startGame);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (x >= startGame.getX() && x <= (startGame.getX() + startGame.getWidth()) && y >= startGame.getY() && y <= (startGame.getY() + startGame.getHeight())) {
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