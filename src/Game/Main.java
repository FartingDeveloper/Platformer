package Game;

import Loaders.Sound;

import javax.swing.*;
import java.awt.*;

/**
 * Created by HP PC on 24.02.2017.
 */
public class Main {
    public static void main(String[] args) {

        String musicPath = "theme.wav";
        Sound sound = new Sound(musicPath);

        Dimension dim = new Dimension(800, 540);

        Game loop = new Game();
        loop.setPreferredSize(dim);
        loop.setFocusable(true);

        JFrame frame = new JFrame();
        frame.add(loop);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        loop.start();
        sound.start();

        frame.setVisible(true);
    }
}
