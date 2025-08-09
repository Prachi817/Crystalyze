import java.awt.Point;

/**
 * GameController.java
 * Acts as the intermediary between the view (GUI) and the model (Board).
 * It processes user input and orchestrates the game logic.
 */
public class GameController {

    private Board board;
    private GameGUI gui;
    private Point selectedJewel; // Stores the (row, col) of the first selected jewel
    private int score;

    /**
     * Constructor for GameController.
     * @param gui The main GameGUI instance.
     */
    public GameController(GameGUI gui) {
        this.gui = gui;
        this.board = new Board();
        this.selectedJewel = null;
        this.score = 0;
        board.populateBoard();
    }

    /**
     * Handles the logic when a jewel on the board is clicked.
     * @param row The row of the clicked jewel.
     * @param col The column of the clicked jewel.
     */
    public void jewelClicked(int row, int col) {
        if (selectedJewel == null) {
            // First jewel selected
            selectedJewel = new Point(row, col);
        } else {
            // Second jewel selected, attempt a swap
            Point secondJewelPos = new Point(row, col);
            if (isAdjacent(selectedJewel, secondJewelPos)) {
                attemptSwap(selectedJewel, secondJewelPos);
            }
            // Reset selection after an attempt
            selectedJewel = null;
        }
        // Update the view to show the selection or the result of a swap
        gui.updateView();
    }
    
    /**
     * Attempts to swap two jewels and processes the consequences.
     * @param p1 The position of the first jewel.
     * @param p2 The position of the second jewel.
     */
    private void attemptSwap(Point p1, Point p2) {
        board.swapJewels(p1, p2);

        if (board.hasMatches()) {
            // Valid swap, process matches
            processMatches();
        } else {
            // Invalid swap, revert
            board.swapJewels(p1, p2); // Swap back
        }
    }
    
    /**
     * Continuously finds and processes matches until the board is stable.
     */
    private void processMatches() {
        while (board.hasMatches()) {
            int points = board.removeMatches();
            score += points;
            board.collapseGrid();
            board.refillGrid();
            gui.updateScore(score);
        }
        gui.updateView();
    }

    /**
     * Checks if two points (jewel positions) are adjacent.
     * @param p1 The first point.
     * @param p2 The second point.
     * @return true if they are adjacent, false otherwise.
     */
    private boolean isAdjacent(Point p1, Point p2) {
        int rowDiff = Math.abs(p1.x - p2.x);
        int colDiff = Math.abs(p1.y - p2.y);
        // Adjacent if they are in the same row and adjacent columns,
        // or in the same column and adjacent rows.
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }

    /**
     * Gets the current board state.
     * @return The Board object.
     */
    public Board getBoard() {
        return board;
    }
    
    /**
     * Gets the currently selected jewel's position.
     * @return The Point of the selected jewel, or null if none is selected.
     */
    public Point getSelectedJewel() {
        return selectedJewel;
    }
}
