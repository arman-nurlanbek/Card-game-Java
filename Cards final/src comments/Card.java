
//The class represents a playing card with a specific denomination.
public class Card {
    // The denomination value of a card (e.g., 1, 2, 3, 4, ...)
    private int denomination;

    /** 
     * Constructor to create a new Card with a given denomination.
     * @param denomination the value assgined to the card.
     */ 
    public Card(int denomination) {
        this.denomination = denomination;
    }
    /**
     * The method retrieves the denomination of the card.
     * @return denomination the value assigned to the card.
     */
    public int getDenomination() {
        return denomination;
    }

    /**
     * The method provides the string representation of the denomination of the card.
     * @return Denomination of the card in the string representation. 
     */
    @Override
    public String toString() {
        return String.valueOf(denomination);
    }

}
