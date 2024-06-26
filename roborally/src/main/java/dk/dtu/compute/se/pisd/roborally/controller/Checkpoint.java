package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents the checkpoint field action.
 * @author Marcus s214962
 * @author Setare s232629
 *
 */

public class Checkpoint extends FieldAction {
    private int checkpointNumber;

    public Checkpoint(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }


    /**
     * @author Marcus s214962
     * @author Setare s232629
     * @param gameController the game controller
     * @param space the space the player is on

     * @return true if the player is granted a checkpoint number.
     *This method checks if there is a player located on the space.
     *If yes then checks if the players total checkpoints is less than the checkpoint number which the player has landed on
     *If yes then the player is granted a checkpoint number.
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if(space != null){
            Player player = space.getPlayer();
            if(player != null){
                if(player.getCheckpoints()+1 == checkpointNumber){
                    player.setCheckpoints(player.getCheckpoints()+1);
                }

            }
            return true;
        }
        return false;
    }

    /**
     * Get the checkpoint number.
     * @author Marcus s214962
     * @author Setare s232629
     * @return the checkpoint number
     */
    public int getCheckpointNumber() {
        return checkpointNumber;
    }


    /**
     * Set the checkpoint number.
     * @author Marcus s214962
     * @author Setare s232629
     * @param checkpointNumber the checkpoint number
     */
    public void setCheckpointNumber(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }
}
