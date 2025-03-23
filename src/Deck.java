import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a deck of cards used in the Duo card game.
 * The deck consists of a draw pile and a discard pile, where cards are drawn and discarded during gameplay.
 * The deck is initialized with standard cards and shuffled when created.
 */
public class Deck {
    private List<Card> drawPile;
    private List<Card> discardPile;
    
    /**
     * Constructs a new Deck, initializing the draw pile with cards and shuffling the deck.
     */
    public Deck() {
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        initializeDeck(); // Initialize and shuffle deck when constructed
        shuffle();
    }

    /**
     * Copy constructor that creates a new Deck by copying the draw pile and discard pile from another deck.
     * 
     * @param deck the deck to copy from.
     */
    public Deck(Deck deck) { // Copy constructor used in getDeck implementation by DuoCardGame, uses the copy() method of Card in both piles
        this.drawPile = deck.drawPile.stream().map(c -> c.copy()).collect(Collectors.toList());
        this.discardPile = deck.discardPile.stream().map(c -> c.copy()).collect(Collectors.toList());
    }

    /**
     * Initializes the deck with the standard set of cards:
     * - Number cards for each color (0, 1-9)
     * - Action cards (DRAW_TWO, REVERSE, SKIP) for each color
     * - Wild cards (WILD, WILD_DRAW_FOUR, SHUFFLE_HANDS)
     */
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
    
    /**
     * Shuffles the draw pile to randomize the card order.
     */
    public void shuffle() {
        Collections.shuffle(drawPile);
    }
    
    /**
     * Deals a specified number of cards to each player in the provided list.
     * If the draw pile runs out, it reshuffles the discard pile to refill the draw pile.
     * 
     * @param players the list of players to deal cards to.
     * @param count the number of cards to deal to each player.
     */
    public void dealCards(List<Player> players, int count) {
        for (int i = 0; i < count; i++) { // Deal cards to each player in players parameter
            for (Player p : players) {
                if (drawPile.isEmpty()) reshuffle();
                p.addCard(drawCard());
            }
        }
    }
    
    /**
     * Draws a single card from the draw pile.
     * If the draw pile is empty, it reshuffles the discard pile to refill the draw pile.
     * 
     * @return the drawn card.
     */
    public Card drawCard() {
        if (drawPile.isEmpty()) {
            reshuffle();
        }
        return drawPile.remove(0);
    }
    
    /**
     * Retrieves the top card from the discard pile.
     * If the discard pile is empty, returns null.
     * 
     * @return the top card in the discard pile or null if the discard pile is empty.
     */
    public Card getTopDiscardPileCard() {
        if (discardPile.isEmpty()) return null; // Gets top card in Discard Pile if there are any cards in it
        return discardPile.get(discardPile.size() - 1);
    }
    
    /**
     * Adds a card to the discard pile.
     * 
     * @param card the card to be added to the discard pile.
     */
    public void putCardToDiscardPile(Card card) {
        discardPile.add(card);
    }
    
    /**
     * Reshuffles the discard pile into the draw pile.
     * The card on top of the discard pile is kept at the top of the new discard pile.
     * The reshuffling process randomizes the draw pile.
     */
    public void reshuffle() {
        if (discardPile.size() > 1) { // Reshuffles all cards when the draw pile has no cards, except for the card on top of discard pile
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
     * @param card the card to be added to the draw pile.
     */
    public void addCardToDrawPile(Card card) {
        drawPile.add(card);
    }
}
