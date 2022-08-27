package uk.co.caprica.vlcjplayer.view.main;

import static uk.co.caprica.vlcjplayer.Application.application;

import java.awt.CardLayout;

import javax.swing.JPanel;

import uk.co.caprica.vlcjplayer.view.image.ImagePane;

final class VideoContentPane extends JPanel {

    private static final String NAME_DEFAULT = "default";

    private static final String NAME_VIDEO = "video";
    private static final String NAME_AUDIO = "music";

    private static final String NAME_CALLBACK_VIDEO = "callback-video";

    private final CardLayout cardLayout;
    private ImagePane music;
    VideoContentPane() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        music = new ImagePane(ImagePane.Mode.CENTER, getClass().getResource("/icons/media/music.png"), 0.3f);
        add(new ImagePane(ImagePane.Mode.CENTER, getClass().getResource("/vlcj-logo.png"), 0.3f), NAME_DEFAULT);
        add(music, NAME_AUDIO);
        add(application().mediaPlayerComponent(), NAME_VIDEO);
        add(application().callbackMediaPlayerComponent(), NAME_CALLBACK_VIDEO);
    }

    public void showDefault() {
        cardLayout.show(this, NAME_DEFAULT);
    }

    public void playVideo() {
        switch (application().videoOutput()) {
            case EMBEDDED:
                cardLayout.show(this, NAME_VIDEO);
                break;
            case CALLBACK:
                    cardLayout.show(this, NAME_CALLBACK_VIDEO);
                break;
            default:
                throw new IllegalStateException();
        }
    }
    public void playAudio() {
        switch (application().videoOutput()) {
            case EMBEDDED:
                cardLayout.show(this, NAME_AUDIO);
                break;
            case CALLBACK:
                cardLayout.show(this, NAME_AUDIO);

                break;
            default:
                throw new IllegalStateException();
        }
    }

}
