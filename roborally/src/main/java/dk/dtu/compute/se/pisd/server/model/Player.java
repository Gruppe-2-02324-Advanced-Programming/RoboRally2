package dk.dtu.compute.se.pisd.server.model;

import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer playerId;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne
    @JoinColumn(name = "space_id")
    private Space space;

    @Enumerated(EnumType.STRING)
    private Heading heading = Heading.SOUTH;

    @Transient
    private CommandCardField[] program = new CommandCardField[Player.NO_REGISTERS];

    @Transient
    private CommandCardField[] cards = new CommandCardField[Player.NO_CARDS];

    @Column
    private int checkpoints = 0;

    @Column
    private int energyCubes = 0;

    @Column
    private String robotImage;

    public static final int NO_REGISTERS = 5;
    public static final int NO_CARDS = 8;

    // Constructor
    public Player(Board board, String color, String name) {
        this.board = board;
        this.name = name;
        this.color = color;
    }

    // Methods
    public void addEnergyCube() {
        this.energyCubes++;
    }
}