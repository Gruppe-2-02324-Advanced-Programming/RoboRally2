package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;
import javax.swing.JOptionPane;

/** This class represents the laser field action.
 *  When a player lands on a laser, it shows a popup message.
 *  @author
 *
 */
public class Laser extends FieldAction {

    private Heading heading;

    /**
     * Constructor for the laser with a specified heading.
     * @param heading the heading direction of the laser
     */
    public Laser(Heading heading) {
        this.heading = heading;
    }

    /**
     * Default constructor for the laser.
     */
    public Laser() {
        this.heading = Heading.NORTH; // Default heading
    }

    /**
     * Gets the heading direction of the laser.
     * @return the heading direction
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Sets the heading direction of the laser.
     * @param heading the heading direction to set
     */
    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    /**
     * This method shows a popup message when a player lands on a laser - for now.
     * @param gameController the game controller
     * @param space the space the player is on
     * @return true always, as the action is simply to show a popup
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space != null) {
            Player player = space.getPlayer();
            if (player != null) {
                JOptionPane.showMessageDialog(null, "You landed on a laser, this does nothing right now");
                addSpam(player);
                return true;
            }
        }
        return false;
    }
    private void addSpam(Player player) {
        player.getDiscardpile().addCard(new CommandCard(Command.SPAM));
    }
}
