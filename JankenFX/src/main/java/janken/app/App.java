package janken.app;

import janken.system.core.GameDirector;
import janken.system.core.Player;
import janken.user.PatternPredictPlayer;
import janken.user.RepeatPlayer;

import java.util.Scanner;

/**
 * 作成したクラスを用いて，実際にゲームを動かすためのクラス
 */
public class App {
    public static void main(String[] args) throws Exception {
        GameDirector director;
        Scanner scanner = new Scanner(System.in);

        Player[] players = {
                new RepeatPlayer(),
//                new RandomPlayer(),
                new PatternPredictPlayer(),
//                new PatternRepeatPlayer(Hand.ROCK, Hand.ROCK, Hand.PAPER, Hand.ROCK, Hand.SCISSORS, Hand.SCISSORS),
//                new ComputerPlayer()
        };

        director = new GameDirector(players);
        for (int i = 0; i < 100; i++) {
            director.proceed();
        }

        director.printResults();
        scanner.close();
    }
}
