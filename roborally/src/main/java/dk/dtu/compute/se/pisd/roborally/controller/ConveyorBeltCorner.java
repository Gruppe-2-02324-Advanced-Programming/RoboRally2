package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class ConveyorBeltCorner extends FieldAction{
    Heading heading;
    int rotation;

    public ConveyorBeltCorner(Heading heading, int rotation) {
        this.heading = heading;
        this.rotation = rotation;
    }

    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if(space != null){
            Player player  = space.getPlayer();
            if(player != null){
                // Determine the new heading based on the rotation
                Heading newHeading = player.getHeading();
                if (rotation == Gears.LEFT_TURN) {
                    newHeading = newHeading.prev(); // counterclockwise
                } else if (rotation == Gears.RIGHT_TURN) {
                    newHeading = newHeading.next(); // clockwise
                }
                player.setHeading(newHeading);

                // Move the player one step forward in the direction of the new heading
                Space neighbour = gameController.board.getNeighbour(space, newHeading);
                if(neighbour != null){
                    try {
                        gameController.movePlayerToSpace(player, neighbour, newHeading);
                    }catch (GameController.moveNotPossibleException e){
                        //Gør ikke noget
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
}