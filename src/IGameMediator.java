
public interface IGameMediator {
    Player getCurrentPlayer();
    void moveToNextPlayer();
    Deck getDeck();
    Player selectDealer();
    void endRound();
    void nextTurn();
}