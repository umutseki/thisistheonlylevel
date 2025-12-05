// name surname: Umut Efe Seki
// student ID: 2023400204

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

// The Game class handles the main game loop, input, transitions between stages, and UI elements.
public class Game {
    public static boolean isElephantLookingRight = true; // tracks character orientation
    public static boolean resetedGame = false; // tracks if game was reset
    private static int deathNumber = 0; // total number of deaths
    private int stageIndex = 0; // current stage index
    private ArrayList<Stage> stages; // list of all stages
    private long gameTime = 0; // elapsed game time
    private double resetTime = 0;
    private boolean resetGame = false;
    private long totalPausedDuration; // total time spent in pauses
    private boolean showHelp = false; // whether to show help or clue
    private boolean wasMousePressedLastFrame = false;
    private Map map; // current map
    private Player player; // player object
    private long startTime;

    // Constructor to initialize stages
    public Game(ArrayList<Stage> stages) {
        this.stages = stages;
    }

    // Main game loop
    public void play() {
        startTime = System.currentTimeMillis();

        player = new Player(130, 465);
        Stage currentstage = stages.get(stageIndex);
        map = new Map(currentstage, player);

        String finalTime = formatTime(gameTime);

        while (true) {
            resetedGame = false;

            // Quit game
            if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
                System.exit(0);
            }

            // If stage is passed
            if (checkStagePassed(map)) {
                stageIndex++;
                if (stageIndex >= stages.size()) {
                    // All stages completed, show end banner
                    finalTime = formatTime(gameTime);
                    while (!resetedGame) {
                        showEndGameBanner(finalTime);
                    }
                }

                if (stageIndex != 0) {
                    showStagePassedBanner();
                }

                // Move to the next stage
                Stage nextStage = stages.get(stageIndex);
                map = new Map(nextStage, new Player(130, 465));
                showHelp = false;
                continue; // skip current frame
            }

            // Handle keyboard input and game logic
            handleInput(map);
            map.updateButtonPress();

            // Handle mouse click
            boolean isPressed = StdDraw.isMousePressed();
            if (isPressed && !wasMousePressedLastFrame) {
                checkMouseClicks(map);
            }
            wasMousePressedLastFrame = isPressed;

            // UI drawing
            StdDraw.setFont(new Font("Arial", Font.PLAIN, 16));
            StdDraw.clear();
            StdDraw.setPenColor(new Color(56, 93, 172));
            StdDraw.filledRectangle(400, 60, 400, 60); // blue UI background

            // Draw menu
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(250, 85, "Help");
            StdDraw.rectangle(250, 85, 40, 15);
            StdDraw.text(550, 85, "Restart");
            StdDraw.rectangle(550, 85, 40, 15);
            StdDraw.text(400, 20, "RESET THE GAME");
            StdDraw.rectangle(400, 20, 80, 15);

            StdDraw.text(700, 75, "Deaths: " + deathNumber);
            StdDraw.text(700, 50, "Stage: " + (stageIndex + 1));
            StdDraw.text(100, 50, "Time: " + formatTime(gameTime));
            StdDraw.text(100, 75, "Level: 1");

            // Show clue or help
            if (!showHelp) {
                StdDraw.text(400, 85, "Clue:");
                if (stageIndex < stages.size()) {
                    StdDraw.text(400, 55, stages.get(stageIndex).getClue());
                }
            } else {
                StdDraw.text(400, 85, "Help:");
                if (stageIndex < stages.size()) {
                    StdDraw.text(400, 55, stages.get(stageIndex).getHelp());
                }
            }

            // Draw the game world
            map.draw();

            // Spike collision check
            if (checkSpikeCollision(map)) {
                deathNumber++;
                map.restartStage();
                continue; // skip this frame
            }

            // Update time
            long now = System.currentTimeMillis();
            gameTime = (long) (now - startTime - totalPausedDuration);

            // Auto-jump for specific stages
            map.updateAutoJump();
        }
    }

    // Handles keyboard movement and jumping
    private void handleInput(Map map) {
        Stage stage = map.getStage();
        Player player = map.getPlayer();

        int[] keys = stage.getKeyCodes();
        int right = keys[0];
        int left = keys[1];
        int up = keys[2];

        if (StdDraw.isKeyPressed(right)) {
            map.movePlayer('R');
            isElephantLookingRight = true;
        }
        if (StdDraw.isKeyPressed(left)) {
            map.movePlayer('L');
            isElephantLookingRight = false;
        }
        if (up != -1 && StdDraw.isKeyPressed(up)) {
            if (map.isPlayerOnGround()) {
                player.setVelocityY(stage.getVelocityY());
            }
        }

        // Apply gravity and vertical movement
        player.setVelocityY(player.getVelocityY() + stage.getGravity());
        map.applyVerticalMovement();
    }

    // Checks if mouse clicked one of the UI buttons
    private void checkMouseClicks(Map map) {
        if (StdDraw.isMousePressed()) {
            double mouseX = StdDraw.mouseX();
            double mouseY = StdDraw.mouseY();

            if (inBox(mouseX, mouseY, 210, 290, 100, 70)) {
                showHelp = true;
            }

            if (inBox(mouseX, mouseY, 510, 590, 100, 70)) {
                deathNumber++;
                map.restartStage();
            }

            if (inBox(mouseX, mouseY, 320, 480, 35, 5)) {
                showResetedGameBanner();
                resetGame();
            }
        }
    }

    // Checks if a coordinate is inside a rectangle
    private boolean inBox(double x, double y, double leftX, double rightX, double upY, double downY) {
        return x >= leftX && x <= rightX &&
                y >= downY && y <= upY;
    }


    // Check if player collides with any spike
    private boolean checkSpikeCollision(Map map) {
        Player player = map.getPlayer();
        double px = player.getX();
        double py = player.getY();

        for (int[] spike : map.getSpikes()) {
            if (px + 10 > spike[0] && px - 10 < spike[2] &&
                    py + 10 > spike[1] && py - 10 < spike[3]) {
                return true;
            }
        }
        return false;
    }

    // Check if player has reached the exit pipe
    private boolean checkStagePassed(Map map) {
        Player player = map.getPlayer();
        double px = player.getX();
        double py = player.getY();
        double pw = player.getWidth() / 2.0;
        double ph = player.getHeight() / 2.0;

        int[] exitPipe = map.getExitPipe();
        double left = exitPipe[0];
        double right = exitPipe[2];
        double bottom = exitPipe[1];
        double top = exitPipe[3];

        return !(px + pw < left || px - pw > right || py + ph < bottom || py - ph > top);
    }

    // Show banner after completing a stage
    private void showStagePassedBanner() {
        long pauseStart = System.currentTimeMillis();
        StdDraw.setPenColor(new Color(0, 100, 0));
        StdDraw.filledRectangle(400, 300, 500, 80);

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 30));
        StdDraw.text(400, 320, "You passed the stage");
        StdDraw.text(400, 280, "But is the level over?");
        StdDraw.show();
        StdDraw.pause(2000);

        long pauseEnd = System.currentTimeMillis();
        totalPausedDuration += (pauseEnd - pauseStart);
    }

    // Show banner when game is reset
    private void showResetedGameBanner() {
        long pauseStart = System.currentTimeMillis();
        StdDraw.setPenColor(new Color(0, 100, 0));
        StdDraw.filledRectangle(400, 300, 500, 80);

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 40));
        StdDraw.text(400, 300, "RESETING THE GAME...");
        StdDraw.show();
        StdDraw.pause(2000);

        long pauseEnd = System.currentTimeMillis();
        totalPausedDuration += (pauseEnd - pauseStart);
    }

    // Final banner after game is completed
    private void showEndGameBanner(String finalTime) {
        StdDraw.clear();
        StdDraw.setPenColor(new Color(0, 100, 0));
        StdDraw.filledRectangle(400, 300, 500, 100);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 30));
        StdDraw.text(400, 330, "CONGRATULATIONS YOU FINISHED THE LEVEL ");
        StdDraw.text(400, 290, "PRESS A TO PLAY AGAIN");
        StdDraw.setFont(new Font("Arial", Font.BOLD, 20));
        StdDraw.text(400, 255, "You finished with " + deathNumber + " deaths in " + finalTime);
        StdDraw.show();
        StdDraw.pause(10);
        waitForRestartOrQuit();
    }

    // Waits for the user to restart or quit
    private void waitForRestartOrQuit() {
        if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
            System.exit(0);
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_A)) {
            resetGame();
            startTime = System.currentTimeMillis();
            totalPausedDuration = 0;
            resetedGame = true;
        }
    }

    // Resets the game to its initial state
    private void resetGame() {
        stageIndex = 0;
        player = new Player(130, 465);
        map = new Map(stages.get(stageIndex), player);
        deathNumber = 0;
        startTime = System.currentTimeMillis();
        totalPausedDuration = 0;
        showHelp = false;
    }

    // Formats game time as mm:ss:ms
    private String formatTime(long millis) {
        long minutes = millis / 60000;
        long seconds = (millis % 60000) / 1000;
        long ms = (millis % 1000) / 10;
        return String.format("%02d:%02d:%02d", minutes, seconds, ms);
    }
}
