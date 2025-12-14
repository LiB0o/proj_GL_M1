package gl.morpion.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {

    private static MediaPlayer musicPlayer;
    private static double musicVolume = 0.5;
    private static double sfxVolume = 0.5;
    private static boolean muted = false;

    public static void startMenuMusic() {
        // /audio/menu.mp3 dans resources
        var url = AudioManager.class.getResource("/audio/menu.mp3");
        if (url == null) {
            System.err.println("Audio not found: /audio/menu.mp3");
            return;
        }

        if (musicPlayer != null) return; // déjà lancé

        Media media = new Media(url.toExternalForm());
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        applyVolumes();
        musicPlayer.play();
    }

    public static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }

    public static void playClick() {
        playSfx("/audio/click.wav");
    }

    public static void playSfx(String resourcePath) {
        if (muted) return;

        var url = AudioManager.class.getResource(resourcePath);
        if (url == null) {
            System.err.println("SFX not found: " + resourcePath);
            return;
        }

        AudioClip clip = new AudioClip(url.toExternalForm());
        clip.setVolume(sfxVolume);
        clip.play();
    }

    public static void setMusicVolume(double v) {
        musicVolume = clamp01(v);
        applyVolumes();
    }

    public static void setSfxVolume(double v) {
        sfxVolume = clamp01(v);
    }

    public static void setMuted(boolean b) {
        muted = b;
        applyVolumes();
    }

    public static boolean isMuted() { return muted; }

    private static void applyVolumes() {
        if (musicPlayer != null) {
            musicPlayer.setVolume(muted ? 0.0 : musicVolume);
        }
    }

    private static double clamp01(double v) { return Math.max(0.0, Math.min(1.0, v)); }
}
