import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void logFileName() {
        CardGame game = new CardGame(4);
        int playerId = 2;
        Player player = game.getPlayer(playerId);
        String expected = "player" + playerId + "_output.txt";
        assertEquals(expected, player.logFileName());
    }

    @Test
    void addCard() {
        CardGame game = new CardGame(4);
        int playerId = 2;
        Player player = game.getPlayer(playerId);
        player.addCard(new Card(3));
        player.addCard(new Card(2));
        String expected = "player " + playerId + ": 3 2";
        assertEquals(expected, player.logPlayer());
    }

    @Test
    void isWin() {
        CardGame game = new CardGame(4);
        int playerId = 2;
        Player player = new Player(playerId, game);
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        player.addCard(new Card(3));
        player.addCard(new Card(2));
        assertFalse(player.isWin());

        player = new Player(playerId, game);
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        player.addCard(new Card(2));
        assertTrue(player.isWin());

        player = new Player(playerId, game);
        player.addCard(new Card(1));
        player.addCard(new Card(1));
        player.addCard(new Card(1));
        player.addCard(new Card(1));
        assertTrue(player.isWin());
    }

    @Test
    void turn() {
        CardGame game = new CardGame(4);
        int playerId = 2;
        Player player = game.getPlayer(playerId);
        player.addCard(new Card(playerId));
        player.addCard(new Card(playerId));
        player.addCard(new Card(playerId+2));
        player.addCard(new Card(playerId));

        CardDeck fromDeck = new CardDeck(playerId);
        fromDeck.discardCard(new Card(playerId));

        CardDeck toDeck = new CardDeck(playerId+1);

        player.turn(fromDeck, toDeck);

        String expected = "player " + playerId + " draws a " + playerId + " from deck " + playerId +
                "\nplayer " + playerId + " discards a " + (playerId+2) + " to deck " + (playerId+1) +
                "\nplayer " + playerId + " current hand is " +
                playerId + " " + playerId + " " + playerId + " " + playerId;
        assertEquals(expected, player.logActions());
        assertTrue(player.isWin());
    }

    @Test
    void doStart() {
        CardGame game = new CardGame(4);
        int playerId = 2;
        Player player = game.getPlayer(playerId);
        player.addCard(new Card(1));
        player.addCard(new Card(1));
        player.addCard(new Card(2));
        player.addCard(new Card(3));

        player.doStart();

        String expected = "player " + playerId + " initial hand 1 1 2 3";
        assertEquals(expected, player.logActions());
    }

    @Test
    void doWin() {
        CardGame game = new CardGame(4);
        int playerId = 2;
        Player player = game.getPlayer(playerId);
        player.addCard(new Card(1));
        player.addCard(new Card(1));
        player.addCard(new Card(2));
        player.addCard(new Card(3));

        player.doWin();

        String expected = "player " + playerId + " wins";
        assertEquals(expected, player.logActions());
    }

    @Test
    void doWinInform() {
        CardGame game = new CardGame(4);

        int playerId = 2;
        Player player = game.getPlayer(playerId);
        player.addCard(new Card(1));
        player.addCard(new Card(1));
        player.addCard(new Card(2));
        player.addCard(new Card(3));

        int winPlayerId = 3;
        Player winPlayer = game.getPlayer(winPlayerId);

        player.doWinInform(winPlayer);

        String expected = "player " + winPlayerId + " has informed player " + playerId +
                " that player " + winPlayerId + " has won";
        assertEquals(expected, player.logActions());
    }

    @Test
    void doExit() {
        CardGame game = new CardGame(4);

        int playerId = 2;
        Player player = game.getPlayer(playerId);
        player.addCard(new Card(1));
        player.addCard(new Card(1));
        player.addCard(new Card(2));
        player.addCard(new Card(3));

        player.doExit();

        String expected = "player " + playerId + " exits\n" +
                "player " + playerId + " final hand: 1 1 2 3";
        assertEquals(expected, player.logActions());
    }
}