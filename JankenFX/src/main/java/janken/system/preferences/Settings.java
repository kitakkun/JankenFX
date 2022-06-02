package janken.system.preferences;

import janken.system.core.Rule;
import janken.system.tool.PlayerInstantiator;

/**
 * ゲームの環境設定を記憶するクラス
 */
public class Settings {
    private int numberOfPlayers;
    private PlayerInstantiator instantiator;
    private Rule rule;
    private boolean concludeMatchPerProceed;
    private boolean showResultWindow;

    public Settings() {
        numberOfPlayers = 2;
        rule = Rule.DEFAULT;
        instantiator = new PlayerInstantiator();
        concludeMatchPerProceed = false;
        showResultWindow = false;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public PlayerInstantiator getInstantiator() {
        return instantiator;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Rule getRule() {
        return rule;
    }

    public boolean isConcludeMatchPerProceed() {
        return concludeMatchPerProceed;
    }

    public void setConcludeMatchPerProceed(boolean concludeMatchPerProceed) {
        this.concludeMatchPerProceed = concludeMatchPerProceed;
    }

    public boolean isShowResultWindow() {
        return showResultWindow;
    }

    public void setShowResultWindow(boolean showResultWindow) {
        this.showResultWindow = showResultWindow;
    }
}
