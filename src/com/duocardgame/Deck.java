package com.duocardgame;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a deck of cards used in the game.
 * <p>
 * The deck consists of a draw pile and a discard pile. It provides methods for initializing the deck,
 * shuffling, dealing cards to players, drawing cards, and managing the discard pile.
 * </p>
 */
public class Deck {
    /** The list of cards available to be drawn. */
    private List<Card> drawPile;
    
    /** The list of cards that have been played/discarded. */
    private List<Card> discardPile;
    
    /**
     * Constructs a new {@code Deck} instance, initializes the deck with cards,
     * and shuffles the draw pile.
     */
    public Deck() {
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    /**
     * Constructs a new {@code Deck} as a copy of the provided deck.
     * <p>
     * Both the draw pile and discard pile are deep-copied by creating copies of each card.
     * </p>
     *
     * @param deck the deck to copy from
     */
    public Deck(Deck deck) {
        this.drawPile = deck.drawPile.stream().map(c -> c.copy()).collect(Collectors.toList());
        this.discardPile = deck.discardPile.stream().map(c -> c.copy()).collect(Collectors.toList());
    }

    /**
     * Initializes the deck with a standard set of cards.
     * <p>
     * The deck includes:
     * <ul>
     *   <li>Number cards: One 0 card and two of each card numbered 1-9 for each non-wild color.</li>
     *   <li>Action cards: Two of each {@code DRAW_TWO}, {@code REVERSE}, and {@code SKIP} for each non-wild color.</li>
     *   <li>Wild cards: Four {@code WILD} cards and four {@code WILD_DRAW_FOUR} cards.</li>
     *   <li>A single {@code SHUFFLE_HANDS} card.</li>
     * </ul>
     * </p>
     */
    public void initializeDeck() {
        // Number Cards: one 0 and two of each card 1-9 for every non-wild color.
        for (CardColor color : CardColor.values()) {
            if (color != CardColor.WILD) {
                drawPile.add(new NumberCard(color, 0));
                for (int num = 1; num <= 9; num++) {
                    drawPile.add(new NumberCard(color, num));
                    drawPile.add(new NumberCard(color, num));
                }
            }
        }
        // Action Cards: DRAW_TWO, REVERSE, SKIP (two of each per non-wild color)
        for (CardColor color : CardColor.values()) {
            if (color != CardColor.WILD) {
                drawPile.add(new ActionCard(color, ActionType.DRAW_TWO));
                drawPile.add(new ActionCard(color, ActionType.DRAW_TWO));
                drawPile.add(new ActionCard(color, ActionType.REVERSE));
                drawPile.add(new ActionCard(color, ActionType.REVERSE));
                drawPile.add(new ActionCard(color, ActionType.SKIP));
                drawPile.add(new ActionCard(color, ActionType.SKIP));
            }   
        }
        // Wild and Wild Draw Four: Four of each
        for (int i = 0; i < 4; i++) {
            drawPile.add(new ActionCard(CardColor.WILD, ActionType.WILD));
            drawPile.add(new ActionCard(CardColor.WILD, ActionType.WILD_DRAW_FOUR));
        }
        // Shuffle Hands: 1 card
        drawPile.add(new ActionCard(CardColor.WILD, ActionType.SHUFFLE_HANDS));
    }
    
    /**
     * Shuffles the draw pile.
     */
    public void shuffle() {
        Collections.shuffle(drawPile);
    }
    
    /**
     * Deals a specified number of cards to each player.
     * <p>
     * For each card to be dealt, the method draws a card from the draw pile and adds it to the player's hand.
     * If the draw pile is empty, the deck is reshuffled before drawing a card.
     * </p>
     *
     * @param players the list of players to whom the cards will be dealt
     * @param count the number of cards to deal to each player
     */
    public void dealCards(List<Player> players, int count) {
        for (int i = 0; i < count; i++) {
            for (Player p : players) {
                if (drawPile.isEmpty()) reshuffle();
                p.addCard(drawCard());
            }
        }
    }
    
    /**
     * Draws a card from the top of the draw pile.
     * <p>
     * If the draw pile is empty, the deck is reshuffled before drawing a card.
     * </p>
     *
     * @return the card drawn from the top of the draw pile
     */
    public Card drawCard() {
        if (drawPile.isEmpty()) {
            reshuffle();
        }
        return drawPile.remove(0);
    }
    
    /**
     * Returns the top card from the discard pile.
     *
     * @return the last card in the discard pile, or {@code null} if the discard pile is empty
     */
    public Card getTopDiscardPileCard() {
        if (discardPile.isEmpty()) return null;
        return discardPile.get(discardPile.size() - 1);
    }
    
    /**
     * Adds the specified card to the discard pile.
     *
     * @param card the card to add to the discard pile
     */
    public void putCardToDiscardPile(Card card) {
        discardPile.add(card);
    }
    
    /**
     * Reshuffles the deck when the draw pile is empty.
     * <p>
     * All cards from the discard pile except for the top card are added to the draw pile,
     * the discard pile is cleared (keeping the top card), and then the draw pile is shuffled.
     * </p>
     */
    public void reshuffle() {
        if (discardPile.size() > 1) {
            Card top = discardPile.remove(discardPile.size() - 1);
            drawPile.addAll(discardPile);
            discardPile.clear();
            discardPile.add(top);
            shuffle();
        }
    }
    
    /**
     * Adds a card to the draw pile.
     *
     * @param card the card to add to the draw pile
     */
    public void addCardToDrawPile(Card card) {
        drawPile.add(card);
    }
}
