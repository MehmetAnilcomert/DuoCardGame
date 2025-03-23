import java.util.*;

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
    
    private void resetRound() {
        deck = new Deck();
        for (Player p : players) {
            p.clearHand();
        }
        startGame();
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public Player getWinner() {
        return gameWinner;
    }
    
    public void reverseDirection() {
        direction *= -1; // Reverses direction by inverting (1: left, -1: right)
    }
    
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
    
    public void setCurrentColor(CardColor color) {
        currentColor = color; // Update the current color in the game, set after playing a Wild Card
    }
    
    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    @Override
    public void moveToNextPlayer() {
        // Move to the next player based on the direction (1 for left, -1 for right), also ensures index wraps around correctly within the player list.
        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
        if (currentPlayerIndex < 0) { 
            currentPlayerIndex += players.size();
        }
    }

    
    @Override
    public Deck getDeck() {
        return new Deck(deck); // Return a copy to avoid direct modification
    }

    @Override
    public void setDeck(Deck deck) {
        this.deck = new Deck(deck); // Store a defensive copy
    }

    
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
    
    @Override
    public void endRound() {
        roundEnded = true;
    }
    
    public List<Player> getPlayers() {
        return new ArrayList<>(players); // Returns a new list containing the same Player instances (shallow copy of players)
    }    

    public int getRoundNumber() {
        return roundNumber;
    }
}
