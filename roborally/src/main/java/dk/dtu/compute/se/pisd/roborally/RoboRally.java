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
package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.MainMenuView;
import dk.dtu.compute.se.pisd.roborally.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;

/**
 * The main class for the RoboRally application. This class is responsible for
 * the setup of the primary stage and the
 * primary scene. The primary scene contains a menu bar and a pane for the board
 * view. The board view is created and added
 * to the pane when a new game is created or loaded. The class also contains the
 * main method for starting the application.
 *
 * @see AppController
 * @see GameController
 * @see BoardView
 * @see RoboRallyMenuBar
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class RoboRally extends Application {

    private static final int MIN_APP_WIDTH = 600;
    private Stage stage;
    private BorderPane boardRoot;
    private GameController gameController;


    /**
     * Initializes the application. This method is called before the start method
     * and is used to initialize the application.
     */
    @Override
    public void init() throws Exception {
        super.init();
    }


    /**
     * Starts the application. This method is called after the init method and is
     * used to start the application.
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        AppController appController = new AppController(this);

        RoboRallyMenuBar menuBar = new RoboRallyMenuBar(appController);

        boardRoot = new BorderPane();
        VBox vbox = new VBox(menuBar, boardRoot);
        vbox.setMinWidth(MIN_APP_WIDTH);

        Scene primaryScene = new Scene(vbox);
        primaryScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F11) {
                stage.setFullScreen(!primaryStage.isFullScreen());
            }
        });

        MainMenuView mainMenuView = new MainMenuView(appController);
        boardRoot.setCenter(mainMenuView);

        stage.setScene(primaryScene);
        stage.setTitle("RoboRally");
        stage.setOnCloseRequest(
                e -> {
                    e.consume();
                    appController.exit();
                });
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnCloseRequest(e -> {
            e.consume();
            appController.exit();
        });
        stage.setResizable(true);
        // Remove or comment out these lines
        // stage.setMaximized(true);
        // stage.setFullScreen(true);
        // Optionally, set a default window size
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setX((double) (Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (double) MIN_APP_WIDTH / 2);
        stage.setY(0.0);
        stage.getIcons().add(new Image("/assets/default.png"));

        stage.show();
    }

    /**
     * Method to create a new board view for the given game controller. If a board
     * view is already present, it is removed
     * before the new board view is created and added to the pane.
     */

    public void createBoardView(GameController gameController) {
        boardRoot.getChildren().clear();
        if (gameController != null) {
            BoardView boardView = new BoardView(gameController);
            boardRoot.setCenter(boardView);
        }
        stage.sizeToScene();
    }

    public void createMainMenuView() {
        MainMenuView mainMenuView = new MainMenuView(new AppController(this));
        boardRoot.getChildren().clear();
        boardRoot.setCenter(mainMenuView);
    }

    @Override
    public void stop() throws Exception {
        // Clear the current view and set the main menu view
        createMainMenuView();
        super.stop();

        // XXX just in case we need to do something here eventually;
        // but right now the only way for the user to exit the app
        // is delegated to the exit() method in the AppController,
        // so that the AppController can take care of that.
    }

    /**
     * Main method to start the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
