
public interface IGameMediator {
    Player getCurrentPlayer();
    void moveToNextPlayer();
    Deck getDeck();
    void setDeck(Deck deck);
    Player selectDealer();
    void endRound();
}