import java.util.*;

public class Deck {
    private List<Card> drawPile;
    private List<Card> discardPile;
    private List<Card> shufflePile;
    
    public Deck() {
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        shufflePile = new ArrayList<>();
        initializeDeck();
        shuffle();
    }
    
    public void initializeDeck() {
        // Number Cards: 1 adet 0 ve 2 adet 1-9 her renkten.
        for (CardColor color : CardColor.values()) {
            if (color != CardColor.WILD) {
                drawPile.add(new NumberCard(color, 0));
                for (int num = 1; num <= 9; num++) {
                    drawPile.add(new NumberCard(color, num));
                    drawPile.add(new NumberCard(color, num));
                }
            }
        }
        // Action Cards: DRAW_TWO, REVERSE, SKIP (her renkten 2 adet)
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
        // Wild ve Wild Draw Four: Her biri 4 adet
        for (int i = 0; i < 4; i++) {
            drawPile.add(new ActionCard(CardColor.WILD, ActionType.WILD));
            drawPile.add(new ActionCard(CardColor.WILD, ActionType.WILD_DRAW_FOUR));
        }
        // Shuffle Hands: 1 adet
        drawPile.add(new ActionCard(CardColor.WILD, ActionType.SHUFFLE_HANDS));
    }
    
    public void shuffle() {
        Collections.shuffle(drawPile);
    }
    
    public void dealCards(List<Player> players, int count) {
        for (int i = 0; i < count; i++) {
            for (Player p : players) {
                if (drawPile.isEmpty()) reshuffle();
                p.addCard(drawCard());
            }
        }
    }
    
    public Card drawCard() {
        if (drawPile.isEmpty()) {
            reshuffle();
        }
        return drawPile.remove(0);
    }
    
    public Card getTopDiscardPileCard() {
        if (discardPile.isEmpty()) return null;
        return discardPile.get(discardPile.size() - 1);
    }
    
    public void putCardToDiscardPile(Card card) {
        discardPile.add(card);
    }
    
    public void reshuffle() {
        if (discardPile.size() > 1) {
            Card top = discardPile.remove(discardPile.size() - 1);
            drawPile.addAll(discardPile);
            discardPile.clear();
            discardPile.add(top);
            shuffle();
        }
    }
    
    public void addCardToDrawPile(Card card) {
        drawPile.add(card);
    }
}