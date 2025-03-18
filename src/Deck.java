
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        // Add number cards (0-9) for each color
        for (CardColor color : new CardColor[]{CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW}) {
            // One zero card per color
            cards.add(new NumberCard(color, 0));
            // Two of each number 1-9 per color
            for (int i = 1; i <= 9; i++) {
                cards.add(new NumberCard(color, i));
                cards.add(new NumberCard(color, i));
            }

            // Add action cards
            // Two of each per color: Skip, Reverse, Draw Two
            for (int i = 0; i < 2; i++) {
                cards.add(new ActionCard(color, ActionType.SKIP));
                cards.add(new ActionCard(color, ActionType.REVERSE));
                cards.add(new ActionCard(color, ActionType.DRAW_TWO));
            }
        }

        // Add Wild cards (4 each)
        for (int i = 0; i < 4; i++) {
            cards.add(new ActionCard(CardColor.WILD, ActionType.WILD));
            cards.add(new ActionCard(CardColor.WILD, ActionType.WILD_DRAW_FOUR));
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        return cards.remove(cards.size() - 1);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    // Kartları oyunculara dağıtan metot
    public void dealCards(List<Player> players, int count) {
        for (int i = 0; i < count; i++) {
            for (Player p : players) {
                if (drawPile.isEmpty()) {
                    reshuffle();
                }
                p.addCard(drawCard());
            }
        }
    }

    // Bir kartı Discard Pile'a ekleyen metot
    public void putCardToDiscardPile(Card card) {
        discardPile.add(card);
    }

    // Bir kartı Draw Pile'a ekleyen metot (örneğin dealer seçiminde kullanılıyor)
    public void addCardToDrawPile(Card card) {
        drawPile.add(card);
    }

}
