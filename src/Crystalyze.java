import javax.swing.SwingUtilities;

/**
 * Crystalyze.java
 * The main entry point for the Crystalyze game.
 * This class is responsible for initializing and starting the game's graphical user interface.
 */
public class Crystalyze {

    public static void main(String[] args) {
        // The game GUI is created and run on the Event Dispatch Thread (EDT)
        // to ensure thread safety for Swing components.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameGUI();
            }
        });
    }
}