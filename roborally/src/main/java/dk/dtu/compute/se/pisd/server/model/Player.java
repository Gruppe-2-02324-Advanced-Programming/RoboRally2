package dk.dtu.compute.se.pisd.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Entity class representing a player in the game.
 * This class is mapped to the "players" table in the database.
 * 
 * @author Marcus Jagd Hansen, s214962
 */
@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    /**
     * The unique identifier for the player.
     * This field is auto-generated.
     * 
     * @author Marcus Jagd Hansen, s214962
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the player.
     * 
     * @author Marcus Jagd Hansen, s214962
     */
    private String name;

    /**
     * The list of cards assigned to the player.
     * This field is mapped to the "player_cards" table with a join column
     * "player_id".
     * 
     * @author Marcus Jagd Hansen, s214962
     */
    @ElementCollection
    @CollectionTable(name = "player_cards", joinColumns = @JoinColumn(name = "player_id"))
    @Column(name = "card")
    private List<String> cards;

}
