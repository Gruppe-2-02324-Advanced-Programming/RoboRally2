package dk.dtu.compute.se.pisd.server.controller;

import dk.dtu.compute.se.pisd.server.model.Game;
import dk.dtu.compute.se.pisd.server.model.Player;
import dk.dtu.compute.se.pisd.server.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing game-related operations.
 * This controller provides endpoints for creating games, retrieving game
 * information,
 * updating player cards, and adding players to games.
 * 
 * @author Marcus Jagd Hansen, s214962
 */
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    /**
     * Constructor for GameController.
     * 
     * @param gameService The service used for game-related operations.
     * @author Marcus Jagd Hansen, s214962
     */
    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Endpoint to create a new game.
     * 
     * @return A ResponseEntity containing the ID of the created game.
     * @author Marcus Jagd Hansen, s214962
     */
    @PostMapping("/createGame")
    public ResponseEntity<Long> createGame() {
        Game savedGame = gameService.createGame();
        return ResponseEntity.ok(savedGame.getId());
    }

    /**
     * Endpoint to retrieve a game by its ID.
     * 
     * @param id The ID of the game to retrieve.
     * @return A ResponseEntity containing the Game if found, or a not found status
     *         if not found.
     * @author Marcus Jagd Hansen, s214962
     */
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        Optional<Game> game = gameService.getGameById(id);
        return game.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to retrieve the cards of a player in a specific game.
     * 
     * @param gameId   The ID of the game.
     * @param playerId The ID of the player.
     * @return A ResponseEntity containing a list of card names if found, or a not
     *         found status if not found.
     * @author Marcus Jagd Hansen, s214962
     */
    @GetMapping("/{gameId}/player/{playerId}/cards")
    public ResponseEntity<List<String>> getPlayerCards(@PathVariable Long gameId, @PathVariable Long playerId) {
        Optional<List<String>> cards = gameService.getPlayerCards(gameId, playerId);
        return cards.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to update the cards of a player in a specific game.
     * 
     * @param gameId   The ID of the game.
     * @param playerId The ID of the player.
     * @param newCards A list of new cards to assign to the player.
     * @return A ResponseEntity containing the updated Player if successful, or a
     *         not found status if not found.
     * @author Marcus Jagd Hansen, s214962
     */
    @PutMapping("/{gameId}/player/{playerId}/cards")
    public ResponseEntity<Player> updatePlayerCards(@PathVariable Long gameId, @PathVariable Long playerId,
            @RequestBody List<String> newCards) {
        Optional<Player> player = gameService.updatePlayerCards(gameId, playerId, newCards);
        return player.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to add a player with default cards to a specific game.
     * 
     * @param gameId     The ID of the game.
     * @param playerName The name of the player to add.
     * @return A ResponseEntity containing the ID of the added player.
     * @author Marcus Jagd Hansen, s214962
     */
    @PostMapping("/{gameId}/addPlayer")
    public ResponseEntity<Long> addPlayerWithDefaultCards(@PathVariable Long gameId, @RequestParam String playerName) {
        Player player = gameService.addPlayerWithDefaultCards(gameId, playerName);
        System.out.println("GameController: " + player.getId());
        return ResponseEntity.ok(player.getId());
    }
}
