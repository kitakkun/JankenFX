package janken.user;

import janken.system.core.Player;
import janken.system.datamodel.Record;
import janken.system.datamodel.Hand;
import janken.system.datamodel.RecordBook;
import janken.system.manager.HistoryProvider;
import janken.util.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * ある手の次に出やすい手を毎回予測し，その予測結果に勝てるような手を出すプレイヤー<br>
 * 勝敗結果及び出した手の履歴の利用例として追加した
 */
public class PatternPredictPlayer extends Player {

    private final Random rnd;

    class HandCounter {
        private final HashMap<Hand, Integer> count;

        public HandCounter() {
            count = new HashMap<>();
            reset();
        }

        public void count(Hand hand) {
            count.put(hand, count.get(hand) + 1);
        }

        public boolean notEnoughData() {
            boolean notEnoughData = true;
            Hand[] hands = Hand.values();
            for (int i = 0; i < hands.length; i++) {
                notEnoughData &= count.get(hands[i]) == 0;
            }
            return notEnoughData;
        }

        public Hand getMostFrequentHand() {
            return MapUtil.getMaxKey(count);
        }

        public void reset() {
            count.put(Hand.ROCK, 0);
            count.put(Hand.SCISSORS, 0);
            count.put(Hand.PAPER, 0);
        }
    }


    public PatternPredictPlayer() {
        super("PatternPredictPlayer");
        rnd = new Random();
    }

    public PatternPredictPlayer(String name) {
        super(name);
        rnd = new Random();
    }

    public Hand throwHand(HistoryProvider hp) {
        RecordBook[] rbs = hp.getAllRecordBook(this);
        // 候補の手
        ArrayList<Hand> options = new ArrayList<>();
        HandCounter counter = new HandCounter();
        // 各プレイヤーの最も出しそうな手を予測する．
        for (RecordBook rb : rbs) {
            // カウンタのリセット
            counter.reset();

            Record lastRec = rb.getLastRecord();
            // データが存在しなければスキップ
            if (lastRec == null) {
                continue;
            }
            // 一つ前の手の次に出やすい手を予測する
            int startIndex = 1;
            if (rb.countRecords() > 1000) {
                startIndex = rb.countRecords() - 1000;
            }
            for (int i = startIndex; i < rb.countRecords(); i++) {
                Record prev = rb.getRecord(i - 1);
                if (prev.getHand() == lastRec.getHand()) {
                    Record current = rb.getRecord(i);
                    counter.count(current.getHand());
                }
            }
            // データが十分であれば，勝てそうな手を候補に追加
            if (!counter.notEnoughData()) {
                Hand mf = counter.getMostFrequentHand();
                options.add(getWinHand(mf));
            }
        }

        counter.reset();
        // 全候補の手で一番可能性の高いものを出す
        if (options.size() == 0) {
            Hand[] hands = Hand.values();
            hand = hands[rnd.nextInt(hands.length)];
        } else {
            for (Hand hand : options) {
                counter.count(hand);
            }
            hand = counter.getMostFrequentHand();
        }
        return hand;
    }

    private Hand getWinHand(Hand hand) {
        if (hand == Hand.ROCK) return Hand.PAPER;
        else if (hand == Hand.PAPER) return Hand.SCISSORS;
        else return Hand.ROCK;
    }
}

