// name surname: Umut Efe Seki
// student ID: 2023400204

// The Player class represents the controllable character or object in the game.
// It stores positional data, size, and vertical velocity for movement and physics calculations.
public class Player {
    // Player's current X and Y coordinates on the screen or game world
    private double x, y;

    // Width and height of the player object (used for collision detection and rendering)
    private double width, height;

    // Vertical velocity used for jumping and gravity effects
    private double velocityY;

    // Constructor initializes the player's position and sets a fixed size (20x20) and zero initial vertical velocity
    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.width = 20;
        this.height = 20;
        this.velocityY = 0;
    }

    // Getter method to retrieve X position
    public double getX() {
        return x;
    }

    // Setter method to update X position
    public void setX(double x) {
        this.x = x;
    }

    // Getter method to retrieve Y position
    public double getY() {
        return y;
    }

    // Setter method to update Y position
    public void setY(double y) {
        this.y = y;
    }

    // Getter for the width of the player
    public double getWidth() {
        return width;
    }

    // Getter for the height of the player
    public double getHeight() {
        return height;
    }

    // Returns the current vertical velocity of the player
    public double getVelocityY() {
        return velocityY;
    }

    // Sets the vertical velocity (used in gravity and jumping mechanics)
    public void setVelocityY(double v) {
        this.velocityY = v;
    }

    // Moves the player to a spawn point (typically after death or restart)
    // Resets vertical velocity to 0 to prevent unexpected falling or jumping
    public void respawn(int[] spawnPoint) {
        this.x = spawnPoint[0];
        this.y = spawnPoint[1];
        this.velocityY = 0;
    }
}

