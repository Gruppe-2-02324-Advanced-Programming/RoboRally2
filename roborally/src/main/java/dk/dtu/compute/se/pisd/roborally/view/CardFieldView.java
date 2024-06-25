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
import dk.dtu.compute.se.pisd.roborally.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 * A view for a field for a command card. This view is used to display a command card
 * and to allow the user to drag and drop the card to another field.
 * The view is also used to display the cards in the program registers of a player.
 * The view is an observer of the field and updates the view when the field changes.
 * The view also allows to drag and drop a card from one field to another.
 *
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class CardFieldView extends GridPane implements ViewObserver {

 /**
     * The data format for the drag and drop of a command card.
     */
    final public static  DataFormat ROBO_RALLY_CARD = new DataFormat("games/roborally/cards");
/**
     * The width of the card field.
     */
    final public static int CARDFIELD_WIDTH = 65;

    /**
     * The height of the card field.
     */
    final public static int CARDFIELD_HEIGHT = 100;
    /**
     * The border for the card field.
     */

    final public static Border BORDER = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2)));

    /**
     * The background for the card field.
     */
    final public static Background BG_DEFAULT = new Background(new BackgroundFill(Color.WHITE, null, null));

    /**
     * The background for the card field when it is dragged.
     */
    final public static Background BG_DRAG = new Background(new BackgroundFill(Color.GRAY, null, null));

    /**
     * The background for the card field when a card is dropped on it.
     */
    final public static Background BG_DROP = new Background(new BackgroundFill(Color.LIGHTGRAY, null, null));
/**
     * The background for the card field when it is active.
     */
    final public static Background BG_ACTIVE = new Background(new BackgroundFill(Color.YELLOW, null, null));
    /**
     * The background for the card field when it is done.
     */
    final public static Background BG_DONE = new Background(new BackgroundFill(Color.GREENYELLOW,  null, null));
/**
     * The field for which this view is created.
     */
    private final CommandCardField field;
    /**
     * The label for the card field.
     */
    private final Label label;
/**
     * The image view for the card field.
     */
    private final ImageView imageView;
/**
     * The game controller for the game.
     */
    private final GameController gameController;
/**
     * The constructor for the view of a card field.
     *
     * @param gameController the game controller for the game.
     * @param field the field for which this view is created.
     */
    public CardFieldView(@NotNull GameController gameController, @NotNull CommandCardField field) {
        this.gameController = gameController;
        this.field = field;

        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(5, 5, 5, 5));

        this.setBorder(BORDER);
        this.setBackground(BG_DEFAULT);

        this.setPrefWidth(CARDFIELD_WIDTH);
        this.setMinWidth(CARDFIELD_WIDTH);
        this.setMaxWidth(CARDFIELD_WIDTH);
        this.setPrefHeight(CARDFIELD_HEIGHT);
        this.setMinHeight(CARDFIELD_HEIGHT);
        this.setMaxHeight(CARDFIELD_HEIGHT);

        label = new Label("This is a slightly longer text");
        label.setWrapText(true);
        label.setMouseTransparent(true);
        this.add(label, 0, 0);

        imageView = new ImageView();
        imageView.setFitHeight(CARDFIELD_HEIGHT);
        imageView.setFitWidth(CARDFIELD_WIDTH);
        //this.getChildren().clear();
        this.add(imageView, 0, 0);

        this.setOnDragDetected(new OnDragDetectedHandler());
        this.setOnDragOver(new OnDragOverHandler());
        this.setOnDragEntered(new OnDragEnteredHandler());
        this.setOnDragExited(new OnDragExitedHandler());
        this.setOnDragDropped(new OnDragDroppedHandler());
        this.setOnDragDone(new OnDragDoneHandler());

        field.attach(this);
        update(field);
    }
/**
     * This method updates the view of the card field.
     */
    private String cardFieldRepresentation(CommandCardField cardField) {
        if (cardField.player != null) {

            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                CommandCardField other = cardField.player.getProgramField(i);
                if (other == cardField) {
                    return "P," + i;
                }
            }

            for (int i = 0; i < Player.NO_CARDS; i++) {
                CommandCardField other = cardField.player.getCardField(i);
                if (other == cardField) {
                    return "C," + i;
                }
            }
        }
        return null;

    }

    /**
     * This method updates the view of the card field.
     */
    private CommandCardField cardFieldFromRepresentation(String rep) {
        if (rep != null && field.player != null) {
            String[] strings = rep.split(",");
            if (strings.length == 2) {
                int i = Integer.parseInt(strings[1]);
                if ("P".equals(strings[0])) {
                    if (i < Player.NO_REGISTERS) {
                        return field.player.getProgramField(i);
                    }
                } else if ("C".equals(strings[0])) {
                    if (i < Player.NO_CARDS) {
                        return field.player.getCardField(i);
                    }
                }
            }
        }
        return null;
    }


/**
     * This method updates the view of the card field.
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == field && subject != null) {
            CommandCard card = field.getCard();
            if (card != null && field.isVisible()) {
                imageView.setImage(card.getImage());
            } else {
                imageView.setImage(null);
                label.setText("");
            }

        }
    }
/**
     * This method is called when the drag is detected.
     */
    private class OnDragDetectedHandler implements EventHandler<MouseEvent> {
    /**
     * This method is called when the drag is detected.
     * @param event the mouse event that triggered the drag.
     */
        @Override
        public void handle(MouseEvent event) {
            Object t = event.getTarget();
            if (t instanceof ImageView source) {
                CommandCardField cardField = field;
                if (cardField != null &&
                        cardField.getCard() != null &&
                        cardField.player != null &&
                        cardField.player.board != null &&
                        cardField.player.board.getPhase().equals(Phase.Programming)) {
                    Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                    Image image = source.getImage();
                    db.setDragView(image);

                    ClipboardContent content = new ClipboardContent();
                    content.put(ROBO_RALLY_CARD, cardFieldRepresentation(cardField));

                    db.setContent(content);
                    setBackground(BG_DRAG);
                }
            }
            event.consume();
        }

    }
/**
     * This method is called when the drag is over.
     */
    private class OnDragOverHandler implements EventHandler<DragEvent> {


        /**
         * This method is called when the drag is over.
         * @param event the drag event.
         */
        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView target) {
                CommandCardField cardField = target.field;
                if (cardField != null &&
                        (cardField.getCard() == null || event.getGestureSource() == target) &&
                        cardField.player != null &&
                        cardField.player.board != null) {
                    if (event.getDragboard().hasContent(ROBO_RALLY_CARD)) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
            }
            event.consume();
        }

    }

    private class OnDragEnteredHandler implements EventHandler<DragEvent> {
        /**
         * This method is called when the drag enters the field.
         * @param event the drag event.
         */
        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView target) {
                CommandCardField cardField = target.field;
                if (cardField != null &&
                        cardField.getCard() == null &&
                        cardField.player != null &&
                        cardField.player.board != null) {
                    if (event.getGestureSource() != target &&
                            event.getDragboard().hasContent(ROBO_RALLY_CARD)) {
                        target.setBackground(BG_DROP);
                    }
                }
            }
            event.consume();
        }

    }
/**
     * This method is called when the drag exits the field.
     */
    private class OnDragExitedHandler implements EventHandler<DragEvent> {
    /**
     * This method is called when the drag exits the field.
     * @param event the drag event.
     */
        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView target) {
                CommandCardField cardField = target.field;
                if (cardField != null &&
                        cardField.getCard() == null &&
                        cardField.player != null &&
                        cardField.player.board != null) {
                    if (event.getGestureSource() != target &&
                            event.getDragboard().hasContent(ROBO_RALLY_CARD)) {
                        target.setBackground(BG_DEFAULT);
                    }
                }
            }
            event.consume();
        }

    }

    /**
     * This method is called when the drag is dropped.
     */
    private class OnDragDroppedHandler implements EventHandler<DragEvent> {


        /**
         * This method is called when the drag is dropped.
         * @param event the drag event.
         */
        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView target) {
                CommandCardField cardField = target.field;

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (cardField != null &&
                        cardField.getCard() == null &&
                        cardField.player != null &&
                        cardField.player.board != null) {
                    if (event.getGestureSource() != target &&
                            db.hasContent(ROBO_RALLY_CARD)) {
                        Object object = db.getContent(ROBO_RALLY_CARD);
                        if (object instanceof String) {
                            CommandCardField source = cardFieldFromRepresentation((String) object);
                            if (source != null && gameController.moveCards(source, cardField)) {
                                // CommandCard card = source.getCard();
                                // if (card != null) {
                                // if (gameController.moveCards(source, cardField)) {
                                // cardField.setCard(card);
                                success = true;
                                // }
                            }
                        }
                    }
                }
                event.setDropCompleted(success);
                target.setBackground(BG_DEFAULT);
            }
            event.consume();
        }

    }

    /**
     * This method is called when the drag is done.
     */
    private class OnDragDoneHandler implements EventHandler<DragEvent> {


        /**
         * This method is called when the drag is done.
         * @param event the drag event.
         */
        @Override
        public void handle(DragEvent event) {
            Object t = event.getTarget();
            if (t instanceof CardFieldView source) {
                source.setBackground(BG_DEFAULT);
            }
            event.consume();
        }

    }

}
