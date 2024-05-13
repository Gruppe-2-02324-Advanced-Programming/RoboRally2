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
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 *  The view of the board of the game. The board is displayed as a grid of spaces.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class BoardView extends VBox implements ViewObserver {
    /**
     * The board of the game.
     */
    private Board board;

    /**
     * The grid pane for the board.
     */

    private GridPane mainBoardPane;

    /**
     * The view of the spaces of the board.
     */
    private SpaceView[][] spaces;

    /**
     * The view of the players of the game.
     */

    private PlayersView playersView;

    /**
     * The label for the status of the game.
     */
    private Label statusLabel;

    /**
     * The event handler for the spaces.
     */

    private SpaceEventHandler spaceEventHandler;
/**
     * The constructor of the board view. The view is created for the given game controller.
     *
     * @param gameController the game controller for the game.
     */
    public BoardView(@NotNull GameController gameController) {
        board = gameController.board;

        mainBoardPane = new GridPane();
        playersView = new PlayersView(gameController);
        statusLabel = new Label("<no status>");

        this.getChildren().add(mainBoardPane);
        this.getChildren().add(playersView);
        this.getChildren().add(statusLabel);

        spaces = new SpaceView[board.width][board.height];

        spaceEventHandler = new SpaceEventHandler(gameController);

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                SpaceView spaceView = new SpaceView(space);
                spaces[x][y] = spaceView;
                mainBoardPane.add(spaceView, x, y);
                spaceView.setOnMouseClicked(spaceEventHandler);
            }
        }

        board.attach(this);
        update(board);
    }
/**
     * Update the view of the board. This method is called when the board changes.
     *
     * @param subject the subject of the update.
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == board) {
            Phase phase = board.getPhase();
            statusLabel.setText(board.getStatusMessage());
        }
    }
/**
     * The event handler for the spaces. This handler is used to move the current player to a space
     * when the space is clicked - is going to be removed later.
     */
    // XXX this handler and its uses should eventually be deleted! This is just to help test the
    //     behaviour of the game by being able to explicitly move the players on the board!
    private class SpaceEventHandler implements EventHandler<MouseEvent> {

        /**
         * The game controller for the game.
         */
        final public GameController gameController;
/**
         * The constructor of the space event handler.
         *
         * @param gameController the game controller for the game.
         */
        public SpaceEventHandler(@NotNull GameController gameController) {
            this.gameController = gameController;
        }
/**
         * Handle the mouse event for a space.
         *
         * @param event the mouse event.
         */
        @Override
        public void handle(MouseEvent event) {
            Object source = event.getSource();
            if (source instanceof SpaceView) {
                SpaceView spaceView = (SpaceView) source;
                Space space = spaceView.space;
                Board board = space.board;

                if (board == gameController.board) {
                    gameController.moveCurrentPlayerToSpace(space);
                    event.consume();
                }
            }
        }

    }

}
