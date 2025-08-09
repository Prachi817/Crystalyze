/**
 * Jewel.java
 * A simple data class representing a single jewel on the board.
 * It holds the jewel's type.
 */
public class Jewel {

    private JewelType type;

    /**
     * Constructor for Jewel.
     * @param type The type of this jewel.
     */
    public Jewel(JewelType type) {
        this.type = type;
    }

    /**
     * Gets the type of the jewel.
     * @return The JewelType enum value.
     */
    public JewelType getType() {
        return type;
    }
}
