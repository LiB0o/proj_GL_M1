package gl.morpion.settings;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class SettingsModel {

    private static final Path FILE = Paths.get(
            System.getProperty("user.home"),
            ".morpion",
            "settings.conf"
    );

    private int musicVolume = 50;
    private int sfxVolume = 50;
    private boolean muted = false;
    private boolean fullscreen = false;
    private String resolution = "1200x800";

    // =========================
    // LOAD
    // =========================
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
    public int getMusicVolume() { return musicVolume; }
    public int getSfxVolume() { return sfxVolume; }
    public boolean isMuted() { return muted; }
    public boolean isFullscreen() { return fullscreen; }
    public String getResolution() { return resolution; }

    // =========================
    // SETTERS
    // =========================
    public void setMusicVolume(int v) { musicVolume = clamp(v); }
    public void setSfxVolume(int v) { sfxVolume = clamp(v); }
    public void setMuted(boolean b) { muted = b; }
    public void setFullscreen(boolean b) { fullscreen = b; }
    public void setResolution(String r) { resolution = r; }

    // =========================
    // UTILS
    // =========================
    private int clamp(int v) {
        return Math.max(0, Math.min(100, v));
    }
}
