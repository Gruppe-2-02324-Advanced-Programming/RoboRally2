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
import dk.dtu.compute.se.pisd.roborally.controller.fields.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.controller.fields.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.fields.DoubleConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.fields.ConveyorBeltCorner;
import dk.dtu.compute.se.pisd.roborally.controller.fields.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.fields.Gears;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.jetbrains.annotations.NotNull;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * SpaceView is a view of a space on the board. It is responsible for drawing the spaces on the board.
 * and updating the view when the space changes. For example if there is a player on the space, the view should update
 * to show the player on the space. Also, if there is a wall on the space, the view should update to show the wall. etc
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Christoffer s205449
 */
public class SpaceView extends StackPane implements ViewObserver {
/**
     * The height and width of the space
     */

    final public static int SPACE_HEIGHT = 50; // 75;
    /**
     * The height and width of the space
     */

    final public static int SPACE_WIDTH = 50; // 75;
/**
     * The space that this view represents
     */

    public final Space space;

    /**
     * Constructor for the SpaceView class
     * @param space the space that this view should represent
     */
    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: pink;");
        } else {
            this.setStyle("-fx-background-color: purple;");
        }


        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }


    /**
     * Draws the walls possibly other stuff
     *
     * @author Christoffer Fink, s205449@dtu.dk
     * @author Setare Izadi, s232629@dtu.dk
     */
    @Override
    public void updateView(Subject subject) {
        this.getChildren().clear();

        // Load the empty field image and set it as the background of the space
        Image emptyFieldImage = new Image("/assets/empty.png");
        ImageView emptyFieldView = new ImageView(emptyFieldImage);
        emptyFieldView.setFitWidth(SPACE_WIDTH);
        emptyFieldView.setFitHeight(SPACE_HEIGHT);
        emptyFieldView.setPreserveRatio(false);
        this.getChildren().add(emptyFieldView);


        // Check if the current space has a gear action and display the corresponding image
        for (FieldAction action : space.getActions()) {
            if (action instanceof Gears gears) {
                ImageView gearImageView;
                if (gears.rotation == Gears.LEFT_TURN) {
                    Image gearImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/gearLeft.png")));
                    gearImageView = new ImageView(gearImage);
                } else if (gears.rotation == Gears.RIGHT_TURN) {
                    Image gearImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/gearRight.png")));
                    gearImageView = new ImageView(gearImage);
                } else {
                    continue; // If it's not left or right, we'll skip this action
                }

                gearImageView.setFitWidth(SPACE_WIDTH);
                gearImageView.setFitHeight(SPACE_HEIGHT);
                gearImageView.setPreserveRatio(false);
                this.getChildren().add(gearImageView);
                break;
            }
        }

        // Check if the current space contains a ConveyorBeltCorner
        for (FieldAction action : space.getActions()) {
            if (action instanceof ConveyorBeltCorner conveyorBeltCorner) {
                // Load the conveyor belt corner image
                Image conveyorBeltCornerImage;
                if (conveyorBeltCorner.getRotation() == Gears.LEFT_TURN) {
                    conveyorBeltCornerImage = new Image("/assets/greenTurnLeft.png");
                } else {
                    conveyorBeltCornerImage = new Image("/assets/greenTurnRight.png");
                }
                ImageView conveyorBeltCornerView = new ImageView(conveyorBeltCornerImage);
                conveyorBeltCornerView.setFitWidth(SPACE_WIDTH);
                conveyorBeltCornerView.setFitHeight(SPACE_HEIGHT);
                conveyorBeltCornerView.setPreserveRatio(false);
                // Rotate the conveyor belt corner image based on its heading
                switch (conveyorBeltCorner.getHeading()) {
                    case NORTH:
                        conveyorBeltCornerView.setRotate(0);
                        break;
                    case EAST:
                        conveyorBeltCornerView.setRotate(90);
                        break;
                    case SOUTH:
                        conveyorBeltCornerView.setRotate(180);
                        break;
                    case WEST:
                        conveyorBeltCornerView.setRotate(270);
                        break;
                }

                this.getChildren().add(conveyorBeltCornerView);
                break;
            }
        }




        // Check if the current space contains a conveyor belt
        for (FieldAction action : space.getActions()) {
            if (action instanceof ConveyorBelt conveyorBelt) {
                Image conveyorBeltImage;
                conveyorBeltImage = new Image("/assets/green.png");
                ImageView conveyorBeltView = new ImageView(conveyorBeltImage);
                conveyorBeltView.setFitWidth(SPACE_WIDTH);
                conveyorBeltView.setFitHeight(SPACE_HEIGHT);
                conveyorBeltView.setPreserveRatio(false);
                // Rotate the conveyor belt image based on its heading
                switch (conveyorBelt.getHeading()) {
                    case NORTH:
                        conveyorBeltView.setRotate(0);
                        break;
                    case EAST:
                        conveyorBeltView.setRotate(90);
                        break;
                    case SOUTH:
                        conveyorBeltView.setRotate(180);
                        break;
                    case WEST:
                        conveyorBeltView.setRotate(270);
                        break;
                }

                this.getChildren().add(conveyorBeltView);
                break;
            }
        }

        for (FieldAction action : space.getActions()) {
            if (action instanceof DoubleConveyorBelt) {
                DoubleConveyorBelt doubleConveyorBelt = (DoubleConveyorBelt) action;
                Image doubleConveyorBeltImage = new Image("/assets/blue.png");
                ImageView doubleConveyorBeltView = new ImageView(doubleConveyorBeltImage);
                doubleConveyorBeltView.setFitWidth(SPACE_WIDTH);
                doubleConveyorBeltView.setFitHeight(SPACE_HEIGHT);
                doubleConveyorBeltView.setPreserveRatio(false);

                // Rotate the image based on its heading
                switch (doubleConveyorBelt.getHeading()) {
                    case NORTH:
                        doubleConveyorBeltView.setRotate(0);
                        break;
                    case EAST:
                        doubleConveyorBeltView.setRotate(90);
                        break;
                    case SOUTH:
                        doubleConveyorBeltView.setRotate(180);
                        break;
                    case WEST:
                        doubleConveyorBeltView.setRotate(270);
                        break;
                }

                this.getChildren().add(doubleConveyorBeltView);
                break;
            }
        }

        Image wallImage = new Image("/assets/wall.png");





        // Check if the current space is a checkpoint
        for (FieldAction action : space.getActions()) {
            if (action instanceof Checkpoint) {
                Checkpoint checkpoint = (Checkpoint) action;
                Image checkpointImage = new Image("/assets/" + checkpoint.getCheckpointNumber() + ".png");
                ImageView checkpointView = new ImageView(checkpointImage);
                checkpointView.setFitWidth(SPACE_WIDTH);
                checkpointView.setFitHeight(SPACE_HEIGHT);
                checkpointView.setPreserveRatio(false);
                this.getChildren().add(checkpointView);
                break;
            }
        }


        // Draw walls
        for (Heading wall : space.getWalls()) {

            ImageView wallView = new ImageView(wallImage);

            switch (wall) {
                case NORTH:
                    wallView.setFitHeight(SPACE_HEIGHT);
                    wallView.setFitWidth(15);  // Set the width for horizontal wall
                    wallView.setRotate(90); // Rotate 90 degrees for North wall
                    wallView.setTranslateY(-SPACE_HEIGHT / 2.4);
                    StackPane.setAlignment(wallView, Pos.TOP_CENTER);
                    break;
                case EAST:
                    wallView.setFitHeight(SPACE_HEIGHT);
                    wallView.setFitWidth(15);
                    // No rotation needed for East wall
                    StackPane.setAlignment(wallView, Pos.CENTER_RIGHT);
                    break;
                case SOUTH:
                    wallView.setFitHeight(SPACE_HEIGHT);
                    wallView.setFitWidth(15);
                    wallView.setRotate(270); // Rotate 270 degrees for South wall
                    wallView.setTranslateY(SPACE_HEIGHT / 2.4);
                    StackPane.setAlignment(wallView, Pos.BOTTOM_CENTER);
                    break;
                case WEST:
                    wallView.setFitHeight(SPACE_HEIGHT);
                    wallView.setFitWidth(15);
                    wallView.setRotate(180); // Rotate 180 degrees for the West wall (if the image is top/bottom specific)
                    StackPane.setAlignment(wallView, Pos.CENTER_LEFT);
                    break;
            }

            wallView.setPreserveRatio(false);
            this.getChildren().add(wallView);
        }

        // Draw player
        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
        }

    }

}



