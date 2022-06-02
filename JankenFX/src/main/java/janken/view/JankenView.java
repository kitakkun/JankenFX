package janken.view;

import janken.system.Constants;
import janken.system.core.GameDirector;
import janken.system.core.Player;
import janken.system.core.Rule;
import janken.system.core.ScoreGameDirector;
import janken.system.preferences.Settings;
import janken.user.GUIHumanPlayer;
import janken.view.components.PlayerStatusView;
import janken.view.components.ResultView;
import janken.view.menus.AboutView;
import janken.view.menus.SettingView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class JankenView extends Pane {

    @FXML
    private ResourceBundle resources;

    // プレイヤー情報を制御するビュー
    @FXML
    private PlayerStatusView playerStatusView;

    // 試合回数を制御するコントローラ
    @FXML
    private Spinner<Integer> timesSpinner;

    // メインプログラムから渡された環境設定クラス
    private Settings settings;
    // じゃんけんの試合を制御するクラス
    private GameDirector director;

    // ゲームに用いるプレイヤーの配列
    private Player[] players;

    private Stage stage;

    @FXML
    private CheckMenuItem score, winSequence, loseSequence;

    public JankenView() {
        ResourceBundle bundle = ResourceBundle.getBundle("janken.bundles.UIResources");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("janken-view.fxml"), bundle);
        fxmlLoader.setControllerFactory(param -> this);
        try {
            Node view = fxmlLoader.load();
            this.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        timesSpinner.editorProperty().get().setAlignment(Pos.CENTER);
        // timesSpinnerを数値のみ入力可にする
        NumberFormat format = NumberFormat.getIntegerInstance();
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePosition = new ParsePosition(0);
                format.parse(c.getControlNewText(), parsePosition);
                if (parsePosition.getIndex() == 0 ||
                        parsePosition.getIndex() < c.getControlNewText().length()) {
                    return null;
                }
            }
            return c;
        };
        TextFormatter<Integer> numberFormatter = new TextFormatter<Integer>(new IntegerStringConverter(), 1, filter);
        timesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Constants.MAX_GAME, 1));
        timesSpinner.getEditor().setTextFormatter(numberFormatter);

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void updateTable(ActionEvent e) {
        CheckMenuItem item = (CheckMenuItem) e.getSource();
        String id = item.getId();
        playerStatusView.setColumnVisible(resources.getString(id), item.isSelected());
        stage.sizeToScene();
    }

    /**
     * 環境設定クラスをメインプログラムから受け取ります．
     * @param settings 環境設定クラス
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
        setUpView(settings);
    }

    /**
     * 設定をもとにビューを再構築します．
     * @param settings 環境設定クラス
     */
    public void setUpView(Settings settings) {
        playerStatusView.applySettings(settings);
        // メニューの有効化・無効化
        if (settings.getRule() == Rule.DEFAULT) {
            playerStatusView.setColumnVisible(resources.getString(score.getId()), false);
            playerStatusView.setColumnVisible(resources.getString(winSequence.getId()), false);
            playerStatusView.setColumnVisible(resources.getString(loseSequence.getId()), false);
        } else if (settings.getRule() == Rule.SCORE_BASE) {
            playerStatusView.setColumnVisible(resources.getString(score.getId()), score.isSelected());
            playerStatusView.setColumnVisible(resources.getString(winSequence.getId()), loseSequence.isSelected());
            playerStatusView.setColumnVisible(resources.getString(loseSequence.getId()), winSequence.isSelected());
        }
        setUpMenus(settings);
        if (stage != null) {
            stage.sizeToScene();
        }
    }

    private void setUpMenus(Settings settings) {
        if (settings.getRule() == Rule.DEFAULT) {
            score.setDisable(true);
            winSequence.setDisable(true);
            loseSequence.setDisable(true);
        } else if (settings.getRule() == Rule.SCORE_BASE) {
            score.setDisable(false);
            winSequence.setDisable(false);
            loseSequence.setDisable(false);
        }
    }

    /**
     * timesSpinnerに指定された回数だけゲーム進行をします．
     */
    @FXML
    protected void onProcButtonClick(ActionEvent event) {
        readyGameDirector();
        proceedGames();
        updateViews();
        if (settings.isShowResultWindow() && settings.isConcludeMatchPerProceed()) {
            showResult();
        }
    }

    /**
     * GameDirectorの初期化を行います．
     */
    private void readyGameDirector() {
        if (isPlayerUpdated() || director == null || settings.isConcludeMatchPerProceed()) {
            this.players = playerStatusView.getPlayers();
            switch (settings.getRule()) {
                case SCORE_BASE -> director = new ScoreGameDirector(players);
                default -> director = new GameDirector(players);
            }
        }
    }

    private boolean isPlayerUpdated() {
        Player[] players = playerStatusView.getPlayers();
        if (this.players == null || players.length != this.players.length) {
            return true;
        }
        for (int i = 0; i < players.length; i++) {
            if (!this.players[i].equals(players[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * ゲームを進行します．
     */
    private void proceedGames() {
        int gameNum = timesSpinner.getValue();
        if (containsHumanPlayer(players)) {
            timesSpinner.getValueFactory().setValue(1);
            gameNum = 1;
        }
        director.proceed(gameNum);
    }

    /**
     * ビューを更新します.
     */
    private void updateViews() {
        if (director instanceof ScoreGameDirector) {
            ScoreGameDirector sd = (ScoreGameDirector) director;
            playerStatusView.update(sd.getProvider(), sd.getScoreProvider());
        }
        playerStatusView.update(director.getProvider());
        if (stage != null) {
            stage.sizeToScene();
        }
    }

    /**
     * 最終結果を表示します．
     */
    private void showResult() {
        Stage stage = new Stage();
        ResultView resultView = new ResultView();
        resultView.setResult(director);
        Scene scene = new Scene(resultView);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (KeyCode.ESCAPE == e.getCode()) {
                stage.close();
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    /**
     * 人間のプレイヤーを含むかどうかを返します
     * @param players ゲームに参加するプレイヤー
     * @return 人間のプレイヤーを含むか
     */
    private boolean containsHumanPlayer(Player[] players) {
        for (Player player : players) {
            if (player instanceof GUIHumanPlayer) {
                return true;
            }
        }
        return false;
    }

    /**
     * About画面を開きます．※プログラムの情報を記したもの．
     */
    @FXML
    protected void openAbout() {
        Stage stage = new Stage();
        AboutView view = new AboutView();
        stage.setTitle(resources.getString("about"));
        Scene scene = new Scene(view);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (KeyCode.ESCAPE == e.getCode()) {
                stage.close();
            }
        });
        stage.show();
    }

    /**
     * 設定画面を開きます．
     */
    @FXML
    public void openSettings() {
        SettingView view = new SettingView();
        view.setSettings(settings);
        Scene scene = new Scene(view);
        Stage stage = new Stage();
        stage.setTitle(resources.getString("game-settings"));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setOnHiding(e -> {
            if (view.isSettingsUpdated()) {
                setUpView(settings);
            }
        });
        stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (KeyCode.ESCAPE == e.getCode()) {
                stage.close();
            }
        });
        stage.show();
    }


}