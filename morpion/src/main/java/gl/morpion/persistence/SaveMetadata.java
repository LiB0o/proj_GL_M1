package gl.morpion.persistence;

/**
 * Small DTO used only for displaying save files in the UI.
 */
public class SaveMetadata {
    private final String fileName;      // ex: "Partie_Mahdi_vs_Bot.json"
    private final String saveName;      // nom logique
    private final String mode;          // PVP, PVBOT, ...
    private final String botDifficulty; // Easy / Medium / Hard / null
    private final String savedAt;       // date/heure en string

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

    public String getFileName() {
        return fileName;
    }

    public String getSaveName() {
        return saveName;
    }

    public String getMode() {
        return mode;
    }

    public String getBotDifficulty() {
        return botDifficulty;
    }

    public String getSavedAt() {
        return savedAt;
    }

    @Override
    public String toString() {
        // texte qui sera affiché dans la ListView par défaut
        String diff = (botDifficulty != null && !botDifficulty.isBlank())
                ? " [" + botDifficulty + "]"
                : "";
        return saveName + "  (" + mode + diff + ")  -  " + savedAt;
    }
}
