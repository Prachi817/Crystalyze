import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Board.java
 * Represents the game's data model. It manages the 8x8 grid of jewels,
 * finds matches, removes them, and refills the board.
 */
public class Board {

    private static final int GRID_SIZE = 8;
    private Jewel[][] grid;
    private Random random = new Random();

    /**
     * Constructor for Board.
     * Initializes the grid.
     */
    public Board() {
        grid = new Jewel[GRID_SIZE][GRID_SIZE];
    }

    /**
     * Fills the board with random jewels, ensuring no initial matches exist.
     */
    public void populateBoard() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                do {
                    grid[row][col] = new Jewel(JewelType.getRandom());
                } while (hasInitialMatch(row, col));
            }
        }
    }

    /**
     * Checks if placing a jewel at a given position creates a match of 3.
     * Used during population to prevent instant matches on startup.
     * @param row The row to check.
     * @param col The column to check.
     * @return true if a match is formed, false otherwise.
     */
    private boolean hasInitialMatch(int row, int col) {
        JewelType type = grid[row][col].getType();
        // Check horizontal match
        if (col >= 2 && grid[row][col-1].getType() == type && grid[row][col-2].getType() == type) {
            return true;
        }
        // Check vertical match
        if (row >= 2 && grid[row-1][col].getType() == type && grid[row-2][col].getType() == type) {
            return true;
        }
        return false;
    }

    /**
     * Finds all matches (3 or more) on the current board.
     * @return A list of points representing the jewels to be removed.
     */
    private List<Point> findMatches() {
        List<Point> matches = new ArrayList<>();
        // Find horizontal matches
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE - 2; col++) {
                Jewel j1 = grid[row][col];
                if (j1 == null) continue;
                Jewel j2 = grid[row][col + 1];
                Jewel j3 = grid[row][col + 2];
                if (j2 != null && j3 != null && j1.getType() == j2.getType() && j1.getType() == j3.getType()) {
                    matches.add(new Point(row, col));
                    matches.add(new Point(row, col + 1));
                    matches.add(new Point(row, col + 2));
                }
            }
        }
        // Find vertical matches
        for (int col = 0; col < GRID_SIZE; col++) {
            for (int row = 0; row < GRID_SIZE - 2; row++) {
                Jewel j1 = grid[row][col];
                 if (j1 == null) continue;
                Jewel j2 = grid[row + 1][col];
                Jewel j3 = grid[row + 2][col];
                if (j2 != null && j3 != null && j1.getType() == j2.getType() && j1.getType() == j3.getType()) {
                    matches.add(new Point(row, col));
                    matches.add(new Point(row + 1, col));
                    matches.add(new Point(row + 2, col));
                }
            }
        }
        return matches;
    }

    /**
     * Checks if the board currently contains any matches.
     * @return true if there is at least one match, false otherwise.
     */
    public boolean hasMatches() {
        return !findMatches().isEmpty();
    }
    
    /**
     * Removes all matched jewels from the board and returns the score.
     * @return The number of jewels removed (points).
     */
    public int removeMatches() {
        List<Point> matches = findMatches();
        for (Point p : matches) {
            grid[p.x][p.y] = null; // Set matched jewels to null
        }
        return matches.size();
    }

    /**
     * Simulates gravity, making jewels fall down to fill empty spaces.
     */
    public void collapseGrid() {
        for (int col = 0; col < GRID_SIZE; col++) {
            int emptyRow = GRID_SIZE - 1;
            for (int row = GRID_SIZE - 1; row >= 0; row--) {
                if (grid[row][col] != null) {
                    // Swap the current jewel with the lowest empty spot
                    swapJewels(new Point(row, col), new Point(emptyRow, col));
                    emptyRow--;
                }
            }
        }
    }
    
    /**
     * Fills any remaining empty spaces at the top of the grid with new random jewels.
     */
    public void refillGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] == null) {
                    grid[row][col] = new Jewel(JewelType.getRandom());
                }
            }
        }
    }

    /**
     * Swaps two jewels on the grid.
     * @param p1 Position of the first jewel.
     * @param p2 Position of the second jewel.
     */
    public void swapJewels(Point p1, Point p2) {
        Jewel temp = grid[p1.x][p1.y];
        grid[p1.x][p1.y] = grid[p2.x][p2.y];
        grid[p2.x][p2.y] = temp;
    }

    /**
     * Gets the jewel at a specific grid location.
     * @param row The row.
     * @param col The column.
     * @return The Jewel at the specified location.
     */
    public Jewel getJewel(int row, int col) {
        return grid[row][col];
    }
}

