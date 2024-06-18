package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class MainMenuView extends StackPane {

    public MainMenuView(AppController appController) {

        // Set the background image for the StackPane
        Image backgroundImage = new Image("/assets/gearLeft.png");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        this.setBackground(new Background(backgroundImg));

        // Create a VBox to hold the buttons
        VBox buttonBox = new VBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(200, 0, 0, 0)); // Add padding at the top

        // Create buttons
        Button continueButton = new Button("Continue");
        Button newGameButton = new Button("New Game");
        Button loadGameButton = new Button("Load Game");
        Button rulesButton = new Button("Rules");

        // Apply inline styles to buttons
        styleButton(continueButton);
        styleButton(newGameButton);
        styleButton(loadGameButton);
        styleButton(rulesButton);

        // Set button actions
        continueButton.setOnAction(e -> appController.continueGame());
        newGameButton.setOnAction(e -> appController.newGame());
        loadGameButton.setOnAction(e -> appController.loadGame());
        rulesButton.setOnAction(e -> appController.showRules());

        // Add buttons to the VBox
        buttonBox.getChildren().addAll(continueButton, newGameButton, loadGameButton, rulesButton);

        // Add the VBox to the StackPane
        this.getChildren().add(buttonBox);
    }

    private void styleButton(Button button) {
        button.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-padding: 10 20 10 20; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-color: #4CAF50; " +
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #45a049; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-padding: 10 20 10 20; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-color: #4CAF50; " +
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-padding: 10 20 10 20; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-color: #4CAF50; " +
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand;"
        ));

        button.setOnMousePressed(e -> button.setStyle(
                "-fx-background-color: #3e8e41; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-padding: 10 20 10 20; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-color: #4CAF50; " +
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand;"
        ));

        button.setOnMouseReleased(e -> button.setStyle(
                "-fx-background-color: #45a049; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-padding: 10 20 10 20; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-color: #4CAF50; " +
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand;"
        ));
    }
}
