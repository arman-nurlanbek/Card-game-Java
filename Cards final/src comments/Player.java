import java.util.ArrayList;
import java.util.List;

/**
 * The class represents a player in a game.
 */
public class Player implements Runnable {

    // The id of a player.
    private int playerId;

    // Object of the class "CardGame".
    private CardGame game;

    // The list represents a list of cards.
    private List<Card> cards;

    // Object of the class "GameActionsLogger".
    private GameActionsLogger logger;

    /**
     * Constructor to create a new player with given ID of player - "playerId" and game in which a player plays "game".
     * @param playerId ID of a player
     * @param game Game in which a player plays.
     */
    public Player(int playerId, CardGame game) {
        this.playerId = playerId;
        this.game = game;
        cards = new ArrayList<>();
        logger = new GameActionsLogger();
    }

    /**
     * The method retrieves a String with an action of a player.
     * @return String with an action.
     */
    public String logActions() {
        return logger.toString();
    }

    /**
     * The method retrieves a String with a name for a .txt file with a player's actions.
     * @return String with a name for a .txt file.
     */
    public String logFileName() {
        return "player" + playerId + "_output.txt";
    }

    /**
     * The method retrieves ID of a player - "playerId".
     * @return ID of a player.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * The method adds a card to a list of cards.
     * @param card represents a card with a denomination.
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * The method checks if a player has a winning hand of cards.
     * @return True if a player has a winning hand of cards and false if they do not.
     */
    public boolean isWin() {
        if ((cards == null) || (cards.size() < 4)) return false;
        int num = cards.get(0).getDenomination();
        for (Card card : cards) {
            if (card.getDenomination() != num) return false;
        }
        return true;
    }

    /**
     * Executes a player's turn by performing a series of actions: drawing a card from one deck,
     * Possibly discarding a card to another deck, and logging these actions.
     * This method is synchronized to ensure thread safety as it modifies shared resources.
     * @param fromDeck represents a deck of cards from which a player draws a card.
     * @param toDeck represents a deck of cards to which a player discards a card.
     */
    public synchronized void turn(CardDeck fromDeck, CardDeck toDeck) {
        // Print the current contents of both decks if debugging is enabled.
        if (CardGame.LOG_DEBUG) {
            System.out.println("from " + fromDeck.getContents());
            System.out.println("to " + toDeck.getContents());
            System.out.println(logPlayer());
        }

        // Draw a card from the specified deck and add it to the player's hand.
        Card card = fromDeck.drawCard();
        addCard(card);
        logger.logAction(toString() + " draws a " + card.toString() + " from " + fromDeck.toString(), CardGame.LOG_DEBUG);

        // Determine which cards in the player's hand are eligible for discarding.
        List<Card> toDiscard = new ArrayList<>();
        for (Card c : cards) {
            if (c.getDenomination() != playerId) {
                toDiscard.add(c);
            }
        }

        // Randomly select a card to discard from the eligible cards.
        int cardNum = GameTools.randomInt(toDiscard.size());

        // Remove the selected card from the player's hand and discard it to the specified deck.
        Card cardDiscard = toDiscard.get(cardNum);
        cards.remove(cardDiscard);
        toDeck.discardCard(cardDiscard);

        // Print the player's current hand if debugging is enabled.
        logger.logAction(toString() + " discards a " + cardDiscard.toString() + " to " + toDeck.toString(), CardGame.LOG_DEBUG);
        logger.logAction(toString() + " current hand is" + logCards(), CardGame.LOG_DEBUG);
        if (CardGame.LOG_DEBUG) {
            System.out.println("from " + fromDeck.getContents());
            System.out.println("to " + toDeck.getContents());
        }
    }

    /**
     * The method retrieves a string with an ID of a player - "playerId".
     * @return ID of a player - "playerId".
     */
    @Override
    public String toString() {
        return "player " + playerId;
    }

    /**
     * The method retrieves a string with information on cards - "logCards()".
     * The method is protected to be used only inside of the internal workings.
     * @return String with logCards().
     */
    protected String logPlayer() {
        return toString() +":" + logCards();
    }

    /**
     * The method creates a string with information on each card in list "cards".
     * @return String with an information on each card in the list "cards".
     */
    private String logCards() {
        String res = "";
        for (Card card : cards) {
            res += " " + card.toString();
        }
        return res;
    }

    /**
     * The method represents start of action of a player showing initial hand of a player.
     */
    public void doStart() {
        logger.logAction(toString() + " initial hand" + logCards());
    }

    /**
     * The method creates a log if a player wins.
     */
    public void doWin() {
        game.winReport(this);
        logger.logAction(toString() + " wins");
    }

    /**
     * The method creates a log of a winner player informing that a player has won.
     * @param winPlayer
     */
    public void doWinInform(Player winPlayer) {
        logger.logAction(winPlayer.toString() + " has informed " + toString() + " that " + winPlayer.toString() + " has won");
    }

    /**
     * The method creates a log that a player exits a game with the player's final gand.
     */
    public void doExit() {
        logger.logAction(toString() + " exits");
        logger.logAction(toString() + " final hand:" + logCards());
    }

    /**
     * The main execution method for the Player thread. This method defines the player's behavior during the game.
     * It repeatedly checks the game state and performs actions based on the player's current status and the game's rules.
     * This method runs in a separate thread for each player, allowing simultaneous gameplay.
     */
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
