package dk.dtu.compute.se.pisd.server;

import dk.dtu.compute.se.pisd.server.view.IpAddressWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the RoborallyApplication.
 * This class contains the main method which starts the Spring Boot application.
 * 
 * @author Marcus Jagd Hansen, s214962
 */
@SpringBootApplication
public class RoborallyApplication {

	/**
	 * The main method which serves as the entry point for the Spring Boot
	 * application.
	 * It runs the application and opens the IP address window.
	 * 
	 * @param args Command line arguments passed to the application.
	 * @author Marcus Jagd Hansen, s214962
	 */
	public static void main(String[] args) {
		SpringApplication.run(RoborallyApplication.class, args);
		IpAddressWindow.openWindow();
	}

}
