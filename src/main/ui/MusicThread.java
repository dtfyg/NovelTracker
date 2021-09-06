package ui;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MusicThread implements Runnable {
    private Clip clipC;
    private boolean star = false;
    private List<String> playlist = new ArrayList<>();
    private List<Integer> error = new ArrayList<>();
    int prev;
    int cur;
    int max = 27;
    int min = 1;
    int range = max - min +  1;

    public MusicThread() {
        init();
    }

    @Override
    public void run() {
        try {
            int n = (int) (Math.random() * range) + min;
            while (prev == n) {
                n = (int) (Math.random() * range) + min;
            }
            cur = n;
            error.add(n);
            CountDownLatch syncLatch = new CountDownLatch(1);
            AudioInputStream sound = listOfSongs(n);

            clipC = AudioSystem.getClip();
            clipC.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) {
                    syncLatch.countDown();
                }
            });
            System.out.println("Current Song: " + n + ". " + playlist.get(n - 1));
            System.out.println("-------------------------------------------");
            clipC.open(sound);
            clipC.start();
            syncLatch.await();
            prev = n;
            run();
        } catch (UnsupportedAudioFileException u) {
            System.out.println("Unsupported Audio");
            System.out.println(error);
            System.out.println("Failed on " + cur);
        } catch (IOException i) {
            System.out.println("IOException");
            System.out.println(error);
            System.out.println("Failed on " + cur);
        } catch (Exception e) {
            System.out.println("Random Error");
            System.out.println(error);
            System.out.println("Failed on " + cur);
        }

    }


    public AudioInputStream listOfSongs(int n) throws UnsupportedAudioFileException, IOException {
        AudioInputStream s;
        switch (n) {
            case 1:
                s = AudioSystem.getAudioInputStream(new File("./data/Game/CelesteSoundtrack.wav"));
                break;
            case 2:
                s = AudioSystem.getAudioInputStream(new File("./data/Game/mantisLords.wav"));
                break;
            case 3:
                s = AudioSystem.getAudioInputStream(new File("./data/Game/Ori.wav"));
                break;
            case 4:
                s = AudioSystem.getAudioInputStream(new File("./data/Game/OriRestoring.wav"));
                break;
            case 5:
                s = AudioSystem.getAudioInputStream(new File("./data/Game/ToTheMoon.wav"));
                break;
            case 6:
                s = AudioSystem.getAudioInputStream(new File("./data/Game/Anima.wav"));
                break;
            case 7:
                s = AudioSystem.getAudioInputStream(new File("./data/Game/Undyne.wav"));
                break;
            case 8:
                s = AudioSystem.getAudioInputStream(new File("./data/Game/SpiderDance.wav"));
                break;
            case 9:
                s = AudioSystem.getAudioInputStream(new File("./data/Pop/SomeNights.wav"));
                break;
            case 10:
                s = AudioSystem.getAudioInputStream(new File("./data/Pop/Stitches.wav"));
                break;
            case 11:
                s = AudioSystem.getAudioInputStream(new File("./data/Pop/StoneCold.wav"));
                break;
            case 12:
                s = AudioSystem.getAudioInputStream(new File("./data/Pop/SmoothCriminal.wav"));
                break;
            case 13:
                s = AudioSystem.getAudioInputStream(new File("./data/Pop/TeenageDream.wav"));
                break;
            case 14:
                s = AudioSystem.getAudioInputStream(new File("./data/Pop/SaySomething.wav"));
                break;
            case 15:
                s = AudioSystem.getAudioInputStream(new File("./data/Pop/AllOfMe.wav"));
                break;
            case 16:
                s = AudioSystem.getAudioInputStream(new File("./data/Pop/7years.wav"));
                break;
            case 17:
                s = AudioSystem.getAudioInputStream(new File("./data/Pop/MadWorld.wav"));
                break;
            case 18:
                s = AudioSystem.getAudioInputStream(new File("./data/Weeb/littleDYK.wav"));
                break;
            case 19:
                s = AudioSystem.getAudioInputStream(new File("./data/Weeb/LoneliestGirl.wav"));
                break;
            case 20:
                s = AudioSystem.getAudioInputStream(new File("./data/Weeb/Sincerely.wav"));
                break;
            case 21:
                s = AudioSystem.getAudioInputStream(new File("./data/Weeb/Inferno.wav"));
                break;
            case 22:
                s = AudioSystem.getAudioInputStream(new File("./data/Weeb/Dorororo.wav"));
                break;
            case 23:
                s = AudioSystem.getAudioInputStream(new File("./data/Weeb/CryingForRain.wav"));
                break;
            case 24:
                s = AudioSystem.getAudioInputStream(new File("./data/Weeb/Charles.wav"));
                break;
            case 25:
                s = AudioSystem.getAudioInputStream(new File("./data/Weeb/BloodyStream.wav"));
                break;
            case 26:
                s = AudioSystem.getAudioInputStream(new File("./data/Weeb/Angel.wav"));
                break;
            default:
                s = AudioSystem.getAudioInputStream(new File("./data/Game/Tetris99.wav"));
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
        playlist.add("Deemo - Anima");
        playlist.add("Undertale - Battle against a True Hero");
        playlist.add("Undertale - Spider Dance Remix");
        playlist.add("FUN - Some Nights");
        playlist.add("Shawn Mendes - Stitches");
        playlist.add("Angelica Hales - Stone Cold");
        playlist.add("GLEE - Smooth Criminal");
        playlist.add("Katy Perry - Teenage Dream");
        playlist.add("Pentatonix - Say Something");
        playlist.add("John Legend - All of Me");
        playlist.add("Lukas Grahms - 7 years");
        playlist.add("Gary Jules - Mad World");
        playlist.add("Nightcore - Little Do you Know");
        playlist.add("Carol and Tuesday - Loneliest Girl");
        playlist.add("Violet Evergarden - Sincerely");
        playlist.add("Fire Force - Inferno");
        playlist.add("Dororo - Opening theme");
        playlist.add("Minami - Crying for Rain");
        playlist.add("Charles - Self Cover");
        playlist.add("JoJo - Bloody Stream");
        playlist.add("Nightcore - Angel with a Shotgun");
        playlist.add("Tetris 99 - Main Theme");
    }

    public void stopClip() {
        clipC.close();
    }
}
