package gl.morpion.model;

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
	public BotPlayer(String name, int point, int level,Symbol symbol) {
        super(name, point,symbol);
        // TODO - implement BotPlayer.BotPlayer
		throw new UnsupportedOperationException();
	}

}