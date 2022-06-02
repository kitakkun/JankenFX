package janken.user;

import janken.system.datamodel.Hand;
import janken.system.manager.HistoryProvider;
import janken.system.core.Player;

/**
 * 引数に与えられたパターンを繰り返すプレイヤー
 */
public class PatternRepeatPlayer extends Player {

    private final Hand[] pattern;
    private int index;

    public PatternRepeatPlayer(String name, Hand ... pattern) {
        super(name);
        this.pattern = pattern;
        index = 0;
    }

    public PatternRepeatPlayer(Hand... pattern) {
        super("PatternRepeatPlayer");
        this.pattern = pattern;
        index = 0;
    }

    @Override
    public Hand throwHand(HistoryProvider hp) {
        hand = pattern[index];
        index = (index + 1) % pattern.length;
        return hand;
    }
}
