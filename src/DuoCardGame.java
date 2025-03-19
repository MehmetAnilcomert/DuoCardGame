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
        
        for (Player p : players) {
            System.out.println(p.getName() + " hand: " + p.getHand());
        }
        
        currentPlayerIndex = (players.indexOf(dealer) + 1) % players.size();
        
        Card startingCard = deck.drawCard();
        deck.putCardToDiscardPile(startingCard);
        currentColor = startingCard.getColor();
        System.out.println("Starting Discard Pile card: " + startingCard);
        
        if (startingCard instanceof ActionCard) {
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
            if (cardToPlay != null) {
                currentPlayer.playCard(cardToPlay);
                deck.putCardToDiscardPile(cardToPlay);
                currentColor = cardToPlay.getColor();
                System.out.println(currentPlayer.getName() + " plays " + cardToPlay);
                if (cardToPlay instanceof ActionCard) {
                    ((ActionCard) cardToPlay).executeEffect(this);
                }
            } else {
                Card drawn = deck.drawCard();
                currentPlayer.addCard(drawn);
                System.out.println(currentPlayer.getName() + " draws " + drawn);
                if (drawn.isPlayable(topCard)) {
                    currentPlayer.playCard(drawn);
                    deck.putCardToDiscardPile(drawn);
                    currentColor = drawn.getColor();
                    System.out.println(currentPlayer.getName() + " plays drawn card " + drawn);
                    if (drawn instanceof ActionCard) {
                        ((ActionCard) drawn).executeEffect(this);
                    }
                }
            }
            
            if (currentPlayer.getHand().isEmpty()) {
                System.out.println(currentPlayer.getName() + " wins the round!");
                updateScores(currentPlayer);
                roundEnded = true;
            }
            if (!roundEnded) {
                moveToNextPlayer();
            }
        }
        
        for (Player p : players) {
            if (p.getScore() >= 500) {
                gameOver = true;
                gameWinner = p;
                break;
            }
        }

        logger.logGameStatus(this);
        roundNumber += 1;
        
        if (!gameOver) {
            resetRound();
        }
    }
    
    private void updateScores(Player roundWinner) {
        int roundScore = 0;
        for (Player p : players) {
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
        direction *= -1;
    }
    
    public void shuffleHands() {
        List<Card> shufflePile = new ArrayList<>();
        for (Player p : players) {
            shufflePile.addAll(p.getHand());
            p.clearHand();
        }
        Collections.shuffle(shufflePile);
        int index = 0;
        while (!shufflePile.isEmpty()) {
            players.get(index % players.size()).addCard(shufflePile.remove(0));
            index++;
        }
    }
    
    public void setCurrentColor(CardColor color) {
        currentColor = color;
    }
    
    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    @Override
    public void moveToNextPlayer() {
        // Ensure the direction is either 1 (forward) or -1 (backward)
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
        Player selected = null;
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
        deck.shuffle();
        return selected;
    }
    
    @Override
    public void endRound() {
        roundEnded = true;
    }
    
    public List<Player> getPlayers() {
        return new ArrayList<>(players); // Shallow copy of the list
    }    

    public int getRoundNumber() {
        return roundNumber;
    }
}
