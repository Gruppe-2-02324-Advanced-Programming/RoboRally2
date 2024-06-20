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
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.Initialisation;

/**
 *
 * The class of the game board. The board is a rectangular grid of spaces. The
 * board
 * keeps track of the players on the board and the current player. It also keeps
 * track of the phase of the game.
 * 
 * @Expose is used to serialize the field for saving the board.
 * @author Ekkart Kindler, ekki@dtu.dk
 * @auhtor Christoffer, s205449
 * @auhtor Setare, s232629
 * @auhtor Phillip, s224278
 * @auhtor Emily, s191174
 * @auhtor Jacob, s164958
 *
 */

public class Board extends Subject {
    // @Expose
    public final int width;
    // @Expose
    public final int height;
    @Expose
    public final String boardName;
    // @Expose
    private Integer gameId;
    // @Expose
    private final Space[][] spaces;
    @Expose
    private final List<Player> players = new ArrayList<>();
    @Expose
    private Player current;
    @Expose
    private Phase phase = Initialisation;
    @Expose
    private int step = 0;
    // @Expose
    private boolean stepMode;
    @Expose
    private int totalCheckpoints = 0;
    @Expose
    private int counter;
    // @Expose
    private boolean won = false;

    /**
     * Returns the program fields of the given player on the board.
     *  @param playerNumber the number of the player
     * @author Marcus s214942
     */
    private Long gameID;

    public List<String> getProgramFields(int playerNumber) {
        Player player = players.get(playerNumber);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(player.getProgramField(i).getCard().getName());
        }
        return list;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        if (counter != this.counter) {
            this.counter = counter;
            notifyChange();
        }
    }

    /*
     * public void setBoard(Board b) {
     * this.boardName = b.boardName;
     * this.width = b.width;
     * this.height = b.height;
     * spaces = new Space[b.width][b.height];
     * for (int x = 0; x < b.width; x++) {
     * for (int y = 0; y < b.height; y++) {
     * Space space = new Space(this, x, y);
     * spaces[x][y] = space;
     * }
     * }
     * this.stepMode = false;
     * }
     */

    /**
     * Creates a new board with the given width and height and the given name.
     *
     */

    public Board(int width, int height, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        this.gameID = 1L;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;

    }

    public Board(int width, int height) {
        this(width, height, "defaultboard");
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }

    public Long getGameID() {
        return this.gameID;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    /**
     * Returns the space at the given position on the board.
     *
     */
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    public String getBoardName() {
        return boardName;
    }

    /**
     * Returns the number of players on the board.
     *
     */
    public int getPlayersNumber() {
        return players.size();
    }

    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    /**
     * Returns the player with the given index on the board.
     *
     */
    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     * Returns the current player on the board.
     *
     */
    public Player getCurrentPlayer() {
        return current;
    }

    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    /**
     * Returns the phase of the game. Setters and getters for phase and step
     *
     */
    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    /**
     * Returns whether the board is in step mode.
     *
     */
    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    /**
     * Returns the number of the given player on the board. If the player is not
     *
     */
    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space   the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable)
     *         neighbour
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;
        }

        return getSpace(x, y);
    }

    /**
     * Returns the number of checkpoints on the board. Getters and setters for
     * totalCheckpoints and won
     *
     */
    public int getTotalCheckpoints() {
        return totalCheckpoints;
    }

    public void setTotalCheckpoints(int totalCheckpoints) {
        this.totalCheckpoints = totalCheckpoints;
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns whether the game is won. The game is won, if one of the players
     *
     */
    public boolean isWon() {
        for (Player p : players) {
            // System.out.println(p.getCheckpoints() + ":" + totalCheckpoints);
            if (p.getCheckpoints() == totalCheckpoints && totalCheckpoints > 0) {
                won = true;
                break;
            }
        }
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
        notifyChange();
    }

    /**
     * Returns the status message of the board. The status message contains
     *
     */
    public String getStatusMessage() {
        return  getPhase().name() + " Phase, " +
                getCurrentPlayer().getName() +
                ", Turn Counter " + counter;
    }

}
