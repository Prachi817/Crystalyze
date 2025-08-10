# Crystalyze

## 1. Overview

Crystalyze is a single-player, tile-matching puzzle game developed in Java with Swing. Inspired by classics like Bejeweled, the game is played on an 8x8 grid filled with colorful jewels. The primary objective is to score points by swapping adjacent jewels to create alignments of three or more identical jewels, either horizontally or vertically.

When a match is made, the jewels are cleared from the board, and new jewels fall from above to fill the empty spaces, creating opportunities for "chain reactions" and score multipliers. The game is designed with a clean Model-View-Controller (MVC) architecture to separate game logic, user interface, and control flow.

---

## 2. Features

This beta version of Crystalyze is fully functional and includes the following features:

* **Core Gameplay:** An 8x8 grid that populates with seven types of jewels, with a robust match-detection system.
* **Two Game Modes:**
    * **Timed Mode:** Players have 2 minutes to achieve the highest score possible.
    * **Moves Mode:** Players have a limited 50 moves to maximize their score.

<div align="center">
  <img src="screenshots/timed mode.png" alt="Timed Mode" width="45%" />
  <img src="screenshots/moves mode.png" alt="Moves Mode" width="45%" />
</div>
<p align="center"><em>Left: Timed Mode gameplay | Right: Moves Mode gameplay</em></p>

* **Power-Up Jewels:**
    * **Flame Gem:** Created from a 4-jewel match. When matched, it explodes, clearing the 8 surrounding jewels.
    * **Hyper Cube:** Created from a 5-jewel match. When swapped with an adjacent jewel, it clears all jewels of that color from the board.
* **Full Animation Suite:**
    * Smooth animations for swapping jewels.
    * A "highlight" flash before jewels are destroyed.
    * A shrinking effect for jewel destruction.
    * A dedicated explosion animation for Flame Gems.
    * Jewels smoothly animate as they fall into place.
* **Dynamic UI:**
    * An initial dialog to select the game mode.
    * Real-time display of the current score and remaining time/moves.
    * On-screen buttons to **Pause/Resume** the game or **Exit** to the main menu.
* **Game Over Logic:** The game correctly detects when no valid moves are left or when the time/move limit is reached, displaying a "Game Over" dialog with the final score.

---

## 3. How to Compile and Run

To compile and run the project from the command line, follow these steps:

1.  **Navigate to the Source Directory:**
    Open a terminal or command prompt and navigate to the `src` directory containing all the `.java` files.

2.  **Compile the Code:**
    Run the Java compiler on all the source files.

    ```bash
    javac *.java
    ```

3.  **Run the Game:**
    Execute the main class, `Crystalyze`.

    ```bash
    java Crystalyze
    ```

The game window should now appear, prompting you to select a game mode.

---

## 4. Project Structure

The project is organized following the principles of the Model-View-Controller (MVC) design pattern:

* **Model (`Board.java`, `Jewel.java`, `JewelType.java`, `PowerUpType.java`):**
    * Represents the core data and rules of the game.
    * Manages the 8x8 grid, finds matches, handles jewel creation/destruction, and contains all the logic for power-ups and valid moves. It has no knowledge of the user interface.

* **View (`GameGUI.java`, `BoardPanel.java`):**
    * Represents the visual presentation of the game.
    * `GameGUI` sets up the main window, score panels, and control buttons.
    * `BoardPanel` is responsible for all custom rendering, including drawing the grid, the 3D-style jewels, and handling all animations (swapping, destruction, explosions, falling).

* **Controller (`GameController.java`, `Game.java`, `GameMode.java`):**
    * Acts as the intermediary between the Model and the View.
    * `GameController` processes player input (mouse clicks), orchestrates the game flow, and manages the animation sequence.
    * `Game` holds the high-level state for a single game session, such as the score, mode, and timer/move count.

---

## 5. Team Members

* Shingirai Bhengesa
* Prachi Yadav
* Nahian Tasnim
