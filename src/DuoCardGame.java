import java.util.*;

public class DuoCardGame implements IGameMediator {
    private List<Player> players;
    private Deck deck;
    private int currentPlayerIndex;
    private int direction; // 1: left, -1: right
    private boolean roundEnded;
    private boolean gameOver;
    private Player gameWinner;
    private CardColor currentColor;
    
    public DuoCardGame() {
        players = new ArrayList<>();
        deck = new Deck();
        direction = 1;
        roundEnded = false;
        gameOver = false;
        currentColor = null;
    }
    
    public void startGame() {
        int numPlayers = new Random().nextInt(3) + 2; // 2 to 4 players
        System.out.println("Starting game with " + numPlayers + " players.");
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player("Player" + i));
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
        
        CSVLogger logger = new CSVLogger();
        logger.logGameStatus(this);
        
        for (Player p : players) {
            if (p.getScore() >= 500) {
                gameOver = true;
                gameWinner = p;
                break;
            }
        }
        
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
            p.getHand().clear();
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
        List<Card> allCards = new ArrayList<>();
        for (Player p : players) {
            allCards.addAll(p.getHand());
            p.getHand().clear();
        }
        Collections.shuffle(allCards);
        int index = 0;
        while (!allCards.isEmpty()) {
            players.get(index % players.size()).addCard(allCards.remove(0));
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
        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
    }
    
    @Override
    public Deck getDeck() {
        return deck;
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
        return players;
    }

    
}
