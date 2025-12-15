package gl.morpion.model;

/**
 * Enumeration of available game modes in the tic-tac-toe game.
 * Supports both standard and custom board configurations with
 * player vs player or player vs bot gameplay.
 */
public enum GameMode {
    /**
     * Player versus Player mode with standard board
     */
    PVP,

    /**
     * Player versus Bot mode with standard board
     */
    PVBOT,

    /**
     * Player versus Player mode with custom board configuration
     */
    CUSTOM_PVP,

    /**
     * Player versus Bot mode with custom board configuration
     */
    CUSTOM_PVBOT
}
