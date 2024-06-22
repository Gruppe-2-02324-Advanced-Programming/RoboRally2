package dk.dtu.compute.se.pisd.server.repo;

import dk.dtu.compute.se.pisd.server.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
