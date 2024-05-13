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
 *
 */



//todo ligenu ændrer den også robottens heading skal laves om til ikke at den ikke skal ændre heading
public class ConveyorBeltCorner extends FieldAction {
    Heading heading;
    private int rotation;

    public ConveyorBeltCorner(Heading heading, int rotation) {
        this.heading = heading;
        this.rotation = rotation;
    }

    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space != null) {
            Player player = space.getPlayer();
            if (player != null) {
                Heading newHeading = heading;
                if (rotation == Gears.LEFT_TURN) {
                    newHeading = heading.prev();
                } else if (rotation == Gears.RIGHT_TURN) {
                    newHeading = heading.next();
                }


                Space neighbor = gameController.board.getNeighbour(space, newHeading);
                if (neighbor != null) {
                    try {
                        gameController.movePlayerToSpace(player, neighbor, newHeading);
                    } catch (GameController.moveNotPossibleException e) {
                        return false;
                    }


                    if (rotation == Gears.LEFT_TURN) {
                        player.setHeading(newHeading.next());
                    } else if (rotation == Gears.RIGHT_TURN) {
                        player.setHeading(newHeading.prev());
                    }

                    return true;
                }
            }
        }
        return false;
    }
    public Heading getHeading() {
        return heading;
    }

    public int getRotation() {
        return rotation;
    }
}