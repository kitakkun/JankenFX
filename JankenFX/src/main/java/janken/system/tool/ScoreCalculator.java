package janken.system.tool;

import janken.system.datamodel.Judge;
import janken.system.datamodel.Record;
import janken.system.datamodel.RecordBook;

/**
 * 点数を計算します．
 */
public class ScoreCalculator {
    public static int calcScore(RecordBook rb) {
        int score = 0;      // 点数
        int winSeq = 0;   // 連勝数
        int loseSeq = 0;    // 連敗数
        for (int i = 0; i < rb.countRecords(); i++) {
            Record rec = rb.getRecord(i);
            Judge judge = rec.getJudge();
            // 連勝数連敗数の更新
            switch (judge) {
                case WIN -> {winSeq++; loseSeq = 0;}
                case LOSE -> {winSeq = 0; loseSeq++;}
                case DRAW -> {winSeq = 0; loseSeq = 0;}
            }
            // 点数加算
            switch (judge) {
                case WIN -> {
                    score += winSeq * winSeq;
                }
                case LOSE -> {
                    score -= Math.min(Math.pow(2, loseSeq - 1), 1000000);
                }
                case DRAW -> {
                    score += 0;
                }
            }
        }
        return score;
    }

    /**
     * 一回分のスコアを算出します．
     * @param judge 試合勝敗データ
     * @param winSeq 連勝数
     * @param loseSeq 連敗数
     * @return
     */
    public static int calcScore(Judge judge, int winSeq, int loseSeq) {
        switch (judge) {
            case WIN -> {
                return winSeq * winSeq;
            }
            case LOSE -> {
                return -Math.min((int)Math.pow(2, loseSeq - 1), 1000000);
            }
            case DRAW -> {
                return 0;
            }
        }
        return 0;
    }
}
