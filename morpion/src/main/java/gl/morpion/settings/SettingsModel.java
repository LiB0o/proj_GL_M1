package gl.morpion.settings;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

/**
 * The {@link SettingsModel} class manages application settings.
 * <p>
 * It loads and saves settings related to sound volumes, screen resolution, fullscreen mode,
 * and mute state. The settings are stored in a configuration file located at {@code ~/.morpion/settings.conf}.
 * </p>
 */
public class SettingsModel {

    /** The path to the settings configuration file. */
    private static final Path FILE = Paths.get(
            System.getProperty("user.home"),
            ".morpion",
            "settings.conf"
    );

    /** The current music volume (range: 0-100). */
    private int musicVolume = 50;

    /** The current sound effects volume (range: 0-100). */
    private int sfxVolume = 50;

    /** Whether the game is muted. */
    private boolean muted = false;

    /** Whether the game is in fullscreen mode. */
    private boolean fullscreen = false;

    /** The screen resolution in the format {@code "WIDTHxHEIGHT"}. */
    private String resolution = "1200x800";

    // =========================
    // LOAD
    // =========================

    /**
     * Loads the settings from the configuration file.
     * <p>
     * If the settings file does not exist, default values are returned.
     * </p>
     *
     * @return an instance of {@link SettingsModel} with the loaded settings
     */
    public static SettingsModel load() {
        SettingsModel s = new SettingsModel();
        if (!Files.exists(FILE)) return s;

        Properties p = new Properties();
        try (Reader r = Files.newBufferedReader(FILE)) {
            p.load(r);

            s.musicVolume = Integer.parseInt(p.getProperty("musicVolume", "50"));
            s.sfxVolume   = Integer.parseInt(p.getProperty("sfxVolume", "50"));
            s.muted       = Boolean.parseBoolean(p.getProperty("muted", "false"));
            s.fullscreen  = Boolean.parseBoolean(p.getProperty("fullscreen", "false"));
            s.resolution  = p.getProperty("resolution", "1200x800");

        } catch (Exception e) {
            System.err.println("Settings load error: " + e.getMessage());
        }

        return s;
    }

    // =========================
    // SAVE
    // =========================

    /**
     * Saves the current settings to the configuration file.
     * <p>
     * If the file or its parent directories do not exist, they are created.
     * </p>
     */
    public void save() {
        try {
            Files.createDirectories(FILE.getParent());

            Properties p = new Properties();
            p.setProperty("musicVolume", String.valueOf(musicVolume));
            p.setProperty("sfxVolume", String.valueOf(sfxVolume));
            p.setProperty("muted", String.valueOf(muted));
            p.setProperty("fullscreen", String.valueOf(fullscreen));
            p.setProperty("resolution", resolution);

            try (Writer w = Files.newBufferedWriter(FILE)) {
                p.store(w, "Morpion settings");
            }

        } catch (Exception e) {
            System.err.println("Settings save error: " + e.getMessage());
        }
    }

    // =========================
    // GETTERS
    // =========================

    /**
     * Returns the current music volume.
     *
     * @return the music volume (range: 0-100)
     */
    public int getMusicVolume() { return musicVolume; }

    /**
     * Returns the current sound effects volume.
     *
     * @return the sound effects volume (range: 0-100)
     */
    public int getSfxVolume() { return sfxVolume; }

    /**
     * Returns whether the game is currently muted.
     *
     * @return {@code true} if muted, {@code false} otherwise
     */
    public boolean isMuted() { return muted; }

    /**
     * Returns whether the game is in fullscreen mode.
     *
     * @return {@code true} if fullscreen, {@code false} otherwise
     */
    public boolean isFullscreen() { return fullscreen; }

    /**
     * Returns the current screen resolution.
     *
     * @return the screen resolution in the format {@code "WIDTHxHEIGHT"}
     */
    public String getResolution() { return resolution; }

    // =========================
    // SETTERS
    // =========================

    /**
     * Sets the music volume.
     * <p>
     * The value is clamped to the range 0-100.
     * </p>
     *
     * @param v the music volume
     */
    public void setMusicVolume(int v) { musicVolume = clamp(v); }

    /**
     * Sets the sound effects volume.
     * <p>
     * The value is clamped to the range 0-100.
     * </p>
     *
     * @param v the sound effects volume
     */
    public void setSfxVolume(int v) { sfxVolume = clamp(v); }

    /**
     * Sets whether the game is muted.
     *
     * @param b {@code true} to mute, {@code false} to unmute
     */
    public void setMuted(boolean b) { muted = b; }

    /**
     * Sets whether the game is in fullscreen mode.
     *
     * @param b {@code true} to enable fullscreen, {@code false} to disable it
     */
    public void setFullscreen(boolean b) { fullscreen = b; }

    /**
     * Sets the screen resolution.
     *
     * @param r the screen resolution in the format {@code "WIDTHxHEIGHT"}
     */
    public void setResolution(String r) { resolution = r; }

    // =========================
    // UTILS
    // =========================

    /**
     * Clamps a value to the range [0, 100].
     *
     * @param v the value to clamp
     * @return the clamped value
     */
    private int clamp(int v) {
        return Math.max(0, Math.min(100, v));
    }
}
