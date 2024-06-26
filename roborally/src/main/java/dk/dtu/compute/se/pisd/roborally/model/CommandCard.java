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
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

/**
 * A command card that can be used by a player to program the robot.
 * This class is responsible for the command card's name and image.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Christoffer, s205449
 * @author Philip, s224278
 */
public class CommandCard extends Subject {

    @Expose
    final public Command command;

    /**
     * Constructor for the CommandCard class.
     * @param command the command to be used.
     */
    public CommandCard(@NotNull Command command) {
        this.command = command;
    }

    /**
     * Method to get the name of the command card.
     * @return the name of the command card.
     */
    public String getName() {
        return command.displayName;
    }

    /**
     * Method to get the image of the command card.
     * @return the image of the command card.
     * @author Phillip s224278
     */
    public Image getImage() {
        return command.cardImage;
    }
}
