package janken.util;

import janken.system.datamodel.Hand;

import java.util.HashMap;

/**
 * 手をカウントする便利クラス
 */
public class HandCounter {
    private final HashMap<Hand, Integer> count;

    public HandCounter() {
        count = new HashMap<>();
        reset();
    }

    /**
     * 手をカウントします
     * @param hand カウンタをプラス１したい手
     */
    public void count(Hand hand) {
        count.put(hand, count.get(hand) + 1);
    }

    /**
     * すべての手のカウント数が同じかどうか
     * @return 各手のカウント数に差がなければtrueを返します
     */
    public boolean isNoDifference() {
        int r = count.get(Hand.ROCK);
        int p = count.get(Hand.PAPER);
        int s = count.get(Hand.SCISSORS);
        return r == p && p == s;
    }

    /**
     * 最もカウント数の多い手を返します
     * @return 最もカウント数の多い手
     */
    public Hand getMostFrequentHand() {
        return MapUtil.getMaxKey(count);
    }

    /**
     * カウンタをリセットします
     */
    public void reset() {
        count.put(Hand.ROCK, 0);
        count.put(Hand.SCISSORS, 0);
        count.put(Hand.PAPER, 0);
    }
}

