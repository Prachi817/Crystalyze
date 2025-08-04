import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameGUI.java
 * Represents the main window of the game.
 * It sets up the JFrame and contains the main components like the game board and score display.
 */
public class GameGUI extends JFrame {

    private BoardPanel boardPanel;
    private JLabel scoreLabel;
    private GameController gameController;

    /**
     * Constructor for GameGUI.
     * Initializes the game window, controller, and UI components.
     */
    public GameGUI() 
    {
        gameController = new GameController(this);
        
        setTitle("Crystalyze");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(50, 50, 50));

        boardPanel = new BoardPanel(gameController);
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        JPanel scorePanel = new JPanel();
        scorePanel.setBackground(new Color(70, 70, 70));
        scorePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            "Score",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            Color.WHITE
        ));
        scoreLabel = new JLabel("0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setForeground(Color.WHITE);
        scorePanel.add(scoreLabel);
        mainPanel.add(scorePanel, BorderLayout.NORTH);

        add(mainPanel);

        pack(); 
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    /**
     * Updates the score display on the UI.
     * @param score The new score to display.
     */
    public void updateScore(int score) 
    {
        scoreLabel.setText(String.valueOf(score));
    }

    /**
     * Triggers a repaint of the game board.
     * This should be called whenever the state of the board changes.
     */
    public void updateView() 
    {
        boardPanel.repaint();
    }
}