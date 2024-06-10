package dk.dtu.compute.se.pisd.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
public class postState {
    public static void saveCurrentGameState(Board board, String name, String url) {
        GsonBuilder builder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation() // This will only include fields marked with @Expose
                .setPrettyPrinting();
        Gson gson = builder.create();

        String json = gson.toJson(board, Board.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(url, entity, String.class);
    }

}