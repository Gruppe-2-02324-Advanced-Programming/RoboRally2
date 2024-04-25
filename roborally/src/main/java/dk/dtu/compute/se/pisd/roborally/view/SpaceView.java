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

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 70; // 75;
    final public static int SPACE_WIDTH = 70; // 75;

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
     *
     */
    @Override
    public void updateView(Subject subject) {
        this.getChildren().clear();

        for (FieldAction action : space.getActions()) {
            if (action instanceof Gears) {
                Gears gear = (Gears) action;
            }
        }

        // Draw player
        // Load the empty field image and set it as the background of the space
        Image emptyFieldImage = new Image("/assets/empty.png");
        ImageView emptyFieldView = new ImageView(emptyFieldImage);
        emptyFieldView.setFitWidth(SPACE_WIDTH);
        emptyFieldView.setFitHeight(SPACE_HEIGHT);
        emptyFieldView.setPreserveRatio(false);
        this.getChildren().add(emptyFieldView);  // Add the empty field view as the first layer

        Image wallImage = new Image("/assets/wall.png");



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

        // Draw walls
        for (Heading wall : space.getWalls()) {

            ImageView wallView = new ImageView(wallImage);

            Line line;
            Text text;
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
    }
}

