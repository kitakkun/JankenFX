package janken.system.manager;

// 過去に出された手と勝敗結果を一括で管理する(GameDirectorから使用する)

import janken.system.core.Player;
import janken.system.datamodel.Hand;
import janken.system.datamodel.Judge;
import janken.system.datamodel.Record;
import janken.system.datamodel.RecordBook;

/**
 * 各プレイヤーの過去に出された手および勝敗結果を一括管理するクラス
 * ゲーム進行側で用いるもので，プレイヤー側ではHistoryProviderを用いる．
 */
public class HistoryRecorder {

    RecordBook[] records;
    private final int playerCnt;
    private final Player[] players;

    /**
     * コンストラクタ
     * @param players ゲームに参加するプレイヤー
     */
    public HistoryRecorder(Player ...players) {
        this.players = players;
        this.playerCnt = players.length;
        records = new RecordBook[playerCnt];
        for (int i = 0; i < playerCnt; i++) {
            records[i] = new RecordBook();
        }
    }

    /**
     * その回の各プレイヤーの手と勝敗データを受け取り記録
     * @param hands 各プレイヤーの出した手
     * @param judges 各プレイヤーの勝敗結果
     */
    public void record(Hand[] hands, Judge[] judges) {
        for (int i = 0; i < playerCnt; i++) {
            Record rec = new Record(hands[i], judges[i]);
            records[i].add(rec);
        }
    }

    /**
     * 全履歴データのコピーを返す
     * @return RecordBookの配列
     */
    public RecordBook[] getRecords() {
        return records.clone();
    }

    /**
     * ユーザー側から安全に履歴データにアクセスするためのクラスHistoryProviderへの変換
     * @return 新しいHistoryProviderインスタンス
     */
    public HistoryProvider toProvider() {
        return new HistoryProvider(records, players);
    }

}
