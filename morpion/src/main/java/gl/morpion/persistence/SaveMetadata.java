package gl.morpion.persistence;

/**
 * Small Data Transfer Object (DTO) used only for displaying save files in the UI.
 * <p>
 * This class is a lightweight, immutable representation of a saved game entry.
 * It is typically used by UI components (e.g., lists or menus) to present
 * human-readable information about available save files.
 * </p>
 */
public class SaveMetadata {

    /** Physical file name of the save (e.g., "Partie_Mahdi_vs_Bot.json"). */
    private final String fileName;

    /** Logical/display name of the save as chosen by the user. */
    private final String saveName;

    /** Game mode associated with the save (e.g., PVP, PVBOT, CUSTOM_PVP, CUSTOM_PVBOT). */
    private final String mode;

    /** Bot difficulty (e.g., Easy / Medium / Hard), or {@code null} if not applicable. */
    private final String botDifficulty;

    /** Save timestamp formatted as a human-readable string. */
    private final String savedAt;

    /**
     * Creates a new {@code SaveMetadata} instance.
     *
     * @param fileName      the physical save file name
     * @param saveName      the logical/display name of the save
     * @param mode          the game mode associated with the save
     * @param botDifficulty the bot difficulty level, or {@code null} if not applicable
     * @param savedAt       the save timestamp as a string
     */
    public SaveMetadata(String fileName,
                        String saveName,
                        String mode,
                        String botDifficulty,
                        String savedAt) {
        this.fileName = fileName;
        this.saveName = saveName;
        this.mode = mode;
        this.botDifficulty = botDifficulty;
        this.savedAt = savedAt;
    }

    /**
     * Returns the physical file name of the save.
     *
     * @return the save file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the logical/display name of the save.
     *
     * @return the save name
     */
    public String getSaveName() {
        return saveName;
    }

    /**
     * Returns the game mode associated with the save.
     *
     * @return the game mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Returns the bot difficulty level, if applicable.
     *
     * @return the bot difficulty, or {@code null} if none
     */
    public String getBotDifficulty() {
        return botDifficulty;
    }

    /**
     * Returns the save timestamp.
     *
     * @return the save timestamp as a string
     */
    public String getSavedAt() {
        return savedAt;
    }

    /**
     * Returns a human-readable string representation of this save metadata.
     * <p>
     * This string is typically displayed directly inside a UI {@code ListView}.
     * </p>
     *
     * @return a formatted string describing the save entry
     */
    @Override
    public String toString() {
        // Text displayed by default in the ListView
        String diff = (botDifficulty != null && !botDifficulty.isBlank())
                ? " [" + botDifficulty + "]"
                : "";
        return saveName + "  (" + mode + diff + ")  -  " + savedAt;
    }
}
