package gl.morpion.model;


/**
 * <h1>class Symbol</h1>
 * Represents a game symbol with its visual representation and type.
 * Each symbol has an image path and a type (CROSS or CIRCLE).
 * <h2>Elements of Symbol</h2>
 */
public class Symbol {

	/**
	 * <h3>typeOfSymbol</h3>
	 * The type of this symbol (CROSS or CIRCLE)
	 */
	TypeOfSymbol typeOfSymbol;

	/**
	 * <h3>image</h3>
	 * Path to the image file representing this symbol
	 */
	private String image;

	/**
	 * <h2>Functions of Symbol</h2>
	 */

	/**
	 * <h3>Symbol</h3>
	 * Creates a new Symbol with specified image and type.
	 *
	 * @param image the path to the image file for this symbol
	 * @param typeSymbol the type of symbol (CROSS or CIRCLE)
	 */
	public Symbol(String image, TypeOfSymbol typeSymbol) {
		this.image = image;
		this.typeOfSymbol = typeSymbol;
	}

	/**
	 * Creates a Symbol from a path string by detecting the symbol type.
	 * Determines the type based on keywords in the path ("croix" for CROSS, "cercle" for CIRCLE).
	 *
	 * @param symbolPath the path to the symbol image
	 * @return a new Symbol instance, or null if type cannot be determined
	 */
	public static Symbol fromString(String symbolPath) {
		if(symbolPath.contains("croix")) {
			return new Symbol(symbolPath, TypeOfSymbol.CROSS);
		} else if(symbolPath.contains("cercle")) {
			return new Symbol(symbolPath, TypeOfSymbol.CIRCLE);
		}
		return null;
	}

	/**
	 * <h3>getImage</h3>
	 * Gets the image path for this symbol.
	 *
	 * @return the path to the symbol's image file
	 */
	public String getImage() {
		return this.image;
	}

	/**
	 * <h3>setImage</h3>
	 * Sets a new image path for this symbol.
	 *
	 * @param image the new image path
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * <h3>getTypeOfSymbol</h3>
	 * Gets the type of this symbol.
	 *
	 * @return the symbol type (CROSS or CIRCLE)
	 */
    public TypeOfSymbol getTypeOfSymbol() {
        return typeOfSymbol;
    }



}