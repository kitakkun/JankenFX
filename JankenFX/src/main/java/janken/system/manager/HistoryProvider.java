package janken.system.manager;

import janken.system.core.Player;
import janken.system.datamodel.RecordBook;

import java.util.ArrayList;

/**
 * プレイヤー側で安全に出した手と勝敗結果の履歴を利用するためのクラス
 * */
public class HistoryProvider {
    private final RecordBook[] books;
    private final Player[] players;

    /**
     * コンストラクタ<br>
     * 引数の配列２つにおいて互いに添字が一対一で対応している必要があります．
     * @param books 各プレイヤーのRecordBookインスタンスの配列
     * @param players 各プレイヤーのインスタンス配列
     */
    public HistoryProvider(RecordBook[] books, Player[] players) {
        this.books = books.clone();
        this.players = players;
    }

    /**
     * 自身の履歴を取得
     * @param player 自分のインスタンス
     * @return 自分の過去に出した手や勝敗が記録されたRecordBookインスタンス
     */
    public RecordBook getRecordBook(Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].equals(player)) {
                return books[i].clone();
            }
        }
        return null;
    }

    /**
     * すべてのプレイヤーの履歴を取得
     * @return すべてのプレイヤーのRecordBookインスタンスの配列
     */
    public RecordBook[] getAllRecordBook() {
        return books.clone();
    }

    /**
     * exceptを除くすべてのプレイヤーの履歴を取得
     * @param except 除外するプレイヤー
     * @return exceptに指定されたプレイヤーを除くすべてのRecordBookインスタンスの配列
     */
    public RecordBook[] getAllRecordBook(Player... except) {
        ArrayList<RecordBook> result = new ArrayList<>();
        for (int i = 0; i < players.length; i++) {
            boolean ignore = false;
            for (Player e : except) {
                if (players[i].equals(e)) {
                    ignore = true;
                    break;
                }
            }
            if (!ignore) {
                result.add(books[i].clone());
            }
        }
        RecordBook[] books = new RecordBook[result.size()];
        result.toArray(books);
        return books;
    }
}
