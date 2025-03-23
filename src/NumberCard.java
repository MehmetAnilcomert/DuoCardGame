public class NumberCard extends Card {
    private final int number;

    public NumberCard(CardColor color, int number) {
        super(color, number); // Number is also the score of the card
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("Number must be between 0 and 9");
        }
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return getColor() + " " + number;
    }

    @Override
    public void executeEffect(IGameMediator mediator) { // No special effects for NumberCard, moves the turn to next player
        mediator.moveToNextPlayer();
    }

    @Override
    public boolean isPlayable(Card topCard) {
        // Check if the the top card is a number card and if the card number or the card color matches
        if (topCard instanceof NumberCard) {
            NumberCard other = (NumberCard) topCard;
            return this.getColor() == other.getColor() || this.number == other.number;
        }
        return this.getColor() == topCard.getColor(); // Check if the card color matches otherwise
    }

    @Override
    public Card copy() {
        // Copy method used in copy constructor of Deck
        return new NumberCard(color, number);
    }
}
