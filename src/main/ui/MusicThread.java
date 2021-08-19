package ui;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MusicThread implements Runnable {
    private Clip clipC;
    private boolean star = false;
    private List<String> playlist = new ArrayList<>();

    public MusicThread() {
        init();
    }

    @Override
    public void run() {
        try {
            int n = (int) (Math.random() * 6) + 1;
            CountDownLatch syncLatch = new CountDownLatch(1);
            AudioInputStream sound = listOfSongs(n);

            clipC = AudioSystem.getClip();
            clipC.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) {
                    syncLatch.countDown();
                }
            });
            System.out.println("Current Song: " + playlist.get(n - 1));
            System.out.println("-------------------------------------");
            clipC.open(sound);
            clipC.start();
            syncLatch.await();
            run();
        } catch (Exception e) {
            System.out.println("Sound exception");
        }

    }


    public AudioInputStream listOfSongs(int n) throws UnsupportedAudioFileException, IOException {
        AudioInputStream s;
        switch (n) {
            case 1:
                s = AudioSystem.getAudioInputStream(new File("./data/CelesteSoundtrack.wav"));
                break;
            case 2:
                s = AudioSystem.getAudioInputStream(new File("./data/mantisLords.wav"));
                break;
            case 3:
                s = AudioSystem.getAudioInputStream(new File("./data/Ori.wav"));
                break;
            case 4:
                s = AudioSystem.getAudioInputStream(new File("./data/OriRestoring.wav"));
                break;
            case 5:
                s = AudioSystem.getAudioInputStream(new File("./data/ToTheMoon.wav"));
                break;
            default:
                s = AudioSystem.getAudioInputStream(new File("./data/Tetris99.wav"));
                break;
        }
        return s;
    }

    public void init() {
        playlist.add("Celeste - Resurrections");
        playlist.add("Hollow Knight - Mantis Lords");
        playlist.add("Ori and the Blind Forest - Lost in the Storm");
        playlist.add("Ori and the Blind Forest - Restoring the Light, Facing the dark");
        playlist.add("To The Moon - Ending Theme");
        playlist.add("Tetris 99 - Main Theme");
    }

    public void stopClip() {
        clipC.close();
    }
}
