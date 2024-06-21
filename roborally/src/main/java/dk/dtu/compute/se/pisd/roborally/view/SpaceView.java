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
import dk.dtu.compute.se.pisd.roborally.controller.*;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SpaceView is a view of a space on the board. It is responsible for drawing the spaces on the board.
 * and updating the view when the space changes. For example if there is a player on the space, the view should update
 * to show the player on the space. Also, if there is a wall on the space, the view should update to show the wall. etc
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Emily, s191174
 *
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
     * Utility method to load an image from a given path
     * @param path the path to the image
     * @return the loaded image
     */
    private Image loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            return new Image(Objects.requireNonNull(is));
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
            return null;
        }
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
        Image emptyFieldImage = loadImage("/assets/empty.png");
        if (emptyFieldImage != null) {
            ImageView emptyFieldView = new ImageView(emptyFieldImage);
            emptyFieldView.setFitWidth(SPACE_WIDTH);
            emptyFieldView.setFitHeight(SPACE_HEIGHT);
            emptyFieldView.setPreserveRatio(false);
            this.getChildren().add(emptyFieldView);
        }

        // Handling GearSpawn
        for (FieldAction action : space.getActions()) {
            if (action instanceof StartGear) {
                Image gearImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/white_Gear.png")));
                ImageView gearImageView = new ImageView(gearImage);
                gearImageView.setFitWidth(SPACE_WIDTH);
                gearImageView.setFitHeight(SPACE_HEIGHT);
                gearImageView.setPreserveRatio(true);  // Keeping aspect ratio might be visually pleasing
                this.getChildren().add(gearImageView);
            }}


        // Handling GearSpawn
        for (FieldAction action : space.getActions()) {
            if (action instanceof StartGear) {
                Image gearImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/white_Gear.png")));
                ImageView gearImageView = new ImageView(gearImage);
                gearImageView.setFitWidth(SPACE_WIDTH);
                gearImageView.setFitHeight(SPACE_HEIGHT);
                gearImageView.setPreserveRatio(true);  // Keeping aspect ratio might be visually pleasing
                this.getChildren().add(gearImageView);
            }}



        // Check if the current space has a pit
        for (FieldAction action : space.getActions()) {
            if (action instanceof Pit) {
                Pit pit = (Pit) action;
                Image pitImage = loadImage("/assets/hole.png");
                if (pitImage != null) {
                    ImageView pitView = new ImageView(pitImage);
                    pitView.setFitWidth(SPACE_WIDTH);
                    pitView.setFitHeight(SPACE_HEIGHT);
                    pitView.setPreserveRatio(false);
                    this.getChildren().add(pitView);
                }
            }
        }

        // Check if the current space has a gear action and display the corresponding image
        for (FieldAction action : space.getActions()) {
            if (action instanceof Gears gears) {
                ImageView gearImageView;
                if (gears.rotation == Gears.LEFT_TURN) {
                    Image gearImage = loadImage("/assets/gearLeft.png");
                    if (gearImage != null) {
                        gearImageView = new ImageView(gearImage);
                    } else {
                        continue;
                    }
                } else if (gears.rotation == Gears.RIGHT_TURN) {
                    Image gearImage = loadImage("/assets/gearRight.png");
                    if (gearImage != null) {
                        gearImageView = new ImageView(gearImage);
                    } else {
                        continue;
                    }
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
                    conveyorBeltCornerImage = loadImage("/assets/greenTurnLeft.png");
                } else {
                    conveyorBeltCornerImage = loadImage("/assets/greenTurnRight.png");
                }
                if (conveyorBeltCornerImage != null) {
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
        }

        // Check if the current space contains a conveyor belt
        for (FieldAction action : space.getActions()) {
            if (action instanceof ConveyorBelt conveyorBelt) {
                Image conveyorBeltImage = loadImage("/assets/green.png");
                if (conveyorBeltImage != null) {
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
        }

        for (FieldAction action : space.getActions()) {
            if (action instanceof DoubleConveyorBelt) {
                DoubleConveyorBelt doubleConveyorBelt = (DoubleConveyorBelt) action;
                Image doubleConveyorBeltImage = loadImage("/assets/blue.png");
                if (doubleConveyorBeltImage != null) {
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
        }

        Image wallImage = loadImage("/assets/wall.png");

        // Check if the current field is a Laser
        for (FieldAction action : space.getActions()) {
            if (action instanceof Laser laser) {
                Image laserImage = loadImage("/assets/laser.png");
                if (laserImage != null) {
                    ImageView laserView = new ImageView(laserImage);
                    laserView.setFitWidth(SPACE_WIDTH); // Scale down
                    laserView.setFitHeight(SPACE_HEIGHT / 7); // Scale down
                    laserView.setPreserveRatio(false);

                    // Rotate the laser image based on its heading
                    switch (laser.getHeading()) {
                        case NORTH:
                            laserView.setRotate(90);
                            break;
                        case EAST:
                            laserView.setRotate(0);
                            break;
                        case SOUTH:
                            laserView.setRotate(90);
                            break;
                        case WEST:
                            laserView.setRotate(0);
                            StackPane.setAlignment(laserView, Pos.CENTER_LEFT);
                            break;
                    }

                    this.getChildren().add(laserView);
                }
            }
        }

        // Check if the current field is a LaserStart
        for (FieldAction action : space.getActions()) {
            if (action instanceof LaserStart laserStart) {
                Image laserStartImage = loadImage("/assets/laserStart.png");
                if (laserStartImage != null) {
                    ImageView laserStartView = new ImageView(laserStartImage);
                    laserStartView.setFitWidth(SPACE_WIDTH / 2); // Scale down
                    laserStartView.setFitHeight(SPACE_HEIGHT / 2); // Scale down
                    laserStartView.setPreserveRatio(false);

                    // Rotate and position the laser start image based on its heading
                    switch (laserStart.getHeading()) {
                        case NORTH:
                            laserStartView.setRotate(90);
                            StackPane.setAlignment(laserStartView, Pos.TOP_CENTER);
                            break;
                        case EAST:
                            laserStartView.setRotate(180); // Rotate 90 degrees to the left (0 degrees clockwise)
                            StackPane.setAlignment(laserStartView, Pos.CENTER_RIGHT);
                            break;
                        case SOUTH:
                            laserStartView.setRotate(270); // Rotate 90 degrees to the left (90 degrees clockwise)
                            StackPane.setAlignment(laserStartView, Pos.BOTTOM_CENTER);
                            break;
                        case WEST:
                            laserStartView.setRotate(0); // Rotate 90 degrees to the left (180 degrees clockwise)
                            StackPane.setAlignment(laserStartView, Pos.CENTER_LEFT);
                            break;
                    }

                    this.getChildren().add(laserStartView);
                }
            }
        }

        // Check if the current field is a pit
        for (FieldAction action : space.getActions()) {
            if (action instanceof Pit) {
                Pit pit = (Pit) action;
                Image pitImage = loadImage("/assets/pit.png");
                if (pitImage != null) {
                    ImageView pitView = new ImageView(pitImage);
                    pitView.setFitWidth(SPACE_WIDTH);
                    pitView.setFitHeight(SPACE_HEIGHT);
                    pitView.setPreserveRatio(false);
                    this.getChildren().add(pitView);
                }
            }
        }

        // Check if the current space is a checkpoint
        for (FieldAction action : space.getActions()) {
            if (action instanceof Checkpoint checkpoint) {
                Image checkpointImage = loadImage("/assets/" + checkpoint.getCheckpointNumber() + ".png");
                if (checkpointImage != null) {
                    ImageView checkpointView = new ImageView(checkpointImage);
                    checkpointView.setFitWidth(SPACE_WIDTH);
                    checkpointView.setFitHeight(SPACE_HEIGHT);
                    checkpointView.setPreserveRatio(false);
                    this.getChildren().add(checkpointView);
                }
            }
        }

        // Draw walls
        if (wallImage != null) {
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
