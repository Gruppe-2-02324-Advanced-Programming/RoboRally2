package dk.dtu.compute.se.pisd.roborally.controller.fields;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
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
 *
 */

//todo Currently also changes the heading of the robot, which it should not do
public class ConveyorBeltCorner extends FieldAction {
    Heading heading;
    private int rotation;

    /**
     * Constructor for the conveyor belt corner
     * @autor Christoffer s205449
     * @param heading the heading of the conveyor belt corner
     * @param rotation the rotation of the conveyor belt corner
     */
    public ConveyorBeltCorner(Heading heading, int rotation) {
        this.heading = heading;
        this.rotation = rotation;
    }

    /**
     * This method moves the player to the next space in the direction of the conveyor belt corner
     * and changes the player's heading based on the conveyor belt corner's rotation
     * @autor Christoffer s205449
     * @param gameController the game controller
     * @param space the space the player is on
     * @return true if the player is moved to the next space in the direction of the conveyor belt corner
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space != null) {
            Player player = space.getPlayer();
            if (player != null) {
                Heading newHeading = this.heading;
                if (this.rotation == Gears.LEFT_TURN) {
                    newHeading = this.heading.prev();
                } else if (this.rotation == Gears.RIGHT_TURN) {
                    newHeading = this.heading.next();
                }

                Space nextSpace = gameController.board.getNeighbour(space, newHeading);
                if (nextSpace != null && nextSpace.getPlayer() == null) {
                    try {
                        player.setHeading(newHeading);
                        gameController.movePlayerToSpace(player, nextSpace, newHeading);
                        // Check for another conveyor action recursively
                        FieldAction action = nextSpace.findAction(ConveyorBelt.class);
                        if (action != null) {
                            action.doAction(gameController, nextSpace);
                        }
                        return true;
                    } catch (GameController.moveNotPossibleException e) {
                        return false;
                    }
                }
            }
        }
        return false;
    }



    /**
     * Get the heading of the conveyor belt corner
     * @autor Christoffer s205449
     * @return the heading of the conveyor belt corner
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Get the rotation of the conveyor belt corner
     * @autor Christoffer s205449
     * @return the rotation of the conveyor belt corner
     */
    public int getRotation() {
        return rotation;
    }
}