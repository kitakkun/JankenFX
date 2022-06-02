package janken.user;

import janken.system.core.Player;
import janken.system.datamodel.Record;
import janken.system.datamodel.Hand;
import janken.system.datamodel.Judge;
import janken.system.manager.HistoryProvider;

import java.util.Random;

/**
 * 負けたらランダムな手を，勝てば同じ手を繰り返し出すプレイヤー
 */
public class ComputerPlayer extends Player {

    private final Random random;

    public ComputerPlayer() {
        super( "ComputerPlayer");
        random = new Random();
    }

    public ComputerPlayer(String name) {
        super(name);
        random = new Random();
    }

    public Hand throwHand(HistoryProvider hp) {
        Hand[] hands = Hand.values();
        Record rc = hp.getRecordBook(this).getLastRecord();
        if (rc != null && rc.getJudge() == Judge.WIN){
            hand = rc.getHand();
        } else {
            int i = random.nextInt(hands.length);
            hand = hands[i];
        }
        return hand;
    }
}
