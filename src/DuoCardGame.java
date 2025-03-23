import java.util.*;

/**
 * The DuoCardGame class implements the IGameMediator interface and represents a card game involving multiple players.
 * The game is based on a modified version of a card game where players draw and play cards, and accumulate points.
 * The game ends when one player reaches a score of 500 or more.
 */
public class DuoCardGame implements IGameMediator {
    private List<Player> players;
    private Deck deck;
    private int currentPlayerIndex;
    private int direction; // 1: left, -1: right
    private int roundNumber;
    private boolean roundEnded;
    private boolean gameOver;
    private Player gameWinner;
    private CardColor currentColor;
    private CSVLogger logger;

    /**
     * Constructor to initialize the DuoCardGame.
     */
    public DuoCardGame() {
        players = new ArrayList<>();
        deck = new Deck();
        direction = 1;
        roundEnded = false;
        gameOver = false;
        currentColor = null;
        logger = new CSVLogger();
        roundNumber = 1;
    }
    
    /**
     * Starts the game by initializing players, selecting the dealer, and shuffling the deck.
     * The first round starts by dealing 7 cards to each player.
     */
    public void startGame() {
        if (players.isEmpty()) { // Only initialize players when first starting the game
            int numPlayers = new Random().nextInt(3) + 2; // 2 to 4 players
            System.out.println("Starting game with " + numPlayers + " players.");
            for (int i = 1; i <= numPlayers; i++) {
                players.add(new Player("Player " + i));
            }
        }
        
        // Dealer selection: each player draws a card; highest score becomes dealer.
        Player dealer = selectDealer();
        System.out.println("Dealer: " + dealer.getName());
        
        deck.shuffle();
        deck.dealCards(players, 7);
        
        for (Player p : players) { // Give each player 7 cards and output their hands
            System.out.println(p.getName() + " hand: " + p.getHand());
        }
        
        currentPlayerIndex = (players.indexOf(dealer) + 1) % players.size();
        
        Card startingCard = deck.drawCard(); // Top card from Draw Pile starts and is put on the Discard Pile
        deck.putCardToDiscardPile(startingCard);
        currentColor = startingCard.getColor();
        System.out.println("Starting Discard Pile card: " + startingCard);
        
        if (startingCard instanceof ActionCard) { // If top card is an action card, its effect is followed
            ((ActionCard) startingCard).executeEffect(this);
        }
    }
    
    /**
     * Starts a round of the game. Players take turns to play cards or draw from the deck.
     * The round continues until a player empties their hand, after which scores are updated.
     */
    public void playRound() {
        System.out.println("Round started. Current direction: " + (direction == 1 ? "Left" : "Right"));
        roundEnded = false;
        while (!roundEnded) {
            Player currentPlayer = getCurrentPlayer();
            Card topCard = deck.getTopDiscardPileCard();
            System.out.println("Top card: " + topCard + " | Current color: " + currentColor);
            System.out.println(currentPlayer.getName() + "'s turn.");
            
            Card cardToPlay = currentPlayer.choosePlayableCard(topCard);
            if (cardToPlay != null) { // Player has a card that is playable
                currentPlayer.playCard(cardToPlay);
                deck.putCardToDiscardPile(cardToPlay);
                currentColor = cardToPlay.getColor();
                System.out.println(currentPlayer.getName() + " plays " + cardToPlay);
                if (cardToPlay instanceof ActionCard) {
                    ((ActionCard) cardToPlay).executeEffect(this);
                }
            } else { // Player has no card that is playable, draw from the Draw Pile
                Card drawn = deck.drawCard();
                currentPlayer.addCard(drawn);
                System.out.println(currentPlayer.getName() + " draws " + drawn);
                if (drawn.isPlayable(topCard)) { // If the drawn card can be played then the player plays it
                    currentPlayer.playCard(drawn);
                    deck.putCardToDiscardPile(drawn);
                    currentColor = drawn.getColor();
                    System.out.println(currentPlayer.getName() + " plays drawn card " + drawn);
                    if (drawn instanceof ActionCard) {
                        ((ActionCard) drawn).executeEffect(this);
                    }
                }
            }
            
            if (currentPlayer.getHand().isEmpty()) { // Players has no cards in their hand and wins the round
                System.out.println(currentPlayer.getName() + " wins the round!");
                updateScores(currentPlayer);
                roundEnded = true;
            }
            if (!roundEnded) {
                moveToNextPlayer();
            }
        }
        
        for (Player p : players) { // Check if any players have a score of 500 or higher and declare the winner
            if (p.getScore() >= 500) {
                gameOver = true;
                gameWinner = p;
                break;
            }
        }

        logger.logGameStatus(this); // Logger logs the status of each round
        roundNumber += 1;
        
        if (!gameOver) {
            resetRound();
        }
    }
    
    /**
     * Updates the scores of the players after each round. The winner earns points based on the remaining cards in opponents' hands.
     *
     * @param roundWinner The player who won the round.
     */
    private void updateScores(Player roundWinner) {
        int roundScore = 0;
        for (Player p : players) { // Add opponent players' remaining cards' scores to the winner player of a round
            if (p != roundWinner) {
                for (Card c : p.getHand()) {
                    roundScore += c.getScore();
                }
            }
        }
        roundWinner.addScore(roundScore);
        System.out.println(roundWinner.getName() + " earns " + roundScore + " points. Total score: " + roundWinner.getScore());
    }
    
    /**
     * Resets the round by shuffling the deck and clearing players' hands.
     * The game will then restart with a new round.
     */
    private void resetRound() {
        deck = new Deck();
        for (Player p : players) {
            p.clearHand();
        }
        startGame();
    }
    
    /**
     * Returns whether the game is over.
     * 
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Returns the winner of the game.
     * 
     * @return The player who won the game.
     */
    public Player getWinner() {
        return gameWinner;
    }
    
    /**
     * Reverses the direction of play. If the current direction is 1 (left), it becomes -1 (right), and vice versa.
     */
    public void reverseDirection() {
        direction *= -1; // Reverses direction by inverting (1: left, -1: right)
    }
    
    /**
     * Shuffles the hands of all players by gathering all their cards into a shuffle pile, shuffling it, and redistributing the cards.
     */
    public void shuffleHands() {
        List<Card> shufflePile = new ArrayList<>();
        for (Player p : players) { // Get all cards from players' hands and shuffle them 
            shufflePile.addAll(p.getHand());
            p.clearHand();
        }
        Collections.shuffle(shufflePile);
        int index = 0;
        while (!shufflePile.isEmpty()) { // Distribute cards to players until the shuffle pile is empty
            players.get(index % players.size()).addCard(shufflePile.remove(0));
            index++;
        }
    }
    
    /**
     * Sets the current color of the game after a Wild Card is played.
     * 
     * @param color The new color to be set.
     */
    public void setCurrentColor(CardColor color) {
        currentColor = color; // Update the current color in the game, set after playing a Wild Card
    }
    
    /**
     * Retrieves the player whose turn it currently is.
     * 
     * @return the current Player.
     */
    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    /**
    * Advances the turn for the next Player based on the current direction of play (left or right).
    */
    @Override
    public void moveToNextPlayer() {
        // Move to the next player based on the direction (1 for left, -1 for right), also ensures index wraps around correctly within the player list.
        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
        if (currentPlayerIndex < 0) { 
            currentPlayerIndex += players.size();
        }
    }

    /**
     * Retrieves the copy of the current deck.
     * 
     * @return the Deck instance copy.
     */
    @Override
    public Deck getDeck() {
        return new Deck(deck); // Return a copy to avoid direct modification
    }

    /**
     * Sets a new deck to be used.
     * 
     * @param deck the Deck instance to be set.
     */
    @Override
    public void setDeck(Deck deck) {
        this.deck = new Deck(deck); // Store a defensive copy
    }

    /**
     * Selects the dealer by having each player draw a card, and the player with the highest card score becomes the dealer.
     * The drawn cards are returned to the deck and shuffled after selection.
     * 
     * @return The player selected as the dealer.
     */
    @Override
    public Player selectDealer() {
        Player selected = null;  // Each player draws one card, and the player with the highest-scoring card is selected as the dealer 
        int highest = -1;
        for (Player p : players) { 
            Card drawn = deck.drawCard();
            System.out.println(p.getName() + " draws " + drawn + " for dealer selection.");
            if (drawn.getScore() > highest) {
                highest = drawn.getScore();
                selected = p;
            }
            deck.addCardToDrawPile(drawn);
        }
        deck.shuffle(); // The drawn cards are returned to the draw pile and shuffled afterward
        return selected;
    }
    
    /**
     * Ends the current round of the game to transition to the next round.
     * Called when a player wins the round by having no cards in their hand.
     */
    @Override
    public void endRound() {
        roundEnded = true;
    }
    
    /**
     * Returns a list of players participating in the game.
     * 
     * @return A new list containing all players.
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players); // Returns a new list containing the same Player instances (shallow copy of players)
    }    

    /**
     * Returns the current round number.
     * 
     * @return The current round number.
     */
    public int getRoundNumber() {
        return roundNumber;
    }
    
    /**
     * Sets the current round number.
     * 
     * @param roundNumber The new round number.
     */
    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }
}
