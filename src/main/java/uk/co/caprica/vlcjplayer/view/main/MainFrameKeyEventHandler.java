package uk.co.caprica.vlcjplayer.view.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainFrameKeyEventHandler implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("keyReleased: "+e.getKeyChar());
    }
}
