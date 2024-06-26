package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;


/**
 * This class is used to represent a conveyor belt corner field action
 * It extends the FieldAction class and implements the doAction method to move the player to the correct neighboring space
 * and change the player's heading based on the conveyor belt's rotation when the player lands on the conveyor belt corner
 * The class has a heading and a rotation field to store the conveyor belt's heading and rotation respectively
 * @author Christoffer s205449
 * @author Emily, s191174
 */

public class ConveyorBeltCorner extends FieldAction {
    Heading heading;
    private int rotation;

    /**
     * Constructor for the conveyor belt corner
     *
     * @param heading the heading of the conveyor belt corner
     * @author Christoffer s205449
     */
    public ConveyorBeltCorner(Heading heading) {
        this.heading = heading;
        this.rotation = rotation;
    }

    /**
     * This method moves the player to the next space in the direction of the conveyor belt corner
     * and changes the player's heading based on the conveyor belt corner's rotation
     * @author Emily, s191174
     * @author Christoffer s205449
     * @param gameController the game controller
     * @param space the space the player is on
     * @return true if the player is moved to the next space in the direction of the conveyor belt corner
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space != null) {
            Player player = space.getPlayer();
            if (player != null) {
                // Calculate the heading to move the player
                Heading moveHeading = heading; // heading of the conveyor belt corner
                if (rotation == Gears.LEFT_TURN) {
                    moveHeading = heading.prev();  // move left relative to the conveyor's heading
                } else if (rotation == Gears.RIGHT_TURN) {
                    moveHeading = heading.next();  // move right relative to the conveyor's heading
                }

                // Find the next space in the new heading direction
                Space nextSpace = gameController.board.getNeighbour(space, moveHeading);
                if (nextSpace != null && nextSpace.getPlayer() == null) {
                    try {
                        gameController.movePlayerToSpace(player, nextSpace, moveHeading);
                        return true;
                    } catch (GameController.moveNotPossibleException e) {
                        return false;  // Movement failed
                    }
                }
            }
        }
        return false;
    }




    /**
     * Get the heading of the conveyor belt corner
     * @author Christoffer s205449
     * @return the heading of the conveyor belt corner
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Get the rotation of the conveyor belt corner
     * @author Christoffer s205449
     * @return the rotation of the conveyor belt corner
     */
    public int getRotation() {
        return rotation;
    }
}
