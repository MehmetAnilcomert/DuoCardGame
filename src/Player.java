
import java.util.*;

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
        return hand;
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
        List<Card> playable = new ArrayList<>();
        for (Card c : hand) {
            if (c.isPlayable(topCard))
                playable.add(c);
        }
        if (playable.isEmpty())
            return null;
        Card chosen = playable.get(0);
        for (Card c : playable) {
            if (c instanceof NumberCard && topCard instanceof NumberCard) {
                if (c.getColor() == topCard.getColor() && c.getScore() > chosen.getScore()){
                    chosen = c;
                }
            } else {
                chosen = c;
                break;
            }
        }
        return chosen;
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
