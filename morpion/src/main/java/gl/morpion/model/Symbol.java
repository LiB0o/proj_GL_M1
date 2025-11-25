package gl.morpion.model;

public class Symbol {

	TypeOfSymbol typeOfSymbol;
	private String image;

	/**
	 *
	 * @param image
	 * @param typeSymbol
	 */
	public Symbol(String image, TypeOfSymbol typeSymbol) {
		this.image = image;
		this.typeOfSymbol = typeSymbol;
	}

	public static Symbol fromString(String symbolPath) {
		if(symbolPath.contains("croix")) {
			return new Symbol(symbolPath, TypeOfSymbol.CROSS);
		} else if(symbolPath.contains("cercle")) {
			return new Symbol(symbolPath, TypeOfSymbol.CIRCLE);
		}
		return null;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

    public TypeOfSymbol getTypeOfSymbol() {
        return typeOfSymbol;
    }
    


}