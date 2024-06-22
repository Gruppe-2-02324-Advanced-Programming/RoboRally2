package dk.dtu.compute.se.pisd.server;

import dk.dtu.compute.se.pisd.server.view.IpAddressWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class RoborallyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoborallyApplication.class, args);
		IpAddressWindow.openWindow();
	}

}
