package gl.morpion.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymbolTest {

    @Test
    void getImage() {
        Symbol s1 = new Symbol("croix.pnj", TypeOfSymbol.CROSS);
        Symbol s2 = new Symbol("circle.pnj", TypeOfSymbol.CIRCLE);
        assertAll(
                "Test Get Image",
                () -> assertNotNull(s1.getImage()),
                () -> assertNotNull(s2.getImage()),
                () -> assertEquals("croix.pnj", s1.getImage())

        );


    }
    @Test
    @DisplayName("Image Is Empty ")
    void image_is_Empty() {
        Symbol s = new Symbol("", TypeOfSymbol.CIRCLE);
        assertEquals("", s.getImage());
    }

    @Test
    @DisplayName("Image NULL")
    void image_canBeNull() {
        Symbol s = new Symbol(null, TypeOfSymbol.CROSS);
        assertNull(s.getImage());
    }

    @Test
    @DisplayName("Test Type")
    void Same_type_Use() {
        Symbol s1 = new Symbol("", TypeOfSymbol.CROSS);
        Symbol s2 = new Symbol("", TypeOfSymbol.CIRCLE);
        assertAll(
                "Test get Type",
                () -> assertEquals(TypeOfSymbol.CROSS, s1.getTypeOfSymbol()),
                () -> assertEquals(TypeOfSymbol.CIRCLE, s2.getTypeOfSymbol())
        );

    }

    @Test
    @DisplayName("Test SetImage")
    void changeImage_with_setImage() {
        Symbol s = new Symbol("croix.pnj", TypeOfSymbol.CROSS);
        s.setImage("croix_v2.pnj");
        assertEquals("croix_v2.pnj", s.getImage());
        assertEquals(TypeOfSymbol.CROSS, s.getTypeOfSymbol());
    }
    @Test
    @DisplayName("Test if is differently ")
    void differentTypes_() {
        Symbol s1 = new Symbol("croix.pnj", TypeOfSymbol.CROSS);
        Symbol s2 = new Symbol("circle.pnj", TypeOfSymbol.CIRCLE);
        assertNotEquals(s1.getImage(), s2.getImage());
        assertNotEquals(s1.getTypeOfSymbol(), s2.getTypeOfSymbol());
    }
    @Test
    @DisplayName("Test SameValue")
    void SameValue_Use() {
        Symbol s1 = new Symbol("croix.pnj", TypeOfSymbol.CROSS);
        s1.setImage("croix.pnj");
        assertEquals("croix.pnj", s1.getImage());
        assertEquals(TypeOfSymbol.CROSS, s1.getTypeOfSymbol());
    }

}