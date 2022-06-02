package janken.user;

import janken.system.datamodel.Hand;
import janken.system.manager.HistoryProvider;
import janken.system.core.Player;

import java.util.Scanner;

/**
 * 標準入力を通して手動で手を選択するプレイヤー
 */
public class HumanPlayer extends Player {

    private final Scanner scanner;

    public HumanPlayer(String name) {
        super( name);
        this.scanner = new Scanner(System.in);
    }

    public HumanPlayer() {
        super("HumanPlayer");
        this.scanner = new Scanner(System.in);
    }

    public Hand throwHand(HistoryProvider hp) {
        String message = "グーならば0を，チョキならば1を，パーならば2を入力：";
        System.out.print(message);
        String input = scanner.next();
        switch (input) {
            case "0" -> hand = Hand.ROCK;
            case "1" -> hand = Hand.SCISSORS;
            case "2" -> hand = Hand.PAPER;
            default -> hand = Hand.ROCK;
        }
        return hand;
    }
}
