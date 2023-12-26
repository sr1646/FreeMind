package uk.co.caprica.vlcjplayer.view.main;

import uk.co.caprica.vlcjplayer.Application;
import uk.co.caprica.vlcjplayer.view.util.AlertBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FavFolderButtonEventListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event) {
            Object o = event.getSource();
            JButton b = null;
            String favFolderName = "";

            if(o instanceof JButton){
                b = (JButton)o;
            }else {
                AlertBox.infoBox("Can not add to favourite as folder name is blank","Invalid folder name");
                return;
            }
            if(b == null){
                AlertBox.errorBox("Something went wrong Button object not found during cast","internal error");
                return;
            }
            favFolderName = b.getToolTipText();

            Application.application().addFavouriteFileTo(favFolderName);
        }
}
