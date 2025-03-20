package com.duocardgame;
/**
 * Enumerates the various types of actions that can be performed by an action card.
 * <p>
 * Each constant represents a specific game action that has a unique effect when played.
 * </p>
 */
public enum ActionType {
    /** Forces the next player to draw two cards. */
    DRAW_TWO,
    /** Reverses the direction of play. */
    REVERSE,
    /** Skips the next player's turn. */
    SKIP,
    /** A wild card that allows the player to choose a new color. */
    WILD,
    /** A wild card that allows the player to choose a new color and forces the next player to draw four cards. */
    WILD_DRAW_FOUR,
    /** Shuffles the hands of all players and allows the player to choose a new color. */
    SHUFFLE_HANDS;
}
