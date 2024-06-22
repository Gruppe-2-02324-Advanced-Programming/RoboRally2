package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

/** This class represents the pit field action.
 *  When a player lands on a pit, it shows a popup message.
 *  @author
 *
 */
public class Pit extends FieldAction {

    /**
     * Constructor for the pit.
     */
    public Pit() {
    }

    /**
     * This method resets the player's position to a random start point when they land on a pit.
     *
     * @param gameController the game controller
     * @param space the space the player is on
     * @return true if the player was reset, false otherwise
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space != null) {
            Player player = space.getPlayer();
            if (player != null) {
                List<Space> startPoints = gameController.getBoard().getGearSpawnPoints();
                if (!startPoints.isEmpty()) {
                    // Choose a random start point to move the player to
                    Random random = new Random();
                    Space startPoint = startPoints.get(random.nextInt(startPoints.size()));
                    player.setSpace(startPoint);  // Assuming setSpace handles the actual placement and any necessary updates
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }



}