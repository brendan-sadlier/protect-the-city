package util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundEffect {

    public void playSound(String sound) {
        try {

            File soundFile = new File(sound);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            // Get a sound clip resource
            Clip clip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream
            clip.open(audioIn);
            clip.start();

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading audio file");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Audio line unavailable");
            e.printStackTrace();
        }
    }
}
