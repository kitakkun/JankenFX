package janken.system.core;

import janken.system.datamodel.Hand;
import janken.system.manager.HistoryProvider;

/**
 * ゲーム内で用いるプレイヤーの元となるクラス
 */
public abstract class Player {
    protected String name;
    protected Hand hand;

    public Player(String name) {
        this.name = name;
    }

    public Player() {
        this.name = "Player";
    }
    // グーチョキパーの手を出す
    public abstract Hand throwHand(HistoryProvider hp);

    /**
     * プレイヤーの名前を取得
     * @return プレイヤーの名前
     */
    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    /**
     * プレイヤーの手を取得
     * @return プレイヤーの手
     */
    public final Hand getHand() {
        return hand;
    }
}
