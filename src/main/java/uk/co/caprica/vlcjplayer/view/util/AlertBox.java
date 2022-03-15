package uk.co.caprica.vlcjplayer.view.util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlertBox {
    public static void infoBox(String infoMessage, String titleBar)
    {
        JTextArea textarea= new JTextArea(infoMessage);
        textarea.setEditable(true);
        JOptionPane.showMessageDialog(null, textarea, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
    public static void errorBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.ERROR_MESSAGE);
    }
    public static void warningBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.WARNING_MESSAGE);
    }
    public static void flashMessage(String message,String Title){
        JOptionPane pane = new JOptionPane(message,
                JOptionPane.INFORMATION_MESSAGE);
        final JDialog dialog = pane.createDialog(null, Title);

        Timer timer = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
        dialog.dispose();
    }
    public static void destryableMessage(String message,String Title){

    }
}
