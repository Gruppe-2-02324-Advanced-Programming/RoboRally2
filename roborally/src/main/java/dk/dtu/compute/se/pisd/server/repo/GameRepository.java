package dk.dtu.compute.se.pisd.server.repo;

import dk.dtu.compute.se.pisd.server.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Game entities.
 * This interface extends JpaRepository to provide CRUD operations for Game
 * entities.
 * 
 * @author Marcus Jagd Hansen, s214962
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
