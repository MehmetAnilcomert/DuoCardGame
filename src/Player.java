
import java.util.*;

public class Player {
    private String name;
    private List<Card> hand;
    private int score;
    private Random random;
    
    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
        score = 0;
        random = new Random();
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
        for (CardColor color : CardColor.values()){
            colorCount.put(color, 0);
        }
        for (Card c : hand) {
            if (c.getColor() != null) {
                colorCount.put(c.getColor(), colorCount.get(c.getColor()) + 1);
            }
        }
        CardColor chosen = CardColor.BLUE;
        int max = -1;
        for (Map.Entry<CardColor, Integer> entry : colorCount.entrySet()){
            if (entry.getValue() > max){
                max = entry.getValue();
                chosen = entry.getKey();
            }
        }
        return chosen;
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
