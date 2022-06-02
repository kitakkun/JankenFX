package janken.system.manager;

import janken.system.core.Player;

public class ScoreProvider {
    private int[] scores;
    private int[] winSequences;     // 連勝数
    private int[] loseSequences;    // 連敗数
    private Player[] players;
    public ScoreProvider(Player[] players, int[] winSequences, int[] loseSequences, int[] scores) {
        this.scores = scores;
        this.players = players;
        this.winSequences = winSequences;
        this.loseSequences = loseSequences;
    }

    public int getScore(Player player) {
        for (int i = 0; i < players.length; i++) {
            if (player.equals(players[i])) {
                return scores[i];
            }
        }
        System.out.println("The specified player doesn't exist.");
        return 0;
    }

    public int getWinSequence(Player player) {
        for (int i = 0; i < players.length; i++) {
            if (player.equals(players[i])) {
                return winSequences[i];
            }
        }
        System.out.println("The specified player doesn't exist.");
        return 0;
    }

    public int getLoseSequence(Player player) {
        for (int i = 0; i < players.length; i++) {
            if (player.equals(players[i])) {
                return loseSequences[i];
            }
        }
        System.out.println("The specified player doesn't exist.");
        return 0;
    }
}
