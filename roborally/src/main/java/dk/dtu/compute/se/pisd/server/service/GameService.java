package dk.dtu.compute.se.pisd.server.service;


import dk.dtu.compute.se.pisd.server.model.Game;
import dk.dtu.compute.se.pisd.server.model.Player;
import dk.dtu.compute.se.pisd.server.repo.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame() {
        return gameRepository.save(new Game());
    }

    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

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
