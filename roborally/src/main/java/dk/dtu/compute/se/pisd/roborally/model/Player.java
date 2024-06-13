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

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * Class for the player in the game. This class is a subject of the observer
 * pattern and can be observed by the GUI.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Setare, s232629
 * @author Christoffer, s205449
 * @author Jacob, s164958
 * @author Emily, s191174
 * @auhtor Phillip, s224278
 *
 */
public class Player extends Subject {

    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 9;

    final public Board board;
    @Expose
    private int checkpoints = 0;
    @Expose
    private String name;
    @Expose
    private String color;
    @Expose
    private Space space;
    @Expose
    private Heading heading = SOUTH;
    @Expose
    private CommandCardField[] program;
    @Expose
    private CommandCardField[] cards;

    private String robotImage;

    public String getRobotImage() {
        return robotImage;
    }

    /**
     * Constructor for the Player class.
     *
     */
    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
    }

    /**
     * Method to get the name of the player.
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the name of the player.
     *
     */
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * Method to get the color of the player.
     *
     */
    public String getColor() {
        return color;
    }

    /**
     * Method to set the color of the player.
     *
     */
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    /**
     * Method to get the program of the player.
     *
     */
    public void setProgramField(int i, CommandCardField field) {
        if (i >= 0 && i < NO_REGISTERS) {
            program[i] = field;
        }
    }

    /**
     * Method to get the program of the player.
     *
     */
    public void setCardField(int i, CommandCardField field) {
        if (i >= 0 && i < NO_CARDS) {
            cards[i] = field;
        }
    }

    /**
     * Method to get the program of the player.
     *
     */
    public void setRobotImage(String robotImage) {
        this.robotImage = robotImage;
    }

    public Space getSpace() {
        return space;
    }

    /**
     * Method to set the space of the player.
     *
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    /**
     * Method to get the heading of the player.
     *
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Method to set the heading of the player.
     */
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * Getters and setters for the program and cards of the player. Also the
     * checkpoints.
     *
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    public CommandCardField getCardField(int i) {
        return cards[i];
    }

    public void setCheckpoints(int checkpoints) {
        this.checkpoints = checkpoints;
    }

    public int getCheckpoints() {
        return checkpoints;
    }

    private int energyCubes = 0; // Field to store energy cubes

    /**
     * Adds one energy cube to the player's current count. This method should be
     * called
     * when a Power Up card is played.
     *
     * @author Emily, s191174
     */
    public void addEnergyCube() {
        this.energyCubes++;
        notifyChange(); // Notify observers of the change
    }

    /**
     * Getters for the energy cubes.
     *
     */
    public int getEnergyCubes() {
        return energyCubes;
    }

}
