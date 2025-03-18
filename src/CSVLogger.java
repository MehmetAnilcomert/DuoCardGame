import java.io.*;

public class CSVLogger {
    public void logGameStatus(DuoCardGame game) {
        try {
            File file = new File("Files/game_status.csv");
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fw);
            StringBuilder sb = new StringBuilder();
            sb.append("Round End,");
            for (Player p : game.getPlayers()) {
                sb.append(p.getName() + ":" + p.getScore() + ",");
            }
            pw.println(sb.toString());
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
