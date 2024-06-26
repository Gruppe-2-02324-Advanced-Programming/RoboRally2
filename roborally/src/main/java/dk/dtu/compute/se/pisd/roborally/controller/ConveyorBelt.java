package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/** This class represents the conveyor belt field action.
 *  @author Christoffer s205449
 *
 */
public class ConveyorBelt extends FieldAction {
    Heading heading;

    /**
     * Constructor for the conveyor belt.
     * @author Christoffer s205449
     * @param heading the heading of the conveyor belt
     */
    public ConveyorBelt(Heading heading) {
        this.heading = heading;
    }

    /**
     * This method moves the player to the next space in the direction of the conveyor belt.
     * @author Christoffer s205449
     * @param gameController the game controller
     * @param space the space the player is on
     * @return true if the player is moved to the next space in the direction of the conveyor belt
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space != null) {
            Player player = space.getPlayer();
            if (player != null) {
                Space nextSpace = gameController.board.getNeighbour(space, this.heading);
                if (nextSpace != null && nextSpace.getPlayer() == null) { // Ensure the space is empty
                    try {
                        gameController.movePlayerToSpace(player, nextSpace, this.heading);
                        // Recursively handle movement if there's another conveyor
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
     * Get the heading of the conveyor belt.
     * @author Christoffer s205449
     * @return the heading of the conveyor belt
     */
    public Heading getHeading() {
        return heading;
    }
}