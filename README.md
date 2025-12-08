# Tetris Game (CW2025)

## GitHub

[https://github.com/belalalkafoury/CW2025.git](https://github.com/belalalkafoury/CW2025.git)

## Compilation Instructions

1. **Prerequisites:**
   - Java 23 or compatible version
   - Maven 3.x
   - JavaFX SDK 21.0.6 (managed automatically via Maven)

2. **Compile and Run:**
   ```bash
   mvn clean javafx:run
   ```

3. **Project Structure:**
   - All Java classes are located in `src/main/java/com/comp2042/`
   - Resources (images, fonts, sounds, styles) are in `src/main/resources/`
   - FXML files for UI layouts are in `src/main/resources/`

## Implemented and Working Properly

### Game Modes
- **Classic Mode**: Traditional Tetris gameplay with increasing difficulty levels based on lines cleared
- **Time Attack Mode**: Race against the clock with random duration (30-120 seconds) and dynamically calculated target score
- **Puzzle Mode**: Start with 10 rows of garbage blocks. Clear 40 lines to win
- **Reverted Controls Mode**: Left and right movement controls are swapped

### Gameplay Features
- **Next 3 Pieces Preview**: Display queue showing the next 3 pieces
- **Hold Piece System**: Press 'H' to hold and swap pieces (once per turn)
- **Ghost Piece**: Visual outline showing where the current piece will land
- **7-Bag Randomizer**: Fair distribution of all 7 piece types
- **Wall Kick System**: Pieces can rotate near walls using offset testing
- **Combo System**: Consecutive line clears multiply score with visual/audio feedback

### Controls
- `LEFT/A` / `RIGHT/D`: Move left/right (swapped in Reverted Mode)
- `UP/W`: Rotate piece counterclockwise
- `DOWN/S`: Soft drop (+1 point per cell)
- `SPACE`: Hard drop (+2 points per cell)
- `H`: Hold piece
- `ESC`: Pause game

### Visual Features
- Particle explosion animations for line clears
- Combo and score floating text animations
- Line clear flash and fade transitions
- Neon aesthetic with glowing effects
- Countdown timer (3-2-1) before game starts
- Ghost piece visualization

### User Interface
- Main menu with animated Tetris logo
- Name entry panel (max 8 characters or "GUEST")
- Game over panel with play again/leaderboard options
- Settings panel (animations, ghost piece, volume controls)
- How to play instructions panel
- Leaderboard panel with per-mode high scores
- Pause menu with mode switching

### High Score System
- Per-mode leaderboards (Classic, Time Attack, Puzzle, Reverted)
- Player name tracking with score updates
- Persistent storage to `highscores.dat`
- In-game high score display
- Guest scores excluded from leaderboard

### Sound & Music
- Background title music
- Sound effects for all game actions
- Separate volume controls for Sound FX and Music
- Settings persistence using Java Preferences API

### Scoring System
- Soft Drop: +1 point per cell
- Hard Drop: +2 points per cell
- Line Clear: Base 50 points × (lines cleared)²
- Combo Multiplier: (1 + combo count)
- Formula: `(Lines × 50 × Lines) × (1 + Combo)`
- Level progression: Every 10 lines increases level (affects drop speed)

## Implemented but Not Working Properly

None identified. All implemented features are functioning as expected.

## Features Not Implemented

- **Multiplayer Mode**: No local or online multiplayer functionality
- **Save/Load Game State**: Cannot save mid-game progress and resume later
- **Custom Key Bindings**: Key controls are hardcoded and cannot be customized
- **Level Selector**: No ability to choose starting difficulty level (always starts at level 1)

## New Java Classes

### UI Components (`com.comp2042.view`)
- **`Particle.java`**: Handles particle explosion animations for line clears
- **`GameModePanel.java`**: Displays game mode selection screen with animated mode images
- **`NameEntryPanel.java`**: Panel for entering player name with validation (max 8 characters)
- **`LeaderboardPanel.java`**: Hall of Fame display with tabbed interface for different game modes
- **`GameOverPanel.java`**: Game over screen with Play Again, Leaderboard, and Main Menu options
- **`SettingsPanel.java`**: Settings menu with toggles and volume controls
- **`HowToPlayPanel.java`**: Displays game instructions using an image overlay
- **`TetrisLogo.java`**: Animated logo component with colored letters, gradients, and interactive effects

### Utilities (`com.comp2042.util`)
- **`FontLoader.java`**: Centralized font loading utility with caching (Press Start 2P, Tetricide, Digital)
- **`Logger.java`**: Simple logging utility for consistent error, warning, and info messages
- **`GameConstants.java`**: Centralized constants for window dimensions, brick sizes, offsets, game mode targets, and volume settings
- **`BrickColorMapper.java`**: Utility for mapping brick color codes to JavaFX Paint objects

### Factories (`com.comp2042.view.factory`)
- **`ButtonFactory.java`**: Factory for creating styled buttons with consistent appearance and hover effects

### Renderers (`com.comp2042.view.renderer`)
- **`GameBoardRenderer.java`**: Handles rendering of the game board matrix and grid visualization
- **`GhostPieceRenderer.java`**: Handles rendering of the ghost piece preview
- **`NextPieceRenderer.java`**: Handles rendering of next pieces and held piece previews

### Animation (`com.comp2042.view.animation`)
- **`AnimationFactory.java`**: Factory for creating common game animations (floating text, blink effects)

### Game Mode Strategies (`com.comp2042.logic.mode`)
- **`GameModeStrategy.java`**: Strategy interface for different game modes
- **`ClassicModeStrategy.java`**: Strategy implementation for Classic Tetris mode
- **`TimeAttackModeStrategy.java`**: Strategy implementation for Time Attack mode with timer management
- **`PuzzleModeStrategy.java`**: Strategy implementation for Puzzle mode with garbage row setup
- **`RevertedModeStrategy.java`**: Strategy implementation for Reverted mode
- **`GameModeStrategyFactory.java`**: Factory for creating game mode strategy instances

### Controllers (`com.comp2042.controller`)
- **`MainMenuAnimationController.java`**: Controls animated falling Tetris pieces on main menu background

### Models (`com.comp2042.model`)
- **`HighScoreEntry.java`**: Data class representing a single high score entry (name, score, game mode)
- **`GameSettings.java`**: Persistent settings manager using Java Preferences API

### Logic (`com.comp2042.logic.score`)
- **`HighScoreManager.java`**: Manages per-mode high score storage and retrieval with file I/O

## Modified Java Classes

### `GameController.java`
- Added Time Attack mode initialization with random duration and target score calculation
- Added Puzzle mode setup integration
- Added combo system integration with visual and audio feedback
- Added win condition checking for Time Attack and Puzzle modes
- Integrated player name for high score tracking
- **Refactored to use Strategy pattern for game modes** (replaced if/else chains with GameModeStrategy)
- **Extracted handleBrickLanding() into smaller methods** (mergeBrickToBoard, handleLineClears, handleNoClears, spawnNewBrickOrGameOver)

### `GuiController.java`
- Added Next 3 Pieces display system
- Added Hold Piece display panel
- Added particle explosion system for line clears
- Added combo animation display
- Added score addition animation
- Added per-mode high score display
- Added mode-specific UI configuration (Time Attack timer, Puzzle goal counter)
- Added comprehensive view reset functionality for mode switching
- **Extracted rendering logic to GameBoardRenderer, GhostPieceRenderer, and NextPieceRenderer**
- **Replaced inline button styling with ButtonFactory**
- **Replaced color mapping logic with BrickColorMapper utility**
- **Replaced animation creation with AnimationFactory**
- **Moved all UI constants to GameConstants class**

### `GameBoard.java`
- Replaced single `nextBrick` with `Deque<Brick> nextBricks` for Next 3 Pieces system
- Added Hold Piece mechanics (`heldBrick`, `canHold` flag)
- Implemented Wall Kick System with offset testing
- Added Puzzle Mode initialization with garbage line generation
- Added defensive copying for encapsulation
- **Optimized newGame() method**
- **Moved constants to GameConstants** (START_X, START_Y, NEXT_PIECES_COUNT, PUZZLE_GARBAGE_HEIGHT)

### `Score.java`
- Added combo counter (`IntegerProperty combo`)
- Added combo increment/reset methods

### `ScoreService.java`
- Refactored scoring logic to calculate points internally (Single Responsibility Principle)
- Added combo multiplier to line clear bonus calculation

### `ClearRow.java`
- Removed `scoreBonus` field (scoring moved to ScoreService)
- Simplified to only contain line clear data

### `MatrixOperations.java`
- Added `generateGarbage()` method for Puzzle Mode
- Removed score calculation (moved to ScoreService)

### `BrickFactory.java`
- Replaced random brick generation with 7-Bag Randomizer system
- Added `refillBag()` method to ensure fair piece distribution

### `InputHandler.java`
- Added GameMode parameter to constructor
- Implemented Reverted Controls Mode logic (swaps left/right)

### `MainMenuController.java`
- Added Name Entry Panel integration
- Added game mode selection with callback system
- Added high score manager initialization
- Added `TetrisLogo` integration for animated main menu
- Added `MainMenuAnimationController` for background falling pieces
- Integrated `GameSettings` for loading user preferences

### `ViewData.java`
- Replaced `nextBrickData` with `List<int[][]> nextPieces` for Next 3 Pieces
- Added `heldBrickData` field for Hold Piece display

### `SoundController.java`
- Added separate volume control methods for sound FX and music
- Integrated with `GameSettings` for persistent volume preferences
- **Refactored setSoundVolume() to iterate over collection**
- **Replaced System.err.println with Logger utility**

### `Main.java`
- **Updated to use GameConstants for window dimensions**

### UI Panel Classes
- **`NameEntryPanel.java`**: Replaced inline button styling with ButtonFactory, updated to use FontLoader
- **`SettingsPanel.java`**: Replaced inline button styling with ButtonFactory, updated to use FontLoader
- **`TetrisLogo.java`**: Updated to use FontLoader.loadTetricideFont()
- **`GameModePanel.java`**: Updated to use FontLoader utility
- **`HowToPlayPanel.java`**: Updated to use FontLoader utility
- **`LeaderboardPanel.java`**: Updated to use FontLoader utility
- **`GameOverPanel.java`**: Updated to use FontLoader utility

### Logic Classes
- **`HighScoreManager.java`**: Replaced System.err.println with Logger utility
- **`HighScoreService.java`**: Replaced System.err.println with Logger utility

## Unexpected Problems

### Block Spawning Outside Border
Initially, blocks would spawn visually outside the game border due to coordinate system mismatches between matrix dimensions and UI positioning. Resolved by correcting matrix dimensions and adjusting UI layout.

### Game State Persistence Between Modes
When switching game modes, garbage lines from Puzzle Mode would persist. Fixed by adding explicit matrix zeroing in `newGame()` and creating comprehensive `resetView()` method that clears all UI elements.

### Name Entry Panel Not Visible
The Name Entry Panel was created but not added to the scene graph. Fixed by ensuring the panel is added to the root Pane in `MainMenuController.initialize()`.

### Leaderboard Missing Reverted Tab
The leaderboard only showed tabs for Classic, Time Attack, and Puzzle modes. Added the missing Reverted tab and ensured `GameMode.REVERTED` is handled in save/load logic.

### Duplicate High Scores for Same Player
When a player achieved multiple high scores, duplicate entries were created. Fixed by updating `HighScoreManager` to check for existing entries by name and update the score if higher.

### Test Failure for 7-Bag Randomizer
Unit test `testSevenBagRandomizer()` was failing because it didn't account for the initial brick created by `newGame()`. Fixed by correctly accounting for the initial brick in the test.

### Particle Positioning Issues
Particles spawned during line clears were not visible due to incorrect coordinate calculations. Fixed by calculating positions relative to `gameAreaPane` and adding particles to `rootPane`.

### Hard Drop Animation Timing
Hard drop flash animation was called before the piece locked. Fixed by calling the animation after the board is refreshed.

## License

This project is part of coursework (CW2025).

