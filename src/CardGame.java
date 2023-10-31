import java.util.ArrayList;
import java.util.List;

public class CardGame {

    private boolean activeGame;

    private List<Player> players;

    private List<CardDeck> decks;

    private CardGame(int playerCount) {
        players = new ArrayList<>();
        decks = new ArrayList<>();
        for (int i=1; i<=playerCount; i++) {
            players.add(new Player(i));
            decks.add(new CardDeck(i));
        }
    }

    private void loadInputData() {
        // TODO

        activeGame = true;
    }

    public synchronized void winReport(Player player) {
        // TODO

        activeGame = false;
    }

    public synchronized void turn(Player player) {
        // TODO
        int id = player.getPlayerId();

        int fromDeck = id;
        int toDeck = id+1;
        if (toDeck > decks.size()) {
            toDeck = 1;
        }

        Card card = decks.get(fromDeck).drawCard();

    }

    public static void main(String[] args) {

    }
}
