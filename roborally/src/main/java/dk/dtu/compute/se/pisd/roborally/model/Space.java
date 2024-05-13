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
package dk.dtu.compute.se.pisd.roborally.model;

import com.google.gson.annotations.Expose;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;

import java.util.ArrayList;
import java.util.List;

/**
 * A space on the board of the game. A space can have a player on it and
 * can have walls in different directions - also, a spaceAction
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Space extends Subject {
    /**
     * The player that is on this space (or null, if there is no player on this space).
     */
    private Player player;
    /**
     * The walls around this space.
     */
    private List<Heading> walls = new ArrayList<>();

    /**
     * The actions that can be performed on this space.
     */
    private List<FieldAction> actions = new ArrayList<>();
    /**
     * The board to which this space belongs.
     */
    public final Board board;

    /**
     * The coordinates of this space on the board.
     */
    @Expose
    public final int x;

    /**
     * The coordinates of this space on the board.
     */
    @Expose
    public final int y;
/**
     * The constructor of the space.
     *
     * @param board the board to which this space belongs.
     * @param x the x-coordinate of this space on the board.
     * @param y the y-coordinate of this space on the board.
     */

    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
    }
/**
     * Get the player that is on this space.
     *
     * @return the player that is on this space (or null, if there is no player on this space).
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set the player that is on this space.
     *
     * @param player the player that is on this space (or null, if there is no player on this space).
     */
    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    /**
     * This method adds walls as field action
     * @author Christoffer Fink s205449
     * @return the walls around this space.
     */
    public List<Heading> getWalls() {
        return walls;
    }

    /**
     * This method adds actions as field action
     * @return the actions that can be performed on this space.
     */

    public List<FieldAction> getActions() {
        return actions;
    }

    /**
     * This method adds walls as field action
     * @param wall the wall to be added to this space.
     */

    public void addWall(Heading wall) {
        if(!walls.contains(wall))
            walls.add(wall);
    }
/**
     * This method adds actions as field action
     * @param action the action to be added to this space.
     */
    public void addAction(FieldAction action) {
        if(!actions.contains(action))
            actions.add(action);
    }
/**
     * This method removes walls as field action
 */
    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

}
