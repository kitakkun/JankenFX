package janken.system.datamodel;

import janken.system.datamodel.Hand;
import janken.system.datamodel.Judge;

/**
 * どんな手を出してどんな勝敗結果となったかを一括で管理するためのクラス
 */
public class Record {
    private final Hand hand;
    private final Judge judge;

    /**
     * コンストラクタ
     * @param hand 出した手
     * @param judge 勝敗情報
     */
    public Record(Hand hand, Judge judge) {
        this.hand = hand;
        this.judge = judge;
    }

    /**
     * 手を取得
     * @return 出した手
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * 勝敗を取得
     * @return 勝敗情報
     */
    public Judge getJudge() {
        return judge;
    }
}
