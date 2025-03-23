/**
 * Represents an action card in the Duo card game.
 * Each ActionCard has an action type that triggers specific effects during the game.
 * The card can be played if the top card's color matches or if the card is a Wild, Wild Draw Four, or Shuffle Hands action card.
 */
public class ActionCard extends Card {
    private ActionType actionType;

    /**
     * Constructs a new ActionCard with the specified color and action type.
     * The card's score is determined based on its action type.
     * 
     * @param color the color of the card.
     * @param actionType the action type of the card.
     */
    public ActionCard(CardColor color, ActionType actionType) {
        super(color, determineScore(actionType));
        this.actionType = actionType;
    }

    /**
     * Determines the score of the action card based on its action type.
     * 
     * @param actionType the action type of the card.
     * @return the score associated with the given action type.
     */
    private static int determineScore(ActionType actionType) {
        switch(actionType) { // Determine card score by action type
            case DRAW_TWO:
            case REVERSE:
            case SKIP:
                return 20;
            case WILD:
            case WILD_DRAW_FOUR:
                return 50;
            case SHUFFLE_HANDS:
                return 40;
            default:
                return 0;
        }
    }

    /**
     * Retrieves the action type of this ActionCard.
     * 
     * @return the action type of the card.
     */
    public ActionType getActionType() {
        return actionType;
    }

    /**
     * Determines if this ActionCard can be played on top of the given card.
     * Action cards can be played on top of matching colors or when the action type matches.
     * Wild, Wild Draw Four, and Shuffle Hands can be played on top of any card.
     * 
     * @param topCard the card at the top of the discard pile.
     * @return true if this card can be played on the top card, otherwise false.
     */
    @Override
    public boolean isPlayable(Card topCard) {
        // Wild, Wild Draw Four and Shuffle Hands action cards can be played on top of any card
        if(actionType == ActionType.WILD || actionType == ActionType.WILD_DRAW_FOUR || actionType == ActionType.SHUFFLE_HANDS) 
            return true;
        // Check if the card symbol or the card color matches otherwise
        return this.color == topCard.getColor() ||
               (topCard instanceof ActionCard && ((ActionCard)topCard).getActionType() == this.actionType);
    }

    /**
     * Executes the effect of playing this ActionCard.
     * The effect depends on the action type of the card. This can include drawing cards,
     * reversing the game direction, skipping turns, changing colors, and shuffling hands.
     * 
     * @param mediator the game mediator that handles game actions.
     */
    @Override
    public void executeEffect(IGameMediator mediator) {
        System.out.println("Executing effect of " + actionType);
        switch(actionType) {
            case DRAW_TWO: // The next player draws two cards and their turn is skipped
                mediator.moveToNextPlayer();
                Player drawTwoNextPlayer = mediator.getCurrentPlayer();
                Deck drawTwoDeck = mediator.getDeck();
                for(int i = 0; i < 2; i++){
                    Card drawn = drawTwoDeck.drawCard();
                    drawTwoNextPlayer.addCard(drawn);
                    System.out.println(drawTwoNextPlayer.getName() + " draws " + drawn);
                }
                mediator.setDeck(drawTwoDeck);
                break;
            case REVERSE: // Reverse the direction of the mediator if it's an instance of DuoCardGame
                if (mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).reverseDirection();
                    System.out.println("Game direction reversed.");
                }
                break;
            case SKIP: // Skip the turn of the next player
                mediator.moveToNextPlayer();
                System.out.println(mediator.getCurrentPlayer().getName() + " is skipped.");
                break;
            case WILD: // The player who played Wild will choose a color based on their hand
                Player wildCurrentPlayer = mediator.getCurrentPlayer();
                CardColor wildChosenColor = wildCurrentPlayer.chooseColor();
                if(mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).setCurrentColor(wildChosenColor);
                }
                System.out.println(wildCurrentPlayer.getName() + " chooses color " + wildChosenColor);
                color = wildChosenColor;
                break;
            case WILD_DRAW_FOUR: // The player who played Wild Draw Four will choose a color based on their hand
                Player wildFourCurrentPlayer = mediator.getCurrentPlayer();
                CardColor wildFourChosenColor = wildFourCurrentPlayer.chooseColor();
                if(mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).setCurrentColor(wildFourChosenColor);
                }
                color = wildFourChosenColor;
                mediator.moveToNextPlayer();
                Player wildFourNextPlayer = mediator.getCurrentPlayer(); // The next player draws four cards and is skipped
                Deck drawFourDeck = mediator.getDeck();
                for(int i = 0; i < 4; i++){
                    Card drawn = drawFourDeck.drawCard();
                    wildFourNextPlayer.addCard(drawn);
                    System.out.println(wildFourNextPlayer.getName() + " draws " + drawn);
                }
                mediator.setDeck(drawFourDeck);
                break;
            case SHUFFLE_HANDS: // The player who played Shuffle Cards will shuffle and distribute cards of all players and choose a color based on their shuffled hand
                System.out.println("Shuffling hands among players.");
                if(mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).shuffleHands();
                    Player shuffleCurrentPlayer = mediator.getCurrentPlayer();
                    CardColor chosen = shuffleCurrentPlayer.chooseColor();
                    ((DuoCardGame)mediator).setCurrentColor(chosen);
                    System.out.println(shuffleCurrentPlayer.getName() + " chooses color " + chosen);
                    color = chosen;
                }
                break;
            default:
                break;
        }
    }

    /**
     * Returns a string representation of the ActionCard, including its color and action type.
     * 
     * @return a string representation of the ActionCard.
     */
    @Override
    public String toString() {
        return color + " " + actionType;
    }

    /**
     * Creates a copy of this ActionCard.
     * The copied card has the same color and action type as the original.
     * 
     * @return a new ActionCard that is a copy of this one.
     */
    @Override
    public Card copy() {
        // Copy method used in copy constructor of Deck
        return new ActionCard(color, actionType);
    }
}
