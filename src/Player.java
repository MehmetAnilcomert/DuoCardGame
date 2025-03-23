import java.util.*;

/**
 * Represents a player in the game, maintaining their name, hand of cards, and score.
 * The player can add, remove or choose playable cards from their hand, choose a color if they have played a wild card and track their score.
 */
public class Player {
    private String name;
    private List<Card> hand;
    private int score;
    
    /**
     * Creates a new player with the given name and an empty hand.
     * The player's initial score is set to 0.
     *
     * @param name the name of the player.
     */
    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
        score = 0;
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets a shallow copy of the player's hand.
     * Modifying the returned list does not affect the player's actual hand.
     *
     * @return a copy of the player's hand.
     */
    public List<Card> getHand() {
        return new ArrayList<>(hand); // Get shallow copy of the hand list (new list, but same Card instances)
    }
    
    /**
     * Clears the player's hand. Typically used when cards are distributed from a deck.
     */
    public void clearHand() {
        hand = new ArrayList<>(); // Clear the hand list, used in shuffling for Shuffle Hands card
    }
    
    /**
     * Gets the player's current score.
     *
     * @return the player's score.
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Adds the specified score to the player's current score.
     *
     * @param s the score to add.
     */
    public void addScore(int s) {
        score += s;
    }
    
    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add.
     */
    public void addCard(Card card) {
        hand.add(card);
    }
    
    /**
     * Removes the specified card from the player's hand.
     *
     * @param card the card to remove.
     */
    public void removeCard(Card card) {
        hand.remove(card);
    }
    
    /**
     * Chooses a playable card from the player's hand based on the top card of the discard pile.
     * The card is selected by categorizing cards into same-color, different-color, and wild cards.
     * If a same-color card is preferred, the highest scoring same-color card is chosen.
     * If no same-color cards are preferred or available, a random playable card (including different-color or wild cards) is chosen.
     *
     * @param topCard the top card of the discard pile to match.
     * @return a playable card, or null if no playable card is found.
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
            return diffColoredCards.get(random.nextInt(diffColoredCards.size())); // Random different-color card
        }
        if (!sameColorCards.isEmpty()) { // Fallback to same-color if no playable different-color cards and prioritized 
            return sameColorCards.get(0);
        }
        if (!wildCards.isEmpty()) {
            return wildCards.get(random.nextInt(wildCards.size())); // Random Wild card
        }
    
        return null; // No playable card
    }
    
    /**
     * Chooses a color based on the player's hand.
     * The player will prefer colors they have the most of, but will fall back to a random non-WILD color if necessary.
     *
     * @return the chosen card color.
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
     * Plays the specified card and removes it from the player's hand.
     *
     * @param card the card to play.
     * @return the played card.
     */
    public Card playCard(Card card) {
        removeCard(card);
        return card;
    }

    /**
     * Returns a string representation of the player, including their name and hand of cards.
     *
     * @return a string representation of the player.
     */
    @Override
    public String toString() {
        return name + " " + hand;
    }
}
