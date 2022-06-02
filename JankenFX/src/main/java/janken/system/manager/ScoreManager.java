package janken.system.manager;

import janken.system.core.Player;
import janken.system.tool.ScoreCalculator;
import janken.system.datamodel.Judge;

/**
 * 各プレイヤーの点数を管理します．
 * 処理を軽くするために毎回全試合のデータを参照し算出することはしません．
 */
public class ScoreManager {

    private Player[] players;
    private int[] winSequences;     // 連勝数
    private int[] loseSequences;    // 連敗数
    private int[] scores;           // 点数
    private final int playerCnt;    // プレイヤー数

    public ScoreManager(Player ... players) {
        playerCnt = players.length;
        this.players = players;
        winSequences = new int[playerCnt];
        loseSequences = new int[playerCnt];
        scores = new int[playerCnt];
    }

    /**
     * 1試合分のスコアを計算します．
     */
    public void calcScore(Judge[] judges) {
        for (int i = 0; i < playerCnt; i++) {
            // 連勝数・連敗数の更新
            switch (judges[i]) {
                case WIN -> { winSequences[i]++; loseSequences[i] = 0; }
                case LOSE -> { winSequences[i] = 0; loseSequences[i]++; }
                case DRAW -> { winSequences[i] = 0; loseSequences[i] = 0;}
            }
            scores[i] += ScoreCalculator.calcScore(judges[i], winSequences[i], loseSequences[i]);
        }
    }
    /**
     * プレイヤー1人のスコアを取得します．
     */
    public int getScore(Player player) {
        for (int i = 0; i < playerCnt; i++) {
            if (player.equals(players[i])) {
                return scores[i];
            }
        }
        return 0;
    }

    /**
     * スコアが範囲内に収まっているかどうか
     */
    public boolean scoreIsInRange() {
        for (int score : scores) {
            if (score > 20000 || score < -1000000000) {
                return false;
            }
        }
        return true;
    }

    public ScoreProvider toProvider() {
        return new ScoreProvider(players, winSequences, loseSequences, scores);
    }
}
