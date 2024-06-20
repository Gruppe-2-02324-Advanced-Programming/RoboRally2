package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;
import javax.swing.JOptionPane;

/** This class represents the laser start field action.
 *  @author Christoffer s205449
 *
 */
public class LaserStart extends FieldAction {

    private Heading heading;

    /**
     * Constructor for the laser start with a specified heading.
     * @param heading the heading direction of the laser start
     */
    public LaserStart(Heading heading) {
        this.heading = heading;
    }


    /**
     * Gets the heading direction of the laser start.
     * @return the heading direction
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Sets the heading direction of the laser start.
     * @param heading the heading direction to set
     */
    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    /**
     * this method does nothing purely for cosmetic
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        return false;
    }
}
