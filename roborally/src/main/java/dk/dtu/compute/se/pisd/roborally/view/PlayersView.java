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
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.scene.control.TabPane;

/**
 * The view of the players of the game. The view is a tab pane with a tab for
 * each player.
 * The view is updated when the current player changes.
 *
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class PlayersView extends TabPane implements ViewObserver {
    /**
     * The board of the game.
     */
    private Board board;
    /**
     * The player views for the players of the game.
     */
    private PlayerView[] playerViews;
    /**
     * The controller of the game.
     */
    private GameController gameController;

    /**
     * The constructor of the players view. The view is created for the given game
     * controller.
     *
     * @param gameController the game controller for the game.
     */
    public PlayersView(GameController gameController) {
        this.gameController = gameController;
        board = gameController.board;

        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        playerViews = new PlayerView[board.getPlayersNumber()];
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            playerViews[i] = new PlayerView(gameController, board.getPlayer(i));
            this.getTabs().add(playerViews[i]);
        }
        board.attach(this);
        update(board);

        this.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            int newIndex = this.getSelectionModel().getSelectedIndex();
            handleTabChange(newIndex);
        });
    }

    /**
     * Update the view of the players. This method is called when the current player
     * changes.
     *
     * @param subject the subject of the update.
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == board) {
            Player current = board.getCurrentPlayer();
            this.getSelectionModel().select(board.getPlayerNumber(current));
            gameController.changeCurrentTabIndex(getCurrentTabIndex());
        }
    }

    /**
     * Returnerer nummeret på den aktuelt valgte fane.
     * 
     * @return nummeret på den aktuelt valgte fane.
     * @author Marcus s214942
     */
    public int getCurrentTabIndex() {
        return this.getSelectionModel().getSelectedIndex();
    }

    /**
     * Handles actions to be performed when the tab is changed.
     *
     * @param newIndex the index of the newly selected tab
     * @author Marcus s214942
     */
    private void handleTabChange(int newIndex) {
        // System.out.println("Current Tab Index: " + newIndex);
        gameController.changeCurrentTabIndex(newIndex);
    }

}
