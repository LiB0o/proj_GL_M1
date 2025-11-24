package gl.morpion.model;


/**
 * <h1>class Symbol</h1>
 * <h2>Elements of Symbol</h2>
 */
public class Symbol {

	/**
	 * <h3>typeOfSymbol</h3>
	 * enumtype that permit to separate symbols
	 */
	TypeOfSymbol typeOfSymbol;
	/**
	 * <h3>image</h3>
	 * address of the image for the symbol
	 */
	private String image;

	/**
	 * <h2>Functions of Symbol</h2>
	 */

	/**
	 *<h3>Symbol</h3>
	 * constructor of Symbol
	 * @param image the link toward the image of the symbol for the game
	 * @param typeSymbol the type od symbol
	 */
	public Symbol(String image, TypeOfSymbol typeSymbol) {
		this.image = image;
		this.typeOfSymbol = typeSymbol;
	}

	/**
	 * <h3>getImage</h3>
	 * @return the link of the picture
	 */
	public String getImage() {
		return this.image;
	}

	/**
	 * <h3>setImage</h3>
	 * Change the image use for the symbol
	 * @param image the new link
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * <h3>getTypeOfSymbol</h3>
	 * @return the type of the symbol
	 */
    public TypeOfSymbol getTypeOfSymbol() {
        return typeOfSymbol;
    }
    


}