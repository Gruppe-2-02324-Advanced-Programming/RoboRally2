package dk.dtu.compute.se.pisd.roborally.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * This class contains methods for showing dialogs to the user. The dialogs are used to get input from the user.
 * The methods are static and can be called from anywhere in the code.
 * @author Marcus s214962

 */
public class GameDialogs {

    public static Optional<Integer> showPlayerNumberDialog(List<Integer> playerNumberOptions) {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(playerNumberOptions.get(0), playerNumberOptions);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        return dialog.showAndWait();
    }

    /**
     * Show a dialog for entering an IP address
     * @param defaultIp the default IP address to show in the dialog
     * @return the IP address entered by the user
     */
    public static Optional<String> showIpInputDialog(String defaultIp) {
        TextInputDialog dialogip = new TextInputDialog(defaultIp);
        dialogip.setTitle("Insert ip");
        dialogip.setHeaderText("Enter the server IP");
        dialogip.setContentText("URL:");
        return dialogip.showAndWait();
    }


    /**
        * Show a dialog for selecting a player number
     * @param maxPlayers the maximum number of players to show in the dialog
     * @return the player number selected by the user
     */
    public static Optional<Integer> showPlayerSelectionDialog(int maxPlayers) {
        List<Integer> choices = new ArrayList<>();
        for (int i = 1; i <= maxPlayers; i++) {
            choices.add(i);
        }
        ChoiceDialog<Integer> playerNo = new ChoiceDialog<>(choices.get(0), choices);
        playerNo.setTitle("Select Player Number");
        playerNo.setHeaderText("Which player do you want to play as 1-" + maxPlayers);
        playerNo.setContentText("Player Number:");
        return playerNo.showAndWait();
    }

    /**
     * Show a dialog for selecting a player number
     * @return the player number selected by the user
     */
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

    /**
     * Show a dialog for selecting a player number
     * @param title the title of the dialog
     * @param header the header of the dialog
     * @param content  the content of the dialog
     * @return the player number selected by the user
     *
     */
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

    /**
     * Show a dialog for selecting a player number
     * @param title the title of the dialog
     * @param headerText the header of the dialog
     * @param contentText the content of the dialog
     * @return the player number selected by the user
     * @author Marcus s214962
     */
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
