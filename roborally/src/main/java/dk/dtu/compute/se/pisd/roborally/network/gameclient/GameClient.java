package dk.dtu.compute.se.pisd.roborally.network.gameclient;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a game client that can update and retrieve player
 * cards.
 * 
 * @author Marcus Jagd Hansen, s214942
 */
@Component
public class GameClient extends JFrame {

    private final RestTemplate restTemplate;
    private String baseUrl = "http://localhost:8080/games";

    /**
     * Constructor for the game client.
     * Initializes the RestTemplate and user interface.
     * 
     * @author Marcus Jagd Hansen, s214942
     */
    public GameClient(RestTemplate restTemplateMock) {
        this.restTemplate = new RestTemplate();
        initializeUI();
    }

    /**
     * Method to update the base URL of the game client.
     * 
     * @param ip the IP address of the server
     * @author Marcus Jagd Hansen, s214942
     */
    public void updateBaseUrl(String ip) {
        baseUrl = "http://" + ip + ":8080/games";
    }

    /**
     * Method to update the cards of a player in a specific game.
     * 
     * @param gameId   The ID of the game
     * @param playerId The ID of the player
     * @param newCards A list of new cards to assign to the player
     * @author Marcus Jagd Hansen, s214942
     */
    public void updatePlayerCards(Long gameId, Long playerId, List<String> newCards) {
        String url = baseUrl + "/" + gameId + "/player/" + playerId + "/cards";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<String>> request = new HttpEntity<>(newCards, headers);

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);

        // Uncomment if you want to show messages in the UI based on the response
        /*
         * if (response.getStatusCode().is2xxSuccessful()) {
         * JOptionPane.showMessageDialog(this, "Player's cards updated successfully.");
         * } else {
         * JOptionPane.showMessageDialog(this,
         * "Failed to update player's cards. Status code: " + response.getStatusCode());
         * }
         */
    }

    /**
     * Method to retrieve the cards of a player in a game.
     * 
     * @param gameId   The ID of the game
     * @param playerId The ID of the player
     * @return A list of card names if found, or null if not found
     * @author Marcus Jagd Hansen, s214942
     */
    public List<String> getPlayerCards(Long gameId, Long playerId) {
        String url = baseUrl + "/" + gameId + "/player/" + playerId + "/cards";
        ResponseEntity<String[]> response = restTemplate.getForEntity(url, String[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : null;
    }

    /**
     * Method to initialize the user interface of the game client.
     * 
     * @author Marcus Jagd Hansen, s214942
     */
    private void initializeUI() {
        setTitle("Game Client");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton updateButton = new JButton("Update Player Cards");
        JButton getButton = new JButton("Get Player Cards");

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> newCards = Arrays.asList("NewCard1", "NewCard2", "NewCard3", "NewCard4", "NewCard5");
                updatePlayerCards(1L, 1L, newCards);
            }
        });

        getButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> playerCards = getPlayerCards(1L, 1L);
                if (playerCards != null) {
                    JOptionPane.showMessageDialog(GameClient.this, "Player's cards: " + playerCards);
                } else {
                    JOptionPane.showMessageDialog(GameClient.this, "Failed to retrieve player's cards.");
                }
            }
        });

        setLayout(new FlowLayout());
        add(updateButton);
        add(getButton);
    }

    /**
     * Method to create a new game.
     * 
     * @return The ID of the created game, or null if the creation fails
     * @author Marcus Jagd Hansen, s214942
     */
    public Long createGame() {
        String url = baseUrl + "/createGame";
        ResponseEntity<Long> response = restTemplate.postForEntity(url, null, Long.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to create game. Status code: " + response.getStatusCode());
            return null;
        }
    }

    /**
     * Method to add a player to a specific game.
     * 
     * @param gameId     The ID of the game
     * @param playerName The name of the player to add
     * @return The ID of the added player
     * @author Marcus Jagd Hansen, s214942
     */
    public Long addPlayer(Long gameId, String playerName) {
        String url = baseUrl + "/" + gameId + "/addPlayer?playerName=" + playerName;
        ResponseEntity<Long> response = restTemplate.postForEntity(url, null, Long.class);
        return response.getBody();
    }


}
