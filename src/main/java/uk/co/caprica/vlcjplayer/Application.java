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

package uk.co.caprica.vlcjplayer;

import com.google.common.eventbus.EventBus;
import sr.utility.CollectionHelper;
import sr.utility.FileHelper;
import sr.utility.StringUtil;
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.component.callback.ScaledCallbackImagePainter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.renderer.RendererItem;
import uk.co.caprica.vlcjplayer.event.TickEvent;
import uk.co.caprica.vlcjplayer.view.action.mediaplayer.MediaPlayerActions;
import uk.co.caprica.vlcjplayer.view.main.MainFrame;
import uk.co.caprica.vlcjplayer.view.util.AlertBox;

import javax.swing.*;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Global application state.
 */
public final class Application {

    private static final String RESOURCE_BUNDLE_BASE_NAME = "strings/vlcj-player";

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME);

    private static final int MAX_RECENT_MEDIA_SIZE = 10;

    private final EventBus eventBus;

    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    private final CallbackMediaPlayerComponent callbackMediaPlayerComponent;

    private final MediaPlayerActions mediaPlayerActions;

    private final ScheduledExecutorService tickService = Executors.newSingleThreadScheduledExecutor();

    private final Deque<String> recentMedia = new ArrayDeque<>(MAX_RECENT_MEDIA_SIZE);
    private List<File> fileList;
    private String sourceFolder;
    private String destinationFolder;
    private boolean isFileListAvailable=false;
    private int fileTracker=0;
    private int totalFiles=0;
    private File currentlyPlayingFile;
    private List<File> favFileList=new ArrayList<File>();
    private Map<String,List<File>> favFolder =new HashMap<String,List<File>>();
    private List<JButton> favFolderButtonList;
    private Icon favButtonIcon;
    public void PlayPrevVideo() {
        if(fileTracker>1){
            this.fileTracker-=2;
        }else{
            fileTracker=0;
        }
        application().playNext();
    }


    /**
     * Video output can be "EMBEDDED" for the usual hardware-accelerated playback, or "CALLBACK" for the software or
     * "direct-rendering" approach.
     */
    private VideoOutput videoOutput = VideoOutput.CALLBACK;


    public File getCurrentlyPlayingFile() {
        return currentlyPlayingFile;
    }



    public void setFavFolderButtonList(List<JButton> favFolderButtonList) {
        this.favFolderButtonList = favFolderButtonList;
    }

    public void setFavButtonIcon(Icon favButtonIcon) {
        this.favButtonIcon = favButtonIcon;
    }

    public void setSourceFolder(String sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public void setDestinationFolder() {
        if(StringUtil.isEmpty(sourceFolder) || CollectionHelper.isEmpty(favFileList)){
            AlertBox.warningBox("Please Select few video as Favourite First","No video found");
            return;
        }

        File selectedFolder=MainFrame.mainFrame().getSelectedFolder();
        if(selectedFolder!=null){
            String fileDetail=favFolder.entrySet().stream()
                    .map(e -> e.getKey() + "\n++++++++++++++++\n " + e.getValue().toString().replaceAll(", ","\n")+"\n")
                    .collect(Collectors.joining("\n------------------------------------------------------\n"));
            if(MainFrame.mainFrame().moveConfirmation(fileDetail)){
                MainFrame.mainFrame().stopVedeo();
                Application.application().playFile("doNotDeleteSensitiveForApp.mp4");
                MainFrame.mainFrame().stopVedeo();
                this.destinationFolder = selectedFolder.getAbsolutePath();
                StringBuilder favoriteDestination;
                FileHelper fh=new FileHelper();
                for(Map.Entry<String,List<File>> favFolderEntry:favFolder.entrySet()) {
                    favoriteDestination=new StringBuilder(destinationFolder).append(File.separator).append(favFolderEntry.getKey());
                    fh.moveFilesWithSameFolderStructureOnDestination(this.sourceFolder,favoriteDestination.toString(),favFolderEntry.getValue());
                }
                application().initFileList(new File(sourceFolder));
            }
        }else{
            AlertBox.warningBox("Not moving any file as destination folder not selected","No Action Taken");
        }
    }


    private static final class ApplicationHolder {
        private static final Application INSTANCE = new Application();
    }

    public static Application application() {
        return ApplicationHolder.INSTANCE;
    }

    public static ResourceBundle resources() {
        return resourceBundle;
    }

    private Application() {
        eventBus = new EventBus();

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        callbackMediaPlayerComponent = new CallbackMediaPlayerComponent(null, null, null, true, new ScaledCallbackImagePainter());

        mediaPlayerActions = new MediaPlayerActions(mediaPlayerComponent.mediaPlayer());
        tickService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                eventBus.post(TickEvent.INSTANCE);
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void subscribe(Object subscriber) {
        eventBus.register(subscriber);
    }

    public void post(Object event) {
        // Events are always posted and processed on the Swing Event Dispatch thread
        if (SwingUtilities.isEventDispatchThread()) {
            eventBus.post(event);
        }
        else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    eventBus.post(event);
                }
            });
        }
    }

    public EmbeddedMediaPlayerComponent mediaPlayerComponent() {
        return mediaPlayerComponent;
    }

    public CallbackMediaPlayerComponent callbackMediaPlayerComponent() {
        return callbackMediaPlayerComponent;
    }

    public EmbeddedMediaPlayer mediaPlayer() {
        switch (videoOutput) {
            case EMBEDDED:
                return mediaPlayerComponent.mediaPlayer();
            case CALLBACK:
                return callbackMediaPlayerComponent.mediaPlayer();
            default:
                throw new IllegalStateException();
        }
    }

    public VideoOutput videoOutput() {
        return videoOutput;
    }

    public MediaPlayerActions mediaPlayerActions() {
        return mediaPlayerActions;
    }

    public void addRecentMedia(String mrl) {
        if (!recentMedia.contains(mrl)) {
            recentMedia.addFirst(mrl);
            while (recentMedia.size() > MAX_RECENT_MEDIA_SIZE) {
                recentMedia.pollLast();
            }
        }
    }

    public List<String> recentMedia() {
        return new ArrayList<>(recentMedia);
    }

    public void clearRecentMedia() {
        recentMedia.clear();
    }

    public void setRenderer(RendererItem renderer) {
        mediaPlayerComponent.mediaPlayer().setRenderer(renderer);
    }

    public void initFileList(File folder){
        this.fileList=FileHelper.getSpecificTypeFiles(folder.toPath(),FileHelper.VEDEO_FILES_EXTENSION);
        if(CollectionHelper.isNotEmpty(fileList)){
            isFileListAvailable=true;
            totalFiles=fileList.size();
            fileTracker=0;
        }else{
            isFileListAvailable=false;
            totalFiles=0;
            fileTracker=0;
        }
    }
    public void playNext(){
        if(isFileListAvailable){
            if(fileTracker<totalFiles){
                playFile(fileList.get(fileTracker++));

            }else {
               AlertBox.infoBox("Last Video reached, Starting again from beginning","Last Video");
               fileTracker=0;
            }
        }else {
            //do nothing
        }

    }

    public void playFile(String absoluteFilePath) {
        MainFrame.mainFrame().setTitle("Playing: "+absoluteFilePath);
        currentlyPlayingFile=new File(absoluteFilePath);
        mediaPlayer().media().play(absoluteFilePath);
        setFavourite();
    }
    public void playFile(File absoluteFilePath) {
        currentlyPlayingFile=absoluteFilePath;
        playFile(absoluteFilePath.getAbsolutePath());
    }
    public void addFavFolder(String folderName){
        List<File> fileList=new ArrayList<>();
        favFolder.put(folderName,fileList);
    }
    public void addFavouriteFileTo(String favFolderName) {
        if(favFileList.contains(currentlyPlayingFile)){
            if(favFolder.get(favFolderName).contains(currentlyPlayingFile)){//case when we are removing file from favourite folder
                favFileList.remove(currentlyPlayingFile);
                favFolder.get(favFolderName).remove(currentlyPlayingFile);
                setAllButtonAsNormal();
            }else{ // case when we are changing favourite folder i.e. from folder 1 to folder 2
                //1. iterate full map for find in which folder the file is set as favourite
                for(Map.Entry<String,List<File>> favFolderEntry:favFolder.entrySet()){
                    if(favFolderEntry.getValue().contains(currentlyPlayingFile)){
                        //2. remove file from current favourite folder
                        favFolderEntry.getValue().remove(currentlyPlayingFile);
                        break;
                    }
                }
                //3. add file into requested favourite folder
                favFolder.get(favFolderName).add(currentlyPlayingFile);
                setFavourite(favFolderName);
            }
        }else {
            favFileList.add(currentlyPlayingFile);
            favFolder.get(favFolderName).add(currentlyPlayingFile);
            setFavourite(favFolderName);
        }

    }

    private void setFavourite(String favFolderName) {
        for(JButton button:favFolderButtonList){
            if(button.getText().equalsIgnoreCase(favFolderName)){
                    button.setIcon(favButtonIcon);
            }else {
                button.setIcon(null);
            }
        }
    }
    private void setFavourite() {
        if(favFileList.contains(currentlyPlayingFile)){
            String currentFavFolder="";
            boolean favouriteFolderFound=false;
            for(Map.Entry<String,List<File>> favFolderEntry:favFolder.entrySet()) {
                if (favFolderEntry.getValue().contains(currentlyPlayingFile)) {
                    currentFavFolder=favFolderEntry.getKey();
                    favouriteFolderFound=true;
                    break;
                }
            }
            if(favouriteFolderFound){
                setFavourite(currentFavFolder);
            }else{
                AlertBox.warningBox("File is favourite but does not contain in any folder","internal error");
                setAllButtonAsNormal();
            }

        }else{
            //set all button as normal
            setAllButtonAsNormal();
        }
    }

    private void setAllButtonAsNormal() {
        for(JButton button:favFolderButtonList){
            button.setIcon(null);
        }
    }
    public boolean isFolderExist(String folderName){
        if(favFolder.containsKey(folderName)){
            return true;
        }
            return false;
    }
}
