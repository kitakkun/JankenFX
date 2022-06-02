package janken.system.core;


import janken.system.tool.Judger;
import janken.system.datamodel.Hand;
import janken.system.datamodel.Judge;
import janken.system.manager.HistoryProvider;
import janken.system.manager.HistoryRecorder;
import janken.system.manager.ScoreManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * ゲーム進行を管理するクラス
 */
public class GameDirector {

    private final Player[] players;
    private final int playerCnt;
    protected final HistoryRecorder hr;

    protected HistoryProvider provider;

    /**
     * コンストラクタ
     *
     * @param players ゲームに参加するプレイヤーのインスタンス．
     */
    public GameDirector(Player... players) {
        playerCnt = players.length;
        this.players = players;
        hr = new HistoryRecorder(players);
    }

    /**
     * 一回じゃんけんをします．
     */
    public HistoryProvider proceed() {
        // 全員の手を決定する
        Hand[] hands = decideHands(hr.toProvider());
        // 勝敗を判定し更新する
        Judge[] judges = judge(hands);
        // 手と勝敗を記録しておく
        record(hands, judges);
        // 出した手を表示
        printHands(hands);
        // 勝敗を表示
        printJudges(judges);
        provider = hr.toProvider();
        return hr.toProvider();
    }

    /**
     * 複数回じゃんけんをします．
     * @param gameCnt じゃんけんをする回数
     * @return じゃんけんの履歴データ
     */
    public HistoryProvider proceed(int gameCnt) {
        for (int i = 0; i < gameCnt; i++) {
            proceed();
        }
        return hr.toProvider();
    }

    /**
     * 各プレイヤーの手を決定します
     *
     * @param hp プレイヤーの手決定の際に渡す履歴アクセス用クラスのインスタンス
     * @return 各プレイヤーの手
     */
    protected Hand[] decideHands(HistoryProvider hp) {
        Hand[] hands = new Hand[playerCnt];
        for (int i = 0; i < playerCnt; i++) {
            hands[i] = players[i].throwHand(hp);
        }
        return hands;
    }

    /**
     * 勝敗判定をします
     *
     * @param hands 各プレイヤーが出した手
     * @return 各プレイヤーの勝敗情報
     */
    protected Judge[] judge(Hand[] hands) {
        return Judger.judge(hands);
    }

    /**
     * 出した手とその勝敗結果をセットですべて記録します
     *
     * @param hands  各プレイヤーが出した手
     * @param judges 各プレイヤーの勝敗情報
     */
    protected void record(Hand[] hands, Judge[] judges) {
        hr.record(hands, judges);
    }

    /**
     * 各プレイヤーが出した手を標準出力に出力します
     *
     * @param hands 各プレイヤーが出した手
     */
    private void printHands(Hand[] hands) {
        for (int i = 0; i < playerCnt; i++) {
            System.out.printf("%s threw " + hands[i].toString() + "\n", players[i].getName());
        }
    }

    /**
     * 各プレイヤーの勝敗を標準出力へ出力します
     *
     * @param judges 各プレイヤーの勝敗情報
     */
    private void printJudges(Judge[] judges) {
        for (int i = 0; i < playerCnt; i++) {
            System.out.printf("[%s] " + judges[i].toString() + "\n", players[i].getName());
        }
    }

    /**
     * 各プレイヤーの最終結果を標準出力へ出力します
     */
    public void printResults() {
        for (int i = 0; i < playerCnt; i++) {
            HashMap<Judge, Integer> res = hr.getRecords()[i].countJudges();
            System.out.printf("[%s] %s: %d, %s: %d, %s: %d\n",
                    players[i].getName(),
                    Judge.WIN, res.get(Judge.WIN),
                    Judge.LOSE, res.get(Judge.LOSE),
                    Judge.DRAW, res.get(Judge.DRAW)
            );
        }
    }

    public HistoryProvider getProvider() {
        return provider;
    }

    /**
     * 勝者のプレイヤーを取得します．
     * @return 勝者のプレイヤーのインスタンス
     */
    public Player[] getWinners() {
        int maxWinCnt = getMaxWinCnt();
        ArrayList<Player> winners = new ArrayList<>();
        // それぞれの勝ち数を数える
        int[] winCounts = new int[playerCnt];
        for (int i = 0; i < playerCnt; i++) {
            winCounts[i] = provider.getRecordBook(players[i]).countJudges().get(Judge.WIN);
            if (maxWinCnt == winCounts[i]) {
                winners.add(players[i]);
            }
        }

        // すべて同じ勝ち数であるならば，勝敗決定はできない(勝者なし)
        if (Arrays.stream(winCounts).distinct().count() == 1) {
            return null;
        } else {
            return winners.toArray(new Player[0]);
        }
    }

    /**
     * 敗者のプレイヤーを取得します．
     * @return 敗者のプレイヤーのインスタンス
     */
    public Player[] getLosers() {
        Player[] winners = getWinners();
        if (winners == null) {
            return null;
        }
        ArrayList<Player> losers = new ArrayList<>();
        for (int i = 0; i < playerCnt; i++) {
            boolean isLoser = true;
            for (int j = 0; j < winners.length; j++) {
                if (winners[j].equals(players[i])) {
                    isLoser = false;
                    break;
                }
            }
            if (isLoser) {
                losers.add(players[i]);
            }
        }
        return losers.toArray(new Player[0]);
    }

    /**
     * 最大の勝利数を返す
     * @return 最大の勝利数
     */
    private int getMaxWinCnt() {
        int maxWinCnt = 0;
        for (int i = 0; i < playerCnt; i++) {
            int winCnt = provider.getRecordBook(players[i]).countJudges().get(Judge.WIN);
            if (winCnt > maxWinCnt) {
                maxWinCnt = winCnt;
            }
        }
        return maxWinCnt;
    }

    public Player[] getPlayers() {
        return players;
    }

    /**
     * 実行されたゲーム数を取得します．
     * @return 総ゲーム数
     */
    public int getGameCounts() {
        return provider.getRecordBook(players[0]).countRecords();
    }
}
