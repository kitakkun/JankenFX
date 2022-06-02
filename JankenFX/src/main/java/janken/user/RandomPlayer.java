package janken.user;

import janken.system.core.Player;
import janken.system.datamodel.Hand;
import janken.system.manager.HistoryProvider;

import java.util.Random;

/**
 * ランダムに手を出すプレイヤー
 */
public class RandomPlayer extends Player {

    private final Random random;

    public RandomPlayer() {
        super("RandomPlayer");
        random = new Random();
    }

    public RandomPlayer(String name) {
        super(name);
        random = new Random();
    }

    public Hand throwHand(HistoryProvider hp) {
        Hand[] hands = Hand.values();
        int i = random.nextInt(hands.length);
        hand = hands[i];
        return hand;
    }
}