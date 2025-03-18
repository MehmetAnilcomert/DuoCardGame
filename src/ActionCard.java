
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
    
    public void executeEffect(IGameMediator mediator) {
        System.out.println("Executing effect of " + actionType);
        switch(actionType) {
            case DRAW_TWO:
                mediator.moveToNextPlayer();
                Player nextPlayer = mediator.getCurrentPlayer();
                for(int i = 0; i < 2; i++){
                    Card drawn = mediator.getDeck().drawCard();
                    nextPlayer.addCard(drawn);
                    System.out.println(nextPlayer.getName() + " draws " + drawn);
                }
                mediator.moveToNextPlayer();
                break;
            case REVERSE:
                if(mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).reverseDirection();
                    System.out.println("Game direction reversed.");
                }
                break;
            case SKIP:
                mediator.moveToNextPlayer();
                System.out.println(mediator.getCurrentPlayer().getName() + " is skipped.");
                mediator.moveToNextPlayer();
                break;
            case WILD:
                Player current = mediator.getCurrentPlayer();
                CardColor chosenColor = current.chooseColor();
                if(mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).setCurrentColor(chosenColor);
                }
                System.out.println(current.getName() + " chooses color " + chosenColor);
                break;
            case WILD_DRAW_FOUR:
                Player curr = mediator.getCurrentPlayer();
                CardColor newColor = curr.chooseColor();
                if(mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).setCurrentColor(newColor);
                }
                mediator.moveToNextPlayer();
                Player affected = mediator.getCurrentPlayer();
                for(int i = 0; i < 4; i++){
                    Card drawn = mediator.getDeck().drawCard();
                    affected.addCard(drawn);
                    System.out.println(affected.getName() + " draws " + drawn);
                }
                mediator.moveToNextPlayer();
                break;
            case SHUFFLE_HANDS:
                System.out.println("Shuffling hands among players.");
                if(mediator instanceof DuoCardGame) {
                    ((DuoCardGame)mediator).shuffleHands();
                    Player p = mediator.getCurrentPlayer();
                    CardColor chosen = p.chooseColor();
                    ((DuoCardGame)mediator).setCurrentColor(chosen);
                    System.out.println(p.getName() + " chooses color " + chosen);
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    public String toString() {
        return color + " " + actionType;
    }
}
