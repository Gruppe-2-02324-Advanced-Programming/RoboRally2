package dk.dtu.compute.se.pisd.server.view;

import dk.dtu.compute.se.pisd.server.util.NetworkUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IpAddressWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        String ipAddress = NetworkUtil.getIpAddress();

        Label label = new Label("IP Address: " + ipAddress);
        VBox vbox = new VBox(label);
        Scene scene = new Scene(vbox, 300, 100);

        primaryStage.setScene(scene);
        primaryStage.setTitle("IP Address");
        primaryStage.show();
    }

    public static void openWindow() {
        launch();
    }
}
