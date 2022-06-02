package janken.system.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 複数のRecordを一括で管理するクラス
 */
public class RecordBook {
    private final ArrayList<Record> records;

    public RecordBook() {
        records = new ArrayList<>();
    }

    public RecordBook(ArrayList<Record> records) {
        this.records = records;
    }

    public void add(Record rec) {
        records.add(rec);
    }

    @Override
    public RecordBook clone() {
        ArrayList<Record> clone = new ArrayList<>();
        clone.addAll(records);
        RecordBook rb = new RecordBook(clone);
        return rb;
    }

    /**
     * 1回目の結果を取得
     * @return 1回目の結果
     */
    public Record getFirstRecord() {
        if (records.size() == 0) {
            return null;
        } else {
            return records.get(0);
        }
    }

    /**
     * 直近の結果を取得
     * @return 現在の一つ前の結果
     */
    public Record getLastRecord() {
        if (records.size() == 0) {
            return null;
        } else {
            return records.get(records.size() - 1);
        }
    }

    /**
     * 任意の結果を取得
     * @param index 取得するデータのインデックス
     * @return 取得したRecord
     */
    public Record getRecord(int index){
        return records.get(index);
    }

    // 記録の数を返す
    public int countRecords() {
        return records.size();
    }

    // 勝敗をカウントする
    public HashMap<Judge, Integer> countJudges() {
        HashMap<Judge, Integer> count = new HashMap<>();
        count.put(Judge.WIN, 0);
        count.put(Judge.LOSE, 0);
        count.put(Judge.DRAW, 0);
        for (Record rec : records) {
            Judge judge = rec.getJudge();
            count.put(judge, count.get(judge) + 1);
        }
        return count;
    }

    // 各手の勝率を計算する
    public HashMap<Hand, Double> calcWinRate() {
        HashMap<Hand, Double> rate = new HashMap<>();
        rate.put(Hand.ROCK, 0D);
        rate.put(Hand.SCISSORS, 0D);
        rate.put(Hand.PAPER, 0D);
        for (Record rec : records) {
            if (rec.getJudge() == Judge.WIN) {
                Hand hand = rec.getHand();
                rate.put(hand, rate.get(hand) + 1);
            }
        }
        rate.put(Hand.ROCK, rate.get(Hand.ROCK) / records.size());
        rate.put(Hand.SCISSORS, rate.get(Hand.SCISSORS) / records.size());
        rate.put(Hand.PAPER, rate.get(Hand.PAPER) / records.size());
        return rate;
    }
}