import java.awt.*;
<<<<<<< HEAD
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Represents the main window of the game.
 */
public class GameGUI extends JFrame implements ActionListener 
{

    private BoardPanel boardPanel;
    private JLabel scoreLabel;
    private JLabel statusLabel;
    private JButton pauseButton;
    private GameController gameController;
    private JPanel controlPanelWrapper; 

    /**
     * Constructor for GameGUI.
=======
import javax.swing.*;

/**
 * GameGUI.java
 * Represents the main window of the game.
 * It sets up the JFrame and contains the main components like the game board and score display.
 */
public class GameGUI extends JFrame 
{
    private BoardPanel boardPanel;
    private JLabel scoreLabel;
    private GameController gameController;

    /**
     * Constructor for GameGUI.
     * Initializes the game window, controller, and UI components.
>>>>>>> 0daf8ece0b02e82f2b6c82480a9021416f75dc03
     */
    public GameGUI() 
    {
        gameController = new GameController(this);

<<<<<<< HEAD
        setTitle("Crystalyze - Beta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
=======
        setTitle("Crystalyze");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
>>>>>>> 0daf8ece0b02e82f2b6c82480a9021416f75dc03
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(50, 50, 50));

        boardPanel = new BoardPanel(gameController);
        mainPanel.add(boardPanel, BorderLayout.CENTER);

<<<<<<< HEAD
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        topPanel.setOpaque(false);

        JPanel scorePanel = createInfoPanel("Score");
        scoreLabel = new JLabel("0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setForeground(Color.WHITE);
        scorePanel.add(scoreLabel);
        topPanel.add(scorePanel);

        JPanel statusPanel = createInfoPanel("Mode");
        statusLabel = new JLabel("--", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        statusLabel.setForeground(Color.WHITE);
        statusPanel.add(statusLabel);
        topPanel.add(statusPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        controlPanelWrapper = createControlPanel();
        mainPanel.add(controlPanelWrapper, BorderLayout.SOUTH);

        controlPanelWrapper.setVisible(false);

        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        showModeSelectionDialog();
    }
    
    /**
     * Creates the bottom panel with game control buttons.
     * @return The fully constructed control panel.
     */
    private JPanel createControlPanel() 
    {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);

        pauseButton = createStyledButton("Pause");
        JButton exitButton = createStyledButton("Exit");

        buttonPanel.add(pauseButton);
        buttonPanel.add(exitButton);
        
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); // No extra gap needed here
        wrapper.setOpaque(false);
        wrapper.add(buttonPanel);

        return wrapper;
    }

    /**
     * Helper method to create and style a JButton.
     * @param text The text for the button.
     * @return A styled JButton.
     */
    private JButton createStyledButton(String text) 
    {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 40)); 
        button.setBackground(new Color(100, 100, 100));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEtchedBorder());
        button.setActionCommand(text.toUpperCase().replace(" ", "_"));
        button.addActionListener(this);
        return button;
    }

    /**
     * Displays a dialog for the user to select the game mode.
     */
    private void showModeSelectionDialog() 
    {
        String[] options = {"Timed Mode", "Moves Mode"};
        int choice = JOptionPane.showOptionDialog(
            this, "Choose a game mode to start:", "Select Game Mode",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]
        );

        
        if (choice == 0) 
        { // Timed Mode
            gameController.startGame(GameMode.TIMED);
            controlPanelWrapper.setVisible(true); // Show controls once game starts
        } 
        
        else if (choice == 1)
        { // Moves Mode
            gameController.startGame(GameMode.MOVES);
            controlPanelWrapper.setVisible(true); // Show controls once game starts
        } 
        
        else 
        {
            System.exit(0);
        }
    }

    /**
     * Helper method to create styled info panels for score and status.
     */
    private JPanel createInfoPanel(String title) 
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(70, 70, 70));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE), title,
            0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        String command = e.getActionCommand();
        switch (command) {
            // Combined Pause and Resume logic
            case "PAUSE":
            case "RESUME":
                gameController.togglePause();
                break;
            case "EXIT":
                // Quit the current game and return to the mode selection screen
                gameController.quitCurrentGame();
                controlPanelWrapper.setVisible(false);
                showModeSelectionDialog();
                break;
        }
    }

=======
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
>>>>>>> 0daf8ece0b02e82f2b6c82480a9021416f75dc03
    public void updateScore(int score) 
    {
        scoreLabel.setText(String.valueOf(score));
    }

<<<<<<< HEAD
    public void updateStatus(String status, GameMode mode) 
    {
        statusLabel.setText(status);
        String title = "Mode";
        if (mode != null) 
        {
            title = (mode == GameMode.TIMED) ? "Time Left" : "Moves Left";
        }
        ((TitledBorder) ((JPanel) statusLabel.getParent()).getBorder()).setTitle(title);
        statusLabel.getParent().repaint();
    }
    
    public void setPauseButtonText(boolean isPaused) 
    {
        pauseButton.setText(isPaused ? "Resume" : "Pause");
        pauseButton.setActionCommand(isPaused ? "RESUME" : "PAUSE");
    }

=======
    /**
     * Triggers a repaint of the game board.
     * This should be called whenever the state of the board changes.
     */
>>>>>>> 0daf8ece0b02e82f2b6c82480a9021416f75dc03
    public void updateView() 
    {
        boardPanel.repaint();
    }
<<<<<<< HEAD
    
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public void showGameOverDialog(int finalScore) 
    {
        String message = "Game Over!\nYour final score is: " + finalScore;
        Object[] options = {"Play Again", "Exit"};
        int choice = JOptionPane.showOptionDialog(this, message, "Game Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) 
        {
            controlPanelWrapper.setVisible(false); 
            showModeSelectionDialog();
        } else {
            System.exit(0);
        }
    }
}
=======
}
>>>>>>> 0daf8ece0b02e82f2b6c82480a9021416f75dc03
