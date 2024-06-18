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

public class RoboRally extends Application {

    private static final int MIN_APP_WIDTH = 600;
    private Stage stage;
    private BorderPane boardRoot;
    private GameController gameController;

    @Override
    public void init() throws Exception {
        super.init();
    }

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
        stage.setOnCloseRequest(e -> {
            e.consume();
            appController.exit();
        });
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setX((double) (Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (double) MIN_APP_WIDTH / 2);
        stage.setY(0.0);
        stage.getIcons().add(new Image("file:src/main/resources/assets/default.png"));

        stage.show();
    }

    public void createBoardView(GameController gameController) {
        boardRoot.getChildren().clear();
        if (gameController != null) {
            BoardView boardView = new BoardView(gameController);
            boardRoot.setCenter(boardView);
        }
        stage.sizeToScene();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
