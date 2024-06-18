package dk.dtu.compute.se.pisd.roborally.model;

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

    public void shuffle() {
        for (int i = 0; i < cards.length; i++) {
            int j = (int) (Math.random() * cards.length);
            CommandCard temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
    }

    public void resuffle(Deck deck) {
        CommandCard[] newCards = new CommandCard[cards.length + deck.size()];
        for (int i = 0; i < cards.length; i++) {
            newCards[i] = cards[i];
        }
        for (int i = 0; i < deck.size(); i++) {
            newCards[i + cards.length] = deck.getCard(i);
        }
        cards = newCards;
        shuffle();
    }

    public CommandCard drawCard(int index) {
        CommandCard card = cards[index];
        CommandCard[] newCards = new CommandCard[cards.length - 1];
        for (int i = 0; i < newCards.length; i++) {
            newCards[i] = cards[i + 1];
        }
        cards = newCards;
        return card;
    }
}
