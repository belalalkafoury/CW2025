# Tetris Game (CW2025)



## GitHub Repo

[https://github.com/belalalkafoury/CW2025.git]

## Compilation Instructions

- **Step 1:** Ensure all Java classes (`Main`, `GameController`, `GuiController`, `GameBoard`, etc.) are in the correct packages under `src/main/java/com/comp2042/`.

- **Step 2:** Make sure JavaFX is installed or configured. This project uses Maven to handle JavaFX dependencies via the `javafx-maven-plugin`.

- **Step 3:** Compile and run the application:

    - **Via Terminal:**

      ```bash

      mvn clean javafx:run

      ```

## Requirements

- Java 23 or compatible version

- Maven 3.x

- JavaFX SDK 21.0.6 (managed automatically via Maven)

## Implemented and Working Properly

- **Game Modes:**

  - **Classic Mode**: Traditional Tetris gameplay with increasing difficulty levels based on lines cleared

  - **Time Attack Mode**: Race against the clock with random duration (30-120 seconds) and dynamically calculated target score. Win by reaching the target before time runs out

  - **Puzzle Mode**: Start with 10 rows of garbage blocks at the bottom. Clear 40 lines to win. Each garbage row maintains at least one empty cell to prevent immediate line clears

  - **Reverted Controls Mode**: Left and right movement controls are swapped for an added challenge. Up (rotate) and Down (soft drop) remain unchanged

- **Advanced Gameplay Mechanics:**

  - **Next 3 Pieces Preview**: Display queue showing the next 3 pieces coming up, allowing strategic planning

  - **Hold Piece System**: Press 'H' to hold the current piece and swap it later. Can only hold once per turn (resets when a new piece spawns)

  - **Ghost Piece**: Visual outline indicator showing where the current piece will land, helping with placement decisions

  - **7-Bag Randomizer**: Guarantees fair distribution of all 7 piece types (I, J, L, O, S, T, Z). Each set of 7 pieces contains exactly one of each type before refilling the bag

  - **Wall Kick System**: Pieces can rotate even when adjacent to walls or other blocks. Tests multiple offset positions to find valid rotation spots

  - **Combo System**: Consecutive line clears multiply your score. Formula: `(Lines Cleared × 50 × Lines Cleared) × (1 + Combo Count)`. Visual "COMBO x2!" animation and audio pitch shifting for higher combos

- **Tile Movement & Controls:**

  - Arrow Keys / WASD: Move and rotate pieces

    - `LEFT/A` / `RIGHT/D`: Move left/right (swapped in Reverted Mode)

    - `UP/W`: Rotate piece counterclockwise

    - `DOWN/S`: Soft drop (faster fall, +1 point per cell)

  - `SPACE`: Hard drop (instant drop to landing position, +2 points per cell)

  - `H`: Hold piece

  - `ESC`: Pause game

  - `N`: New game (debug key)

- **Visual Features & Animations:**

  - **Particle Explosion System**: When clearing lines, 5-10 animated particles explode from each cleared block, flying outward and fading away

  - **Combo Animation**: Floating "COMBO x2!" text that scales up and fades out when combos occur

  - **Score Animation**: Floating score addition text showing points earned

  - **Line Clear Animation**: Sequential flash (white) and fade transitions when lines are cleared

  - **Neon Aesthetic**: Modern cyberpunk-inspired visual style with glowing effects, drop shadows, and vibrant colors

  - **Smooth Transitions**: Polished animations for all UI elements, menu transitions, and game state changes

  - **Countdown Timer**: 3-2-1 countdown before game starts

  - **Ghost Piece Visualization**: Semi-transparent outline showing landing position

- **User Interface:**

  - **Main Menu**: Animated Tetris logo and game mode selection

  - **Name Entry Panel**: Enter player name (max 8 characters) or play as "GUEST"

  - **Game Over Panel**: Display final score with options to play again, view leaderboard, or return to main menu

  - **Settings Panel**: Toggle animations on/off, toggle ghost piece display, separate Sound FX and Music volume controls (with +/- buttons), reset high scores, reset all options to defaults

  - **How to Play Panel**: Display game instructions and controls

  - **Leaderboard Panel**: Hall of Fame showing top scores per game mode (Classic, Time Attack, Puzzle, Reverted) in a centered 500x500 popup window

  - **Pause Menu**: Change game mode, resume, or return to main menu mid-game

- **High Score System:**

  - **Per-Mode Leaderboards**: Separate high score tracking for each game mode (CLASSIC, TIME_ATTACK, PUZZLE, REVERTED)

  - **Player Names**: Save scores with player names. If the same name achieves a higher score, the existing entry is updated

  - **Guest Exclusion**: "GUEST" scores are not saved to the leaderboard

  - **Persistent Storage**: High scores saved to `highscores.dat` file and loaded on game startup

  - **In-Game Display**: Shows current mode's highest score and player name in the bottom right during gameplay

  - **Hall of Fame**: Accessible from game over screen or main menu, displays top scores with tabs for each game mode

- **Sound & Music:**

  - Background title music

  - Sound effects for: brick movement, rotation, hard drop, line clear, combo (with pitch shifting), game over, next level/victory

  - Separate volume controls for Sound FX and Music in settings (persistent across sessions)

  - Settings persistence using Java Preferences API

- **Game State Management:**

  - **Game Over Detection**: Automatically detects when no valid moves remain (piece spawns in occupied space)

  - **Win Conditions**:

    - Time Attack: Reach target score before timer reaches 0:00

    - Puzzle: Clear 40 lines

    - Classic: Survive as long as possible (traditional)

  - **Restart Functionality**: Play again button starts a fresh game in the same mode

  - **Mode Switching**: Can change game mode from pause menu or main menu

- **Scoring System:**

  - Soft Drop: +1 point per cell dropped

  - Hard Drop: +2 points per cell dropped

  - Line Clear: Base 50 points × (lines cleared)²

  - Combo Multiplier: (1 + combo count) applied to line clear score

  - Final Formula: `(Lines × 50 × Lines) × (1 + Combo)`

  - Level progression: Every 10 lines cleared increases the level (affects drop speed)

- **Code Quality & Architecture:**

  - Clean separation of concerns (Model-View-Controller pattern)

  - Defensive copying to prevent external modification of game state

  - DRY principles: Helper methods eliminate code duplication

  - Magic numbers extracted to named constants

  - Comprehensive unit tests for core mechanics (7-bag, wall kicks, hold, puzzle mode)

## Implemented but Not Working Properly

- **None identified**: All implemented features are functioning as expected.

## Features Not Implemented

- **Multiplayer Mode**: No local or online multiplayer functionality was implemented

- **Save/Load Game State**: Cannot save mid-game progress and resume later

- **Custom Key Bindings**: Key controls are hardcoded and cannot be customized by the user

- **Level Selector**: No ability to choose starting difficulty level (always starts at level 1)

## New Java Classes

- **`Particle.java`**: Handles particle explosion animations for line clears. Extends Rectangle with automatic animation (translate + fade) and cleanup

- **`GameModePanel.java`**: Displays game mode selection screen with animated mode images and callbacks

- **`NameEntryPanel.java`**: Panel for entering player name before starting a game. Includes validation (max 8 characters) and "GUEST" option

- **`LeaderboardPanel.java`**: Hall of Fame display with tabbed interface for different game modes. Shows top scores in a centered popup window

- **`GameOverPanel.java`**: Game over screen displaying final score with buttons for Play Again, Leaderboard, and Main Menu

- **`SettingsPanel.java`**: Settings menu with toggles for animations, ghost piece, sound volume slider, and reset high scores button

- **`HowToPlayPanel.java`**: Displays game instructions and controls using an image overlay

- **`HighScoreManager.java`**: Manages per-mode high score storage and retrieval. Handles file I/O, score updates, and filtering

- **`HighScoreEntry.java`**: Data class representing a single high score entry (name, score, game mode)

- **`TetrisLogo.java`**: Animated logo component for the main menu. Displays "TETRIS" text with colored letters, beveled gradients, glow effects, and interactive animations (hover and click effects)

- **`GameSettings.java`**: Persistent settings manager using Java Preferences API. Stores and loads user preferences for animations, ghost piece, sound FX volume, and music volume across game sessions

- **`MainMenuAnimationController.java`**: Controls animated falling Tetris pieces on the main menu background. Manages 10 pieces with random speeds, rotations, and collision detection for visual appeal

## Modified Java Classes

- **`GameController.java`**: 

  - Added Time Attack mode initialization with random duration and target score calculation

  - Added Puzzle mode setup integration

  - Added combo system integration with visual and audio feedback

  - Added win condition checking for Time Attack and Puzzle modes

  - Integrated player name for high score tracking

- **`GuiController.java`**: 

  - Added Next 3 Pieces display system

  - Added Hold Piece display panel

  - Added particle explosion system for line clears

  - Added combo animation display

  - Added score addition animation

  - Added per-mode high score display

  - Added mode-specific UI configuration (Time Attack timer, Puzzle goal counter)

  - Added comprehensive view reset functionality for mode switching

  - Refactored rendering code using helper methods (DRY principle)

- **`GameBoard.java`**: 

  - Replaced single `nextBrick` with `Deque<Brick> nextBricks` for Next 3 Pieces system

  - Added Hold Piece mechanics (`heldBrick`, `canHold` flag)

  - Implemented Wall Kick System with offset testing

  - Added Puzzle Mode initialization with garbage line generation

  - Added defensive copying for encapsulation

- **`Score.java`**: 

  - Added combo counter (`IntegerProperty combo`)

  - Added combo increment/reset methods

- **`ScoreService.java`**: 

  - Refactored scoring logic to calculate points internally (Single Responsibility Principle)

  - Added combo multiplier to line clear bonus calculation

- **`ClearRow.java`**: 

  - Removed `scoreBonus` field (scoring moved to ScoreService)

  - Simplified to only contain line clear data

- **`MatrixOperations.java`**: 

  - Added `generateGarbage()` method for Puzzle Mode

  - Removed score calculation (moved to ScoreService)

- **`BrickFactory.java`**: 

  - Replaced random brick generation with 7-Bag Randomizer system

  - Added `refillBag()` method to ensure fair piece distribution

- **`InputHandler.java`**: 

  - Added GameMode parameter to constructor

  - Implemented Reverted Controls Mode logic (swaps left/right)

- **`MainMenuController.java`**: 

  - Added Name Entry Panel integration

  - Added game mode selection with callback system

  - Added high score manager initialization

- **`ViewData.java`**: 

  - Replaced `nextBrickData` with `List<int[][]> nextPieces` for Next 3 Pieces

  - Added `heldBrickData` field for Hold Piece display

- **`SoundController.java`**: 

  - Added separate volume control methods for sound FX and music

  - Integrated with `GameSettings` for persistent volume preferences

- **`MainMenuController.java`**: 

  - Added `TetrisLogo` integration for animated main menu

  - Added `MainMenuAnimationController` for background falling pieces

  - Integrated `GameSettings` for loading user preferences

## Unexpected Problems

- **Block Spawning Outside Border**: 

  Initially, blocks would spawn visually outside the game border. This was caused by coordinate system mismatches between the matrix dimensions and UI positioning. The issue was resolved by correcting matrix dimensions (`int[height][width]` vs `int[width][height]`), adjusting UI layout, and applying clipping rectangles, though this fix was later reverted per user request.

- **Game State Persistence Between Modes**: 

  When switching game modes (e.g., Puzzle → Time Attack), garbage lines from Puzzle Mode would persist. This occurred because `GameBoard.newGame()` wasn't fully resetting the matrix and `GuiController` wasn't clearing visual elements. The solution was to add explicit matrix zeroing in `newGame()` and create a comprehensive `resetView()` method that clears all UI elements (brickPanel, holdPanel, next pieces, displayMatrix).

- **Name Entry Panel Not Visible**: 

  The Name Entry Panel was created but not added to the scene graph, causing it to never appear. Fixed by ensuring the panel is added to the root Pane in `MainMenuController.initialize()` and properly shown/hidden during the game start flow.

- **High Score Display in Game Over Menu**: 

  Initially, high scores were displayed in the game over screen, cluttering the interface. Removed per user request to simplify the game over experience.

- **Leaderboard Missing Reverted Tab**: 

  The leaderboard only showed tabs for Classic, Time Attack, and Puzzle modes, but not Reverted. Added the missing tab and ensured `GameMode.REVERTED` is handled in save/load logic.

- **Duplicate High Scores for Same Player**: 

  When a player achieved multiple high scores, duplicate entries were created in the leaderboard. Fixed by updating `HighScoreManager` to check for existing entries by name and update the score if the new score is higher.

- **Test Failure for 7-Bag Randomizer**: 

  Unit test `testSevenBagRandomizer()` was failing (Expected 7, Actual 6). The issue was that the test didn't account for the initial brick created by `newGame()`. Fixed by correctly accounting for the initial brick in the test.

- **Particle Positioning Issues**: 

  Particles spawned during line clears were not visible due to incorrect coordinate calculations and parent container issues. Fixed by calculating positions relative to `gameAreaPane` and adding particles to `rootPane` instead of `groupNotification`.

- **Hard Drop Animation Timing**: 

  Hard drop flash animation was called before the piece locked, making it ineffective. Fixed by calling the animation after the board is refreshed, though this feature was later removed per user request.

## Project Structure

```
src/main/java/com/comp2042/

├── controller/          # Game logic and UI controllers

│   ├── GameController.java      # Main game loop and mode logic

│   ├── GuiController.java       # UI rendering and animations

│   ├── MainMenuController.java  # Main menu management

│   ├── MainMenuAnimationController.java  # Main menu falling pieces

│   ├── InputHandler.java        # Keyboard input handling

│   ├── SoundController.java     # Audio management

│   └── AnimationController.java # Animation timeline management

├── model/               # Game state and data models

│   ├── GameBoard.java          # Core game board logic

│   ├── Score.java              # Score tracking

│   ├── GameMode.java           # Game mode enumeration

│   ├── GameSettings.java       # Persistent settings manager

│   └── HighScoreEntry.java     # High score data structure

├── view/                # UI components and panels

│   ├── Particle.java           # Particle animation class

│   ├── TetrisLogo.java         # Animated main menu logo

│   ├── LeaderboardPanel.java   # Hall of Fame display

│   ├── NameEntryPanel.java     # Player name input

│   ├── SettingsPanel.java      # Settings menu

│   └── HowToPlayPanel.java     # Instructions panel

├── logic/               # Core game mechanics

│   ├── board/                  # Board operations and rotations

│   ├── bricks/                 # Piece types and factory

│   ├── collision/              # Collision detection

│   └── score/                  # Scoring logic and high scores

└── main/                # Application entry point

    └── Main.java

```

## License

This project is part of coursework (CW2025).

