import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * BoardPanel.java
 * A custom JPanel that visually represents the game board.
 * It handles rendering the jewels and capturing mouse clicks from the player.
 */
public class BoardPanel extends JPanel 
{
    public static final int GRID_SIZE = 8;
    public static final int JEWEL_SIZE = 60; 

    private GameController gameController;

    /**
     * Constructor for BoardPanel.
     * @param controller The game controller to handle user input.
     */
    public BoardPanel(GameController controller) 
    {
        this.gameController = controller;
        setPreferredSize(new Dimension(GRID_SIZE * JEWEL_SIZE, GRID_SIZE * JEWEL_SIZE));
        setBackground(new Color(100, 100, 100));

        addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mousePressed(MouseEvent e) 
            {
                int col = e.getX() / JEWEL_SIZE;
                int row = e.getY() / JEWEL_SIZE;

                if (row < GRID_SIZE && col < GRID_SIZE) 
                {
                    gameController.jewelClicked(row, col);
                }
            }
        });
    }

    /**
     * Overrides the paintComponent method to draw the game board and jewels.
     * @param g The Graphics object to protect.
     */
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Board board = gameController.getBoard();
        if (board == null) return;

        for (int row = 0; row < GRID_SIZE; row++) 
        {
            for (int col = 0; col < GRID_SIZE; col++) 
            {
                Jewel jewel = board.getJewel(row, col);
                if (jewel != null) 
                {
                    g.setColor(getJewelColor(jewel.getType()));
                    g.fillRect(col * JEWEL_SIZE, row * JEWEL_SIZE, JEWEL_SIZE, JEWEL_SIZE);

                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(col * JEWEL_SIZE, row * JEWEL_SIZE, JEWEL_SIZE, JEWEL_SIZE);
                }
            }
        }
        
        Point selected = gameController.getSelectedJewel();
        if (selected != null) 
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(selected.y * JEWEL_SIZE, selected.x * JEWEL_SIZE, JEWEL_SIZE, JEWEL_SIZE);
        }
    }

    /**
     * Maps a JewelType to a specific color for rendering.
     * @param type The type of the jewel.
     * @return The color associated with the jewel type.
     */
    private Color getJewelColor(JewelType type) 
    {
        switch (type) 
        {
            case RED: return Color.RED;
            case GREEN: return Color.GREEN;
            case BLUE: return Color.BLUE;
            case YELLOW: return Color.ORANGE; 
            case PURPLE: return Color.MAGENTA;
            case ORANGE: return new Color(255, 140, 0); 
            case WHITE: return Color.CYAN; 
            default: return Color.BLACK;
        }
    }
}