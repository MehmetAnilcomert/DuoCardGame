
public class ActionCard extends Card {
    private ActionType actionType;
    
    public ActionCard(CardColor color, ActionType actionType) {
        super(color, determineScore(actionType));
        this.actionType = actionType;
    }
    
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
    
    public ActionType getActionType() {
        return actionType;
    }
    
    @Override
    public boolean isPlayable(Card topCard) {
        if(actionType == ActionType.WILD || actionType == ActionType.WILD_DRAW_FOUR || actionType == ActionType.SHUFFLE_HANDS)
            return true;
        return this.color == topCard.getColor() ||
               (topCard instanceof ActionCard && ((ActionCard)topCard).getActionType() == this.actionType);
    }
    
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
                mediator.moveToNextPlayer();
                break;
            case REVERSE:
                if (mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).reverseDirection();
                    System.out.println("Game direction reversed.");
                }
                mediator.moveToNextPlayer();
                break;
            case SKIP:
                mediator.moveToNextPlayer();
                System.out.println(mediator.getCurrentPlayer().getName() + " is skipped.");
                mediator.moveToNextPlayer();
                break;
            case WILD:
                Player wildCurrentPlayer = mediator.getCurrentPlayer();
                CardColor wildChosenColor = wildCurrentPlayer.chooseColor();
                if(mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).setCurrentColor(wildChosenColor);
                }
                System.out.println(wildCurrentPlayer.getName() + " chooses color " + wildChosenColor);
                color = wildChosenColor;
                mediator.moveToNextPlayer();
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
                mediator.moveToNextPlayer();
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
                mediator.moveToNextPlayer();
                break;
            default:
                break;
        }
    }
    
    @Override
    public String toString() {
        return color + " " + actionType;
    }

    @Override
    public Card copy() {
        return new ActionCard(color, actionType);
    }
}
