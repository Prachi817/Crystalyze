import java.awt.Point;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * GameController.java 
 * Manages the game flow, state, and interaction between the model and view.
 */
public class GameController {

    private Game game;
    private GameGUI gui;
    private Point selectedJewel;
    private boolean isAnimating = false;
    private Timer gameTimer;

    public GameController(GameGUI gui) {
        this.gui = gui;
    }

    public void startGame(GameMode mode) {
        this.game = new Game(mode);
        this.selectedJewel = null;
        this.isAnimating = false;
        
        if (gameTimer != null) gameTimer.stop();
        
        if (mode == GameMode.TIMED) {
            gameTimer = new Timer(1000, e -> updateTimer());
            gameTimer.start();
        }
        
        updateUI();
        gui.setPauseButtonText(false);
        gui.updateView();
    }
    
    private void updateTimer() {
        if(isPaused()) return;
        game.decrementTimer();
        updateUI();
        if (game.isGameOver()) {
            endGame();
        }
    }
    
    private void endGame() {
        if(gameTimer != null) gameTimer.stop();
        game.endGame();
        gui.showGameOverDialog(game.getScore());
    }

    public void quitCurrentGame() {
        if (gameTimer != null) gameTimer.stop();
        if (game != null) game.endGame();
        isAnimating = false;
        selectedJewel = null;
        game = null;
        updateUI();
        gui.updateView();
    }

    public void jewelClicked(int row, int col) {
        if (isAnimating || isPaused()) return;

        if (selectedJewel == null) {
            selectedJewel = new Point(row, col);
        } else {
            Point secondJewelPos = new Point(row, col);
            if (!selectedJewel.equals(secondJewelPos) && getBoard().isAdjacent(selectedJewel, secondJewelPos)) {
                isAnimating = true;
                attemptSwap(selectedJewel, secondJewelPos);
            }
            selectedJewel = null;
        }
        gui.updateView();
    }

    private void attemptSwap(Point p1, Point p2) {
        gui.getBoardPanel().animateSwap(p1, p2, () -> {
            boolean isValidSwap = getBoard().checkAndPerformSwap(p1, p2);
            
            if (isValidSwap) {
                if (game.getMode() == GameMode.MOVES) {
                    game.decrementMoves();
                }
                processMatches();
            } else {
                gui.getBoardPanel().animateSwap(p2, p1, () -> {
                    isAnimating = false;
                    gui.updateView();
                });
            }
        });
    }
    
    private void processMatches() {
        final AtomicInteger chain = new AtomicInteger(1);
        
        Runnable processNextChain = new Runnable() {
            @Override
            public void run() {
                if (game == null) {
                    isAnimating = false;
                    return;
                }
                
                Set<Point> matches = getBoard().findAllMatches();
                
                // If the board is stable (no more matches)
                if (matches.isEmpty()) {
                    // Now check for game over conditions
                    if (game.isGameOver() || !getBoard().hasValidMoves()) {
                        SwingUtilities.invokeLater(() -> endGame());
                    }
                    isAnimating = false;
                    return;
                }

                // If there are matches, continue the animation chain
                gui.getBoardPanel().animateHighlight(matches, () -> {
                    Set<Point> explosionCenters = getBoard().getExplosionCenters(matches);
                    if (!explosionCenters.isEmpty()) {
                        gui.getBoardPanel().animateExplosion(explosionCenters, () -> {
                            continueChain(chain, this);
                        });
                    } else {
                        gui.getBoardPanel().animateDestruction(matches, () -> {
                            continueChain(chain, this);
                        });
                    }
                });
            }
        };

        processNextChain.run();
    }
    
    private void continueChain(AtomicInteger chain, Runnable next) {
        game.addToScore(getBoard().clearAndCreatePowerUps() * 10 * chain.get());
        updateUIAndBoard();

        Map<Point, Point> fallMap = getBoard().collapseGrid();
        gui.getBoardPanel().animateFall(fallMap, () -> {
            getBoard().refillGrid();
            updateUIAndBoard();
            
            chain.incrementAndGet();
            SwingUtilities.invokeLater(next);
        });
    }
    
    private void updateUIAndBoard() {
        SwingUtilities.invokeLater(() -> {
            updateUI();
            gui.updateView();
        });
    }
    
    public void togglePause() {
        if (game == null || game.isGameOver()) return;
        game.togglePause();
        gui.setPauseButtonText(game.isPaused());
    }
    
    private void updateUI() {
        if (game == null) {
            gui.updateScore(0);
            gui.updateStatus("--", null);
            return;
        }
        gui.updateScore(game.getScore());
        gui.updateStatus(game.getStatusString(), game.getMode());
    }
    
    // Getters
    public Board getBoard() { return game != null ? game.getBoard() : null; }
    public Point getSelectedJewel() { return selectedJewel; }
    public boolean isGameActive() { return game != null && !game.isGameOver(); }
    public boolean isPaused() { return game != null && game.isPaused(); }
    public boolean isAnimating() { return isAnimating; }
}

