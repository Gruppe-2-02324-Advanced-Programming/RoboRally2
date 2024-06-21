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
                // Correctly access the findRebootSpace method on the instance of Board
                Space rebootSpace = gameController.getBoard().findRebootSpace();
                if (rebootSpace != null) {
                    player.setSpace(rebootSpace); // Move the player to the reboot space
                    // Additional logic for handling the player's orientation or other effects can be added here
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }





}