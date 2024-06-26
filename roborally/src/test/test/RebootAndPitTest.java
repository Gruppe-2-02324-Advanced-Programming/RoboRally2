import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.Gears;
import dk.dtu.compute.se.pisd.roborally.controller.Pit;
import dk.dtu.compute.se.pisd.roborally.controller.Reboot;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;

public class RebootAndPitTest {

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
        @Test
        void PitAndReboot() {
            Board board = gameController.board;
            Player current = board.getCurrentPlayer();
            board.getSpace(0,1).addAction(new Pit());
            board.getSpace(0,6).addAction(new Reboot());
            gameController.moveForward(current);
            board.getSpace(0,1).getActions().get(0).doAction(gameController, board.getSpace(0,1));
            Assertions.assertEquals(current, board.getSpace(0, 6).getPlayer(), "Player " + current.getName() + " should beSpace (0,6)!");
            Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        }


    }

