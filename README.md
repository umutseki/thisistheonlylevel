# thisistheonlylevel
This project is a Java implementation of the puzzle-platformer “This Is the Only Level,” developed for the CmpE 160 Object-Oriented Programming course. The game contains a single level repeated across multiple stages, where each stage changes how the controls, physics, or environment behave. The implementation uses StdDraw to render the map, player, obstacles, and UI elements, and follows the mechanics defined in the project specification.
The project is built around several core classes. Game manages the main loop, keyboard input, stage flow, timers, and death tracking. Map draws the environment and handles collision detection for walls, spikes, doors, pipes, and the stage-specific button that opens the exit. Player controls movement, gravity, jumping physics, and stage-dependent control variations. Stage defines the behavior differences between stages, such as reversed movement, auto-jumping, modified gravity, and other rule changes. The entry file (UmutEfeSeki.java) launches the game.
Requirements:
Java 17 or later
StdDraw library (provided in course materials)
No external libraries aside from StdDraw are needed.
How to run:
Place all .java files and the StdDraw library in the same directory.
Compile everything using:
javac *.java
Run the game with:
java UmutEfeSeki
A window will open and the game will begin at Stage 1. Movement, physics, and mechanics vary by stage as defined in the assignment.
The project demonstrates object-oriented design, event-driven input handling, collision logic, basic physics simulation, and graphical rendering using StdDraw.
