/**
 * Abstract representation of a card in the Duo card game.
 * Each card has a color and score, and may have specific effects that are triggered during gameplay.
 */
public abstract class Card {
    protected CardColor color; // protected properties to allow access to subclasses
    protected int score;
    
    /**
     * Constructor to initialize a card with a specific color and score.
     * 
     * @param color the color of the card
     * @param score the score of the card
     */
    public Card(CardColor color, int score) {
        this.color = color;
        this.score = score;
    }
    
    /**
     * Gets the score of the card.
     * 
     * @return the score of the card
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Gets the color of the card.
     * 
     * @return the color of the card
     */
    public CardColor getColor() {
        return color;
    }
    
    /**
     * Determines if the card can be played on top of the given card.
     * 
     * @param topCard the card on top of the pile
     * @return true if the card is playable, false otherwise
     */
    public abstract boolean isPlayable(Card topCard);

    /**
     * Executes the effect of the card within the game.
     * 
     * @param mediator the game mediator that manages the game state
     */
    public abstract void executeEffect(IGameMediator mediator);

    /**
     * Creates a copy of the card.
     * 
     * @return a new instance that is a copy of the current card
     */
    public abstract Card copy();
    
    /**
     * Returns a string representation of the card.
     * 
     * @return a string describing the card
     */
    @Override
    public abstract String toString();
}
