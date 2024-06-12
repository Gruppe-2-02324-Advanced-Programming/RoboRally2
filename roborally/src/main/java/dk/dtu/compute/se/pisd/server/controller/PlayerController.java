package dk.dtu.compute.se.pisd.server.controller;

import dk.dtu.compute.se.pisd.server.model.Player;
import dk.dtu.compute.se.pisd.server.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Integer id) {
        Optional<Player> player = playerService.getPlayerById(id);
        return player.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Player createPlayer(@RequestBody Player player) {
        return playerService.savePlayer(player);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Integer id, @RequestBody Player playerDetails) {
        Optional<Player> player = playerService.getPlayerById(id);
        if (player.isPresent()) {
            Player existingPlayer = player.get();
            existingPlayer.setName(playerDetails.getName());
            existingPlayer.setColor(playerDetails.getColor());
            existingPlayer.setSpace(playerDetails.getSpace());
            existingPlayer.setHeading(playerDetails.getHeading());
            existingPlayer.setCheckpoints(playerDetails.getCheckpoints());
            existingPlayer.setEnergyCubes(playerDetails.getEnergyCubes());
            existingPlayer.setRobotImage(playerDetails.getRobotImage());
            final Player updatedPlayer = playerService.savePlayer(existingPlayer);
            return ResponseEntity.ok(updatedPlayer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Integer id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
}
