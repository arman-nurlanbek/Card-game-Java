import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTest {

    /**
     * This test verifies the logFileName method of the CardDeck class.
     * It checks if the log file name generated for a card deck matches the expected format.
     */
    @Test
    void logFileName() {
        int deckId = 3;
        CardDeck deck = new CardDeck(deckId);
        String expected = "deck" + deckId + "_output.txt";
        assertEquals(expected, deck.logFileName());
    }

    /**
     * This test checks the getContents method of the CardDeck class.
     * It verifies that the method correctly returns a string representation of the cards in the deck.
     */
    @Test
    void getContents() {
        int deckId = 3;
        CardDeck deck = new CardDeck(deckId);
        deck.discardCard(new Card(1));
        deck.discardCard(new Card(3));
        deck.discardCard(new Card(3));
        deck.discardCard(new Card(7));
        String expected = "deck" + deckId + " contents: 1 3 3 7";
        assertEquals(expected, deck.getContents());
    }

    /**
     * This test ensures that the drawCard method of the CardDeck class functions correctly.
     * It tests both the successful drawing of cards and the throwing of an exception when the deck is empty.
     */
    @Test
    void drawCard() {
        int deckId = 3;
        CardDeck deck = new CardDeck(deckId);
        deck.discardCard(new Card(1));
        deck.discardCard(new Card(3));
        deck.discardCard(new Card(3));
        deck.discardCard(new Card(7));

        assertEquals(1, deck.drawCard().getDenomination());
        assertEquals(3, deck.drawCard().getDenomination());
        assertEquals(3, deck.drawCard().getDenomination());
        assertEquals(7, deck.drawCard().getDenomination());

        GamePlayException e = assertThrows(GamePlayException.class, deck::drawCard);
        assertEquals("Empty deck "+deckId, e.getMessage());
    }

    /**
     * This test verifies the discardCard method of the CardDeck class.
     * It checks if cards are correctly added to the deck and can be drawn in the correct order.
     */
    @Test
    void discardCard() {
        int deckId = 3;
        CardDeck deck = new CardDeck(deckId);
        deck.discardCard(new Card(1));
        assertEquals(1, deck.drawCard().getDenomination());

        deck.discardCard(new Card(3));
        deck.discardCard(new Card(7));
        assertEquals(3, deck.drawCard().getDenomination());
        assertEquals(7, deck.drawCard().getDenomination());

        GamePlayException e = assertThrows(GamePlayException.class, deck::drawCard);
        assertEquals("Empty deck "+deckId, e.getMessage());
    }
}