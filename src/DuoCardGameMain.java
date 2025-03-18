
public class DuoCardGameMain {
    public static void main(String[] args) {
        DuoCardGame game = new DuoCardGame();
        game.startGame();
        while (!game.isGameOver()) {
            game.playRound();
        }
        System.out.println("Game over! " + game.getWinner().getName() + " wins the game with score: " + game.getWinner().getScore());
    }
}
