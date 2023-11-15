import java.util.ArrayList;
import java.util.List;

public class CardDeck {

    private int deckId;

    private List<Card> cards;

//    private GameActionsLogger logger;

    public CardDeck(int deckId) {
        this.deckId = deckId;
        this.cards = new ArrayList<>();
//        logger = new GameActionsLogger();
    }

    public int getDeckId() {
        return deckId;
    }

    @Override
    public String toString() {
        return "deck " + deckId;
    }

    private String logCards() {
        String res = "";
        for (Card card : cards) {
            res += " " + card.toString();
        }
        return res;
    }

    public String logFileName() {
        return "deck" + deckId + "_output.txt";
    }

    public String getContents() {
        return "deck" + deckId + " contents:" + logCards();
    }

    public synchronized Card drawCard() throws GamePlayException {
        if (cards.size() > 0) {
            return cards.remove(0);
        }
        throw new GamePlayException("Empty " + toString());
    }

    public synchronized void discardCard(Card card) {
        cards.add(card);
    }

}
