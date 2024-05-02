package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/** @author Christoffer s205449
 *
 */
public class ConveyorBelt extends FieldAction {
    Heading heading;

    public ConveyorBelt(Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space != null) {
            Player player = space.getPlayer();
            Space neighbor = gameController.board.getNeighbour(space, heading);
            if (player != null && neighbor != null) {
                try {
                    gameController.movePlayerToSpace(player, neighbor, heading);
                } catch (GameController.moveNotPossibleException e) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public Heading getHeading() {
        return heading;
    }
}