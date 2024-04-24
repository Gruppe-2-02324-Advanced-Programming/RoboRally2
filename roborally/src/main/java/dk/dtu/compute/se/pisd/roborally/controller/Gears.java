package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;

/**
 * ...
 *
 * @author Setare Izadi, s232629@dtu.dk
 *
 */

public class Gears extends FieldAction {

    public static final int LEFT_TURN = -90;
    public static final int RIGHT_TURN = 90;

    private final int rotation;

    public Gears(int rotation) {
        this.rotation = rotation;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null) {
            // Determine the new heading based on the gear's rotation
            Heading newHeading = player.getHeading();
            if (rotation == Gears.LEFT_TURN) {
                newHeading = newHeading.prev(); // counterclockwise
            } else if (rotation == Gears.RIGHT_TURN) {
                newHeading = newHeading.next(); // clockwise
            }
            player.setHeading(newHeading);
            return true;
        }
        return false;
    }
}
