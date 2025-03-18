public class NumberCard extends Card {
    private final int number;

    public NumberCard(CardColor color, int number) {
        super(color, number);
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
    public void executeEffect(IGameMediator mediator) {
        mediator.moveToNextPlayer();
    }

    @Override
    public boolean isPlayable(Card otherCard) {
        if (otherCard instanceof NumberCard) {
            NumberCard other = (NumberCard) otherCard;
            return this.getColor() == other.getColor() || this.number == other.number;
        }
        return this.getColor() == otherCard.getColor();
    }
}
