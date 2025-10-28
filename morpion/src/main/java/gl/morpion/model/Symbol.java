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