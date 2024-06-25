package dk.dtu.compute.se.pisd.server.view;

import dk.dtu.compute.se.pisd.server.network.NetworkUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Class representing a window that displays the IP address.
 * This class extends the JavaFX Application class and displays a window with
 * the IP address.
 * 
 * @author Marcus Jagd Hansen, s214962
 */
public class IpAddressWindow extends Application {

    /**
     * Starts the JavaFX application and displays the IP address window.
     * 
     * @param primaryStage The primary stage for this application, onto which the
     *                     application scene can be set.
     * @author Marcus Jagd Hansen, s214962
     */
    @Override
    public void start(Stage primaryStage) {
        String ipAddress = NetworkUtils.getIpAddress();

        Label label = new Label("IP Address: " + ipAddress);
        VBox vbox = new VBox(label);
        Scene scene = new Scene(vbox, 300, 100);

        primaryStage.setScene(scene);
        primaryStage.setTitle("IP Address");
        primaryStage.show();
    }

    /**
     * Opens the IP address window by launching the JavaFX application.
     * 
     * @author Marcus Jagd Hansen, s214962
     */
    public static void openWindow() {
        launch();
    }
}
