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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import java.nio.file.Files;

/**
 * A class to load a board from a file. The board is stored in a JSON file.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {
    /**
     * @author Christoffer s205449
     *         <p>
     *         This class is used to load a board from a file. The board is stored
     *         in a JSON file.
     *         The class also contains methods to save the current game and load a
     *         saved game still in todo.
     */
    private static final String BOARDSFOLDER = "boards";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String[] BOARDS = new String[] { "defaultboard", "circleJerk", "Wooooow" };
    public static final String JSON_EXT = "json";
    public static final int BOARD_WIDTH = 16;
    public static final int BOARD_HEIGHT = 8;
    public static final String SAVED_GAMES_FOLDER = "roborally/src/main/resources/savedGames";

    public static List<String> getBoards() {
        List<String> boards = new ArrayList<>();
        Collections.addAll(boards, BOARDS);
        return boards;
    }

    /**
     * Loads a board from a file. The board is stored in a JSON file.
     *
     */
    public static void saveCurrentGame(Board board, String name) {
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

    /**
     * Loads a board from a file. The board is stored in a JSON file.
     *
     */
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
        GsonBuilder simpleBuilder = new GsonBuilder().registerTypeAdapter(FieldAction.class,
                new Adapter<FieldAction>());
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

    public static Board loadGame(String gameName) {
        String filename = SAVED_GAMES_FOLDER + File.separator + gameName + "." + JSON_EXT;

        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>())
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting();
        Gson gson = builder.create();

        try (FileReader fileReader = new FileReader(filename);
                JsonReader reader = gson.newJsonReader(fileReader)) {
            return gson.fromJson(reader, Board.class);
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public static JSONObject loadJSON(String gameName) {
        String filePath = SAVED_GAMES_FOLDER + File.separator + gameName + "." + JSON_EXT;
        File file = new File(filePath);

        // Check if the file exists
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return null;
        }

        // Read the file content
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.println("Error reading the file: " + filePath);
            e.printStackTrace();
            return null;
        }

        // Parse the JSON content
        try {
            JSONObject jsonObject = new JSONObject(content);
            return jsonObject;
        } catch (org.json.JSONException e) {
            System.err.println("Error parsing JSON content from the file: " + filePath);
            e.printStackTrace();
            return null;
        }
    }

}
