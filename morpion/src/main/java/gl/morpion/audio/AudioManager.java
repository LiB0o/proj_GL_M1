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
    // optionnel : musique du menu
    private static final String MENU_MUSIC = "/audio/menu.mp3";

    // =========================
    // MUSIC
    // =========================
    public static void startMenuMusic() {
        if (muted) return;

        var url = AudioManager.class.getResource(MENU_MUSIC);
        if (url == null) return;

        if (musicPlayer != null) return;

        Media media = new Media(url.toExternalForm());
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        applyMusicVolume();
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
