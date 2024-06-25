package dk.dtu.compute.se.pisd.server.service;

import dk.dtu.compute.se.pisd.server.model.Game;
import dk.dtu.compute.se.pisd.server.model.Player;
import dk.dtu.compute.se.pisd.server.repo.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing game-related operations.
 * This class provides methods to create games, retrieve game information,
 * update player cards, and add players to games.
 * 
 * @author Marcus Jagd Hansen, s214962
 */
@Service
public class GameService {

    private final GameRepository gameRepository;

    /**
     * Constructor for GameService.
     * 
     * @param gameRepository The repository used for game persistence.
     * @author Marcus Jagd Hansen, s214962
     */
    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Creates a new game and saves it in the repository.
     * 
     * @return The created Game object.
     * @author Marcus Jagd Hansen, s214962
     */
    public Game createGame() {
        return gameRepository.save(new Game());
    }

    /**
     * Retrieves a game by its ID.
     * 
     * @param id The ID of the game to retrieve.
     * @return An Optional containing the Game if found, or empty if not found.
     * @author Marcus Jagd Hansen, s214962
     */
    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

    /**
     * Retrieves the cards of a player in a specific game.
     * 
     * @param gameId   The ID of the game.
     * @param playerId The ID of the player.
     * @return An Optional containing a list of card names if found, or empty if not
     *         found.
     * @author Marcus Jagd Hansen, s214962
     */
    public Optional<List<String>> getPlayerCards(Long gameId, Long playerId) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            for (Player player : game.getPlayers()) {
                if (player.getId().equals(playerId)) {
                    return Optional.of(player.getCards());
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Updates the cards of a player in a specific game.
     * 
     * @param gameId   The ID of the game.
     * @param playerId The ID of the player.
     * @param newCards A list of new cards to assign to the player.
     * @return An Optional containing the updated Player if successful, or empty if
     *         not found.
     * @author Marcus Jagd Hansen, s214962
     */
    public Optional<Player> updatePlayerCards(Long gameId, Long playerId, List<String> newCards) {
        Optional<Game> gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            for (Player player : game.getPlayers()) {
                if (player.getId().equals(playerId)) {
                    player.setCards(newCards);
                    gameRepository.save(game);
                    return Optional.of(player);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Adds a player with default cards to a specific game.
     * 
     * @param gameId     The ID of the game.
     * @param playerName The name of the player to add.
     * @return The added Player object.
     * @throws RuntimeException if the player is not saved properly.
     * @author Marcus Jagd Hansen, s214962
     */
    public Player addPlayerWithDefaultCards(Long gameId, String playerName) {
        Game game = waitForGame(gameId);
        Player player = new Player();
        player.setName(playerName);
        player.setCards(Arrays.asList("card1", "card2", "card3", "card4", "card5"));

        game.getPlayers().add(player);
        gameRepository.save(game);

        Optional<Player> savedPlayer = game.getPlayers().stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst();

        System.out.println("Service (after adding to game): " + player.getId());
        return savedPlayer.orElseThrow(() -> new RuntimeException("Player not saved properly"));
    }

    /**
     * Waits for a game with the specified ID to be available.
     * Continuously checks the repository until the game is found.
     * 
     * @param gameId The ID of the game to wait for.
     * @return The found Game object.
     * @author Marcus Jagd Hansen, s214962
     */
    private Game waitForGame(Long gameId) {
        while (true) {
            Optional<Game> gameOptional = gameRepository.findById(gameId);
            if (gameOptional.isPresent()) {
                return gameOptional.get();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
