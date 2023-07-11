package academy.mindswap.p1g2.casino.server.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class PlaySound {
    private Clip clip;

    public PlaySound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }
    public void stop() {
        clip.stop();
        clip.flush();
    }

}
