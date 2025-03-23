/**
 * Represents a number card in the Duo card game.
 * Each NumberCard has a color and a number, which also serves as the score of the card.
 * The card can be played if the number or color matches the top card in the discard pile.
 */
public class NumberCard extends Card {
    private final int number;

    /**
     * Constructs a new NumberCard with the specified color and number.
     * 
     * @param color the color of the card.
     * @param number the number of the card, must be between 0 and 9.
     * @throws IllegalArgumentException if the number is not between 0 and 9.
     */
    public NumberCard(CardColor color, int number) {
        super(color, number); // Number is also the score of the card
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("Number must be between 0 and 9");
        }
        this.number = number;
    }

    /**
     * Retrieves the number of the card.
     * 
     * @return the number of the card.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns a string representation of the NumberCard, including its color and number.
     * 
     * @return a string representation of the NumberCard.
     */
    @Override
    public String toString() {
        return getColor() + " " + number;
    }

    /**
     * Executes the effect of playing this NumberCard.
     * In this case, there are no special effects, and the turn is simply passed to the next player.
     * 
     * @param mediator the game mediator that handles turn management.
     */
    @Override
    public void executeEffect(IGameMediator mediator) { 
        // No special effects for NumberCard, moves the turn to next player
        mediator.moveToNextPlayer();
    }

    /**
     * Determines if this NumberCard can be played on top of the given card.
     * A NumberCard can be played if the top card's color or number matches this card's color or number.
     * 
     * @param topCard the card at the top of the discard pile.
     * @return true if this card can be played on the top card, otherwise false.
     */
    @Override
    public boolean isPlayable(Card topCard) {
        // Check if the top card is a number card and if the card number or the card color matches
        if (topCard instanceof NumberCard) {
            NumberCard other = (NumberCard) topCard;
            return this.getColor() == other.getColor() || this.number == other.number;
        }
        return this.getColor() == topCard.getColor(); // Check if the card color matches otherwise
    }

    /**
     * Creates a copy of this NumberCard.
     * The copied card has the same color and number as the original.
     * 
     * @return a new NumberCard that is a copy of this one.
     */
    @Override
    public Card copy() {
        // Copy method used in copy constructor of Deck
        return new NumberCard(color, number);
    }
}
