package janken.user;

import janken.system.datamodel.Hand;
import janken.system.manager.HistoryProvider;
import janken.system.core.Player;
import janken.view.components.SelectHandView;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * GUIで手を選択するプレイヤー
 */
public class GUIHumanPlayer extends Player {
    private Stage stage;
    private SelectHandView selectHandView;

    public GUIHumanPlayer() {
        super("GUIHumanPlayer");
        readyStage();
    }

    public GUIHumanPlayer(String name) {
        super(name);
        readyStage();
    }

    private void readyStage() {
        selectHandView = new SelectHandView();
        Scene scene = new Scene(selectHandView);
        stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
    }

    @Override
    public Hand throwHand(HistoryProvider hp) {
        selectHandView.resetIndicator();
        stage.showAndWait();
        Hand hand = selectHandView.getHand();
        if (hand == null) {
            return Hand.ROCK;
        } else {
            return hand;
        }
    }
}
