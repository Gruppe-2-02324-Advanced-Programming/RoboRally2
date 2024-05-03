package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class DoubleConveyorBelt extends FieldAction {
    private final Heading heading;

    public DoubleConveyorBelt(Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space != null) {
            Player player = space.getPlayer();
            if (player != null) {
                Space nextSpace = gameController.board.getNeighbour(space, heading);
                if (nextSpace != null) {
                    try {
                        // Move to the first space
                        gameController.movePlayerToSpace(player, nextSpace, heading);
                        // Attempt to move to the second space
                        Space secondSpace = gameController.board.getNeighbour(nextSpace, heading);
                        if (secondSpace != null) {
                            gameController.movePlayerToSpace(player, secondSpace, heading);
                            return true;
                        }
                    } catch (GameController.moveNotPossibleException e) {
                        // Log the exception or handle it if necessary
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public Heading getHeading() {
        return heading;
    }
}

