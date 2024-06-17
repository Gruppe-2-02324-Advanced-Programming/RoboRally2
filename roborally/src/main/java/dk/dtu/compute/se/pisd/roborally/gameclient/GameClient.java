package dk.dtu.compute.se.pisd.roborally.gameclient;

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

@Component
public class GameClient extends JFrame {

    private final RestTemplate restTemplate;
    private String baseUrl = "http://localhost:8080/games";

    public GameClient() {
        this.restTemplate = new RestTemplate();
        initializeUI();
    }

    public void updateBaseUrl(String ip) {
        baseUrl = "http://" + ip + ":8080/games";
    }

    public void updatePlayerCards(Long gameId, Long playerId, List<String> newCards) {
        String url = baseUrl + "/" + gameId + "/player/" + playerId + "/cards";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<String>> request = new HttpEntity<>(newCards, headers);

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JOptionPane.showMessageDialog(this, "Player's cards updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to update player's cards. Status code: " + response.getStatusCode());
        }
    }

    public List<String> getPlayerCards(Long gameId, Long playerId) {
        String url = baseUrl + "/" + gameId + "/player/" + playerId + "/cards";
        ResponseEntity<String[]> response = restTemplate.getForEntity(url, String[].class);
        return response.getBody() != null ? Arrays.asList(response.getBody()) : null;
    }

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

    public Long addPlayer(Long gameId, String playerName) {
        String url = baseUrl + "/" + gameId + "/addPlayer?playerName=" + playerName;
        ResponseEntity<Long> response = restTemplate.postForEntity(url, null, Long.class);

        return response.getBody();

    }

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
