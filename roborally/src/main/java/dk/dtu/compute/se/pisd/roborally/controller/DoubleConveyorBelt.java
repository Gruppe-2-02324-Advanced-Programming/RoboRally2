package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to represent a double conveyor belt field action.
 * It extends the FieldAction class and implements the doAction method to move the player to the correct neighboring space
 * when the player lands on the double conveyor belt.
 * The class has a heading field to store the conveyor belt's heading.
 * The player is moved twice in the same direction if both moves are valid.
 * @autor Emily s191174
 */
public class DoubleConveyorBelt extends FieldAction {
    private final Heading heading;

    /**
     * Constructor for the double conveyor belt.
     * @autor Emily s191174
     * @param heading the heading of the conveyor belt
     */
    public DoubleConveyorBelt(Heading heading) {
        this.heading = heading;
    }

    /**
     * Executes the action of moving a player two spaces in the direction of this conveyor belt.
     * The player is moved twice in the same direction if both moves are valid.
     *
     * @author Emily, s191174
     * @param gameController the game controller managing the game logic
     * @param space the initial space from which the player starts
     * @return true if the player was successfully moved two spaces; false if any move failed
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if(space != null){
            Player player  = space.getPlayer();
            Space neighbour = gameController.board.getNeighbour(space, heading);
            if(player != null && neighbour != null){
                try {
                    gameController.movePlayerToSpace(player, neighbour, heading);
                    neighbour=gameController.board.getNeighbour(player.getSpace(), heading);
                    gameController.movePlayerToSpace(player, neighbour, heading);

                }catch (GameController.moveNotPossibleException e){
                    //Gør ikke noget
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Get the heading of the conveyor belt.
     * @autor Emily s191174
     * @return the heading of the conveyor belt
     */
    public Heading getHeading() {
        return heading;
    }
}
