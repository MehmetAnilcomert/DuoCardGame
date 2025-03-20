package com.duocardgame;

/**
 * The main class to run the Duo Card Game.
 * <p>
 * This class contains the main method which initializes the game,
 * starts the game loop, and prints the final winner once the game is over.
 * </p>
 */
public class DuoCardGameMain {
    /**
     * The entry point of the Duo Card Game application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        DuoCardGame game = new DuoCardGame();
        game.startGame();
        while (!game.isGameOver()) {
            game.playRound();
        }
        System.out.println("Game over! " + game.getWinner().getName() + " wins the game with score: " + game.getWinner().getScore());
    }
}
