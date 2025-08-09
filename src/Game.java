/**
 * Manages the high-level state of a single game session.
 * This includes the game mode, score, board state, timer/move count, and pause state.
 */
public class Game {

    private Board board;
    private GameMode mode;
    private int score;
    private int timerSeconds;
    private int movesLeft;
    private boolean isPaused;
    private boolean isGameOver;

    public Game(GameMode mode) {
        this.mode = mode;
        this.board = new Board();
        this.score = 0;
        this.isPaused = false;
        this.isGameOver = false;

        if (mode == GameMode.TIMED) {
            this.timerSeconds = 120; // 2 minutes
        } else {
            this.movesLeft = 50;
        }
        board.populateBoard();
    }
    
    public void decrementTimer() {
        if (!isPaused && timerSeconds > 0) {
            timerSeconds--;
            if (timerSeconds <= 0) {
                timerSeconds = 0;
                isGameOver = true;
            }
        }
    }

    public void decrementMoves() {
        if (movesLeft > 0) {
            movesLeft--;
            if (movesLeft <= 0) {
                movesLeft = 0;
                isGameOver = true;
            }
        }
    }

    public void addToScore(int points) { this.score += points; }
    public void togglePause() { this.isPaused = !this.isPaused; }
    public void endGame() { this.isGameOver = true; }

    // Getters
    public Board getBoard() { return board; }
    public GameMode getMode() { return mode; }
    public int getScore() { return score; }
    public boolean isPaused() { return isPaused; }
    public boolean isGameOver() { return isGameOver; }

    public String getStatusString() {
        if (mode == GameMode.TIMED) {
            int minutes = timerSeconds / 60;
            int seconds = timerSeconds % 60;
            return String.format("%02d:%02d", minutes, seconds);
        } else {
            return String.valueOf(movesLeft);
        }
    }
}
