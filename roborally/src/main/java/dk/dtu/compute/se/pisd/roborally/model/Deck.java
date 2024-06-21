package dk.dtu.compute.se.pisd.roborally.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
  * A class for the decks in the game
  * @author jacob, s164958
  */
public class Deck {
    private List<CommandCard> cards;

    public Deck() {
        cards = new ArrayList<>();
    }

    public List<CommandCard> getCards() {
        return cards;
    }

    public void setCards(List<CommandCard> cards) {
        this.cards = cards;
    }

    public void addCard(CommandCard card) {
        cards.add(card);
    }

    public void removeCard(CommandCard card) {
        cards.remove(card);
    }

    public void shuffleCards() {
        Collections.shuffle(cards, new Random());
    }

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

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void initializeDeck(List<CommandCard> deck) {
        cards.clear();
        cards.addAll(deck);
    }

    /**
     * This method creates a default deck for the game.
     *
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
