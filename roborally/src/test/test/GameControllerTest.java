import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.Laser;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;


/**
 * Test class for {@link GameController}.
 * This class tests the functionality of the GameController class in handling game interactions
 */
class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }
    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null, "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    /**
     * Testing if player moves forward when supposed to
     * @author Christoffer Fink s205449
     * Testing if player moves forward when supposed to
     */
    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(),
                "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(),
                "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(),
                "Space (0,0) should be empty!");
    }
    /**
     * Same as moveForward test just checking if twice
     * @author Christoffer Fink s205449
     */
    @Test
    void fastForward() {
        // samme som moveForward her gøres det bare 2 gange
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();

        gameController.fastForward(player);

        Assertions.assertEquals(player, board.getSpace(0, 2).getPlayer(),
                "Player " + player.getName() + " should space be (0,2)!");
        Assertions.assertEquals(Heading.SOUTH, player.getHeading(),
                "Player should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(),
                "Space (0,0) should be empty!");
    }


    /**
     * Same as moveForward test just checking if it performs the function three times
     * @author Setare Izadi, s232629
     */
    @Test
    void fwdThree() {
        // samme som moveForward her gøres det bare 3 gange
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();

        gameController.moveThree(player);

        Assertions.assertEquals(player, board.getSpace(0, 3).getPlayer(),
                "Player " + player.getName() + " should space be (0,3)!");
        Assertions.assertEquals(Heading.SOUTH, player.getHeading(),
                "Player should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(),
                "Space (0,0) should be empty!");
    }


    /**
     *@author Christoffer, s205449
     * turnRight test
     *against west, because start pos is SOUTH
     */
    @Test
    void turnRight(){
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        gameController.turnRight(player);
        Assertions.assertEquals(Heading.WEST, player.getHeading(), "player should be heading WEST");
    }

    /**
     *  turnLeft test
     *  against east ~~~
     * @author Christoffer s205449
     */
    @Test
    void turnLeft() {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        gameController.turnLeft(player);
        Assertions.assertEquals(Heading.EAST, player.getHeading(), "player should be heading EAST");

    }

    /**
     * Testing if the player makes an uturn when supposed to
     * @author Christoffer Fink s205449
     *
     */
    @Test
    void uTurn() {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        gameController.uTurn(player);
        Assertions.assertEquals(Heading.NORTH, player.getHeading(),"player should be heading NORTH" );
        Assertions.assertEquals(player, board.getSpace(0, 0).getPlayer(),
                "Player " + player.getName() + "space should be (0,0)");

    }

    /**
     * Testing again command card by setting up a player with a forward and again card in their programmingfield
     * @Author Jacob, s164958
     * @Param player
     * @Return void
     */
    @Test
    void testAgain(){
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        System.out.println(player.getName());
        CommandCard pwr = new CommandCard(Command.POWER_UP);
        board.setStep(1);
        player.getProgramField(0).setCard(pwr);
        gameController.again(player);
        Assertions.assertEquals(1, player.getEnergyCubes(), "Player should have 1 energy cube");
    }

    /**
     * Test to ensure timer decreases over time and transitions correctly at zero.
     * @Author Setare Izadi, s232629
     */
    @Test
    void testTimerExpirationEndsProgrammingPhase() {
        gameController.setTimer(1);  // Set timer to 1 second for quick test
        try {
            Thread.sleep(1500);  // Wait for timer to expire
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assertions.assertEquals(Phase.Initialisation, gameController.board.getPhase(),
                "Phase should switch to Activation after timer expires.");
    }


    /**
     * Test player movement is blocked by a wall.
     * @Author Setare Izadi, s232629
     */
    @Test
    void testMovementBlockedByWall() {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        Space currentSpace = board.getSpace(0, 0);
        currentSpace.addWall(Heading.SOUTH);  // Add a wall blocking the movement

        gameController.moveForward(player);

        Assertions.assertEquals(player, currentSpace.getPlayer(),
                "Player should remain in the same space due to a wall.");
        Assertions.assertNull(board.getSpace(0, 1).getPlayer(),
                "No player should be able to move to space (0,1).");
    }

    /**
     * Test command execution for non-movement commands.
     * @Author Setare Izadi, s232629
     */
    @Test
    void testExecutePowerUpCommand() {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        int initialEnergy = player.getEnergyCubes();

        CommandCard powerUpCard = new CommandCard(Command.POWER_UP);
        gameController.executeCommand(player, powerUpCard.command);

        Assertions.assertEquals(initialEnergy + 1, player.getEnergyCubes(),
                "Player should have one more energy cube after executing POWER_UP command.");
    }
}



