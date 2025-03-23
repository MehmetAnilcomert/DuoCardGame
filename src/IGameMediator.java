/**
 * Interface for a card game that involves players and a deck.
 * Defines methods for handling player turns, selecting a dealer, and managing the deck.  
 */
public interface IGameMediator {
    /**
     * Retrieves the player whose turn it currently is.
     * 
     * @return the current Player.
     */
    Player getCurrentPlayer();

    /**
    * Advances the turn for the next Player in the card game.
    */
    void moveToNextPlayer();
    
    /**
     * Retrieves the current deck used in the card game.
     * 
     * @return the Deck instance associated with the game.
     */
    Deck getDeck();
    
    /**
     * Sets the deck to be used in the card game.
     * 
     * @param deck the Deck instance to set for the game.
     */
    void setDeck(Deck deck);

    /**
     * Selects the dealer Player based on the game rules.
     * 
     * @return the Player selected as the dealer.
     */
    Player selectDealer();

    /**
     * Ends the current round of the game to transition to the next round.
     */
    void endRound();
}