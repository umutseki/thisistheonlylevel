// name surname: Umut Efe Seki
// student ID: 2023400204

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Color;


public class UmutEfeSeki {
    public static void main(String[] args) {

        int nullButton = -1;
        StdDraw.enableDoubleBuffering();


        // Given Stages
        Stage s1 = new Stage(-0.45, 3.65, 10, 1, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP, "Arrow keys are required", "Arrow keys move player ,press button and enter the second pipe"); // normal game
        Stage s2 = new Stage(-0.45, 3.65, 10, 2, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, "Not always straight forward", "Right and left buttons reversed"); // Reversed Buttons
        Stage s3 = new Stage(-2, 3.65, 24, 3, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, nullButton, "A bit bouncy here", "You jump constantly"); // bouncing
        Stage s4 = new Stage(-0.45, 3.65, 10, 4, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP, "Never gonna give you up", "Press button 5 times "); //
        Stage s5 = new Stage(-0.45, 3.65, 10, 5, KeyEvent.VK_H, KeyEvent.VK_F, KeyEvent.VK_T, "Center of Attention", "Keys are at the center of keyboard.");
        // Add a new stage here

        // Add the stages to the arraylist
        ArrayList<Stage> stages = new ArrayList<Stage>();
        stages.add(s1);
        stages.add(s2);
        stages.add(s3);
        stages.add(s4);
        stages.add(s5);


        // Draw the game area
        StdDraw.setCanvasSize(800, 600);
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 600);


        Game game = new Game(stages);
        game.play();

    }
}
