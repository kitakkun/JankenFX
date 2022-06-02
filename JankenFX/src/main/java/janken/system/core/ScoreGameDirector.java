package janken.system.core;

import janken.system.datamodel.Hand;
import janken.system.datamodel.Judge;
import janken.system.manager.HistoryProvider;
import janken.system.manager.ScoreManager;
import janken.system.manager.ScoreProvider;

public class ScoreGameDirector extends GameDirector {
    private final ScoreManager scoreManager;
    private boolean isFinished;

    public ScoreGameDirector(Player ... players) {
        super(players);
        scoreManager = new ScoreManager(players);
        isFinished = false;
    }

    @Override
    public HistoryProvider proceed() {
        if (isFinished) return hr.toProvider();
        Hand[] hands = decideHands(hr.toProvider());
        Judge[] judges = judge(hands);
        record(hands, judges);
        scoreManager.calcScore(judges);
        provider = hr.toProvider();
        // ゲーム終了条件のチェック
        if (!scoreManager.scoreIsInRange()) {
            isFinished = true;
        }
        return hr.toProvider();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public int getScore(Player player) {
        return scoreManager.getScore(player);
    }

    public ScoreProvider getScoreProvider() {
        return scoreManager.toProvider();
    }
}
