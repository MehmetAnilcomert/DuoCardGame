public abstract class Card {
    protected CardColor color;
    protected int score;
    
    public Card(CardColor color, int score) {
        this.color = color;
        this.score = score;
    }
    
    public int getScore() {
        return score;
    }
    
    public CardColor getColor() {
        return color;
    }
    
    public abstract boolean isPlayable(Card topCard);

    public abstract void executeEffect(IGameMediator mediator);
    
    @Override
    public abstract String toString();
}
