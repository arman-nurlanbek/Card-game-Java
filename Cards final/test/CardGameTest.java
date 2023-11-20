import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CardGameTest {

    private static String TEST_01 = "7\n" +
            "8\n" +
            "0\n" +
            "12\n" +
            "14\n" +
            "15\n" +
            "16\n" +
            "6\n" +
            "3\n" +
            "7\n" +
            "8\n" +
            "1\n" +
            "6\n" +
            "3\n" +
            "7\n" +
            "9\n" +
            "27\n" +
            "6\n" +
            "4\n" +
            "1\n" +
            "6\n" +
            "7\n" +
            "41\n" +
            "1\n" +
            "5\n" +
            "6\n" +
            "7\n" +
            "1\n" +
            "4\n" +
            "5\n" +
            "4\n" +
            "178\n" +
            "78";

    private static String TEST_02 = "1\n" +
            "7\n" +
            "8\n" +
            "0\n" +
            "12\n" +
            "14\n" +
            "15\n" +
            "16\n" +
            "6\n" +
            "3\n" +
            "7\n" +
            "8\n" +
            "1\n" +
            "6\n" +
            "3\n" +
            "-7\n" +
            "27\n" +
            "6\n" +
            "4\n" +
            "1\n" +
            "6\n" +
            "7\n" +
            "41\n" +
            "1\n" +
            "5\n" +
            "6\n" +
            "7\n" +
            "1\n" +
            "4\n" +
            "5\n" +
            "4\n" +
            "1";

    private static String TEST_03 = "7\n" +
            "8\n" +
            "0\n" +
            "12\n" +
            "14\n" +
            "15\n" +
            "16\n" +
            "6\n" +
            "3\n" +
            "7\n" +
            "8\n" +
            "1\n" +
            "6\n" +
            "3\n" +
            "7\n" +
            "9\n" +
            "27\n" +
            "6\n" +
            "4\n" +
            "1\n" +
            "6\n" +
            "7\n" +
            "41\n" +
            "1\n" +
            "5\n" +
            "6\n" +
            "7\n" +
            "1\n" +
            "4\n" +
            "5\n" +
            "4\n" +
            "1";

    /**
     * Tests the loadInputData method of the CardGame class.
     * This test verifies that the method correctly processes valid input data 
     * and throws GamePlayExceptions with appropriate messages for invalid data.
     */
    @Test
    void loadInputData() throws IOException {
        String[] files = {TEST_01, TEST_02};
        String[] messages = {"Input pack does not contain 32 lines!", "Negative card denomination: -7"};

        int playerCount = 4;
        for (int i=0; i<files.length; i++) {

            CardGame game = new CardGame(playerCount);
            String thrown = null;
            try {
                game.loadInputData(files[i]);
            } catch (GamePlayException e) {
                thrown = e.getMessage();
            }
            assertNotNull(thrown);
            assertEquals(thrown, messages[i]);
        }

        CardGame game = new CardGame(playerCount);
        String thrown = null;
        try {
            game.loadInputData(TEST_03);
        } catch (GamePlayException e) {
            thrown = e.getMessage();
        }
        assertNull(thrown);
    }

    /**
     * Tests the winReport method of the CardGame class.
     * It verifies that the game correctly reports a player's win and informs other players.
     */
    @Test
    void winReport() {
        int playerCount = 4;
        CardGame game = new CardGame(playerCount);
        game.startGame();

        int winPlayerId = 2;
        Player player = game.getPlayer(winPlayerId);
        player.addCard(new Card(winPlayerId));
        player.addCard(new Card(winPlayerId));
        player.addCard(new Card(winPlayerId));
        player.addCard(new Card(winPlayerId));

        game.winReport(player);

        for (int playerId=1; playerId<=playerCount; playerId++) {
            if (playerId != winPlayerId) {
                String expected = "player " + winPlayerId + " has informed player " + playerId +
                        " that player " + winPlayerId + " has won";
                assertEquals(expected, game.getPlayer(playerId).logActions());
            }
        }
    }

    /**
     * Tests the turn method of the CardGame class.
     * It verifies that a player's turn is executed correctly, 
     * including drawing and discarding cards and checking for a win.
     */ 
    @Test
    void turn() {
        CardGame game = new CardGame(4);
        int playerId = 2;
        Player player = game.getPlayer(playerId);
        player.addCard(new Card(playerId));
        player.addCard(new Card(playerId));
        player.addCard(new Card(playerId+2));
        player.addCard(new Card(playerId));

        CardDeck fromDeck = game.getDeck(playerId);
        fromDeck.discardCard(new Card(playerId));

        game.turn(player);

        String expected = "player " + playerId + " draws a " + playerId + " from deck " + playerId +
                "\nplayer " + playerId + " discards a " + (playerId+2) + " to deck " + (playerId+1) +
                "\nplayer " + playerId + " current hand is " +
                playerId + " " + playerId + " " + playerId + " " + playerId;
        assertEquals(expected, player.logActions());
        assertTrue(player.isWin());
    }
}