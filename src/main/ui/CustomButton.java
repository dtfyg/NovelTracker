package ui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class CustomButton extends JToggleButton implements ActionListener {

    private Image imgDesign;
    private Clip clipC;
    private boolean pressed = false;

    public CustomButton(Image img) {
        imgDesign = img;
        enableInputMethods(true);
        addActionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawOval(getX(),getY(),getWidth(),getHeight());
       // g.drawImage(imgDesign, imgDesign.getWidth(null), imgDesign.getHeight(null), null);
    }


    private void playCeleste(boolean stop) {
        try {
            if (!stop) {
                AudioInputStream sound = AudioSystem.getAudioInputStream(new File("./data/CelesteSoundtrack.wav"));
                clipC = AudioSystem.getClip();
                clipC.open(sound);
                clipC.start();
            } else {
                clipC.close();
            }
        } catch (Exception e) {
            System.err.println("Sound error");
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        playCeleste(pressed);
        if (pressed) {
            pressed = false;
        } else {
            pressed = true;
        }
    }
}
