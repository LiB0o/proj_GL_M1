package gl.morpion.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Centralized audio utility for the application.
 * <p>
 * This class manages:
 * </p>
 * <ul>
 *     <li>Menu music playback using a simple playlist</li>
 *     <li>Sound effects (SFX) such as click, return and quit</li>
 *     <li>Global audio settings (music volume, sfx volume, mute)</li>
 * </ul>
 *
 * <p>
 * Notes:
 * </p>
 * <ul>
 *     <li>Menu music is started once via {@link #startMenuMusic()} and loops through a playlist.</li>
 *     <li>SFX are played using short-lived {@link MediaPlayer} instances and disposed automatically.</li>
 *     <li>If muted, both music and SFX are prevented from playing.</li>
 * </ul>
 */
public class AudioManager {

    // =========================
    // MUSIC (menu)
    // =========================
    private static MediaPlayer musicPlayer;

    private static double musicVolume = 0.5;
    private static double sfxVolume = 0.7;
    private static boolean muted = false;

    // SFX (single sounds for the whole UI)
    private static final String CLICK_SFX = "/audio/play.wav";
    private static final String QUIT_SFX = "/audio/quit.wav";
    private static final String RETURN_SFX = "/audio/return.wav";

    // MENU PLAYLIST
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

    /**
     * Starts playing the menu music playlist.
     * <p>
     * This method is safe to call multiple times:
     * if music is already playing (musicPlayer != null) it will do nothing.
     * </p>
     *
     * <p>
     * Side effects:
     * </p>
     * <ul>
     *     <li>Creates a {@link MediaPlayer} for the first track of the playlist</li>
     *     <li>Registers an end-of-media handler to automatically play the next track</li>
     * </ul>
     */
    public static void startMenuMusic() {
        if (muted) return;
        if (musicPlayer != null) return;

        menuIndex = 0;
        playNextMenuTrack();
    }

    /**
     * Plays the next track in the menu playlist.
     * <p>
     * If the end of the playlist is reached, the playlist loops back to the beginning.
     * </p>
     *
     * <p>
     * Side effects:
     * </p>
     * <ul>
     *     <li>Creates and starts a new {@link MediaPlayer}</li>
     *     <li>Disposes the previous player when the track ends</li>
     * </ul>
     */
    private static void playNextMenuTrack() {
        if (muted) return;

        if (menuIndex >= MENU_PLAYLIST.length) {
            menuIndex = 0; // loop playlist
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

    /**
     * Plays the quit sound effect.
     */
    public static void playQuit() {
        playSfx(QUIT_SFX);
    }

    /**
     * Plays the return/back navigation sound effect.
     */
    public static void playReturn() {
        playSfx(RETURN_SFX);
    }

    /**
     * Stops any currently playing music and releases underlying resources.
     * <p>
     * Side effects: stops and disposes the current {@link MediaPlayer} instance.
     * </p>
     */
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

    /**
     * Plays the generic click sound effect used by buttons.
     */
    public static void playClick() {
        playSfx(CLICK_SFX);
    }

    /**
     * Plays a short sound effect referenced by a classpath resource.
     * <p>
     * This method creates a short-lived {@link MediaPlayer} and disposes it automatically
     * when playback ends.
     * </p>
     *
     * @param resourcePath the classpath resource path (e.g. {@code "/audio/play.wav"})
     */
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

            // Auto cleanup
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

    /**
     * Enables or disables global mute.
     * <p>
     * When muted:
     * </p>
     * <ul>
     *     <li>Music volume is applied as 0.0</li>
     *     <li>SFX playback is prevented</li>
     * </ul>
     *
     * @param b {@code true} to mute all sounds, {@code false} to unmute
     */
    public static void setMuted(boolean b) {
        muted = b;
        applyMusicVolume();
    }

    /**
     * Returns whether audio is currently muted.
     *
     * @return {@code true} if muted, otherwise {@code false}
     */
    public static boolean isMuted() {
        return muted;
    }

    /**
     * Sets the music volume.
     * <p>
     * Expected range: 0.0 to 1.0 (values are clamped).
     * </p>
     *
     * @param v desired music volume in [0.0, 1.0]
     */
    public static void setMusicVolume(double v) {
        musicVolume = clamp01(v);
        applyMusicVolume();
    }

    /**
     * Sets the sound effects volume.
     * <p>
     * Expected range: 0.0 to 1.0 (values are clamped).
     * </p>
     *
     * @param v desired SFX volume in [0.0, 1.0]
     */
    public static void setSfxVolume(double v) {
        sfxVolume = clamp01(v);
    }

    /**
     * Applies the current music volume to the active music player (if any),
     * taking into account the mute state.
     */
    private static void applyMusicVolume() {
        if (musicPlayer != null) {
            musicPlayer.setVolume(muted ? 0.0 : musicVolume);
        }
    }

    /**
     * Clamps a value to the range [0.0, 1.0].
     *
     * @param v the value to clamp
     * @return the clamped value
     */
    private static double clamp01(double v) {
        return Math.max(0.0, Math.min(1.0, v));
    }
}
