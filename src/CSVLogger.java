import java.io.*;

/**
 * A logger for logging the status of the Duo card game to a CSV file.
 * It logs the game's round number, players' scores, and the winner player.
 * The CSV file is created in the "Files" directory and is appended with each log.
 */
public class CSVLogger {
    private boolean isFirstLog = true;  // Track whether it's the first time logging

    /**
     * Logs the current Duo card game status to a CSV file.
     * The status includes the round number, players' names, and their scores.
     * If the game is over, the winner's name is logged as well.
     * The "game_status.csv" file is created in the "Files" directory, and each log entry is appended.
     *
     * @param game the current Duo card game instance to log.
     * @throws IOException if an I/O exception occurs if the file could not be written properly
     */
    public void logGameStatus(DuoCardGame game) {
        try {
            File file = new File("Files/game_status.csv");
            file.getParentFile().mkdirs(); // Create Files directory if it doesn't exist
            
            // Check if it's the first log, if so overwrite; otherwise, append
            FileWriter fw = new FileWriter(file, !isFirstLog); // Use false for overwrite, true for append
            PrintWriter pw = new PrintWriter(fw);
            StringBuilder sb = new StringBuilder();

            // If it's the first log, write the headers
            if (isFirstLog) {
                StringBuilder header = new StringBuilder();
                header.append("Round");
                for (Player p : game.getPlayers()) {
                    header.append(",").append(p.getName());
                }
                pw.println(header.toString());
                isFirstLog = false;  // Mark subsequent logs to append
            }

            // Log the current round and player scores
            sb.append("Round ").append(game.getRoundNumber());
            for (Player p : game.getPlayers()) {
                sb.append(",").append(p.getScore());
            }
            pw.println(sb.toString());
            
            // Log the winner player
            if (game.isGameOver()) {
                StringBuilder footer = new StringBuilder();
                footer.append("Winner,").append(game.getWinner().getName());
                pw.println(footer.toString());
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
