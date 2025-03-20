package com.duocardgame;

import java.util.*;

/**
 * Implements the mediator for the Duo Card Game, managing game flow, players, deck, and game state.
 * <p>
 * This class is responsible for starting the game, processing rounds, updating scores,
 * and managing special actions like reversing play direction or shuffling hands.
 * It also logs game status using a CSV logger.
 * </p>
 */
public class DuoCardGame implements IGameMediator {
    /** List of players participating in the game. */
    private List<Player> players;
    
    /** The deck containing the draw and discard piles. */
    private Deck deck;
    
    /** The index of the current player in the players list. */
    private int currentPlayerIndex;
    
    /** The direction of play: 1 for left (forward) and -1 for right (backward). */
    private int direction;
    
    /** The current round number. */
    private int roundNumber;
    
    /** Indicates whether the current round has ended. */
    private boolean roundEnded;
    
    /** Indicates whether the game is over. */
    private boolean gameOver;
    
    /** The player who won the game (if game over). */
    private Player gameWinner;
    
    /** The current active color, which can change during gameplay. */
    private CardColor currentColor;
    
    /** Logger for tracking and saving game status to a CSV file. */
    private CSVLogger logger;

    /**
     * Constructs a new DuoCardGame instance and initializes the game state.
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
     * Starts the game by initializing players, selecting a dealer, shuffling and dealing cards,
     * and setting up the initial discard pile.
     */
    public void startGame() {
        // Only initialize players when first starting the game.
        if (players.isEmpty()) {
            int numPlayers = new Random().nextInt(2, 5);
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
        
        // Set the starting player (next to the dealer).
        currentPlayerIndex = (players.indexOf(dealer) + 1) % players.size();
        
        // Draw the starting card and set the current color.
        Card startingCard = deck.drawCard();
        deck.putCardToDiscardPile(startingCard);
        currentColor = startingCard.getColor();
        System.out.println("Starting Discard Pile card: " + startingCard);
        
        // Execute the effect if the starting card is an action card.
        if (startingCard instanceof ActionCard) {
            ((ActionCard) startingCard).executeEffect(this);
        }
    }
    
    /**
     * Plays a round of the game.
     * <p>
     * The method processes each player's turn, handles card plays, draws, and special actions.
     * It updates the round and game state, logs the game status, and resets the round if necessary.
     * </p>
     */
    public void playRound() {
        System.out.println("Round started. Current direction: " + (direction == 1 ? "Left" : "Right"));
        roundEnded = false;
        while (!roundEnded) {
            Player currentPlayer = getCurrentPlayer();
            Card topCard = deck.getTopDiscardPileCard();
            System.out.println("Top card: " + topCard + " | Current color: " + currentColor);
            System.out.println(currentPlayer.getName() + "'s turn.");
            
            // Player chooses a playable card.
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
                // If no playable card, draw one from the deck.
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
            
            // Check if the current player has emptied their hand, winning the round.
            if (currentPlayer.getHand().isEmpty()) {
                System.out.println(currentPlayer.getName() + " wins the round!");
                updateScores(currentPlayer);
                roundEnded = true;
            }
            if (!roundEnded) {
                moveToNextPlayer();
            }
        }
        
        // Check if any player's score has reached or exceeded 500, ending the game.
        for (Player p : players) {
            if (p.getScore() >= 500) {
                gameOver = true;
                gameWinner = p;
                break;
            }
        }

        // Log the game status at the end of the round.
        logger.logGameStatus(this);
        roundNumber += 1;
        
        // Reset the round if the game is not over.
        if (!gameOver) {
            resetRound();
        }
    }
    
    /**
     * Updates the scores after a round is won.
     * <p>
     * The round winner receives points equal to the sum of the scores of all cards remaining in the other players' hands.
     * </p>
     *
     * @param roundWinner the player who won the round
     */
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
    
    /**
     * Resets the game state for a new round.
     * <p>
     * This includes creating a new deck, clearing each player's hand, and starting the game again.
     * </p>
     */
    private void resetRound() {
        deck = new Deck();
        for (Player p : players) {
            p.clearHand();
        }
        startGame();
    }
    
    /**
     * Checks if the game is over.
     *
     * @return {@code true} if the game is over; {@code false} otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Returns the winner of the game.
     *
     * @return the winning player, or {@code null} if the game is not over
     */
    public Player getWinner() {
        return gameWinner;
    }
    
    /**
     * Reverses the direction of play.
     */
    public void reverseDirection() {
        direction *= -1;
    }
    
    /**
     * Shuffles the hands of all players.
     * <p>
     * All cards from each player's hand are combined, shuffled, and then redistributed evenly among the players.
     * </p>
     */
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
    
    /**
     * Sets the current active color.
     *
     * @param color the new current color
     */
    public void setCurrentColor(CardColor color) {
        currentColor = color;
    }
    
    /**
     * Returns the current player whose turn it is.
     *
     * @return the current {@code Player}
     */
    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    /**
     * Moves to the next player's turn based on the current direction.
     * <p>
     * This method updates the current player index, wrapping around the list of players if necessary.
     * </p>
     */
    @Override
    public void moveToNextPlayer() {
        // Ensure the direction is either 1 (forward) or -1 (backward)
        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
        if (currentPlayerIndex < 0) {
            currentPlayerIndex += players.size();
        }
    }

    /**
     * Returns a defensive copy of the current deck.
     * <p>
     * This prevents external modifications to the internal state of the deck.
     * </p>
     *
     * @return a copy of the current {@code Deck}
     */
    @Override
    public Deck getDeck() {
        return new Deck(deck); // Return a copy to avoid direct modification
    }

    /**
     * Sets the current deck to a copy of the provided deck.
     * <p>
     * This method ensures that the internal deck is not directly modified by external references.
     * </p>
     *
     * @param deck the new deck to set
     */
    @Override
    public void setDeck(Deck deck) {
        this.deck = new Deck(deck); // Store a defensive copy
    }

    /**
     * Selects the dealer for the game.
     * <p>
     * Each player draws a card, and the player with the highest scoring card is selected as the dealer.
     * After selection, the drawn cards are returned to the deck and it is reshuffled.
     * </p>
     *
     * @return the {@code Player} selected as the dealer
     */
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
    
    /**
     * Ends the current round.
     */
    @Override
    public void endRound() {
        roundEnded = true;
    }
    
    /**
     * Returns a shallow copy of the list of players.
     *
     * @return a new list containing the players in the game
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players); // Shallow copy of the list
    }    

    /**
     * Returns the current round number.
     *
     * @return the round number
     */
    public int getRoundNumber() {
        return roundNumber;
    }
}
