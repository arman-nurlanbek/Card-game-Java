import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable {

    private int playerId;

    private CardGame game;

    private List<Card> cards;

    private GameActionsLogger logger;

    public Player(int playerId, CardGame game) {
        this.playerId = playerId;
        this.game = game;
        cards = new ArrayList<>();
        logger = new GameActionsLogger();
    }

    public String logActions() {
        return logger.toString();
    }

    public int getPlayerId() {
        return playerId;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public boolean isWin() {
        if ((cards == null) || (cards.size() < 4)) return false;
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

    @Override
    public String toString() {
        return "player" + playerId;
    }

    public String log() {
        return toString() +":" + cards;
    }

    public void doStop() {

    }


    @Override
    public void run() {
        while(game.isActiveGame()) {
            if (isWin()) {
                game.winReport(this);
                break;
            }
            System.out.println("Turn " + toString());
            game.turn(this);

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
