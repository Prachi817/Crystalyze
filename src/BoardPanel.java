import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.*;

/**
 * Visually represents the game board and handles all animations.
 */
public class BoardPanel extends JPanel {

    public static final int GRID_SIZE = 8;
    public static final int JEWEL_SIZE = 60;
    private static final int ANIMATION_DELAY = 10;
    private static final int ANIMATION_STEPS = 15;

    private GameController gameController;
    
    private float animationProgress = 0.0f;
    private Timer animationTimer;
    
    private Point swapFrom, swapTo;
    private Set<Point> highlightingJewels = new HashSet<>();
    private Set<Point> destroyingJewels = new HashSet<>();
    private Set<Point> explodingJewels = new HashSet<>();
    private Map<Point, Point> fallingJewels = new HashMap<>();

    public BoardPanel(GameController controller) {
        this.gameController = controller;
        setPreferredSize(new Dimension(GRID_SIZE * JEWEL_SIZE, GRID_SIZE * JEWEL_SIZE));
        setBackground(new Color(30, 30, 30));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameController.isGameActive() && !gameController.isAnimating()) {
                    int col = e.getX() / JEWEL_SIZE;
                    int row = e.getY() / JEWEL_SIZE;
                    if (row < GRID_SIZE && col < GRID_SIZE) {
                        gameController.jewelClicked(row, col);
                    }
                }
            }
        });
    }

    public void animateSwap(Point from, Point to, Runnable onFinish) {
        this.swapFrom = from;
        this.swapTo = to;
        startAnimation(onFinish);
    }

    public void animateHighlight(Set<Point> matches, Runnable onFinish) {
        this.highlightingJewels = new HashSet<>(matches);
        startAnimation(() -> {
            highlightingJewels.clear();
            onFinish.run();
        });
    }

    public void animateDestruction(Set<Point> matches, Runnable onFinish) {
        this.destroyingJewels = new HashSet<>(matches);
        startAnimation(() -> {
            destroyingJewels.clear();
            onFinish.run();
        });
    }

    public void animateExplosion(Set<Point> centers, Runnable onFinish) {
        this.explodingJewels = new HashSet<>(centers);
        startAnimation(() -> {
            explodingJewels.clear();
            onFinish.run();
        });
    }

    public void animateFall(Map<Point, Point> fallMap, Runnable onFinish) {
        this.fallingJewels = new HashMap<>(fallMap);
        startAnimation(() -> {
            fallingJewels.clear();
            onFinish.run();
        });
    }

    private void startAnimation(Runnable onFinish) {
        animationProgress = 0.0f;
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        animationTimer = new Timer(ANIMATION_DELAY, e -> {
            animationProgress += 1.0f / ANIMATION_STEPS;
            if (animationProgress >= 1.0f) {
                animationTimer.stop();
                animationProgress = 0.0f;
                swapFrom = swapTo = null;
                onFinish.run();
            }
            repaint();
        });
        animationTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Board board = gameController.getBoard();
        if (board == null) return;

        drawGrid(g2d);

        // Draw static jewels
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                Point p = new Point(r, c);
                if (isAnimating(p)) continue;
                if (board.getJewel(r,c) != null) {
                   drawJewel(g2d, board.getJewel(r, c), r, c);
                }
            }
        }
        
        // Draw animations
        if (swapFrom != null) drawSwappingJewels(g2d);
        if (!destroyingJewels.isEmpty()) drawDestroyingJewels(g2d);
        if (!explodingJewels.isEmpty()) drawExplosion(g2d);
        if (!fallingJewels.isEmpty()) drawFallingJewels(g2d);

        // Draw selection highlight
        Point selected = gameController.getSelectedJewel();
        if (selected != null) {
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(selected.y * JEWEL_SIZE + 2, selected.x * JEWEL_SIZE + 2, JEWEL_SIZE - 4, JEWEL_SIZE - 4);
        }
    }

    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(80, 80, 80));
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                g2d.drawRect(i * JEWEL_SIZE, j * JEWEL_SIZE, JEWEL_SIZE, JEWEL_SIZE);
            }
        }
    }

    private boolean isAnimating(Point p) {
        if (swapFrom != null && (p.equals(swapFrom) || p.equals(swapTo))) return true;
        if (destroyingJewels.contains(p)) return true;
        if (fallingJewels.containsKey(p)) return true;
        return false;
    }

    private void drawSwappingJewels(Graphics2D g2d) {
        Jewel j1 = gameController.getBoard().getJewel(swapFrom.x, swapFrom.y);
        Jewel j2 = gameController.getBoard().getJewel(swapTo.x, swapTo.y);
        int x1 = (int) (swapFrom.y * JEWEL_SIZE + (swapTo.y - swapFrom.y) * JEWEL_SIZE * animationProgress);
        int y1 = (int) (swapFrom.x * JEWEL_SIZE + (swapTo.x - swapFrom.x) * JEWEL_SIZE * animationProgress);
        int x2 = (int) (swapTo.y * JEWEL_SIZE - (swapTo.y - swapFrom.y) * JEWEL_SIZE * animationProgress);
        int y2 = (int) (swapTo.x * JEWEL_SIZE - (swapTo.x - swapFrom.x) * JEWEL_SIZE * animationProgress);
        drawJewelAt(g2d, j2, y1, x1, 1.0f);
        drawJewelAt(g2d, j1, y2, x2, 1.0f);
    }

    private void drawDestroyingJewels(Graphics2D g2d) {
        float scale = 1.0f - animationProgress;
        for (Point p : destroyingJewels) {
            Jewel jewel = gameController.getBoard().getJewel(p.x, p.y);
            drawJewelAt(g2d, jewel, p.x * JEWEL_SIZE, p.y * JEWEL_SIZE, scale);
        }
    }

    private void drawExplosion(Graphics2D g2d) {
        g2d.setColor(new Color(255, 200, 50, 150));
        float radius = (JEWEL_SIZE * 1.5f) * animationProgress;
        for (Point p : explodingJewels) {
            float centerX = p.y * JEWEL_SIZE + JEWEL_SIZE / 2;
            float centerY = p.x * JEWEL_SIZE + JEWEL_SIZE / 2;
            g2d.fill(new Ellipse2D.Float(centerX - radius, centerY - radius, radius * 2, radius * 2));
        }
    }

    private void drawFallingJewels(Graphics2D g2d) {
        for (Map.Entry<Point, Point> entry : fallingJewels.entrySet()) {
            Point finalPos = entry.getKey();
            Point startPos = entry.getValue();
            Jewel jewel = gameController.getBoard().getJewel(finalPos.x, finalPos.y);
            int startY = startPos.x * JEWEL_SIZE;
            int finalY = finalPos.x * JEWEL_SIZE;
            int currentY = (int) (startY + (finalY - startY) * animationProgress);
            drawJewelAt(g2d, jewel, currentY, finalPos.y * JEWEL_SIZE, 1.0f);
        }
    }

    private void drawJewel(Graphics2D g, Jewel jewel, int row, int col) {
        drawJewelAt(g, jewel, row * JEWEL_SIZE, col * JEWEL_SIZE, 1.0f);
    }

    private void drawJewelAt(Graphics2D g, Jewel jewel, int y, int x, float scale) {
        if (jewel == null || scale <= 0) return;
        
        float jewelSize = JEWEL_SIZE * scale;
        float centerX = x + JEWEL_SIZE / 2f;
        float centerY = y + JEWEL_SIZE / 2f;

        Shape shape = createJewelShape(jewel.getType(), centerX, centerY, jewelSize * 0.9f);

        // Create a faceted look
        Color baseColor = getJewelColor(jewel.getType());
        Color highlightColor = baseColor.brighter();
        
        // Draw darker base shape for the "edge"
        g.setColor(baseColor.darker());
        g.fill(shape);
        
        // Draw smaller, brighter shape on top
        AffineTransform at = AffineTransform.getTranslateInstance(centerX, centerY);
        at.scale(0.85, 0.85);
        at.translate(-centerX, -centerY);
        Shape innerShape = at.createTransformedShape(shape);

        g.setPaint(new RadialGradientPaint(centerX, centerY, jewelSize, new float[]{0f, 1f}, new Color[]{highlightColor, baseColor}));
        g.fill(innerShape);

        // Highlight flashing effect
        if (highlightingJewels.contains(new Point(y / JEWEL_SIZE, x / JEWEL_SIZE)) && (int)(animationProgress * 20) % 2 == 0) {
            g.setColor(new Color(255, 255, 255, 150));
            g.fill(shape);
        }
        
        // Power-up indicator
        if (jewel.getPowerUp() != PowerUpType.NONE) {
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(3 * scale));
            float powerUpSize = jewelSize * 0.3f;
            if(jewel.getPowerUp() == PowerUpType.FLAME_GEM) {
                 g.draw(new Ellipse2D.Float(centerX - powerUpSize/2, centerY - powerUpSize/2, powerUpSize, powerUpSize));
            } else {
                 g.draw(new Rectangle2D.Float(centerX - powerUpSize/2, centerY - powerUpSize/2, powerUpSize, powerUpSize));
            }
        }
    }

    private Shape createJewelShape(JewelType type, float cx, float cy, float size) {
        float radius = size / 2;
        return new Ellipse2D.Float(cx - radius, cy - radius, size, size);
    }

    private Color getJewelColor(JewelType type) {
        switch (type) {
            case RED: return new Color(220, 20, 60);   // Crimson
            case GREEN: return new Color(0, 201, 87);  // Emerald
            case BLUE: return new Color(30, 144, 255); // DodgerBlue
            case YELLOW: return new Color(255, 215, 0); // Gold
            case PURPLE: return new Color(148, 0, 211); // DarkViolet
            case ORANGE: return new Color(255, 140, 0); // DarkOrange
            case WHITE: return new Color(245, 245, 245); // WhiteSmoke
            default: return Color.BLACK;
        }
    }
}