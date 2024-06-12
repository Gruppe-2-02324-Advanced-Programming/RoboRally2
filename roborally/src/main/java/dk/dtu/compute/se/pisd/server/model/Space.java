package dk.dtu.compute.se.pisd.server.model;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Heading> walls = new ArrayList<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<FieldAction> actions = new ArrayList<>();

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer && (player == null || board == player.getBoard())) {
            this.player = player;
            if (oldPlayer != null) {
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
        }
    }

    public void addWall(Heading wall) {
        if (!walls.contains(wall)) {
            walls.add(wall);
        }
    }

    public void addAction(FieldAction action) {
        if (!actions.contains(action)) {
            actions.add(action);
        }
    }

    public <T extends FieldAction> T findAction(Class<T> actionName) {
        for (FieldAction action : actions) {
            if (actionName.isInstance(action)) {
                return actionName.cast(action);
            }
        }
        return null;
    }
}
