package gl_morpion.model;

public class BotPlayer extends Player {

	private float level;

	public float getLevel() {
		return this.level;
	}

	public void setLevel(float level) {
		this.level = level;
	}

	/**
	 * 
	 * @param level
	 */
	public BotPlayer(String name,Symbol symbol , int point, int level) {
        super(name, symbol);
        // TODO - implement BotPlayer.BotPlayer
		throw new UnsupportedOperationException();
	}

}