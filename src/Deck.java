import java.util.*;
import java.util.stream.Collectors;

public class Deck {
    private List<Card> drawPile;
    private List<Card> discardPile;
    
    public Deck() {
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    public Deck(Deck deck) {
        this.drawPile = deck.drawPile.stream().map(c -> c.copy()).collect(Collectors.toList());
        this.discardPile = deck.discardPile.stream().map(c -> c.copy()).collect(Collectors.toList());
    }

    public void initializeDeck() {
        // Number Cards: Each color has one '0' card and two of each number from 1 to 9
        for (CardColor color : CardColor.values()) {
            if (color != CardColor.WILD) {
                drawPile.add(new NumberCard(color, 0));
                for (int num = 1; num <= 9; num++) {
                    drawPile.add(new NumberCard(color, num));
                    drawPile.add(new NumberCard(color, num));
                }
            }
        }
        // Action Cards: DRAW_TWO, REVERSE and SKIP (2 for each color)
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
        // Wild Cards: WILD and WILD_DRAW_FOUR (4 each)
        for (int i = 0; i < 4; i++) {
            drawPile.add(new ActionCard(CardColor.WILD, ActionType.WILD));
            drawPile.add(new ActionCard(CardColor.WILD, ActionType.WILD_DRAW_FOUR));
        }
        // Shuffle Hands (1 card)
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