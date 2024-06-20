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
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.*;

import dk.dtu.compute.se.pisd.roborally.network.Network;
import dk.dtu.compute.se.pisd.roborally.view.GameDialogs;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * AppController is the main controller of the application. It is responsible
 * for starting and stopping games, and for saving and loading games. It also
 * creates the board view and the player views.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Christoffer s205449
 *
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private RoboRally roboRally;


    private GameController gameController;

    /**
     * The constructor of the AppController.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @param roboRally the RoboRally application object
     */
    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    /**
     * Start a new game. The user is asked to select the number of players
     * for the game and gameboard. The board is initialized with the selected number
     * of
     * players, and the game is started with the programming phase.
     *
     * @author Ekkart Kindler, ekki@dtu.dk
     * @author Christoffer s205449
     * @author Marcus s214962
     */
    public void multiplayer() {
        Optional<Integer> result = GameDialogs.showPlayerNumberDialog(PLAYER_NUMBER_OPTIONS);

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }

            }


            /**
             * Initializes players and assigns them to starting positions.
             *
             * @param board the game board
             * @param numberOfPlayers the number of players selected to play
             */
            private void initializePlayers(Board board, int numberOfPlayers) {
                List<Space> spawnPoints = board.getGearSpawnPoints();
                if (spawnPoints.size() < numberOfPlayers) {
                    throw new IllegalStateException("Not enough start points for the number of players.");
                }

                Collections.shuffle(spawnPoints); // Shuffle to assign starting positions randomly

                for (int i = 0; i < numberOfPlayers; i++) {
                    String playerName = "Player " + (i + 1);
                    String color = PLAYER_COLORS.get(i % PLAYER_COLORS.size());
                    Player player = new Player(board, color, playerName);
                    board.addPlayer(player);
                    player.setSpace(spawnPoints.get(i)); // Assign each player to a start point
                }
                board.setCurrentPlayer(board.getPlayer(0)); // Optionally set the first player as the current player
            }



            gameController = new GameController(initializeBoard());
            int no = result.get();
            Board board = gameController.board;
            board.attach(this);
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i));
            }
            board.setCurrentPlayer(board.getPlayer(0));

            Optional<String> ip = GameDialogs.showIpInputDialog(Network.getIPv4Address());

            ip.ifPresent(url -> {
                gameController.updateBaseUrl(url);
            });

            /*
             * Optional<Integer> playerNumber = GameDialogs.showPlayerSelectionDialog(no);
             * 
             * playerNumber.ifPresent(number -> {
             * gameController.setPlayerNumber(number);
             * });
             */

            Optional<String> hostOrJoin = GameDialogs.showHostOrJoinDialog();

            Long gameID;
            if (hostOrJoin.isPresent() && hostOrJoin.get().equals("Host Game")) {
                gameID = gameController.gameClient.createGame();
            } else {
                Optional<Integer> joingameID = GameDialogs.showIntegerInputDialog("Join game with ID",
                        "Join game with ID", "Join game with ID:");
                if (joingameID.isPresent()) {
                    gameID = (long) joingameID.get();
                } else {
                    gameID = 1L;
                }
            }

            String nickName = GameDialogs.showNameInputDialog("Name", "name", "Name");

            Long playerID = gameController.gameClient.addPlayer(gameID, nickName);

            gameController.setPlayerNumber(playerID.intValue());

            gameController.board.setGameID(gameID);

            gameController.startProgrammingPhase();

            roboRally.createBoardView(gameController);
        }
    }

    /**
     * this method loads the games from the json file and asks the user which of the
     * gameID's they wish to load.
     * The system then finds the game which has the same gameID as the one
     * requested.
     *
     * @author Christoffer s205449
     */





    public void saveGame() {
        if (gameController != null && gameController.board != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Save Game");
            dialog.setHeaderText("Enter the name of the game you want to save:");
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String gameName = result.get();
                LoadBoard.saveCurrentGame(gameController.board, gameName);
                System.out.println("Game saved to " + gameName);
            }
        } else {
            System.out.println("No game is currently active.");
        }
    }





    private List<String> getFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        List<String> fileList = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
            if (files != null) {
                for (File file : files) {
                    fileList.add(file.getName());
                }
            }
        } else {
            System.err.println("Directory does not exist: " + directoryPath);
        }
        return fileList;
    }

    public static String removeJsonExtension(String fileName) {
        return fileName.replaceFirst("[.][jJ][sS][oO][nN]$", "");
    }

    public static int extractNumberFromString(String input) {
        // Define the regular expression pattern to find the number
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        // Find the first occurrence of the pattern in the input string
        if (matcher.find()) {
            // Extract the matched substring
            String numberStr = matcher.group();
            // Convert the extracted substring to an integer
            return Integer.parseInt(numberStr);
        } else {
            throw new IllegalArgumentException("No number found in the input string: " + input);
        }
    }

    /**
     * This method loads the game from the json file and the user can select which
     * game they want to load.
     *
     * @author Marcus s214962
     * @author Christoffer s205449
     */
    public void loadGame() {
        List<String> fileList = getFilesInDirectory(LoadBoard.SAVED_GAMES_FOLDER);

        if (fileList.isEmpty()) {
            System.out.println("No files found in the directory.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(fileList.get(0), fileList);
        dialog.setTitle("Select Game File");
        dialog.setHeaderText("Choose the game file you want to load:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(fileName -> {
            System.out.println("Selected file: " + fileName);
            JSONObject json = LoadBoard.loadJSON(removeJsonExtension(fileName));

            String boardName = json.getString("boardName");
            gameController = new GameController(LoadBoard.loadBoard(boardName));

            gameController.board.setTotalCheckpoints(json.getInt("totalCheckpoints"));
            gameController.board.setCounter(json.getInt("counter"));

            int no = json.getJSONArray("players").length();
            Board board = gameController.board;
            board.attach(this);
            for (int i = 0; i < no; i++) {
                String color = json.getJSONArray("players").getJSONObject(i).getString("color");
                String name = json.getJSONArray("players").getJSONObject(i).getString("name");
                String heading = json.getJSONArray("players").getJSONObject(i).getString("heading");
                Player player = new Player(board, color, name);
                player.setCheckpoints(json.getJSONArray("players").getJSONObject(i).getInt("checkpoints"));
                player.setHeading(Heading.valueOf(heading));
                board.addPlayer(player);

                int x = json.getJSONArray("players").getJSONObject(i).getJSONObject("space").getInt("x");
                int y = json.getJSONArray("players").getJSONObject(i).getJSONObject("space").getInt("y");

                player.setSpace(board.getSpace(x % board.width, y));
            }
            String currentPlayerName = json.getJSONObject("current").getString("name");
            int currentPlayerNumber = extractNumberFromString(currentPlayerName);
            board.setCurrentPlayer(board.getPlayer(currentPlayerNumber));

            Phase phase = Phase.valueOf(json.getString("phase"));
            int step = json.getInt("step");

            gameController.board.setPhase(phase);
            gameController.board.setCurrentPlayer(board.getPlayer(currentPlayerNumber));
            gameController.board.setStep(step);

            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                JSONObject jsonPlayer = json.getJSONArray("players").getJSONObject(i);
                if (player != null) {
                    for (int j = 0; j < Player.NO_REGISTERS; j++) {
                        CommandCardField field = player.getProgramField(j);
                        JSONObject program = jsonPlayer.getJSONArray("program").getJSONObject(j);
                        Command command;
                        boolean visible = program.getBoolean("visible");

                        if (program.has("card")) {
                            command = Command.valueOf(program.getJSONObject("card").getString("command"));
                            field.setCard(new CommandCard(command));
                        } else {
                            field.setCard(null);
                        }
                        field.setVisible(visible);
                    }
                    for (int j = 0; j < Player.NO_CARDS; j++) {
                        CommandCardField field = player.getCardField(j);
                        JSONObject cards = jsonPlayer.getJSONArray("cards").getJSONObject(j);
                        Command command;
                        boolean visible = cards.getBoolean("visible");
                        if (cards.has("card")) {
                            command = Command.valueOf(cards.getJSONObject("card").getString("command"));
                            field.setCard(new CommandCard(command));
                        } else {
                            field.setCard(null);
                        }
                        field.setVisible(visible);
                    }
                }
            }

            roboRally.createBoardView(gameController);
        });
    }

    /**
     * @return the board that the user has selected
     * @author Christoffer s205449
     *         <p>
     *         This method checks which boards are available
     */
    private Board initializeBoard() {
        List<String> boards = LoadBoard.getBoards();
        ChoiceDialog<String> dialog = new ChoiceDialog<>(boards.get(0), boards);
        dialog.setTitle("Select board");
        dialog.setHeaderText("Select board");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return LoadBoard.loadBoard(result.get());
        }
        return null;
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Save Game");
            alert.setHeaderText("Do you want to save the current game?");
            alert.setContentText("Choose your option.");

            ButtonType buttonTypeSave = new ButtonType("Save");
            ButtonType buttonTypeDontSave = new ButtonType("Don't Save");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeDontSave, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeSave) {
                saveGame();
                gameController = null;
                return true;
            } else if (result.isPresent() && result.get() == buttonTypeDontSave) {
                gameController = null;
                // Return to main menu
                roboRally.createMainMenuView();
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
    /**
     * Exit the application, giving the user the option to save the game
     * before exiting. If the user cancels the exit operation, the method
     * returns without exiting the application.
     */
    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    /**
     * Returns true if a game is currently running, false otherwise.
     *
     * @return
     */
    public boolean isGameRunning() {
        return gameController != null;
    }

    /**
     * Does so player can win
     *
     * @param subject the subject which changed
     * @author Christoffer Fink s205449
     * @author Marcus s214962
     * @author Setare s232629
     */
    @Override
    public void update(Subject subject) {
        if (subject instanceof Board) {
            Board board = (Board) subject;
            if (board.isWon()) {
                for (Player player : board.getPlayers()) {
                    if (player.getCheckpoints() == board.getTotalCheckpoints()) {
                        for (Player p : board.getPlayers()) {
                            p.setCheckpoints(0);
                        }
                        ButtonType playAgainButton = new ButtonType("Play Again");
                        ButtonType closeButton = new ButtonType("Close Game");
                        Alert alert = new Alert(AlertType.CONFIRMATION, "Game won by " + player.getName(),
                                ButtonType.OK, playAgainButton, closeButton);
                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == playAgainButton) {
                            multiplayer();
                        } else if (result.isPresent() && result.get() == closeButton) {
                            exit();
                        } else {
                            stopGame();
                        }
                        board.setWon(false);
                        return;
                    }
                }
            }
        }
    }

    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                if (!stopGame()) {
                    return; // User decided not to stop the current game
                }
            }

            gameController = new GameController(initializeBoard());
            Board board = gameController.board;
            board.attach(this);

            initializePlayers(board, result.get()); // Initializing players with the selected number of players

            gameController.startProgrammingPhase();
            roboRally.createBoardView(gameController);
        }
    }

    public void startServer() {
    System.out.println("Start server");
    }




    public void showRules() {
        try {
            URI rulesUri = new URI("https://renegadegamestudios.com/content/File%20Storage%20for%20site/Rulebooks/Robo%20Rally/RoboRally_Rulebook_WEB.pdf");
            Desktop.getDesktop().browse(rulesUri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
