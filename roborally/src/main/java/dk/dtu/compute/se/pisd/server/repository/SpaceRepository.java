package dk.dtu.compute.se.pisd.server.repository;

import dk.dtu.compute.se.pisd.server.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceRepository extends JpaRepository<Space, Integer> {
}
