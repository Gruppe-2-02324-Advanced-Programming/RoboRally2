package dk.dtu.compute.se.pisd.roborally.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
  * A class for the decks in the game
  * @author Jacob, s164958
  * @author Satare, s232629
  */
public class Deck {
    /**
     * The list of cards in the deck
     * @author Jacob, s164958
     * @author Satare, s232629
     */
    private List<CommandCard> cards;

    /**
     * The constructor for the deck
     * It initializes the list of cards to an empty list
     * @author Jacob, s164958
     * @author Satare, s232629
     */
    public Deck() {
        cards = new ArrayList<>();
    }

    /**
     * Getter for the list of cards in the deck
     * @author Jacob, s164958
     * @author Satare, s232629
     * @return The list of cards in the deck
     */
    public List<CommandCard> getCards() {
        return cards;
    }

    /**
     * Setter for the list of cards in the deck
     * @author Jacob, s164958
     * @author Satare, s232629
     * @param cards The list of cards to set the deck to
     */
    public void setCards(List<CommandCard> cards) {
        this.cards = cards;
    }

    /**
     * This method adds a card to the deck.
     * @author Jacob, s164958
     * @author Satare, s232629
     * @param card The card to add to the deck
     */
    public void addCard(CommandCard card) {
        cards.add(card);
    }

    /**
     * This method removes a card from the deck.
     * @author Jacob, s164958
     * @author Satare, s232629
     * @param card The card to remove from the deck
     */
    public void removeCard(CommandCard card) {
        cards.remove(card);
    }

    /**
     * This method shuffles the cards in the deck.
     * @author Jacob, s164958
     * @author Satare, s232629
     */
    public void shuffleCards() {
        Collections.shuffle(cards, new Random());
    }

    /**
     * This method draws a card from the deck.
     * If the deck is empty, it will shuffle the cards from the feed into the deck.
     * @author Jacob, s164958
     * @author Satare, s232629
     * @param feed The deck to draw cards from if the deck is empty
     * @return The card drawn from the deck
     */
    public CommandCard drawCard(Deck feed) {
        if (cards.isEmpty()) {
            cards = feed.getCards();
            feed.setCards(new ArrayList<>());
            shuffleCards();
        }
        CommandCard card = cards.get(0);
        cards.remove(0);
        return card;
    }

    /**
     * This method checks if the deck is empty.
     * @author Jacob, s164958
     * @author Satare, s232629
     * @return True if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * This method initializes the deck with a list of cards.
     * @author Jacob, s164958
     * @author Satare, s232629
     * @param deck The list of cards to initialize the deck with
     */
    public void initializeDeck(List<CommandCard> deck) {
        cards.clear();
        cards.addAll(deck);
    }

    /**
     * This method creates a default deck for the game.
     * @author Jacob, s164958
     * @author Satare, s232629
     * @return A deck
     */

    public static Deck createDefaultDeck() {
        Deck deck = new Deck();
        List<CommandCard> cards = new ArrayList<>();
        cards.add(new CommandCard(Command.FORWARD));
        cards.add(new CommandCard(Command.FORWARD));
        cards.add(new CommandCard(Command.FORWARD));
        cards.add(new CommandCard(Command.RIGHT));
        cards.add(new CommandCard(Command.RIGHT));
        cards.add(new CommandCard(Command.LEFT));
        cards.add(new CommandCard(Command.LEFT));
        cards.add(new CommandCard(Command.FAST_FORWARD));
        cards.add(new CommandCard(Command.FAST_FORWARD));
        cards.add(new CommandCard(Command.FORWARD_THREE));
        cards.add(new CommandCard(Command.FORWARD_THREE));
        cards.add(new CommandCard(Command.BACKUP));
        cards.add(new CommandCard(Command.BACKUP));
        cards.add(new CommandCard(Command.LEFT));
        cards.add(new CommandCard(Command.UTURN));
        cards.add(new CommandCard(Command.OPTION_LEFT_RIGHT));
        cards.add(new CommandCard(Command.FORWARD));
        cards.add(new CommandCard(Command.RIGHT));
        cards.add(new CommandCard(Command.AGAIN));
        cards.add(new CommandCard(Command.POWER_UP));
        //cards.add(new CommandCard(Command.SPAM));
        deck.initializeDeck(cards);
        deck.shuffleCards();
        return deck;
    }
}
