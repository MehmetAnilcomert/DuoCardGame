package com.duocardgame;

/**
 * Defines the methods that a game mediator must implement to manage game flow,
 * including player turns, deck handling, dealer selection, and round control.
 */
public interface IGameMediator {
    /**
     * Returns the current player whose turn it is.
     *
     * @return the current {@code Player}
     */
    Player getCurrentPlayer();
    
    /**
     * Advances the game to the next player's turn.
     */
    void moveToNextPlayer();
    
    /**
     * Returns a copy of the current deck.
     * <p>
     * This method provides a defensive copy to prevent external modification of the deck.
     * </p>
     *
     * @return a copy of the current {@code Deck}
     */
    Deck getDeck();
    
    /**
     * Sets the current deck.
     * <p>
     * The provided deck is stored as a defensive copy to avoid direct external modifications.
     * </p>
     *
     * @param deck the new deck to set
     */
    void setDeck(Deck deck);
    
    /**
     * Selects the dealer for the game.
     * <p>
     * Each player draws a card and the one with the highest scoring card is selected as the dealer.
     * </p>
     *
     * @return the {@code Player} chosen as the dealer
     */
    Player selectDealer();
    
    /**
     * Ends the current round.
     */
    void endRound();
}
