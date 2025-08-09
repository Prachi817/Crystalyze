import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Board.java (Explosion Update)
 * Manages the grid of jewels and all core game logic.
 * from matches of 4 and 5, respectively.
 */
public class Board {

    private static final int GRID_SIZE = 8;
    private Jewel[][] grid;
    private Random random = new Random();
    private Set<Point> lastMatches = new HashSet<>();
    private List<List<Point>> lastMatchLines = new ArrayList<>();

    public Board() {
        grid = new Jewel[GRID_SIZE][GRID_SIZE];
    }

    public void populateBoard() {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                grid[r][c] = new Jewel(JewelType.getRandom());
            }
        }
        while (!hasValidMoves() || !findAllMatches().isEmpty()) {
             for (int r = 0; r < GRID_SIZE; r++) {
                for (int c = 0; c < GRID_SIZE; c++) {
                    grid[r][c] = new Jewel(JewelType.getRandom());
                }
            }
        }
    }

    public boolean hasValidMoves() {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (c < GRID_SIZE - 1 && wouldSwapCreateMatch(new Point(r,c), new Point(r, c+1))) return true;
                if (r < GRID_SIZE - 1 && wouldSwapCreateMatch(new Point(r,c), new Point(r+1, c))) return true;
            }
        }
        return false;
    }

    private boolean wouldSwapCreateMatch(Point p1, Point p2) {
        swapJewels(p1, p2);
        boolean hasMatch = !findAllMatches().isEmpty();
        swapJewels(p1, p2);
        return hasMatch;
    }

    public boolean checkAndPerformSwap(Point p1, Point p2) {
        Jewel j1 = getJewel(p1.x, p1.y);
        Jewel j2 = getJewel(p2.x, p2.y);

        if (j1.getPowerUp() == PowerUpType.HYPER_CUBE) {
            activateHyperCube(j1, j2.getType());
            return true;
        }
        if (j2.getPowerUp() == PowerUpType.HYPER_CUBE) {
            activateHyperCube(j2, j1.getType());
            return true;
        }
        
        swapJewels(p1, p2);
        if (!findAllMatches().isEmpty()) {
            return true;
        } else {
            swapJewels(p1, p2);
            return false;
        }
    }

    private void activateHyperCube(Jewel hyperCube, JewelType typeToClear) {
        hyperCube.setPowerUp(PowerUpType.NONE);
        Set<Point> toClear = new HashSet<>();
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (getJewel(r, c) != null && getJewel(r, c).getType() == typeToClear) {
                    toClear.add(new Point(r,c));
                }
            }
        }
        lastMatches = toClear;
    }

    public Set<Point> findAllMatches() {
        lastMatches.clear();
        lastMatchLines.clear();
        Set<Point> matches = new HashSet<>();
        // Horizontal
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE - 2; ) {
                List<Point> line = findLine(r, c, 0, 1);
                if (line.size() >= 3) {
                    matches.addAll(line);
                    lastMatchLines.add(line);
                }
                c += line.isEmpty() ? 1 : line.size();
            }
        }
        // Vertical
        for (int c = 0; c < GRID_SIZE; c++) {
            for (int r = 0; r < GRID_SIZE - 2; ) {
                List<Point> line = findLine(r, c, 1, 0);
                if (line.size() >= 3) {
                    matches.addAll(line);
                    lastMatchLines.add(line);
                }
                r += line.isEmpty() ? 1 : line.size();
            }
        }
        lastMatches = matches;
        return matches;
    }
    
    public Set<Point> getExplosionCenters(Set<Point> matches) {
        Set<Point> centers = new HashSet<>();
        for (Point p : matches) {
            Jewel jewel = getJewel(p.x, p.y);
            if (jewel != null && jewel.getPowerUp() == PowerUpType.FLAME_GEM) {
                centers.add(p);
            }
        }
        return centers;
    }

    public int clearAndCreatePowerUps() {
        Set<Point> toClear = new HashSet<>(lastMatches);
        
        for (Point p : new HashSet<>(lastMatches)) {
            Jewel jewel = getJewel(p.x, p.y);
            if (jewel != null && jewel.getPowerUp() == PowerUpType.FLAME_GEM) {
                for (int r = p.x - 1; r <= p.x + 1; r++) {
                    for (int c = p.y - 1; c <= p.y + 1; c++) {
                        if (getJewel(r, c) != null) toClear.add(new Point(r, c));
                    }
                }
            }
        }
        
        // Create new power-ups from the original match lines
        for (List<Point> line : lastMatchLines) {
            if (line.size() == 4) {
                Point p = line.get(0); // Create power-up at the start of the line
                grid[p.x][p.y] = new Jewel(getJewel(p.x, p.y).getType(), PowerUpType.FLAME_GEM);
                toClear.remove(p); // Don't clear the new power-up
            } else if (line.size() >= 5) {
                Point p = line.get(0);
                grid[p.x][p.y] = new Jewel(getJewel(p.x, p.y).getType(), PowerUpType.HYPER_CUBE);
                toClear.remove(p);
            }
        }
        
        int clearedCount = toClear.size();
        for (Point p : toClear) {
            grid[p.x][p.y] = null;
        }
        return clearedCount;
    }
    
    public Map<Point, Point> collapseGrid() {
        Map<Point, Point> fallMap = new HashMap<>();
        for (int col = 0; col < GRID_SIZE; col++) {
            int emptyRow = GRID_SIZE - 1;
            for (int row = GRID_SIZE - 1; row >= 0; row--) {
                if (grid[row][col] != null) {
                    Point startPos = new Point(row, col);
                    Point finalPos = new Point(emptyRow, col);
                    if (!startPos.equals(finalPos)) {
                        fallMap.put(finalPos, startPos);
                        swapJewels(startPos, finalPos);
                    }
                    emptyRow--;
                }
            }
        }
        return fallMap;
    }

    public void refillGrid() {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (grid[r][c] == null) {
                    grid[r][c] = new Jewel(JewelType.getRandom());
                }
            }
        }
    }
    
    private List<Point> findLine(int r, int c, int dr, int dc) {
        List<Point> line = new ArrayList<>();
        Jewel startJewel = getJewel(r, c);
        if (startJewel == null) return line;
        JewelType type = startJewel.getType();
        for (int i = 0; i < GRID_SIZE; i++) {
            int newR = r + i * dr;
            int newC = c + i * dc;
            Jewel currentJewel = getJewel(newR, newC);
            if (currentJewel != null && currentJewel.getType() == type) {
                line.add(new Point(newR, newC));
            } else {
                break;
            }
        }
        return line.size() >= 3 ? line : new ArrayList<>();
    }

    public void swapJewels(Point p1, Point p2) {
        Jewel temp = getJewel(p1.x, p1.y);
        grid[p1.x][p1.y] = getJewel(p2.x, p2.y);
        grid[p2.x][p2.y] = temp;
    }

    public boolean isAdjacent(Point p1, Point p2) {
        return (Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y)) == 1;
    }
    
    public Jewel getJewel(int row, int col) {
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) {
            return null;
        }
        return grid[row][col];
    }
}


