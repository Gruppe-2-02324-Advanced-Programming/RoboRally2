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
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.network.gameclient.GameClient;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Controller for managing the game logic of RoboRally. It handles player
 * movements,
 * command execution, and the transition between different phases of the game.
 * <p>
 * The controller is responsible for starting the programming phase, executing
 * programs,
 * and moving players on the board based on their chosen command cards.
 * <p>
 * Usage:
 * 
 * <pre>{@code
 * Board board = new Board(8, 8);
 * GameController controller = new GameController(board);
 * controller.startProgrammingPhase();
 * // ...
 * }</pre>
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Emily, s191174
 * @author Jacob, s164958
 * @author Setare, s232629
 * @author Phillip, s224278
 */

@RestController
public class GameController {

    public Board board;

    public GameClient gameClient;

    public int currentTabIndex;

    public int playerNumber;

    private int timer;
    private boolean[] playersReady;
    private int remainingTime;
    private ScheduledExecutorService scheduler;

    public int curPlayerNo;

    public String playerName;
    public boolean isSinglePlayerMode;

    /**
     * Constructor for the GameController.
     *
     * @param board the board to which the controller is connected
     * @author Ekkart Kindler
     */
    public GameController(@NotNull Board board) {
        this.playerName = "NULL";
        this.board = board;
        currentTabIndex = 0;
        playerNumber = 1;
        playersReady = new boolean[board.getPlayersNumber()];
        timer = 180; // Default value for timer
        remainingTime = timer;
        isSinglePlayerMode = true;
    }

    /**
     * Getter for the timer.
     *
     * @return the timer value
     */
    public int getTimer() {
        return timer;
    }

    /**
     *
     * @author Christoffer s205449
     * @param timer
     */
    public void setTimer(int timer) {
        this.timer = timer;
        this.remainingTime = timer;
    }

    /**
     * Getter for the remaining time.
     * 
     * @author Christoffer s205449
     * @return the remaining time value
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see
     * something
     * happening on the board. This method should eventually be deleted!
     * 
     * @author Ekkart Kindler
     * @param space the space to which the current player should move
     */
    /*public void moveCurrentPlayerToSpace(@NotNull Space space) {
        Player currentPlayer = board.getCurrentPlayer();
        if (space.getPlayer() == null)
            currentPlayer.setSpace(space);
        else
            return;

        int currentPlayerNumber = board.getPlayerNumber(currentPlayer);
        Player nextPlayer = board.getPlayer((currentPlayerNumber + 1) % board.getPlayersNumber());
        board.setCurrentPlayer(nextPlayer);

        board.setCounter(board.getCounter() + 1);
    }
*/
    /**
     * This method starts the programming phase of the game. It sets the phase to
     * PROGRAMMING,
     * sets the current player to the first player, and sets the step to 0.
     * <p>
     * It also sets the program fields of each player to be empty and fills the hand of the players
     * with command cards from their decks.
     * <p>
     * The method is called at the beginning of the game and after each activation
     * phase.
     *
     * @author Ekkart Kindler
     * @author Christoffer, s205449
     * @auhtor Jacob, s164958
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.Programming);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(player.getDrawpile().drawCard(player.getDiscardpile()));
                    // field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }

        remainingTime = timer;
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateTimer, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * This method generates a random command card.
     * 
     * @author Ekkart Kindler
     * @return a random command card
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * This method ends the programming phase, which makes the execute button active
     * to press.
     * 
     * @author Ekkart Kindler
     * @author Christoffer s205449
     */
    public synchronized void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.Activation);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
        setTimer(180);
        executePrograms();
    }

    /**
     * This method makes the program fields of the players visible for the given
     * register.
     * 
     * @author Ekkart Kindler
     * @param register the register for which the program fields should be made
     *                 visible
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    /**
     * This method makes the program fields of the players invisible. This is used
     * to hide the program
     * fields after the programming phase has ended.
     */
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * This method executes the moves which the player has requested
     */
    public void executePrograms() {
        board.setStepMode(false);
        executeNextStepWithDelay();
    }

    /**
     * This method executes the moves which the player has requested in step mode
     * 
     * @author Christoffer, s205449
     */
    private void executeNextStepWithDelay() {
        if (board.getPhase() == Phase.Activation && !board.isStepMode()) {
            executeNextStep();
            // Schedule the next step after a delay (e.g., 1 second)
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(() -> executeNextStepWithDelay(), 1, TimeUnit.SECONDS);
            executor.shutdown();
        }
    }

    /**
     * This method executes the moves which the player has requested in step mode
     */
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    /**
     * This method continues the execution of the programs of the players. It
     * executes the next step
     * of the current player until the phase is not ACTIVATION or the step mode is
     * not set.
     */
    private void continuePrograms() {
        do {
            // executeNextStep();
            executeNextStepWithDelay();
        } while (board.getPhase() == Phase.Activation && !board.isStepMode());
    }

    /**
     * This method executes the next step of the current player. If the phase is
     * ACTIVATION, the next
     * step of the current player is executed. If the phase is not ACTIVATION, the
     * method does nothing.
     * If the step is the last step of the current player, the method starts the
     * programming phase.
     * 
     * @author Emily, s191174
     */
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.Activation && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    if (command.isInteractive()) {
                        board.setPhase(Phase.Player_interaction);
                        return;
                    }
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    for (Player player : board.getPlayers()) {
                        List<FieldAction> actions = player.getSpace().getActions();
                        if (actions != null) {
                            for (FieldAction action : actions) {
                                action.doAction(this, player.getSpace());
                            }

                        }
                    }
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));

                    } else {
                        cleanup();
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    /**
     * This method executes the given command for the specified player.
     * 
     * @author Christoffer, s205449
     */
    public void executeCommandOptionAndContinue(@NotNull Command option) {
        Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer != null &&
                board.getPhase() == Phase.Player_interaction &&
                option != null)
            ;
        board.setPhase(Phase.Activation);
        executeCommand(currentPlayer, option);

        int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
        if (nextPlayerNumber < board.getPlayersNumber()) {
            board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
        } else {
            int step = board.getStep() + 1;
            for (Player player : board.getPlayers()) {
                List<FieldAction> actions = player.getSpace().getActions();
                if (actions != null) {
                    for (FieldAction action : actions) {
                        action.doAction(this, player.getSpace());
                    }
                }

            }
            if (step < Player.NO_REGISTERS) {
                makeProgramFieldsVisible(step);
                board.setStep(step);
                board.setCurrentPlayer(board.getPlayer(0));
            } else {
                startProgrammingPhase();
            }
        }
    }

    /**
     * Executes the given command for the specified player. If the command is
     * POWER_UP,
     * the player will receive one energy cube.
     *
     * @param player  the player to whom the command will apply
     * @param command the command to be executed
     * @author Emily, s191174
     * @author Jacob, s164958
     * @author Setare, s232629
     * @author Phillip, s224278
     */
    // XXX: V2
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            // their execution. This should eventually be done in a more elegant way
            // (this concerns the way cards are modelled as well as the way they are
            // executed).

            switch (command) {
                case FORWARD_THREE:
                    this.moveThree(player);
                    break;
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                case UTURN:
                    this.uTurn(player);
                    break;
                case BACKUP:
                    this.backUp(player);
                    break;
                case AGAIN:
                    this.again(player);
                    break;
                case OPTION_LEFT_RIGHT:
                    this.leftOrRight(player, command);
                    break;
                case POWER_UP:
                    player.addEnergyCube();
                    break;
                case SPAM:
                    this.spam(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * This exception is thrown when a player tries to move to a space that is not
     * possible to move to.
     */

    public static class moveNotPossibleException extends Exception {

        public Space space;

        public Heading heading;

        public Player player;

        /**
         * Here we create the Exception moveIsNotPossible, but for now, nothing happens
         * when thrown
         *
         * @param player  the player that is trying to move
         * @param space   the space the player is trying to move to
         * @param heading the direction the player is trying to move
         */

        public moveNotPossibleException(Player player, Space space, Heading heading) {
            super("Move is not possible");

            this.heading = heading;

            this.space = space;

            this.player = player;
        }
    }

    /**
     * @param player the player to move forward
     * @author Christoffer, s205449
     *         <p>
     *         <p>
     *         The moveForward has been slightly modified with a catch statement at
     *         the bottom, however it has been set to be ignored since it doesn't do
     *         anything
     *         Moves a player one space forward in the direction they are currently
     *         facing.
     *         If the movement is not possible (e.g., due to a wall), the action is
     *         ignored.
     */

    public void moveForward(Player player) {
        if (board != null && player != null && player.board == board) {
            Heading heading = player.getHeading();
            Space space = player.getSpace();
            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                try {
                    movePlayerToSpace(player, target, heading);
                } catch (moveNotPossibleException ignored) {
                }
            }
        }
    }

    /**
     * @author Christoffer, s205449
     *         Same function as moveForward, however the method is set two times to
     *         get the fastForward function
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    /**
     * Moves a player three spaces forward in the direction they are currently
     * facing.
     * This is done by calling {@code moveForward} method three times.
     *
     * @param player the player to move three spaces forward
     * @author Setare Izadi, s232629
     */
    public void moveThree(Player player) {
        moveForward(player);
        moveForward(player);
        moveForward(player);
    }

    /**
     * @param player
     * @param space
     * @param heading
     * @throws moveNotPossibleException
     * @author Christoffer, s205449
     *         <p>
     *         The movePlayerToSpace which relocates the pushed player to the next
     *         space which the pushing player is heading.
     *         If none of the criteria met the moveNotPossibleException will be
     *         thrown.
     */
    public void movePlayerToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading)
            throws moveNotPossibleException {
        Player other = space.getPlayer();
        if (other != null) {
            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                movePlayerToSpace(other, target, heading);
            } else {
                throw new moveNotPossibleException(player, space, heading);
            }
        }
        /**
         * @author Christoffer Fink 205449
         *         Does so the player can't wall through the walls
         */
        if (player.getSpace() != null) {
            if (player.getSpace().getWalls() != null) {
                for (Heading wall : player.getSpace().getWalls()) {
                    if (wall == heading) {
                        throw new moveNotPossibleException(player, space, heading);
                    }
                }
            }
        }
        if (space.getWalls() != null) {
            for (Heading wall : space.getWalls()) {
                if (wall.prev().prev() == heading) {
                    throw new moveNotPossibleException(player, space, heading);
                }
            }
        }

        player.setSpace(space);
    }

    /**
     * Here the player's direction is set to turn right
     * @author Christoffer, s205449
     */
    public void turnRight(@NotNull Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().next());
        }
    }

    /**
     * Here the player's direction is set to turn left
     *  @author Christoffer, s205449
     */
    public void turnLeft(@NotNull Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().prev());
        }
    }

    /**
     * Turns the player around, making a U-turn.
     *
     * @param player the player to turn around
     * @author Marcus Jagd Hansen, s214962
     */
    public void uTurn(@NotNull Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().next().next());
        }
    }

    /**
     * Moves the player backwards
     *
     * @param player
     * @author Phillip, s224278
     */
    public void backUp(@NotNull Player player) {
        if (player != null && player.board == board) {
            uTurn(player);
            moveForward(player);
            uTurn(player);
        }
    }

    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method called when no corresponding controller operation is implemented
     * yet. This
     * should eventually be removed.
     * @author Phillip, s224278
     */
    public void leftOrRight(Player player, Command option) {
        if (player != null && option != null && player.board.getPhase() == Phase.Player_interaction) {
            switch (option) {
                case LEFT:
                    executeCommandOptionAndContinue(Command.LEFT);
                    executeNextStepWithDelay();
                    break;
                case RIGHT:
                    executeCommandOptionAndContinue(Command.RIGHT);
                    executeNextStepWithDelay();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Sets the board of the controller.
     *
     * @param board the board to be set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Gets the board of the controller.
     *
     * @return the board of the controller
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Repeats the command card in the previous register of the player. If it is the
     * first card it does nothing,
     * if the previous card is an again card it will repeat the card before that.
     * 
     * @author Jacob, s164958
     * @param player the player to repeat the command card for
     */
    public void again(Player player) {
        if (player != null && player.board == board) {
            int prevStep = board.getStep() - 1;
            if (prevStep >= 0) {
                CommandCard card = player.getProgramField(prevStep).getCard();
                if (card != null && card.command != Command.AGAIN) {
                    Command command = card.command;
                    if (command.isInteractive()) {
                        board.setPhase(Phase.Player_interaction);
                        return;
                    }
                    executeCommand(player, command);
                }
                if (card != null && card.command == Command.AGAIN) {
                    board.setStep(prevStep);
                    again(player);
                    board.setStep(board.getStep() + 1);
                }
            }
        }
    }

    /**
     * This method draws a card from the drawpile and replace the current card with it.
     * @author Setare, s232629
     * @author Jacob, s164958
     * @param player the player to draw a card for
     */
    public void spam(Player player) {
        player.getProgramField(board.getStep()).setCard(player.getDrawpile().drawCard(player.getDiscardpile()));
    }

    /**
     * This method cleans up the program fields of the players and moves the cards to the discard pile.
     * @author Jacob, s164958
     * @author Setare, s232629
     */
    public void cleanup() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    if (field.getCard() != null) {
                        player.getDiscardpile().addCard(field.getCard());
                    }
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    if (field.getCard() != null) {
                        player.getDiscardpile().addCard(field.getCard());
                    }
                    field.setCard(null);
                    field.setVisible(true);
                }
            }
        }
    }


    /**
     * Changes the current tab index to the new index.
     *
     * @param newIndex the new tab index to set
     * @author Marcus Jagd Hansen, s214962
     */
    public void changeCurrentTabIndex(int newIndex) {
        currentTabIndex = newIndex;
    }

    /**
     * Pushes the cards of the player to the server and gets the cards of the other
     * players.
     * 
     * @author Marcus Jagd Hansen, s214962
     */
    public void getOtherPlayersCards() {
        System.out.println("Getting cards...");
        int playersListLength = board.getPlayersNumber();
        for (int i = 0; i < playersListLength; i++) {
            Long ID = (long) (i + 1);
            List<String> cards = gameClient.getPlayerCards(1L, ID);
            Player p = board.getPlayer(i);
            for (int j = 0; j < cards.size(); j++) {
                CommandCardField from = p.getCardField(j);
                CommandCardField to = p.getProgramField(j);
                Command command = Command.fromDisplayName(cards.get(j));
                to.setCard(new CommandCard(command));
                moveCards(from, to);
                System.out.println("Cards gotten");
            }
        }
    }

    /**
     * Pushes the cards of the player to the server.
     * 
     * @author Marcus Jagd Hansen, s214962
     */
    public void pushYourCards() {
        System.out.println("Pushing cards...");
        Long playerID = (long) playerNumber;
        List<String> cards = board.getProgramFields(playerNumber - 1);
        gameClient.updatePlayerCards(1L, playerID, cards);
        System.out.println("Cards pushed");
    }

    /**
     * Updates the base URL of the game client.
     *
     * @param ip the IP address of the server
     * @author Marcus Jagd Hansen, s214962
     */
    public void updateBaseUrl(String ip) {
        gameClient.updateBaseUrl(ip);
    }

    /**
     * Sets the player number.
     *
     * @param number the player number to set
     * @author Marcus Jagd Hansen, s214962
     */
    public void setPlayerNumber(int number) {
        playerNumber = number;
    }

    /**
     * Gets the player number.
     *
     * @return the player number as an integer
     * @author Marcus Jagd Hansen, s214962
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Updates the remaining time and finishes the programming phase if time is up.
     * @author Phillip, s224278
     */
    private void updateTimer() {
        if (remainingTime > 0) {
            if (board.getPhase() == Phase.Programming) {
                remainingTime--;
            } else {
            }
        } else {
            scheduler.shutdown();
            finishProgrammingPhase();
            setTimer(180);
        }
    }

    /**
     * Sets the name of the player.
     *
     * @param name the name of the player
     * @author Marcus Jagd Hansen, s214962
     */
    public void setPlayerName(String name) {
        playerName = name;
    }

    /**
     * Gets the name of the player.
     *
     * @return the name of the player as a String
     * @author Marcus Jagd Hansen, s214962
     */
    public String getPlayerName() {
        return playerName;
    }

}