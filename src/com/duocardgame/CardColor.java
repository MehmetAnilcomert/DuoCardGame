package com.duocardgame;

/**
 * Enumerates the possible colors for a card in the game.
 * <p>
 * Each constant represents a distinct color that a card can have.
 * The {@code WILD} color is used for cards that can change color or are not associated with a specific color.
 * </p>
 */
public enum CardColor {
    /** Represents the blue color. */
    BLUE,
    /** Represents the green color. */
    GREEN,
    /** Represents the red color. */
    RED,
    /** Represents the yellow color. */
    YELLOW,
    /** Represents a wild card with no fixed color. */
    WILD;
}
