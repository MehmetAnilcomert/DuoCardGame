package com.duocardgame;

import java.util.*;

/**
 * Represents a player in the card game. A player has a name, a hand of cards, and a score.
 * The class provides functionality for managing the player's hand, score, and making game decisions.
 */
public class Player {
    /** The name of the player. */
    private String name;
    /** The list of cards that make up the player's hand. */
    private List<Card> hand;
    /** The player's score. */
    private int score;
    
    /**
     * Constructs a new Player with the given name.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
        score = 0;
    }
    
    /**
     * Returns the name of the player.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns a copy of the player's hand.
     * This method returns a new list to ensure encapsulation.
     *
     * @return a list containing the cards in the player's hand
     */
    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }
    
    /**
     * Clears the player's hand by creating a new, empty list.
     */
    public void clearHand() {
        hand = new ArrayList<>();
    }
    
    /**
     * Returns the current score of the player.
     *
     * @return the player's score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Adds the specified amount to the player's score.
     *
     * @param s the score to add
     */
    public void addScore(int s) {
        score += s;
    }
    
    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add
     */
    public void addCard(Card card) {
        hand.add(card);
    }
    
    /**
     * Removes the specified card from the player's hand.
     *
     * @param card the card to remove
     */
    public void removeCard(Card card) {
        hand.remove(card);
    }
    
    /**
     * Chooses a playable card from the player's hand based on the top card of the discard pile.
     * The method categorizes playable cards into same-color cards, different-colored cards, and wild cards.
     * It then prioritizes the choice based on card color match and score.
     *
     * @param topCard the card on the top of the discard pile
     * @return a playable card if one exists; otherwise, {@code null}
     */
    public Card choosePlayableCard(Card topCard) {
        List<Card> sameColorCards = new ArrayList<>();
        List<Card> diffColoredCards = new ArrayList<>();
        List<Card> wildCards = new ArrayList<>();
    
        // Categorize cards in a single loop
        for (Card c : hand) {
            if (!c.isPlayable(topCard)) continue;
    
            if (c.getColor() == CardColor.WILD) {
                wildCards.add(c);
            } else if (c.getColor() == topCard.getColor()) {
                sameColorCards.add(c);
            } else {
                diffColoredCards.add(c);
            }
        }
    
        // Sort same-color cards by highest score
        sameColorCards.sort(Comparator.comparing(Card::getScore).reversed());
    
        Random random = new Random();
    
        // Randomly choose whether to prioritize same-color or different-color cards
        boolean prioritizeSameColor = random.nextBoolean();
    
        if (prioritizeSameColor && !sameColorCards.isEmpty()) {
            return sameColorCards.get(0); // Highest scoring same-color card
        }
        if (!diffColoredCards.isEmpty()) {
            return diffColoredCards.get(random.nextInt(diffColoredCards.size()));
        }
        if (!sameColorCards.isEmpty()) { // Fallback to same-color if needed
            return sameColorCards.get(0);
        }
        if (!wildCards.isEmpty()) {
            return wildCards.get(random.nextInt(wildCards.size())); // Random Wild card
        }
    
        return null; // No playable card available
    }
    
    /**
     * Chooses a color for a wild card based on the composition of the player's hand.
     * The method counts the non-WILD cards in the player's hand and selects the color
     * with the highest count. In the event of a tie or if no non-WILD cards are present,
     * a random non-WILD color is chosen.
     *
     * @return the chosen {@link CardColor} for the wild card
     */
    public CardColor chooseColor() {
        Map<CardColor, Integer> colorCount = new HashMap<>();
    
        // Initialize count for non-WILD colors only
        for (CardColor color : CardColor.values()) {
            if (color != CardColor.WILD) {
                colorCount.put(color, 0);
            }
        }
    
        // Count non-WILD colors in the player's hand
        for (Card c : hand) {
            if (c.getColor() != null && c.getColor() != CardColor.WILD) {
                colorCount.put(c.getColor(), colorCount.get(c.getColor()) + 1);
            }
        }
    
        // Find the maximum count and collect colors with that count
        List<CardColor> bestColors = new ArrayList<>();
        int max = -1;
        for (Map.Entry<CardColor, Integer> entry : colorCount.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                bestColors.clear();  // Clear previous choices
                bestColors.add(entry.getKey());
            } else if (entry.getValue() == max) {
                bestColors.add(entry.getKey());  // Add to list if same count
            }
        }
    
        // If no color has been chosen (only WILD cards in hand), pick a random non-WILD color
        if (bestColors.isEmpty()) {
            bestColors.addAll(colorCount.keySet()); // All non-WILD colors are valid choices
        }
    
        // Randomly select one of the best colors
        Random rand = new Random();
        return bestColors.get(rand.nextInt(bestColors.size()));
    }
    
    /**
     * Plays the specified card by removing it from the player's hand.
     *
     * @param card the card to play
     * @return the card that was played
     */
    public Card playCard(Card card) {
        removeCard(card);
        return card;
    }
    
    /**
     * Returns a string representation of the player, including the player's name and hand.
     *
     * @return a string representation of the player
     */
    @Override
    public String toString() {
        return name + " " + hand;
    }
}
