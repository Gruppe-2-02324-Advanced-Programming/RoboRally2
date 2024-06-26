package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

/** This class represents the pit field action.
 *  When a player lands on a pit, it shows a popup message.
 *  @author Christoffer s205449
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
                // Use the scheduleReboot method to handle the reboot process
                gameController.getBoard().scheduleReboot(player);
                // Since the reboot is scheduled, we assume the action is successfully initiated
                return true;
            }
        }
        return false; // Return false if there is no player to reboot or if space is null
    }






}