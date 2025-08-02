import java.util.Random;

/**
 * JewelType.java
 * An enumeration representing the different types of jewels available in the game.
 * Includes a method to get a random jewel type.
 */
public enum JewelType {
    RED,
    GREEN,
    BLUE,
    YELLOW,
    PURPLE,
    ORANGE,
    WHITE;

    private static final Random RAND = new Random();

    /**
     * Returns a random JewelType from the available values.
     * @return A random JewelType.
     */
    public static JewelType getRandom() {
        return values()[RAND.nextInt(values().length)];
    }
}
