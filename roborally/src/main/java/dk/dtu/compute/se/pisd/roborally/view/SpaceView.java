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
import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBeltCorner;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.Gears;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 50; // 75;
    final public static int SPACE_WIDTH = 50; // 75;

    public final Space space;


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

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }


    /**
     * Draws the walls posibly other stuff
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
        this.getChildren().add(emptyFieldView);  // Add the empty field view as the first layer


        // Check if the current space is a checkpoint
        for (FieldAction action : space.getActions()) {
            if (action instanceof Checkpoint) {
                Checkpoint checkpoint = (Checkpoint) action;
                // Load the checkpoint image
                Image checkpointImage = new Image("/assets/" + checkpoint.getCheckpointNumber() + ".png");
                ImageView checkpointView = new ImageView(checkpointImage);
                checkpointView.setFitWidth(SPACE_WIDTH);
                checkpointView.setFitHeight(SPACE_HEIGHT);
                checkpointView.setPreserveRatio(false);
                this.getChildren().add(checkpointView);  // Add the checkpoint view as the second layer
                break;
            }
        }



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
                this.getChildren().add(gearImageView); // Added the gear image view as a layer
                break; // There is only one gear per space, we can break the loop after adding it
            }
        }

        // Check if the current space contains a ConveyorBeltCorner
        for (FieldAction action : space.getActions()) {
            if (action instanceof ConveyorBeltCorner) {
                ConveyorBeltCorner conveyorBeltCorner = (ConveyorBeltCorner) action;
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

                this.getChildren().add(conveyorBeltCornerView);  // Add the conveyor belt corner view as the second layer
                break;
            }
        }


        // Check if the current space contains a conveyor belt
        for (FieldAction action : space.getActions()) {
            if (action instanceof ConveyorBelt) {
                ConveyorBelt conveyorBelt = (ConveyorBelt) action;
                // Load the conveyor belt image
                Image conveyorBeltImage = new Image("/assets/green.png"); // replace with the actual path to your conveyor belt image
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

                this.getChildren().add(conveyorBeltView);  // Add the conveyor belt view as the second layer
                break;
            }
        }


        Image wallImage = new Image("/assets/wall.png");




        //todo add player as a picture on board
/*
        // Draw player
        Player player = space.getPlayer();
        if (player != null) {
            System.out.println("/assets/" + player.getName() + ".png");
            // Load the player image. You should have different images per player or use a single image and colorize it dynamically if possible.
            Image playerImage = new Image("/assets/" + player.getName() + ".png"); // Assuming each player has a unique ID
            ImageView playerView = new ImageView(playerImage);
            playerView.setFitWidth(30); // Adjust the width as needed
            playerView.setFitHeight(30); // Adjust the height as needed
            playerView.setPreserveRatio(true);

            // Rotate the player image based on the player's heading
            playerView.setRotate((90 * player.getHeading().ordinal()) % 360);

            // Add player image to the view
            this.getChildren().add(playerView);

            // Optionally, you can align the player in the center of the space
            StackPane.setAlignment(playerView, Pos.CENTER);
        }

*/







        // Draw walls
        for (Heading wall : space.getWalls()) {

            ImageView wallView = new ImageView(wallImage);

            switch (wall) {
                case NORTH:
                    wallView.setFitHeight(SPACE_HEIGHT);  // Set the height for horizontal wall
                    wallView.setFitWidth(15);  // Set the width for horizontal wall
                    wallView.setRotate(90); // Rotate 90 degrees for North wall
                    // Adjust the translation to less than half the space height to position correctly
                    wallView.setTranslateY(-SPACE_HEIGHT / 2.4);  // Adjust this value as needed
                    StackPane.setAlignment(wallView, Pos.TOP_CENTER);
                    break;
                case EAST:
                    wallView.setFitHeight(SPACE_HEIGHT); // Set the height for vertical wall
                    wallView.setFitWidth(15);
                    // No rotation needed for East wall
                    StackPane.setAlignment(wallView, Pos.CENTER_RIGHT);
                    break;
                case SOUTH:
                    wallView.setFitHeight(SPACE_HEIGHT);  // Set the height for horizontal wall
                    wallView.setFitWidth(15);  // Set the width for horizontal wall
                    wallView.setRotate(270); // Rotate 270 degrees for South wall
                    // Translate the image view so that its top edge aligns with the bottom edge of the space
                    wallView.setTranslateY(SPACE_HEIGHT / 2.4);
                    StackPane.setAlignment(wallView, Pos.BOTTOM_CENTER);
                    break;
                case WEST:
                    wallView.setFitHeight(SPACE_HEIGHT); // Set the height for vertical wall
                    wallView.setFitWidth(15);
                    wallView.setRotate(180); // Rotate 180 degrees for the West wall (if the image is top/bottom specific)
                    StackPane.setAlignment(wallView, Pos.CENTER_LEFT);
                    break;
            }

            wallView.setPreserveRatio(false); // Turn off preserve ratio to allow explicit sizing
            this.getChildren().add(wallView);
        }
    }

}



