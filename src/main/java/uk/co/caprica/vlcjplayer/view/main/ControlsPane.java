/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2015 Caprica Software Limited.
 */

package uk.co.caprica.vlcjplayer.view.main;

import static uk.co.caprica.vlcjplayer.Application.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.player.base.LibVlcConst;
import uk.co.caprica.vlcjplayer.Application;
import uk.co.caprica.vlcjplayer.event.PausedEvent;
import uk.co.caprica.vlcjplayer.event.PlayingEvent;
import uk.co.caprica.vlcjplayer.event.ShowEffectsEvent;
import uk.co.caprica.vlcjplayer.event.StoppedEvent;
import uk.co.caprica.vlcjplayer.view.BasePanel;
import uk.co.caprica.vlcjplayer.view.action.mediaplayer.MediaPlayerActions;

import com.google.common.eventbus.Subscribe;
import uk.co.caprica.vlcjplayer.view.util.AlertBox;

final class ControlsPane extends BasePanel {

    private final Icon playIcon = newIcon("play");

    private final Icon pauseIcon = newIcon("pause");

    private final Icon previousIcon = newIcon("previous");

    private final Icon nextIcon = newIcon("next");

    private final Icon fullscreenIcon = newIcon("fullscreen");

    private final Icon extendedIcon = newIcon("extended");

    private final Icon snapshotIcon = newIcon("snapshot");

    private final Icon volumeHighIcon = newIcon("volume-high");


    private final Icon volumeMutedIcon = newIcon("volume-muted");

    private final Icon addFavouriteFolder = newIcon("addFavouriteFolder");
    private final Icon favButtonIcon = newIcon("favorite");

    private final Icon moveFavouriteFolderIcon = newIcon("moveFavouriteFolder");


    private final JButton playPauseButton;

    private final JButton previousButton;

    private final JButton stopButton;

    private final JButton nextButton;

    private final JButton fullscreenButton;

    private final JButton extendedButton;

    private final JButton snapshotButton;

    private final JButton muteButton;
    private final JButton addFolder;

    private final JButton moveFavouriteFolder;

    private final List<JButton> favFolderList=new ArrayList<>();

    private final JSlider volumeSlider;

    ControlsPane(MediaPlayerActions mediaPlayerActions) {
        playPauseButton = new BigButton();
        playPauseButton.setAction(mediaPlayerActions.playbackPlayAction());

        previousButton = new StandardButton();
        previousButton.setIcon(previousIcon);

        stopButton = new StandardButton();
        stopButton.setAction(mediaPlayerActions.playbackStopAction());

        nextButton = new StandardButton();
        nextButton.setIcon(nextIcon);


        fullscreenButton = new StandardButton();
        fullscreenButton.setIcon(fullscreenIcon);
        extendedButton = new StandardButton();
        extendedButton.setIcon(extendedIcon);
        snapshotButton = new StandardButton();
        snapshotButton.setAction(mediaPlayerActions.videoSnapshotAction());
        addFolder = new StandardButton();
        addFolder.setHorizontalAlignment(SwingConstants.LEFT);
        addFolder.setText("Add Folder");
        addFolder.setToolTipText("Add new Favourite folder for moving files");
        addFolder.setIcon(addFavouriteFolder);




        moveFavouriteFolder = new StandardButton();
        moveFavouriteFolder.setText("Move Folder");
        moveFavouriteFolder.setToolTipText("Move Favourite folder to place you like");
        moveFavouriteFolder.setIcon(moveFavouriteFolderIcon);



        muteButton = new StandardButton();
        muteButton.setIcon(volumeHighIcon);
        volumeSlider = new JSlider();
        volumeSlider.setMinimum(LibVlcConst.MIN_VOLUME);
        volumeSlider.setMaximum(LibVlcConst.MAX_VOLUME);



        setLayout(new MigLayout("fill, insets 0 0 0 0", "[]12[]0[]0[]12[]0[]12[]12[]10[]push[]0[]", "[]"));

        add(playPauseButton);
        add(previousButton, "sg 1");
        add(stopButton, "sg 1");
        add(nextButton, "sg 1");

        add(fullscreenButton, "sg 1");
        add(extendedButton, "sg 1");

        add(snapshotButton, "sg 1");
        add(addFolder);
        add(moveFavouriteFolder);


        add(muteButton, "sg 1");
        add(volumeSlider, "wmax 100");

        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                application().mediaPlayer().audio().setVolume(volumeSlider.getValue());
            }
        });

        // FIXME really these should share common actions
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                application().PlayPrevVideo();

            }
        });
        addFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String folder = JOptionPane.showInputDialog("Enter new folder name:");
                if(Application.application().isFolderExist(folder)){
                    AlertBox.infoBox("Favourite Folder Already created with this name","Already exist folder");
                    return;
                }
                JButton newFolder=new StandardButton();
                newFolder.setText(folder);
                add(newFolder);
                favFolderList.add(newFolder);
                newFolder.addActionListener(new FavFolderButtonEventListener());
                Application.application().addFavFolder(folder);
            }
        });
        moveFavouriteFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                application().moveFilesToFolder();
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                application().playNext();
            }
        });
        muteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                application().mediaPlayer().audio().mute();
            }
        });

        fullscreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                application().mediaPlayer().fullScreen().toggle();
            }
        });

        extendedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                application().post(ShowEffectsEvent.INSTANCE);
            }
        });
        Application.application().setFavFolderButtonList(favFolderList);
        Application.application().setFavButtonIcon(favButtonIcon);
    }

    @Subscribe
    public void onPlaying(PlayingEvent event) {
        playPauseButton.setIcon(pauseIcon); // FIXME best way to do this? should be via the action really?
    }

    @Subscribe
    public void onPaused(PausedEvent event) {
        playPauseButton.setIcon(playIcon); // FIXME best way to do this? should be via the action really?
    }

    @Subscribe
    public void onStopped(StoppedEvent event) {
        playPauseButton.setIcon(playIcon); // FIXME best way to do this? should be via the action really?
    }

    private class BigButton extends JButton {

        private BigButton() {
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setHideActionText(true);
        }
    }

    private class StandardButton extends JButton {

        private StandardButton() {
            setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            setHideActionText(true);
        }
    }

    private Icon newIcon(String name) {
        return new ImageIcon(getClass().getResource("/icons/buttons/" + name + ".png"));
    }

}


