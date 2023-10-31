import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    private int playerId;

    private List<Card> cards;

    public Player(int playerId) {
        this.playerId = playerId;
        cards = new ArrayList<>();
    }

    public int getPlayerId() {
        return playerId;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public boolean isWin() {
        if ((cards == null) || (cards.size() > 0)) return false;
        int num = cards.get(0).getDenomination();
        for (Card card : cards) {
            if (card.getDenomination() != num) return false;
        }
        return true;
    }

    public synchronized boolean turn(CardDeck fromDeck, CardDeck toDeck) {
        Card card = fromDeck.drawCard();
        addCard(card);

        List<Card> toDiscard = new ArrayList<>();
        for (Card c : cards) {
            if (c.getDenomination() != playerId) {
                toDiscard.add(c);
            }
        }

        int cardNum = GameTools.randomInt(toDiscard.size());

        Card cardDiscard = toDiscard.get(cardNum);
        cards.remove(cardDiscard);

        toDeck.discardCard(cardDiscard);

        return isWin();
    }
}
