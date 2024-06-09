package dk.dtu.compute.se.pisd.server;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Board board() {
        return new Board(8, 8, "boardName"); // replace with your own Board initialization logic
    }


    @Bean
    public RoboRally roboRally() {
        return new RoboRally(); // replace with your own RoboRally initialization logic
    }
}