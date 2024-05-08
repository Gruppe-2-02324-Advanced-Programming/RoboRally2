/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {
    /**
     * @author Christoffer s205449
     * Board names and width x height
     */
    private static final String BOARDSFOLDER = "boards";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String[] BOARDS = new String[]{"defaultboard","circleJerk","Wooooow"};
    public static final String JSON_EXT = "json";
    public static final int BOARD_WIDTH = 16;
    public static final int BOARD_HEIGHT = 8;
    public static final String SAVED_GAMES_FOLDER = "roborally/src/main/resources/savedGames";


    public static List<String> getBoards() {
        List<String> boards = new ArrayList<>();
        Collections.addAll(boards, BOARDS);
        return boards;
    }

    public static void saveCurrentGame(Board board, String name){
        String filename = SAVED_GAMES_FOLDER + File.separator + name + "." + JSON_EXT;
        GsonBuilder builder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation() // This will only include fields marked with @Expose
                .setPrettyPrinting();
        Gson gson = builder.create();

        try (FileWriter fileWriter = new FileWriter(filename);
             JsonWriter writer = gson.newJsonWriter(fileWriter)) {
            gson.toJson(board, Board.class, writer);
        } catch (IOException e) {
            System.out.println(e);
        }

    }


    public static Board loadBoard(String boardname) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(BOARDSFOLDER + "/" + boardname + "." + JSON_EXT);
        if (inputStream == null) {
            return new Board(BOARD_WIDTH, BOARD_HEIGHT);
        }

        // In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();

        Board result;
        // FileReader fileReader = null;
        JsonReader reader = null;
        try {
            // fileReader = new FileReader(filename);
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);

            result = new Board(template.width, template.height, boardname);
            result.setTotalCheckpoints(template.totalCheckpoints);
            for (SpaceTemplate spaceTemplate : template.spaces) {
                Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
                if (space != null) {
                    space.getActions().addAll(spaceTemplate.actions);
                    space.getWalls().addAll(spaceTemplate.walls);
                }
            }
            reader.close();
            return result;
        } catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                }
            }
        }
        return null;


    }

    public static Board loadActiveBoard(String activeGameName) {
        if (activeGameName == null || activeGameName.isEmpty()) {
            System.out.println("Invalid game name.");
            return null;
        }

        File file = new File(SAVED_GAMES_FOLDER + File.separator + activeGameName + "." + JSON_EXT);
        if (!file.exists()) {
            System.out.println("No saved game found with the name: " + activeGameName);
            return null; // Optionally, handle error more gracefully
        }

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = builder.create();

        try (JsonReader reader = gson.newJsonReader(new FileReader(file))) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            int width = jsonObject.get("width").getAsInt();
            int height = jsonObject.get("height").getAsInt();
            Board board = new Board(width, height);
            board.setTotalCheckpoints(jsonObject.get("totalCheckpoints").getAsInt());

            // Load players
            Type playerListType = new TypeToken<List<Player>>() {
            }.getType();
            List<Player> players = gson.fromJson(jsonObject.getAsJsonArray("players"), playerListType);
            for (Player player : players) {
                board.addPlayer(player);
            }

            // Set the current player based on the 'current' field in JSON
            JsonObject currentPlayerJson = jsonObject.getAsJsonObject("current");
            for (Player player : players) {
                if (player.getName().equals(currentPlayerJson.get("name").getAsString())) {
                    board.setCurrentPlayer(player);
                    break;
                }
            }

            // Set the game phase and other states
            board.setPhase(Phase.valueOf(jsonObject.get("phase").getAsString()));
            board.setStep(jsonObject.get("step").getAsInt());
            board.setStepMode(jsonObject.get("stepMode").getAsBoolean());
            board.setWon(jsonObject.get("won").getAsBoolean());

            // Link players to their respective spaces on the board
            JsonArray spaces = jsonObject.getAsJsonArray("spaces");
            for (JsonElement row : spaces) {
                for (JsonElement spaceJson : row.getAsJsonArray()) {
                    JsonObject spaceObj = spaceJson.getAsJsonObject();
                    int x = spaceObj.get("x").getAsInt();
                    int y = spaceObj.get("y").getAsInt();
                    Space space = board.getSpace(x, y);
                    if (spaceObj.has("player")) {
                        JsonObject playerJson = spaceObj.getAsJsonObject("player");
                        String playerName = playerJson.get("name").getAsString();
                        for (Player player : players) {
                            if (player.getName().equals(playerName)) {
                                space.setPlayer(player);
                                player.setSpace(space);
                                break;
                            }
                        }
                    }
                }
            }

            System.out.println("Game loaded successfully.");
            return board;
        } catch (IOException e) {
            System.out.println("Failed to load the game: " + e.getMessage());
            return null;
        } catch (JsonSyntaxException e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
            return null;
        }
    }




}
