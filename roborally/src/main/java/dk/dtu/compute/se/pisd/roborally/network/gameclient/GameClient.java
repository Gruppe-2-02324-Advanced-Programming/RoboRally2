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
 * This class represents a game client that can update and retrieve player cards
 *
 * @author Marcus s214942
 */
@Component
public class GameClient extends JFrame {

    private final RestTemplate restTemplate;
    private String baseUrl = "http://192.168.68.102:8080/games";


    /**
     * Constructor for the game client
     *
     * @author Marcus s214942
     */
    public GameClient() {
        this.restTemplate = new RestTemplate();
        initializeUI();
    }

    /**
     * Method to update the base url of the game client
     *
     * @param ip the ip address of the server
     * @author Marcus s214942
     */
    public void updateBaseUrl(String ip) {
        baseUrl = "http://" + ip + ":8080/games";
    }

    public void updatePlayerCards(Long gameId, Long playerId, List<String> newCards) {
        String url = baseUrl + "/" + gameId + "/player/" + playerId + "/cards";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<String>> request = new HttpEntity<>(newCards, headers);

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);


/*
        //udkommenteret kode virker ikke på min (Christoffer) og er ikke nødvendig for at programmet kører
        if (response.getStatusCode().is2xxSuccessful()) {
            JOptionPane.showMessageDialog(this, "Player's cards updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to update player's cards. Status code: " + response.getStatusCode());
        }
 */
    }


    /**
     * Method to retrieve the cards of a player in a game
     *
     * @author Marcus s214942
     */
    public List<String> getPlayerCards(Long gameId, Long playerId) {
        String url = baseUrl + "/" + gameId + "/player/" + playerId + "/cards";
        ResponseEntity<String[]> response = restTemplate.getForEntity(url, String[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : null;
    }


    /**
     * Method to initialize the user interface of the game client
     *
     * @author Marcus s214942
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


        /**
         * Method to retrieve the cards of a player in a game
         * @author Marcus s214942
         */
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
     * Main method to test the game client
     *
     * @author Marcus s214942
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameClient client = new GameClient();
                client.setVisible(true);
            }
        });
    }
}
