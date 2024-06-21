package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;
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
     * This method shows a popup message when a player lands on a pit.
     * @autor
     * @param gameController the game controller
     * @param space the space the player is on
     * @return true always, as the action is simply to show a popup
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space != null) {
            Player player = space.getPlayer();
            if (player != null) {

                JOptionPane.showMessageDialog(null, "You landed on pit, this does nothing right now");
                return true;
            }
        }
        return false;
    }
}
