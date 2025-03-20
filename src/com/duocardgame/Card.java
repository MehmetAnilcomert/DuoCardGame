package com.duocardgame;

/**
 * Represents an abstract playing card in the game.
 * <p>
 * Each card has a color and a score, and defines common behaviors such as checking if the card is playable,
 * executing its effect, and creating a copy of itself.
 * </p>
 */
public abstract class Card {
    /** The color of the card. */
    protected CardColor color;
    
    /** The score associated with the card. */
    protected int score;
    
    /**
     * Constructs a card with the specified color and score.
     *
     * @param color the color of the card
     * @param score the score value of the card
     */
    public Card(CardColor color, int score) {
        this.color = color;
        this.score = score;
    }
    
    /**
     * Returns the score of the card.
     *
     * @return the score associated with this card
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Returns the color of the card.
     *
     * @return the color of this card
     */
    public CardColor getColor() {
        return color;
    }
    
    /**
     * Determines whether this card is playable on top of the given card.
     *
     * @param topCard the card on top of the discard pile
     * @return {@code true} if this card can be played on top of {@code topCard}; {@code false} otherwise
     */
    public abstract boolean isPlayable(Card topCard);

    /**
     * Executes the specific effect of this card using the provided game mediator.
     *
     * @param mediator the game mediator that controls game flow and state
     */
    public abstract void executeEffect(IGameMediator mediator);

    /**
     * Creates and returns a copy of this card.
     *
     * @return a new card instance that is a copy of this card
     */
    public abstract Card copy();
    
    /**
     * Returns a string representation of the card.
     * <p>
     * This representation typically includes the card's color and other identifying attributes.
     * </p>
     *
     * @return a string representing this card
     */
    @Override
    public abstract String toString();
}
