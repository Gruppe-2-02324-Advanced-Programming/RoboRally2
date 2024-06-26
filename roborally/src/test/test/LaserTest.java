
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.Laser;
import dk.dtu.compute.se.pisd.roborally.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Laser}.
 * This class tests the functionality of the Laser class in handling game interactions
 * when a player lands on a laser field.
 *
 * @author Setare Izadi, s232629
 */
class LaserTest {

    private GameController gameController;
    private Space space;
    private Player player;
    private Laser laser;

    @BeforeEach
    void setUp() {
        gameController = mock(GameController.class);
        space = mock(Space.class);
        player = mock(Player.class);
        laser = new Laser(Heading.NORTH);
    }

    @Test
    void testDoActionPlayerNull() {
        when(space.getPlayer()).thenReturn(null);

        boolean result = laser.doAction(gameController, space);

        assertFalse(result, "Action should return false when no player is on the space");
    }

    @Test
    void testSetAndGetHeading() {
        laser.setHeading(Heading.EAST);
        assertEquals(Heading.EAST, laser.getHeading(), "Heading should be EAST after setting it to EAST");
    }
}