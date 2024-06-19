package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The view of a player of the game. The view shows the program of the player
 * and the command cards of the player. The view is updated when the player's
 * board changes.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Emily, s191174
 * @author Marcus s214942
 * @author Christoffer s205449
 */
public class PlayerView extends Tab implements ViewObserver {
    /**
     * The player for which this view is created.
     */
    private Player player;
    /**
     * The top level layout of the view.
     */
    private VBox top;
    /**
     * The label showing the number of energy cubes of the player.
     */
    private Label programLabel;
    /**
     * The label showing the number of energy cubes of the player.
     */
    private Label energyCubesLabel;

    /**
     * The pane showing the program of the player.
     */
    private GridPane programPane;

    /**
     * The views of the program cards of the player.
     */
    private Label cardsLabel;

    /**
     * The pane showing the command cards of the player.
     */
    private GridPane cardsPane;
    /**
     * The views of the command cards of the player.
     */
    private CardFieldView[] programCardViews;

    /**
     * The views of the command cards of the player.
     */
    private CardFieldView[] cardViews;

    /**
     * The panel with the buttons for the programming phase.
     */

    private VBox buttonPanel;

    /**
     * The button to finish the programming phase.
     */
    private Button finishButton;

    /**
     * The button to execute the program.
     */
    private Button executeButton;

    private Label timerLabel;

    /**
     * The button to push your cards to the server.
     */
    private Button push;

    private Label playerNo;

    /**
     * The button to pull opponents cards from the server.
     */
    private Button pull;
    /**
     * The button to execute the current register.
     */
    private Button stepButton;
    /**
     * The panel with the buttons for the player interaction phase.
     */
    private VBox playerInteractionPanel;

    /**
     * The controller for the game.
     */

    private GameController gameController;

    private Label readyStatusLabel;

    /**
     * Label to display the checkpoint count.
     */
    private Label checkpointLabel;

    /**
     * The constructor for the view of a player.
     *
     * @param gameController the controller for the game
     * @param player         the player for which this view is created
     */
    public PlayerView(@NotNull GameController gameController, @NotNull Player player) {
        super(player.getName());
        this.setStyle("-fx-text-base-color: " + player.getColor() + ";");

        top = new VBox();
        this.setContent(top);

        this.gameController = gameController;
        this.player = player;

        energyCubesLabel = new Label("Energy Cubes: " + player.getEnergyCubes());
        energyCubesLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        checkpointLabel = new Label("Checkpoints: " + player.getCheckpoints());
        top.getChildren().add(checkpointLabel); // Add the label to the layout

        programLabel = new Label("Program");
        programLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
        readyStatusLabel = new Label("Ready: ");
        top.getChildren().addAll(readyStatusLabel); // Add to top layout

        programPane = new GridPane();
        programPane.setVgap(2.0);
        programPane.setHgap(2.0);
        programCardViews = new CardFieldView[Player.NO_REGISTERS];
        for (int i = 0; i < Player.NO_REGISTERS; i++) {
            CommandCardField cardField = player.getProgramField(i);
            if (cardField != null) {
                programCardViews[i] = new CardFieldView(gameController, cardField);
                programPane.add(programCardViews[i], i, 0);
            }
        }

        playerNo = new Label("Player " + gameController.getPlayerNumber());

        timerLabel = new Label("Time left: " + gameController.getRemainingTime() + "s");
        top.getChildren().add(timerLabel);

        playerInteractionPanel = new VBox();
        playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
        playerInteractionPanel.setSpacing(10.0);

        cardsLabel = new Label("Command Cards");
        cardsPane = new GridPane();
        cardsPane.setVgap(2.0);
        cardsPane.setHgap(2.0);
        cardViews = new CardFieldView[Player.NO_CARDS];
        for (int i = 0; i < Player.NO_CARDS; i++) {
            CommandCardField cardField = player.getCardField(i);
            if (cardField != null) {
                cardViews[i] = new CardFieldView(gameController, cardField);
                cardsPane.add(cardViews[i], i, 0);
            }
        }

        top.getChildren().addAll(programLabel, programPane, cardsLabel, cardsPane, energyCubesLabel);

        if (player.board != null) {
            player.board.attach(this);
            updateView(player.board);
        }

        // Update the timer label periodically
        ScheduledExecutorService timerScheduler = Executors.newScheduledThreadPool(1);
        timerScheduler.scheduleAtFixedRate(() -> {
            javafx.application.Platform.runLater(() -> {
                timerLabel.setText("Time left: " + gameController.getRemainingTime() + "s");
            });
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * This method is called when the observed subject changes.
     *
     * @param subject the subject that has changed.
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == player.board) {
            energyCubesLabel.setText("Energy Cubes: " + player.getEnergyCubes());
            checkpointLabel.setText("Checkpoints: " + player.getCheckpoints());
            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                CardFieldView cardFieldView = programCardViews[i];
                if (cardFieldView != null) {
                    if (player.board.getPhase() == Phase.PROGRAMMING) {
                        cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                    } else {
                        if (i < player.board.getStep()) {
                            cardFieldView.setBackground(CardFieldView.BG_DONE);
                        } else if (i == player.board.getStep()) {
                            if (player.board.getCurrentPlayer() == player) {
                                cardFieldView.setBackground(CardFieldView.BG_ACTIVE);
                            } else if (player.board.getPlayerNumber(player.board.getCurrentPlayer()) > player.board
                                    .getPlayerNumber(player)) {
                                cardFieldView.setBackground(CardFieldView.BG_DONE);
                            } else {
                                cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                            }
                        } else {
                            cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
                        }
                    }
                }
            }

            if (player.board.getPhase() != Phase.PLAYER_INTERACTION) {
                switch (player.board.getPhase()) {
                    case INITIALISATION:
                        break;

                    case PROGRAMMING:
                        break;

                    case ACTIVATION:
                        break;

                    default:
                }

            } else {
                if (!programPane.getChildren().contains(playerInteractionPanel)) {
                    programPane.getChildren().remove(buttonPanel);
                    programPane.add(playerInteractionPanel, Player.NO_REGISTERS, 0);
                }
                playerInteractionPanel.getChildren().clear();

                if (player.board.getCurrentPlayer() == player) {
                    // TODO Assignment A3: these buttons should be shown only when there is
                    // an interactive command card, and the buttons should represent
                    // the player's choices of the interactive command card. The
                    // following is just a mockup showing two options
                    Button optionButton = new Button("Option1: Left");
                    optionButton.setOnAction(e -> gameController.leftOrRight(player, Command.LEFT));
                    optionButton.setDisable(false);
                    playerInteractionPanel.getChildren().add(optionButton);

                    optionButton = new Button("Option 2: Right");
                    optionButton.setOnAction(e -> gameController.leftOrRight(player, Command.RIGHT));
                    optionButton.setDisable(false);
                    playerInteractionPanel.getChildren().add(optionButton);
                }
            }
        }
    }

}
