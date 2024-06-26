package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents the action for rebooting a robot on a specific reboot tile.
 * @author Emily, s191174
 */
public class Reboot extends FieldAction {
    private Space rebootTile = null;

    public Reboot() {
        this.rebootTile = rebootTile;
    }

    /**
     * Executes the action of rebooting a robot on a specific space.
     *
     * @param gameController the game controller
     * @param space the space the player is on
     * @return true if the reboot action was successful, false otherwise
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        Player player = space.getPlayer();
        if (player != null && rebootTile != null) {

            // Move the player to the designated reboot tile
            player.setSpace(rebootTile);
            player.setHeading(player.getHeading().prev());  // Optionally reset the player's heading randomly

            return true;
        }
        return false;
    }
}

