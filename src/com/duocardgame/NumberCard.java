package com.duocardgame;

/**
 * Represents a number card with a specific color and a number value.
 * This card is playable against other cards if they share the same color
 * or the same number (for number cards).
 */
public class NumberCard extends Card {
    /** The number value of the card. Must be between 0 and 9 (inclusive). */
    private final int number;

    /**
     * Constructs a new NumberCard with the specified color and number.
     *
     * @param color  the color of the card
     * @param number the number of the card (must be between 0 and 9)
     * @throws IllegalArgumentException if the number is not between 0 and 9
     */
    public NumberCard(CardColor color, int number) {
        super(color, number);
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("Number must be between 0 and 9");
        }
        this.number = number;
    }

    /**
     * Returns the number value of this card.
     *
     * @return the number of this card
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns a string representation of this card.
     * The format is "color number".
     *
     * @return a string representation of this card
     */
    @Override
    public String toString() {
        return getColor() + " " + number;
    }

    /**
     * Executes the effect of the card.
     * For a number card, this effect is to move to the next player.
     *
     * @param mediator the game mediator used to execute the effect
     */
    @Override
    public void executeEffect(IGameMediator mediator) {
        mediator.moveToNextPlayer();
    }

    /**
     * Determines if this card can be played on top of the specified card.
     * A NumberCard is playable if it shares the same color or number with another NumberCard.
     * If the other card is not a NumberCard, it is playable only if the colors match.
     *
     * @param otherCard the card to compare against
     * @return {@code true} if this card is playable on the other card, {@code false} otherwise
     */
    @Override
    public boolean isPlayable(Card topCard) {
        if (topCard instanceof NumberCard) {
            NumberCard other = (NumberCard) topCard;
            return this.getColor() == other.getColor() || this.number == other.number;
        }
        return this.getColor() == topCard.getColor();
    }

    /**
     * Creates and returns a copy of this card.
     *
     * @return a new NumberCard with the same color and number as this card
     */
    @Override
    public Card copy() {
        return new NumberCard(color, number);
    }
}
