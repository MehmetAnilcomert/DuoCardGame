
public class DuoCardGameMain {
    public static void main(String[] args) {
        DuoCardGame game = new DuoCardGame();  // Initialize and start DuoCardGame
        game.startGame();
        while (!game.isGameOver()) { // Keep playing rounds until the game is over and announce the winner
            game.playRound();
        }
        System.out.println("Game over! " + game.getWinner().getName() + " wins the game with score: " + game.getWinner().getScore());
    }
}
