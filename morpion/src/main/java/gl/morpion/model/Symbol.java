package gl.morpion.model;

public class Symbol {
	TypeOfSymbol typeOfSymbol;
	private String image;

	/**
	 *create a symbol with its image and type
	 * @param image : image name
	 * @param typeSymbol : type of symbol
	 */
	public Symbol(String image, TypeOfSymbol typeSymbol) {
		this.image = image;
		this.typeOfSymbol = typeSymbol;
	}

	/**
	 *
	 * @return : image name
	 */
	public String getImage() {
		return this.image;
	}

	/***
	 *
	 * @param image: update symbol image name
	 */
	public void setImage(String image) {
		this.image = image;
	}

}