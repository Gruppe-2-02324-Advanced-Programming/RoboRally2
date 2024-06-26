/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
     * The label showing the number of cards in the player's drawpile.
     */
    private Label drawpileLabel;

    /**
     * The label showing the number of cards in the player's discardpile.
     */
    private Label discardpileLabel;

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

    private Button readyButton;
    /**
     * The button to execute the program.
     */

    private HBox infHBox;

    private Label timerLabel;

    /**
     * The button to push your cards to the server.
     */
    private Button push;

    private Label playerName;

    private Label gameID;

    /**
     * The button to pull opponents cards from the server.
     */
    private Button pull;

    private VBox playerInteractionPanel;

    /**
     * The controller for the game.
     */

    private GameController gameController;

    /**
     * Label to display the checkpoint count.
     */
    private Label checkpointLabel;

    /**
     * The constructor for the view of a player.
     *
     * @param gameController the controller for the game
     * @param player         the player for which this view is created
     * @author Marcus Jagd Hansen, s214962
     * @author Phillip, s224278
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

        drawpileLabel = new Label("Drawpile: " + player.getDrawpile().getCards().size());
        drawpileLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        discardpileLabel = new Label("Discardpile: " + player.getDiscardpile().getCards().size());
        discardpileLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        checkpointLabel = new Label("Checkpoints: " + player.getCheckpoints());

        programLabel = new Label("Program");
        programLabel.setStyle("-fx-font-size: 10px; -fx-padding: 2px;");

        infHBox = new HBox();
        infHBox.getChildren().addAll(checkpointLabel);
        infHBox.setSpacing(10);
        top.getChildren().add(infHBox);

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

        // XXX the following buttons should actually not be on the tabs of the
        // individual
        // players, but on the PlayersView (view for all players). This should be
        // refactored.

        playerName = new Label(gameController.getPlayerName());
        gameID = new Label("GameID: " + gameController.board.getGameID().intValue());

        pull = new Button("Get Other Players Cards");
        pull.setOnAction(e -> {
            gameController.getOtherPlayersCards();
        });

        push = new Button("Send Your Cards to Server");
        push.setOnAction(e -> {
            gameController.pushYourCards();
        });

        // Ready Button klar til at blive programmeret

        readyButton = new Button("Ready Up");

        finishButton = new Button("Finish Programming");
        finishButton.setOnAction(e -> gameController.finishProgrammingPhase());

        if (gameController.isSinglePlayerMode) {
            buttonPanel = new VBox(finishButton);
        } else {
            buttonPanel = new VBox(push, pull, finishButton, gameID, playerName);
        }

        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setSpacing(3.0);
        // programPane.add(buttonPanel, Player.NO_REGISTERS, 0); done in update now

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

        timerLabel = new Label("Time left: " + gameController.getRemainingTime() + "s");
        infHBox.getChildren().add(timerLabel);

        // Update the timer label periodically
        ScheduledExecutorService timerScheduler = Executors.newScheduledThreadPool(1);
        timerScheduler.scheduleAtFixedRate(() -> {
            javafx.application.Platform.runLater(() -> {
                timerLabel.setText("Time left: " + gameController.getRemainingTime() + "s");
            });
        }, 0, 1, TimeUnit.SECONDS);

        top.getChildren().addAll(programLabel, programPane, cardsLabel, cardsPane, drawpileLabel, discardpileLabel,
                energyCubesLabel);

        if (player.board != null) {
            player.board.attach(this);
            updateView(player.board);
        }
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
            drawpileLabel.setText("Drawpile: " + player.getDrawpile().getCards().size());
            discardpileLabel.setText("Discardpile: " + player.getDiscardpile().getCards().size());
            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                CardFieldView cardFieldView = programCardViews[i];
                if (cardFieldView != null) {
                    if (player.board.getPhase() == Phase.Programming) {
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
            if (player.board.getPhase() != Phase.Player_interaction) {
                if (!programPane.getChildren().contains(buttonPanel)) {
                    programPane.getChildren().remove(playerInteractionPanel);
                    programPane.add(buttonPanel, Player.NO_REGISTERS, 0);
                }
                switch (player.board.getPhase()) {
                    case Initialisation:
                        finishButton.setDisable(true);
                        break;

                    case Programming:
                        finishButton.setDisable(false);
                        break;

                    case Activation:
                        finishButton.setDisable(true);
                        break;

                    default:
                        finishButton.setDisable(true);
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
