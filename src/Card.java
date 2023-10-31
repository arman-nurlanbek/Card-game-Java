public class Card {
    private int denomination;

    public Card(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }

    @Override
    public String toString() {
        return String.valueOf(denomination);
    }

}
