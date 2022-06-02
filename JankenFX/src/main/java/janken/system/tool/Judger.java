package janken.system.tool;

import janken.system.datamodel.Hand;
import janken.system.datamodel.Judge;

import java.util.Arrays;

/**
 * 勝敗の判定を行うクラス
 */
public class Judger {

    /**
     * 各手の勝敗情報を返す
     * @param hands 各プレイヤーが出した手
     * @return 各プレイヤーの勝敗データ
     */
    public static Judge[] judge(Hand[] hands) {
        Judge[] judges = new Judge[hands.length];
        Hand winHand = getWinner(hands);
        // 勝者がいない（あいこ）ならば
        if (winHand == null) {
            Arrays.fill(judges, Judge.DRAW);
        } else {
            for (int i = 0; i < judges.length; i++) {
                if (hands[i] == winHand) {
                    judges[i] = Judge.WIN;
                } else {
                    judges[i] = Judge.LOSE;
                }
            }
        }
        return judges;
    }

    private static Hand getWinner(Hand[] hands) {
        int bitResult = 0b000;
        for (Hand hand : hands) {
            bitResult |= hand.getId();
        }
        if (bitResult == (Hand.ROCK.getId() | Hand.SCISSORS.getId())) {
            return Hand.ROCK;
        } else if (bitResult == (Hand.SCISSORS.getId() | Hand.PAPER.getId())) {
            return Hand.SCISSORS;
        } else if (bitResult == (Hand.ROCK.getId() | Hand.PAPER.getId())) {
            return Hand.PAPER;
        } else {
            return null;
        }
    }
}
