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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import org.jetbrains.annotations.NotNull;

/**
 * The view of a player of the game. The view shows the program of the player
 * and the command cards of the player. The view is updated when the player's
 * board changes.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Emily, s191174
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

    /**
     * The button to push your cards to the server.
     */
    private Button push;

    private Label playerNo;

    private Label gameID;

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

        playerNo = new Label("Player " + gameController.getPlayerNumber());
        gameID = new Label("GameID: " + gameController.board.getGameID().intValue());

        pull = new Button("pull");
        pull.setOnAction(e -> {
            gameController.getOtherPlayersCards();
        });

        push = new Button("push");
        push.setOnAction(e -> {
            gameController.pushYourCards();
        });

        finishButton = new Button("Finish Programming");
        finishButton.setOnAction(e -> gameController.finishProgrammingPhase());

        executeButton = new Button("Execute Program");
        executeButton.setOnAction(e -> gameController.executePrograms());

        stepButton = new Button("Execute Current Register");
        stepButton.setOnAction(e -> gameController.executeStep());

        buttonPanel = new VBox(finishButton, executeButton, stepButton, pull, push, playerNo, gameID);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setSpacing(3.0);
        // programPane.add(buttonPanel, Player.NO_REGISTERS, 0); done in update now

        playerInteractionPanel = new VBox();
        playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
        playerInteractionPanel.setSpacing(3.0);

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

        top.getChildren().add(programLabel);
        top.getChildren().add(programPane);
        top.getChildren().add(cardsLabel);
        top.getChildren().add(cardsPane);
        top.getChildren().add(energyCubesLabel);

        if (player.board != null) {
            player.board.attach(this);
            update(player.board);
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
                if (!programPane.getChildren().contains(buttonPanel)) {
                    programPane.getChildren().remove(playerInteractionPanel);
                    programPane.add(buttonPanel, Player.NO_REGISTERS, 0);
                }
                switch (player.board.getPhase()) {
                    case INITIALISATION:
                        finishButton.setDisable(true);
                        // XXX just to make sure that there is a way for the player to get
                        // from the initialization phase to the programming phase somehow!
                        executeButton.setDisable(false);
                        stepButton.setDisable(true);
                        break;

                    case PROGRAMMING:
                        finishButton.setDisable(false);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
                        break;

                    case ACTIVATION:
                        finishButton.setDisable(true);
                        executeButton.setDisable(false);
                        stepButton.setDisable(false);
                        break;

                    default:
                        finishButton.setDisable(true);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
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
