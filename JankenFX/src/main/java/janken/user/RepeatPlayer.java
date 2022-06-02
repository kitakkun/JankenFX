package janken.user;

import janken.system.datamodel.Hand;

/**
 * グー・チョキ・パーを繰り返すプレイヤー
 */
public class RepeatPlayer extends PatternRepeatPlayer {

    public RepeatPlayer() {
        super("RepeatPlayer", Hand.ROCK, Hand.SCISSORS, Hand.PAPER);
    }

    public RepeatPlayer(String name) {
        super(name, Hand.ROCK, Hand.SCISSORS, Hand.PAPER);
    }

}
