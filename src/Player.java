
import java.util.*;
import java.util.stream.Collectors;

public class Player {
    private String name;
    private List<Card> hand;
    private int score;
    
    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
        score = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }
    
    public void clearHand() {
        hand = new ArrayList<>();
    }
    
    public int getScore() {
        return score;
    }
    
    public void addScore(int s) {
        score += s;
    }
    
    public void addCard(Card card) {
        hand.add(card);
    }
    
    public void removeCard(Card card) {
        hand.remove(card);
    }
    
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
    
        return null; // No playable card
    }
    
    
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
    
    public Card playCard(Card card) {
        removeCard(card);
        return card;
    }
    
    @Override
    public String toString() {
        return name + " " + hand;
    }
}
