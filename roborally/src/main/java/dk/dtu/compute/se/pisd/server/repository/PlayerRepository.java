package dk.dtu.compute.se.pisd.server.repository;

import dk.dtu.compute.se.pisd.server.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {

}
