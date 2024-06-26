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

import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This enumeration defines the commands that can be used in the game. The
 * commands are used to control the robots in the game.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Setare Izadi, s232629@dtu.dk
 * @auhtor Christoffer, s205449
 * @author Phillip, s224278
 * @author Marcus, s214962
 * @auther Jacob, s164958
 */
public enum Command {

    // This is a very simplistic way of realizing different commands.
    FORWARD("Fwd", new Image("assets/cardsMove1.png", 65, 100, true, true)),
    RIGHT("Turn Right", new Image("assets/cardsRight.png", 65, 100, true, true)),
    LEFT("Turn Left", new Image("assets/cardsLeft.png", 65, 100, true, true)),
    FAST_FORWARD("Fast Fwd", new Image("assets/cardsMove2.png", 65, 100, true, true)),
    FORWARD_THREE("Fwd Three", new Image("assets/cardsMove3.png", 65, 100, true, true)),
    BACKUP("Back Up", new Image("assets/cardsMoveBack.png", 65, 100, true, true)),
    UTURN("U-Turn", new Image("assets/cardsUTurn.png", 65, 100, true, true)),
    AGAIN("Again", new Image("assets/cardsAgain.png", 65, 100, true, true)),
    OPTION_LEFT_RIGHT("Left OR Right", new Image("assets/cardLeftRight.png", 65, 100, true, true), LEFT, RIGHT),
    POWER_UP("Power Up", new Image("assets/powerupcard.png", 65, 100, true, true)),
    SPAM("Spam", new Image("assets/cardsSpam.png", 65, 100, true, true));

    // @Expose
    final public String displayName;

    final private List<Command> options;

    final public Image cardImage;

    /**
     * The constructor for a command with a given display name and an image
     * @author Phillip s224278
     */
    Command(String displayName, Image cardImage, Command... options) {
        this.displayName = displayName;
        this.cardImage = cardImage;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }

    public boolean isInteractive() {
        return !options.isEmpty();
    }

    public List<Command> getOptions() {
        return options;
    }

    /**
     * Method to get the command from the display name.
     *
     * @param displayName the display name of the command
     * @return the Command corresponding to the display name, or FORWARD if no match
     *         is found
     * @author Marcus Jagd Hansen, s214962
     */
    public static Command fromDisplayName(String displayName) {
        for (Command command : Command.values()) {
            if (command.displayName.equalsIgnoreCase(displayName)) {
                return command;
            }
        }
        return FORWARD; // This should rarely be reached; consider handling this case appropriately
    }

}
