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

    /**
     * This method creates a default deck for the game.
     *
     * @return A deck
     */
    public Deck getDefaultDeck() {
        CommandCard[] cards = new CommandCard[20];
        cards[0] = new CommandCard(Command.FORWARD);
        cards[1] = new CommandCard(Command.FORWARD);
        cards[2] = new CommandCard(Command.FORWARD);
        cards[3] = new CommandCard(Command.RIGHT);
        cards[4] = new CommandCard(Command.RIGHT);
        cards[5] = new CommandCard(Command.LEFT);
        cards[6] = new CommandCard(Command.LEFT);
        cards[7] = new CommandCard(Command.FAST_FORWARD);
        cards[8] = new CommandCard(Command.FAST_FORWARD);
        cards[9] = new CommandCard(Command.FORWARD_THREE);
        cards[10] = new CommandCard(Command.FORWARD_THREE);
        cards[11] = new CommandCard(Command.BACKUP);
        cards[12] = new CommandCard(Command.BACKUP);
        cards[13] = new CommandCard(Command.LEFT);
        cards[14] = new CommandCard(Command.UTURN);
        cards[15] = new CommandCard(Command.OPTION_LEFT_RIGHT);
        cards[16] = new CommandCard(Command.FORWARD);
        cards[17] = new CommandCard(Command.RIGHT);
        cards[18] = new CommandCard(Command.AGAIN);
        cards[19] = new CommandCard(Command.POWER_UP);
        return new Deck(cards);
    }
}
