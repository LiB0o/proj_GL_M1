package gl.morpion.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {

    // =========================
    // MUSIC (menu)
    // =========================
    private static MediaPlayer musicPlayer;

    private static double musicVolume = 0.5;
    private static double sfxVolume = 0.7;
    private static boolean muted = false;

    // ✅ SFX clic (UN SEUL son pour tous les boutons)
    private static final String CLICK_SFX = "/audio/play.wav";
    private static final String QUIT_SFX = "/audio/quit.wav";
    private static final String RETURN_SFX = "/audio/return.wav";

    // ✅ PLAYLIST MENU
    private static final String MENU_TRACK_1 = "/audio/Intro.mp3";
    private static final String MENU_MUSIC   = "/audio/GameMusic.mp3"; //
    private static final String MENU_TRACK_3 = "/audio/Track3.mp3";
    private static final String MENU_TRACK_2 = "/audio/Track2.mp3";

    private static final String[] MENU_PLAYLIST = {
            MENU_TRACK_1,
            MENU_MUSIC,
            MENU_TRACK_2,
            MENU_TRACK_3

    };

    private static int menuIndex = 0;

    // =========================
    // MUSIC
    // =========================
    public static void startMenuMusic() {
        if (muted) return;
        if (musicPlayer != null) return;

        menuIndex = 0;
        playNextMenuTrack();
    }

    private static void playNextMenuTrack() {
        if (muted) return;

        if (menuIndex >= MENU_PLAYLIST.length) {
            menuIndex = 0; // 🔁 loop playlist
        }

        var url = AudioManager.class.getResource(MENU_PLAYLIST[menuIndex]);
        if (url == null) {
            System.err.println("Music not found: " + MENU_PLAYLIST[menuIndex]);
            menuIndex++;
            playNextMenuTrack();
            return;
        }

        Media media = new Media(url.toExternalForm());
        musicPlayer = new MediaPlayer(media);

        applyMusicVolume();

        musicPlayer.setOnEndOfMedia(() -> {
            musicPlayer.dispose();
            musicPlayer = null;
            menuIndex++;
            playNextMenuTrack();
        });

        musicPlayer.play();
    }

    public static void playQuit() {
        playSfx(QUIT_SFX);
    }

    public static void playReturn() {
        playSfx(RETURN_SFX);
    }

    public static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }

    // =========================
    // SFX (SAFE VERSION)
    // =========================
    public static void playClick() {
        playSfx(CLICK_SFX);
    }

    private static void playSfx(String resourcePath) {
        if (muted) return;

        try {
            var url = AudioManager.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("SFX not found: " + resourcePath);
                return;
            }

            Media media = new Media(url.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            player.setVolume(sfxVolume);

            // nettoyage automatique
            player.setOnEndOfMedia(() -> {
                player.stop();
                player.dispose();
            });

            player.play();

        } catch (Exception e) {
            System.err.println("Audio SFX error: " + e.getMessage());
        }
    }

    // =========================
    // SETTINGS
    // =========================
    public static void setMuted(boolean b) {
        muted = b;
        applyMusicVolume();
    }

    public static boolean isMuted() {
        return muted;
    }

    public static void setMusicVolume(double v) {
        musicVolume = clamp01(v);
        applyMusicVolume();
    }

    public static void setSfxVolume(double v) {
        sfxVolume = clamp01(v);
    }

    private static void applyMusicVolume() {
        if (musicPlayer != null) {
            musicPlayer.setVolume(muted ? 0.0 : musicVolume);
        }
    }

    private static double clamp01(double v) {
        return Math.max(0.0, Math.min(1.0, v));
    }
}
