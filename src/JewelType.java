import java.util.Random;

/**
 * An enumeration for the different types of jewels. 
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

    public static JewelType getRandom() {
        return values()[RAND.nextInt(values().length)];
    }
}
