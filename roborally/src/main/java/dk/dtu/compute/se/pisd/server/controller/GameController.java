package dk.dtu.compute.se.pisd.server.controller;


import dk.dtu.compute.se.pisd.server.model.Game;
import dk.dtu.compute.se.pisd.server.model.Player;
import dk.dtu.compute.se.pisd.server.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/createGame")
    public ResponseEntity<Long> createGame() {
        Game savedGame = gameService.createGame();
        return ResponseEntity.ok(savedGame.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        Optional<Game> game = gameService.getGameById(id);
        return game.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{gameId}/player/{playerId}/cards")
    public ResponseEntity<List<String>> getPlayerCards(@PathVariable Long gameId, @PathVariable Long playerId) {
        Optional<List<String>> cards = gameService.getPlayerCards(gameId, playerId);
        return cards.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{gameId}/player/{playerId}/cards")
    public ResponseEntity<Player> updatePlayerCards(@PathVariable Long gameId, @PathVariable Long playerId,
                                                    @RequestBody List<String> newCards) {
        Optional<Player> player = gameService.updatePlayerCards(gameId, playerId, newCards);
        return player.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{gameId}/addPlayer")
    public ResponseEntity<Long> addPlayerWithDefaultCards(@PathVariable Long gameId, @RequestParam String playerName) {
        Player player = gameService.addPlayerWithDefaultCards(gameId, playerName);
        System.out.println("gameCOntroller: " + player.getId());
        return ResponseEntity.ok(player.getId());
    }
}
