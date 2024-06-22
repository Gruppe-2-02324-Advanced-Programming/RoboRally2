package dk.dtu.compute.se.pisd.roborally.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameDialogs {

    public static Optional<Integer> showPlayerNumberDialog(List<Integer> playerNumberOptions) {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(playerNumberOptions.get(0), playerNumberOptions);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        return dialog.showAndWait();
    }

    public static Optional<String> showIpInputDialog(String defaultIp) {
        TextInputDialog dialogip = new TextInputDialog(defaultIp);
        dialogip.setTitle("Insert ip");
        dialogip.setHeaderText("Enter the server IP");
        dialogip.setContentText("URL:");
        return dialogip.showAndWait();
    }

    public static Optional<Integer> showPlayerSelectionDialog(int maxPlayers) {
        List<Integer> choices = new ArrayList<>();
        for (int i = 1; i <= maxPlayers; i++) {
            choices.add(i);
        }
        ChoiceDialog<Integer> playerNo = new ChoiceDialog<>(choices.get(0), choices);
        playerNo.setTitle("Select Number");
        playerNo.setHeaderText("Choose a number between 1 and " + maxPlayers);
        playerNo.setContentText("Number:");
        return playerNo.showAndWait();
    }

    public static Optional<String> showHostOrJoinDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Host or Join");
        alert.setHeaderText("Do you want to host a game or join a game?");
        alert.setContentText("Choose your option.");

        ButtonType hostButton = new ButtonType("Host Game");
        ButtonType joinButton = new ButtonType("Join Game");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());

        alert.getButtonTypes().setAll(hostButton, joinButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == hostButton) {
                return Optional.of("Host Game");
            } else if (result.get() == joinButton) {
                return Optional.of("Join Game");
            } else {
                return Optional.of("Cancel");
            }
        }
        return Optional.empty();
    }

    public static Optional<Integer> showIntegerInputDialog(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                return Optional.of(Integer.parseInt(result.get()));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter a valid integer.");
                alert.showAndWait();
                return Optional.of(1);
            }
        }
        return Optional.of(1);
    }

    public static String showNameInputDialog(String title, String headerText, String contentText) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);

        // Get the response value
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

}
