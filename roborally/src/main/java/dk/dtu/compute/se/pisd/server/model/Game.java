package dk.dtu.compute.se.pisd.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Entity class representing a game.
 * This class is mapped to the "games" table in the database.
 * 
 * @author Marcus Jagd Hansen, s214962
 */
@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    /**
     * The unique identifier for the game.
     * This field is auto-generated.
     * 
     * @author Marcus Jagd Hansen, s214962
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The list of players in the game.
     * This field is mapped to the "players" table with a join column "game_id".
     * The players are fetched lazily and cascaded on all operations.
     * 
     * @author Marcus Jagd Hansen, s214962
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private List<Player> players;

}
