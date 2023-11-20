import java.util.ArrayList;
import java.util.List;

/**
 * The class represents a deck of cards.
 */
public class CardDeck {
    // The id of a deck of cards
    private int deckId;
    
    // The list that represents a list of cards in the deck
    private List<Card> cards;

//    private GameActionsLogger logger;


    /**
     * Constructor to create a new deck of cards with given ID for the deck.
     * @param deckId
     */
    public CardDeck(int deckId) {
        this.deckId = deckId;
        this.cards = new ArrayList<>();
//        logger = new GameActionsLogger();
    }

    /**
     * The method retrieves a String for initial text output.
     * @return String such as 'deck + deckID'
     */
    @Override
    public String toString() {
        return "deck " + deckId;
    }

    /**
     * The method creates String with information on the denomination of all cards in the deck.
     * @return String 'res' with information of all the cards in a deck.
     */
    private String logCards() {
        String res = "";
        for (Card card : cards) {
            res += " " + card.toString();
        }
        return res;
    }

    /**
     * The method creates a name for the .txt file with the name of a deck
     * @return String with the id of a deck.
     */
    public String logFileName() {
        return "deck" + deckId + "_output.txt";
    }

    /**
     * The method retrieves a String with with the content of a deck with cards.
     * @return String with an information of denomination of cards from logCards() method in a deck.
     */
    public String getContents() {
        return "deck" + deckId + " contents:" + logCards();
    }

    /**
     * The method represents an action in the game which is drawing a card at the top a deck.
     * Synchronized method is used to execute a chance of modifying a deck of cards by multiple threads at a time.
     * @return Removed a deck of cards with already drawn card from the top 
     * @throws GamePlayException When a deck of cards is empty.
     */
    public synchronized Card drawCard() throws GamePlayException {
        if (cards.size() > 0) {
            return cards.remove(0);
        }
        throw new GamePlayException("Empty " + toString());
    }

    /**
     * The method represents an action of discarding a card to the top of a deck.
     * Synchronized method is used to execute a chance of modifying a deck of cards by multiple threads at a time.
     * @param card
     */
    public synchronized void discardCard(Card card) {
        cards.add(card);
    }

}
