package dk.dtu.compute.se.pisd.roborally.model;

import org.jetbrains.annotations.NotNull;

/**
  * A class for the decks in the game
  * @author jacob, s164958
  */
public class Deck {
    private CommandCard[] cards;

    public Deck(CommandCard[] cards) {
        this.cards = cards;
    }

    public CommandCard[] getCards() {
        return cards;
    }

    public CommandCard getCard(int index) {
        return cards[index];
    }

    public int size() {
        return cards.length;
    }

    public void empty() {
        cards = new CommandCard[0];
    }
    public void shuffle() {
        for (int i = 0; i < cards.length; i++) {
            int j = (int) (Math.random() * cards.length);
            CommandCard temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
    }

    public Deck resuffle(Deck deck) {
        Deck newDeck = new Deck(deck.getCards());
        newDeck.shuffle();
        deck.empty();
        return newDeck;
    }

    public CommandCard draw(Deck deck, Deck feedDeck) {
        if (deck.size() == 0) {
            deck = resuffle(feedDeck);
        }
        CommandCard card = deck.getCard(0);
        CommandCard[] newDeck = new CommandCard[deck.size() - 1];
        for (int i = 0; i < newDeck.length; i++) {
            newDeck[i] = deck.getCard(i + 1);
        }
        return card;
    }
}
