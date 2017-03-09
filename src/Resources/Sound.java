package Resources;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by HP PC on 08.03.2017.
 */
public class Sound {
    Clip clip = null;

    public Sound(String musicPath){
        loadMusic(musicPath);
    }

    //Скопипастил А ееее
    private void loadMusic(String musicPath){
        try {
            File soundFile = new File(musicPath); //Звуковой файл

            //Получаем AudioInputStream
            //Вот тут могут полететь IOException и UnsupportedAudioFileException
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);

            //Получаем реализацию интерфейса Clip
            //Может выкинуть LineUnavailableException
            clip = AudioSystem.getClip();

            //Загружаем наш звуковой поток в Clip
            //Может выкинуть IOException и LineUnavailableException
            clip.open(ais);

            clip.setFramePosition(0); //устанавливаем указатель на старт
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
        }
    }

    public void start(){
        clip.start();
    }
}
