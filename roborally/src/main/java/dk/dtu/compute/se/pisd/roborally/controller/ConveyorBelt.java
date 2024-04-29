package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/** @author Christoffer s205449
 *
 */
public class ConveyorBelt extends FieldAction{
    Heading heading;

    public ConveyorBelt(Heading heading) {
        this.heading = heading;
    }

    /**
     *
     * @param gameController The game controller
     * @param space The space the player has landed on
     * @return true if player is moved to neighbour space
     *
     *
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if(space != null){
            Player player  = space.getPlayer();
            Space neighbour = gameController.board.getNeighbour(space, heading);
            if(player != null && neighbour != null){
                try {
                    gameController.movePlayerToSpace(player, neighbour, heading);
                }catch (GameController.moveNotPossibleException e){
                    //Gør ikke noget
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
