package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConveyorBeltTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

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
     * Test to check if the conveyor belt moves the player forward
     *
     * @author Christoffer Fink s205499
     */
    @Test
    void ConveyorBelt1forwardTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        // Set up the conveyor belt action on space (0, 1) to move the player north
        board.getSpace(0, 1).addAction(new ConveyorBelt(Heading.NORTH));

        // Move the player to space (0, 1)
        gameController.moveForward(current); // Assuming this moves to space (0, 1)

        // Execute the conveyor belt action
        board.getSpace(0, 1).getActions().get(0).doAction(gameController, board.getSpace(0, 1));

        // Check that the player has moved to space (0, 2)
        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should be at Space (0, 2)!");
    }


@Test
void ConveyorBeltCornerRightTest() {
    Board board = gameController.board;
    Player current = board.getCurrentPlayer();

    // Set up the conveyor belt corner action on space (0, 1) to turn right and move the player east
    board.getSpace(0, 1).addAction(new ConveyorBeltCorner(Heading.EAST, Gears.RIGHT_TURN));

    // Move the player to space (0, 1)
    gameController.moveForward(current); // Assuming this moves to space (0, 1)

    // Execute the conveyor belt corner action
    board.getSpace(0, 1).getActions().get(0).doAction(gameController, board.getSpace(0, 1));



    Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player should be heading South!");
    Assertions.assertEquals(current, board.getSpace(1, 1).getPlayer(), "Player should be at Space (1, 1)!");
    }
}


