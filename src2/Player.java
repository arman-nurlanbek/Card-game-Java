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

    public String logFileName() {
        return "player" + playerId + "_output.txt";
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

    public synchronized void turn(CardDeck fromDeck, CardDeck toDeck) {
        if (CardGame.LOG_DEBUG) {
            System.out.println("from " + fromDeck.getContents());
            System.out.println("to " + toDeck.getContents());
            System.out.println(log());
        }

        Card card = fromDeck.drawCard();
        addCard(card);
        logger.logAction(toString() + " draws a " + card.toString() + " from " + fromDeck.toString(), CardGame.LOG_DEBUG);

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
        logger.logAction(toString() + " discards a " + cardDiscard.toString() + " to " + toDeck.toString(), CardGame.LOG_DEBUG);
        logger.logAction(toString() + " current hand is " + logCards(), CardGame.LOG_DEBUG);
        if (CardGame.LOG_DEBUG) {
            System.out.println("from " + fromDeck.getContents());
            System.out.println("to " + toDeck.getContents());
        }
    }

    @Override
    public String toString() {
        return "player " + playerId;
    }

    protected String log() {
        return toString() +":" + logCards();
    }

    private String logCards() {
        String res = "";
        for (Card card : cards) {
            res += " " + card.toString();
        }
        return res;
    }

    public void doStart() {
        logger.logAction(toString() + " initial hand " + logCards());
//        if (isWin()) {
//            doWin();
//        }
    }

    public void doWin() {
        game.winReport(this);
        logger.logAction(toString() + " wins");
    }

    public void doWinInform(Player winPlayer) {
        logger.logAction(winPlayer.toString() + " has informed " + toString() + " that " + winPlayer.toString() + " has won");
    }

    public void doExit() {
        logger.logAction(toString() + " exits");
        logger.logAction(toString() + " final hand:" + logCards());
    }


    @Override
    public void run() {
        while(game.isActiveGame()) {
            if (isWin()) {
                doWin();
                break;
            }

            game.turn(this);

            try {
                Thread.sleep(CardGame.SLEEP_PLAYER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
