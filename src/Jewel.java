/**
 * Represents a single jewel on the board.
 * Includes a PowerUpType to track special jewels.
 */
public class Jewel {

    private JewelType type;
    private PowerUpType powerUp;

    public Jewel(JewelType type) {
        this(type, PowerUpType.NONE);
    }

    public Jewel(JewelType type, PowerUpType powerUp) {
        this.type = type;
        this.powerUp = powerUp;
    }

    public JewelType getType() {
        return type;
    }

    public PowerUpType getPowerUp() {
        return powerUp;
    }
    
    public void setPowerUp(PowerUpType powerUp) {
        this.powerUp = powerUp;
    }
}

