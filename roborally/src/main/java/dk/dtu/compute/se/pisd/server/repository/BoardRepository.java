package dk.dtu.compute.se.pisd.server.repository;

import dk.dtu.compute.se.pisd.server.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {
}
