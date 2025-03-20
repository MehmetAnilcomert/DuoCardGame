package com.duocardgame;

import java.io.*;

/**
 * A utility class for logging the game status to a CSV file.
 * <p>
 * This logger writes game status including the round number and each player's score,
 * and appends a footer with the winner's name when the game is over.
 * The first log writes the header, and subsequent logs append data.
 * </p>
 */
public class CSVLogger {
    /**
     * Tracks whether it's the first time logging. If true, the header is written; otherwise, logging is appended.
     */
    private boolean isFirstLog = true;

    /**
     * Logs the current game status to a CSV file.
     * <p>
     * The method writes the round number and player scores to a CSV file located at "Files/game_status.csv".
     * If it's the first log, headers are added. When the game is over, a footer with the winner's name is appended.
     * </p>
     *
     * @param game the {@code DuoCardGame} instance whose status is to be logged
     */
    public void logGameStatus(DuoCardGame game) {
        try {
            File file = new File("Files/game_status.csv");
            file.getParentFile().mkdirs();
            
            // Check if it's the first log, if so overwrite; otherwise, append.
            FileWriter fw = new FileWriter(file, !isFirstLog); // false for overwrite (first log), true for append
            PrintWriter pw = new PrintWriter(fw);
            StringBuilder sb = new StringBuilder();

            // If it's the first log, write the headers.
            if (isFirstLog) {
                StringBuilder header = new StringBuilder();
                header.append("Round");
                for (Player p : game.getPlayers()) {
                    header.append(",").append(p.getName());
                }
                pw.println(header.toString());
                isFirstLog = false;  // Subsequent logs will be appended.
            }

            // Log the current round and player scores.
            sb.append("Round ").append(game.getRoundNumber());
            for (Player p : game.getPlayers()) {
                sb.append(",").append(p.getScore());
            }
            pw.println(sb.toString());
            
            // If the game is over, log the winner's name.
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
