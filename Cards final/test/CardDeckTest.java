import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTest {

    @Test
    void logFileName() {
        int deckId = 3;
        CardDeck deck = new CardDeck(deckId);
        String expected = "deck" + deckId + "_output.txt";
        assertEquals(expected, deck.logFileName());
    }

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