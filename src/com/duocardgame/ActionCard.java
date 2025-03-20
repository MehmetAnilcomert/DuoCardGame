package com.duocardgame;

/**
* Represents an action card in the game which has a specific action type 
* and associated behavior when played.
* <p>
* This card extends the basic {@code Card} class and adds functionality for various
* game actions such as DRAW_TWO, REVERSE, SKIP, WILD, WILD_DRAW_FOUR, and SHUFFLE_HANDS.
* </p>
*/
public class ActionCard extends Card {
    /** The type of action associated with this card. */
    private ActionType actionType;
    
    /**
    * Constructs an {@code ActionCard} with the specified color and action type.
    *
    * @param color the color of the card
    * @param actionType the type of action this card represents
    */
    public ActionCard(CardColor color, ActionType actionType) {
        super(color, determineScore(actionType));
        this.actionType = actionType;
    }
    
    /**
    * Determines the score associated with a given action type.
    *
    * @param actionType the action type
    * @return the score for the action type
    */
    private static int determineScore(ActionType actionType) {
        switch(actionType) {
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
    * Returns the action type of this card.
    *
    * @return the {@code ActionType} of this card
    */
    public ActionType getActionType() {
        return actionType;
    }
    
    /**
     * Checks if this action card is playable on top of the given card.
     * <p>
     * An action card is playable if it is a wild card (WILD, WILD_DRAW_FOUR, or SHUFFLE_HANDS),
     * or if its color matches the top card's color, or if both cards are action cards with the same action type.
     * </p>
     *
     * @param topCard the card currently on top of the discard pile
     * @return {@code true} if this card can be played on top of the given card; {@code false} otherwise
     */
    @Override
    public boolean isPlayable(Card topCard) {
        if(actionType == ActionType.WILD || actionType == ActionType.WILD_DRAW_FOUR || actionType == ActionType.SHUFFLE_HANDS)
            return true;
        return this.color == topCard.getColor() ||
               (topCard instanceof ActionCard && ((ActionCard)topCard).getActionType() == this.actionType);
    }
    
    /**
     * Executes the effect of this action card using the provided game mediator.
     * <p>
     * The effect varies based on the {@code ActionType}:
     * <ul>
     *   <li>{@code DRAW_TWO}: Forces the next player to draw two cards.</li>
     *   <li>{@code REVERSE}: Reverses the direction of play.</li>
     *   <li>{@code SKIP}: Skips the next player's turn.</li>
     *   <li>{@code WILD}: Allows the player to choose a new color.</li>
     *   <li>{@code WILD_DRAW_FOUR}: Allows the player to choose a new color and forces the next player to draw four cards.</li>
     *   <li>{@code SHUFFLE_HANDS}: Shuffles the hands among players and allows choosing a new color.</li>
     * </ul>
     * </p>
     *
     * @param mediator the game mediator that controls the game flow and state
     */
    @Override
    public void executeEffect(IGameMediator mediator) {
        System.out.println("Executing effect of " + actionType);
        switch(actionType) {
            case DRAW_TWO:
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
            case REVERSE:
                if (mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).reverseDirection();
                    System.out.println("Game direction reversed.");
                }
                break;
            case SKIP:
                mediator.moveToNextPlayer();
                System.out.println(mediator.getCurrentPlayer().getName() + " is skipped.");
                break;
            case WILD:
                Player wildCurrentPlayer = mediator.getCurrentPlayer();
                CardColor wildChosenColor = wildCurrentPlayer.chooseColor();
                if(mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).setCurrentColor(wildChosenColor);
                }
                System.out.println(wildCurrentPlayer.getName() + " chooses color " + wildChosenColor);
                color = wildChosenColor;
                break;
            case WILD_DRAW_FOUR:
                Player wildFourCurrentPlayer = mediator.getCurrentPlayer();
                CardColor wildFourChosenColor = wildFourCurrentPlayer.chooseColor();
                if(mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).setCurrentColor(wildFourChosenColor);
                }
                color = wildFourChosenColor;
                mediator.moveToNextPlayer();
                Player wildFourNextPlayer = mediator.getCurrentPlayer();
                Deck drawFourDeck = mediator.getDeck();
                for(int i = 0; i < 4; i++){
                    Card drawn = drawFourDeck.drawCard();
                    wildFourNextPlayer.addCard(drawn);
                    System.out.println(wildFourNextPlayer.getName() + " draws " + drawn);
                }
                mediator.setDeck(drawFourDeck);
                break;
            case SHUFFLE_HANDS:
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
     * Returns a string representation of this action card.
     * <p>
     * The string representation consists of the card's color and action type.
     * </p>
     *
     * @return a string representing the action card
     */
    @Override
    public String toString() {
        return color + " " + actionType;
    }

    /**
     * Creates and returns a copy of this action card.
     *
     * @return a new {@code ActionCard} with the same color and action type as this card
     */
    @Override
    public Card copy() {
        return new ActionCard(color, actionType);
    }
}
