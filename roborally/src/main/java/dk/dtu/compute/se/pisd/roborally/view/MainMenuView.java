package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class MainMenuView extends StackPane {

    public MainMenuView(AppController appController) {

        // Create a VBox to hold the buttons
        VBox buttonBox = new VBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);
        buttonBox.setPadding(new Insets(20, 0, 20, 0)); // Add padding at the top and bottom

        // Create buttons
        Button continueButton = new Button("Continue");
        Button newGameButton = new Button("New Game");
        Button loadGameButton = new Button("Load Game");
        Button rulesButton = new Button("Rules");

        // Apply styles to buttons
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

        // Load the top image
        try {
            Image topImage = new Image("assets/forside.png");
            ImageView imageView = new ImageView(topImage);
            imageView.setFitWidth(300);
            imageView.setPreserveRatio(true);

            // Create a VBox to hold the image and buttons
            VBox mainBox = new VBox();
            mainBox.setAlignment(Pos.TOP_CENTER);
            mainBox.setSpacing(20);
            mainBox.setPadding(new Insets(50, 0, 0, 0)); // Add padding at the top

            // Add image and buttonBox to the mainBox
            mainBox.getChildren().addAll(imageView, buttonBox);

            // Add the mainBox to the StackPane
            this.getChildren().add(mainBox);

        } catch (NullPointerException e) {
            System.err.println("Top image not found: " + e.getMessage());
        }

        // Load the background image for the whole window
        try {
            Image backgroundImage = new Image("assets/forsidebaggrund.jpg");
            BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
            BackgroundImage background = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    backgroundSize
            );
            this.setBackground(new Background(background));
            this.prefWidthProperty().bind(this.getScene().widthProperty());
            this.prefHeightProperty().bind(this.getScene().heightProperty());
        } catch (NullPointerException e) {
            System.err.println("Background image not found: " + e.getMessage());
        }
    }

    private void styleButton(Button button) {
        button.setStyle(
                "-fx-background-color: linear-gradient(#FF0000, #8B0000); " + // Red gradient background
                        "-fx-text-fill: #FFFFFF; " + // White text
                        "-fx-font-size: 18px; " + // Slightly larger font size
                        "-fx-font-weight: bold; " + // Bold text
                        "-fx-padding: 15 30 15 30; " + // Padding inside the button
                        "-fx-background-radius: 5; " + // Rounded corners
                        "-fx-border-color: yellow; " + // Yellow border for warning stripe effect
                        "-fx-border-width: 3px; " +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: linear-gradient(#FF4500, #8B0000); " + // Lighter red gradient on hover
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 30 15 30; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-color: yellow; " +
                        "-fx-border-width: 3px; " +
                        "-fx-cursor: hand;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: linear-gradient(#FF0000, #8B0000); " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 30 15 30; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-color: yellow; " +
                        "-fx-border-width: 3px; " +
                        "-fx-cursor: hand;"
        ));

        button.setOnMousePressed(e -> button.setStyle(
                "-fx-background-color: linear-gradient(#8B0000, #FF0000); " + // Inverted gradient on press
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 30 15 30; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-color: yellow; " +
                        "-fx-border-width: 3px; " +
                        "-fx-cursor: hand;"
        ));

        button.setOnMouseReleased(e -> button.setStyle(
                "-fx-background-color: linear-gradient(#FF4500, #8B0000); " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 30 15 30; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-color: yellow; " +
                        "-fx-border-width: 3px; " +
                        "-fx-cursor: hand;"
        ));
    }
}
