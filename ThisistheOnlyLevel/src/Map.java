// name surname: Umut Efe Seki
// student ID: 2023400204

import java.awt.*;
import java.util.Arrays;

// The Map class handles all level data including obstacles, door mechanics, spikes, and player interaction
public class Map {
    // Rectangular obstacles on the map, each represented as [x1, y1, x2, y2]
    int[][] obstacles = {
            new int[]{0, 120, 120, 270}, new int[]{0, 270, 168, 330},
            new int[]{0, 330, 30, 480}, new int[]{0, 480, 180, 600},
            new int[]{180, 570, 680, 600}, new int[]{270, 540, 300, 570},
            new int[]{590, 540, 620, 570}, new int[]{680, 510, 800, 600},
            new int[]{710, 450, 800, 510}, new int[]{740, 420, 800, 450},
            new int[]{770, 300, 800, 420}, new int[]{680, 240, 800, 300},
            new int[]{680, 300, 710, 330}, new int[]{770, 180, 800, 240},
            new int[]{0, 120, 800, 150}, new int[]{560, 150, 800, 180},
            new int[]{530, 180, 590, 210}, new int[]{530, 210, 560, 240},
            new int[]{320, 150, 440, 210}, new int[]{350, 210, 440, 270},
            new int[]{220, 270, 310, 300}, new int[]{360, 360, 480, 390},
            new int[]{530, 310, 590, 340}, new int[]{560, 400, 620, 430}
    };
    // The pressable button area
    int[] button = new int[]{400, 390, 470, 410};
    private Stage stage;
    private Player player;
    // Button floor coordinates (base area of the button)
    private int[] buttonFloor;
    // Pipes that mark start and exit positions
    private int[][] startPipe;
    private int[][] exitPipe;
    // Deadly spike locations
    private int[][] spikes;
    // Door coordinates
    private int[] door;
    // Door state variables
    private int buttonPressNum = 0;
    private boolean isDoorOpen = false;
    private double doorOffsetY = 0;
    private boolean doorOpening = false;
    private boolean wasOnButtonLastFrame = false;
    private boolean doorClosing = false;

    // Constructor: sets stage, player, and all coordinates
    public Map(Stage stage, Player player) {
        this.stage = stage;
        this.player = player;
        this.spikes = new int[][]{
                new int[]{30, 333, 50, 423}, new int[]{121, 150, 207, 170},
                new int[]{441, 150, 557, 170}, new int[]{591, 180, 621, 200},
                new int[]{750, 301, 769, 419}, new int[]{680, 490, 710, 510},
                new int[]{401, 550, 521, 570}
        };
        this.startPipe = new int[][]{
                new int[]{115, 450, 145, 480},
                new int[]{110, 430, 150, 450}
        };
        this.exitPipe = new int[][]{
                new int[]{720, 175, 740, 215},
                new int[]{740, 180, 770, 210}
        };
        this.door = new int[]{685, 180, 700, 240};
        this.buttonFloor = new int[]{400, 390, 470, 400};
    }

    // Moves the player left or right, with collision checking
    public void movePlayer(char direction) {
        double deltaX = 0;
        double speed = stage.getVelocityX();

        if (direction == 'R') {
            deltaX = speed;
        } else if (direction == 'L') {
            deltaX = -speed;
        }

        double nextX = player.getX() + deltaX;
        double nextY = player.getY();

        boolean collided = false;
        int[] collisionObs = {1, 2, 3, 4};

        for (int[] obs : obstacles) {
            if (checkCollision(nextX, nextY, obs)) {
                collided = true;
                collisionObs = obs;
                break;
            }
        }

        // Check for door collision if it's closed
        if (!collided && !isDoorOpen && checkCollision(nextX, nextY, door)) {
            collided = true;
            collisionObs = door;
        }

        // Move if no collision, else adjust position based on side hit
        if (!collided) {
            player.setX(nextX);
        } else {
            if (deltaX < 0) {
                player.setX(collisionObs[2] + player.getWidth() / 2.0 + 0.01);
            } else if (deltaX > 0) {
                player.setX(collisionObs[0] - player.getWidth() / 2.0 - 0.01);
            }
        }
    }

    // Handles vertical movement (falling or jumping) and collisions
    public void applyVerticalMovement() {
        double nextY = player.getY() + player.getVelocityY();
        double currentX = player.getX();
        boolean collided = false;
        int[] ground = {1, 2, 3, 4};

        for (int[] obs : obstacles) {
            if (checkCollision(currentX, nextY, obs)) {
                collided = true;
                ground = obs;
                break;
            }
        }

        if (!collided) {
            player.setY(nextY);
        } else {
            if (player.getVelocityY() < 0) {
                player.setY(ground[3] + player.getHeight() / 2.0 + 0.01);
            } else if (player.getVelocityY() > 0) {
                player.setY(ground[1] - player.getHeight() / 2.0 - 0.01);
            }
            player.setVelocityY(0);
        }
    }

    // Checks whether the player is standing on the ground
    public boolean isPlayerOnGround() {
        double nextY = player.getY() - 1;
        double currentX = player.getX();

        for (int[] obs : obstacles) {
            if (checkCollision(currentX, nextY, obs)) {
                return true;
            }
        }
        return false;
    }

    // Placeholder for stage switching
    public boolean changeStage() {
        return false;
    }

    // Handles logic when button is pressed
    public void pressButton() {
        if (stage.getStageNumber() != 4) {
            doorOpening = true;
        } else {
            buttonPressNum++;
            if (buttonPressNum >= 5) {
                doorOpening = true;
            }
        }
    }

    // Resets the stage (e.g., when player dies or level restarts)
    public void restartStage() {
        player.respawn(new int[]{130, 465});
        buttonPressNum = 0;
        isDoorOpen = false;
        doorOpening = false;
        wasOnButtonLastFrame = false;
        doorClosing = true;
    }

    // Getters
    public Stage getStage() {
        return stage;
    }

    public Player getPlayer() {
        return player;
    }

    // Main drawing function: draws everything on screen
    public void draw() {
        // Animate door opening
        if (doorOpening && doorOffsetY > -60) {
            doorOffsetY -= 3;
            if (doorOffsetY <= -60) {
                isDoorOpen = true;
                doorOpening = false;
            }
        }

        // Animate door closing
        if (doorClosing && doorOffsetY < 0) {
            doorOffsetY += 3;
            if (doorOffsetY == 0) {
                isDoorOpen = false;
                doorClosing = false;
            }
        }

        // Draw spikes
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.picture(54, 381, "misc/Spikes.png", 120, 55, 90);
        StdDraw.picture(164, 173, "misc/Spikes.png", 140, 50, 180);
        StdDraw.picture(458, 546, "misc/Spikes.png", 180, 50, 0);
        StdDraw.picture(696, 486, "misc/Spikes.png", 45, 50, 0);
        StdDraw.picture(746, 361, "misc/Spikes.png", 160, 50, 270);
        StdDraw.picture(604, 204, "misc/Spikes.png", 45, 50, 180);
        StdDraw.picture(499, 173, "misc/Spikes.png", 180, 50, 180);

        // Draw door if not open
        StdDraw.setPenColor(StdDraw.GREEN);
        double doorCenterX = (door[0] + door[2]) / 2.0;
        double doorCenterY = (door[1] + door[3]) / 2.0 + doorOffsetY;
        double doorWidth = (door[2] - door[0]) / 2.0;
        double doorHeight = (door[3] - door[1]) / 2.0;
        if (!isDoorOpen) {
            StdDraw.filledRectangle(doorCenterX, doorCenterY, doorWidth, doorHeight);
        }

        // Draw obstacles
        StdDraw.setPenColor(stage.getColor());
        for (int[] obs : obstacles) {
            double centerX = (obs[0] + obs[2]) / 2.0;
            double centerY = (obs[1] + obs[3]) / 2.0;
            double width = (obs[2] - obs[0]) / 2.0;
            double height = (obs[3] - obs[1]) / 2.0;
            StdDraw.filledRectangle(centerX, centerY, width, height);
        }

        // Draw button top (only if player isn't on it)
        if (!isPlayerOnButton()) {
            StdDraw.setPenColor(StdDraw.RED);
            double bCenterX = (button[0] + button[2]) / 2.0;
            double bCenterY = (button[1] + button[3]) / 2.0;
            double bWidth = (button[2] - button[0]) / 2.0;
            double bHeight = (button[3] - button[1]) / 2.0;
            StdDraw.filledRectangle(bCenterX, bCenterY, bWidth, bHeight);
        }

        // Draw button floor (base layer)
        StdDraw.setPenColor(StdDraw.BLACK);
        double bfCenterX = (buttonFloor[0] + buttonFloor[2]) / 2.0;
        double bfCenterY = (buttonFloor[1] + buttonFloor[3]) / 2.0;
        double bfWidth = (buttonFloor[2] - buttonFloor[0]) / 2.0;
        double bfHeight = (buttonFloor[3] - buttonFloor[1]) / 2.0;
        StdDraw.filledRectangle(bfCenterX, bfCenterY, bfWidth, bfHeight);

        // Draw player (elephant)
        if (Game.isElephantLookingRight) {
            if (stage.getStageNumber() == 2) {
                StdDraw.picture(player.getX(), player.getY(), "misc/ElephantLeft.png", 20, 20);
            } else {
                StdDraw.picture(player.getX(), player.getY(), "misc/ElephantRight.png", 20, 20);
            }
        } else {
            if (stage.getStageNumber() == 2) {
                StdDraw.picture(player.getX(), player.getY(), "misc/ElephantRight.png", 20, 20);
            } else {
                StdDraw.picture(player.getX(), player.getY(), "misc/ElephantLeft.png", 20, 20);
            }
        }

        // Draw starting pipe
        StdDraw.setPenColor(StdDraw.ORANGE);
        for (int[] part : startPipe) {
            double centerX = (part[0] + part[2]) / 2.0;
            double centerY = (part[1] + part[3]) / 2.0;
            double width = (part[2] - part[0]) / 2.0;
            double height = (part[3] - part[1]) / 2.0;
            StdDraw.filledRectangle(centerX, centerY, width, height);
        }

        // Draw exit pipe
        for (int[] part : exitPipe) {
            double centerX = (part[0] + part[2]) / 2.0;
            double centerY = (part[1] + part[3]) / 2.0;
            double width = (part[2] - part[0]) / 2.0;
            double height = (part[3] - part[1]) / 2.0;
            StdDraw.filledRectangle(centerX, centerY, width, height);
        }

        // Display the updated frame
        StdDraw.show();
        StdDraw.pause(16); // ~60 FPS
    }

    // Checks collision between player and an obstacle
    private boolean checkCollision(double x, double y, int[] obstacle) {
        double px = player.getWidth() / 2.0;
        double py = player.getHeight() / 2.0;

        double left = x - px;
        double right = x + px;
        double bottom = y - py;
        double top = y + py;

        int x1 = obstacle[0];
        int y1 = obstacle[1];
        int x2 = obstacle[2];
        int y2 = obstacle[3];

        return !(right < x1 || left > x2 || top < y1 || bottom > y2);
    }

    // Returns spike positions
    public int[][] getSpikes() {
        return spikes;
    }

    // Checks whether the player is on the button
    public boolean isPlayerOnButton() {
        double px = player.getX();
        double py = player.getY();
        double halfWidth = player.getWidth() / 2.0;
        double halfHeight = player.getHeight() / 2.0;

        double left = px - halfWidth;
        double right = px + halfWidth;
        double bottom = py - halfHeight;
        double top = py + halfHeight;

        return !(right < button[0] || left > button[2] || top < button[1] || bottom > button[3]);
    }

    // Returns the second piece of the exit pipe
    public int[] getExitPipe() {
        return exitPipe[1];
    }

    // Enables automatic jumping for Stage 3
    public void updateAutoJump() {
        if (stage.getStageNumber() == 3 && isPlayerOnGround()) {
            player.setVelocityY(stage.getVelocityY());
        }
    }

    // Updates button press detection based on current and previous frame
    public void updateButtonPress() {
        boolean isOnButton = isPlayerOnButton();

        if (isOnButton && !wasOnButtonLastFrame) {
            pressButton();
        }

        wasOnButtonLastFrame = isOnButton;
    }
}
