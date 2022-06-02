package janken.util;

import janken.system.datamodel.Hand;
import janken.system.datamodel.Judge;
import janken.system.datamodel.RecordBook;

/**
 * RecordBookのデータをカウントする機能を提供します．
 */
public class Counter {
    /**
     * RecordBook内に含まれるHandの数をカウントします．
     * @param rb 処理対象のRecordBook
     * @param hand カウントする手
     * @return RecordBook内でカウントした手の数
     */
    public static int countHand(RecordBook rb, Hand hand) {
        int count = 0;
        for (int i = 0; i < rb.countRecords(); i++) {
            if (rb.getRecord(i).getHand() == hand) {
                count++;
            }
        }
        return count;
    }

    /**
     * RecordBook内に含まれるJudgeの数をカウントします．
     * @param rb 処理対象のRecordBook
     * @param judge カウントする勝敗情報
     * @return RecordBook内でカウントした勝敗情報の数
     */
    public static int countJudge(RecordBook rb, Judge judge) {
        int count = 0;
        for (int i = 0; i < rb.countRecords(); i++) {
            if (rb.getRecord(i).getJudge() == judge) {
                count++;
            }
        }
        return count;
    }
}
