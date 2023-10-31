import java.util.ArrayList;
import java.util.List;

public class CardDeck {

    private int deckId;

    private List<Card> cards;

    public CardDeck(int deckId) {
        this.deckId = deckId;
        this.cards = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "deck" + deckId;
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
