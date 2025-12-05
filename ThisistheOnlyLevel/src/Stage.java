// name surname: Umut Efe Seki
// student ID: 2023400204

import java.awt.*;
import java.util.ArrayList;

// The Stage class represents a single level or configuration in the game.
// Each Stage contains physical properties (like gravity and velocities),
// key bindings for movement, a clue/help text for the player, and a unique color.
public class Stage {
    // Unique identifier for the stage (e.g., Level 1, Level 2...)
    private int stageNumber;

    // Gravity affecting the player or object in this stage
    private double gravity;

    // Horizontal velocity component for the stage
    private double velocityX;

    // Vertical velocity component for the stage
    private double velocityY;

    // Key codes for movement: right, left, and up directions respectively
    private int rightCode;
    private int leftCode;
    private int upCode;

    // A short textual clue about how to solve or understand the stage
    private String clue;

    // Additional help or hint for the player (possibly more detailed than the clue)
    private String help;

    // Each stage has a random color for visual distinction (generated on creation)
    private Color color;

    // Constructor initializes all fields with the given parameters
    public Stage(double gravity, double velocityX, double velocityY, int stageNumber,
                 int rightCode, int leftCode, int upCode,
                 String clue, String help) {
        this.gravity = gravity;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.stageNumber = stageNumber;
        this.rightCode = rightCode;
        this.leftCode = leftCode;
        this.upCode = upCode;
        this.clue = clue;
        this.help = help;

        // Generate a random RGB color for the stage, enhancing UI diversity
        this.color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    // Getter methods to access stage attributes externally

    // Returns the stage number (level ID)
    public int getStageNumber() {
        return stageNumber;
    }

    // Returns the gravity value for this stage
    public double getGravity() {
        return gravity;
    }

    // Returns the X-axis velocity component
    public double getVelocityX() {
        return velocityX;
    }

    // Returns the Y-axis velocity component
    public double getVelocityY() {
        return velocityY;
    }

    // Returns an array containing the key codes for movement
    public int[] getKeyCodes() {
        return new int[]{rightCode, leftCode, upCode};
    }

    // Returns the clue for this stage
    public String getClue() {
        return clue;
    }

    // Returns the help message for this stage
    public String getHelp() {
        return help;
    }

    // Returns the color assigned to this stage
    public Color getColor() {
        return color;
    }
}
