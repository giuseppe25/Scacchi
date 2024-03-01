package src.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Scoreboard implements Serializable {
    private static final long serialVersionUID = 326748962L; // Choose any long value

    private static Scoreboard instance; // Istanza singleton

    private List<Score> scores;

    public static synchronized Scoreboard getInstance() {
        if (instance == null) {
            instance = new Scoreboard();
        }
        return instance;
    }
    public Scoreboard() {
        this.scores = new ArrayList<>();
    }

    // Inner class Score implementa Serializable
    public static class Score implements Serializable {
        private String playerName;
        private int gamesPlayed;
        private int gamesWon;

        public Score(String playerName) {
            this.playerName = playerName;
            this.gamesPlayed = 0;
            this.gamesWon = 0;
        }

        public void playGame() {
            gamesPlayed++;
        }

        public void winGame() {
            gamesWon++;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getGamesPlayed() {
            return gamesPlayed;
        }

        public int getGamesWon() {
            return gamesWon;
        }

    }

    public void addToPlayer(String playerName, boolean isWin) {
        boolean playerFound = false;

        for (Score score : scores) {
            if (score.getPlayerName().equals(playerName)) {
                score.playGame(); // Incrementa il numero di partite giocate
                if (isWin) {
                    score.winGame(); // Incrementa il numero di partite vinte solo se la partita è stata vinta
                }
                playerFound = true;
                break;
            }
        }

        if (!playerFound) {
            System.out.println("Adding  " + playerName);

            // Se il giocatore non è stato trovato, crea un nuovo giocatore con il nome fornito,
            // aggiorna le partite giocate e, se necessario, le partite vinte.
            Score newPlayer = new Score(playerName);
            newPlayer.playGame();
            if (isWin) {
                newPlayer.winGame();
            }
            scores.add(newPlayer);
        }
        saveScoreboard();
    }



    public List<Score> getScores() {
        return scores;
    }

    public void saveScoreboard( ) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("scoreboard.ser"))) {
            System.out.println("Saving scoreboard");
            oos.writeObject(instance);  // Serialize the instance instead of scores
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadScoreboard( ) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("scoreboard.ser"))) {
            instance = (Scoreboard) ois.readObject();  // Deserialize the instance
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}