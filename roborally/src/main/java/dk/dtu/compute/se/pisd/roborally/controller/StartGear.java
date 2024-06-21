package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents gear spawn points on the board for initializing player positions.
 */
public class StartGear extends FieldAction {

    /**
     * This method is used during game initialization to set player's starting positions.
     *
     * @param gameController the game controller
     * @param space the space of this gear
     * @return always returns false since no action is performed during gameplay
     * @author Emily, s191174
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        // This method could be utilized if you need to check for spawns during the game,
        // but typically remains unused.
        return false;
    }
}
