package janken.view.components;

import janken.system.core.Player;
import janken.system.datamodel.Record;
import janken.system.datamodel.Hand;
import janken.system.datamodel.Judge;
import janken.system.datamodel.RecordBook;
import janken.system.manager.HistoryProvider;
import janken.system.manager.ScoreManager;
import janken.system.manager.ScoreProvider;
import janken.system.preferences.Settings;
import janken.system.tool.PlayerInstantiator;
import janken.user.RandomPlayer;
import janken.util.Counter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 各プレイヤーの勝敗情報などを表形式で表示するビューです．
 */
public class PlayerStatusView extends Pane {
    @FXML
    private GridPane table;

    // 各プレイヤー情報を制御するビューをまとめたクラス
    private PlayerView[] playerViews;

    // 見出しのラベル
    private String[] headStrings;
    private Label[] head;

    // 表示情報を記憶する配列
    HashMap<String, Boolean> columnIsVisible;

    @FXML
    private ResourceBundle resources;

    /**
     * コンストラクタ
     */
    public PlayerStatusView() {
        ResourceBundle bundle = ResourceBundle.getBundle("janken.bundles.UIResources");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("player-status-view.fxml"), bundle);
        fxmlLoader.setControllerFactory(param -> this);
        try {
            Node view = fxmlLoader.load();
            this.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初期化処理
     */
    public void initialize() {
        headStrings = new String[]{
                resources.getString("name"),
                resources.getString("playerClass"),
                resources.getString("hand"),
                resources.getString("win"),
                resources.getString("lose"),
                resources.getString("draw"),
                resources.getString("winRate"),
                resources.getString("score"),
                resources.getString("winSequence"),
                resources.getString("loseSequence"),
        };
        head = new Label[headStrings.length];
        for (int i = 0; i < head.length; i++) {
            head[i] = new Label(headStrings[i]);
        }
        columnIsVisible = new HashMap<>();
        for (String str : headStrings) {
            columnIsVisible.put(str, true);
        }

    }

    /**
     * 設定を適用する
     * @param settings 適用する設定
     */
    public void applySettings(Settings settings) {
        // プレイヤー数だけのPlayerViewを準備
        playerViews = new PlayerView[settings.getNumberOfPlayers()];
        // PlayerViewインスタンスを作成
        for (int i = 0; i < playerViews.length; i++) {
            playerViews[i] = new PlayerView(settings.getInstantiator());
            playerViews[i].fillNameField("Player" + (i+1));
        }
        setUpTable();
    }

    /**
     * 表を構築する
     */
    private void setUpTable() {
        // テーブルの初期化
        resetTable();

        List<Node> rowNodes;
        // 表示しない列番号を特定
        List<Integer> exceptCol = new ArrayList<>();
        for (int i = 0; i < headStrings.length; i++) {
            if (columnIsVisible.get(headStrings[i])) continue;
            exceptCol.add(i);
        }
        // 見出しを作成
        int col = 0, row = 0; // 挿入箇所
        rowNodes = List.of(head.clone());
        for (int i = 0; i < rowNodes.size(); i++) {
            if (exceptCol.contains(i)) continue;
            table.add(rowNodes.get(i), col, row);
            col++;
        }
        // プレイヤー状態の行を作成
        row = 1;
        for (PlayerView view : playerViews) {
            rowNodes = List.of(view.getAllNodes());
            col = 0;
            for (int i = 0; i < rowNodes.size(); i++) {
                if (exceptCol.contains(i)) continue;
                table.add(rowNodes.get(i), col, row);
                col++;
            }
            row++;
        }

        // Constraintsの設定
        setConstraints();
    }

    /**
     * テーブルをリセットします．
     */
    private void resetTable() {
        table.getChildren().clear();
        table.getRowConstraints().clear();
        table.getColumnConstraints().clear();
    }

    /**
     * テーブルにConstraintsを設定します．
     */
    private void setConstraints() {
        RowConstraints hRc = new RowConstraints();
        RowConstraints rc = new RowConstraints(50);
        for (int i = 0; i < table.getRowCount(); i++) {
            if (i == 0) table.getRowConstraints().add(hRc);
            else table.getRowConstraints().add(rc);
        }
//        ColumnConstraints cc = new ColumnConstraints();
//        ColumnConstraints cc2 = new ColumnConstraints(70);
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            if (i == 0 || i == 1) table.getColumnConstraints().add(cc);
//            else table.getColumnConstraints().add(cc2);
//        }
    }

    /**
     * カラムが表示されるかどうかを指定します
     * @param key
     * @param visible
     */
    public void setColumnVisible(String key, boolean visible) {
        if (columnIsVisible.containsKey(key)) {
            columnIsVisible.put(key, visible);
        }
        setUpTable();
    }

    /**
     * プレイヤーを取得します．
     */
    public Player[] getPlayers() {
        Player[] players = new Player[playerViews.length];
        for (int i = 0; i < playerViews.length; i++) {
            players[i] = playerViews[i].getPlayer();
        }
        return players;
    }

    /**
     * ビューを更新します．
     */
    public void update(HistoryProvider hp) {
        for (PlayerView view : playerViews) {
            view.update(hp);
        }
    }

    public void update(HistoryProvider provider, ScoreProvider scoreProvider) {
        for (PlayerView view : playerViews) {
            Player player = view.getPlayer();
            int score = scoreProvider.getScore(player);
            int winSeq = scoreProvider.getWinSequence(player);
            int loseSeq = scoreProvider.getLoseSequence(player);
            view.setScore(score);
            view.setWinSeq(winSeq);
            view.setLoseSeq(loseSeq);
        }
        update(provider);
    }
}

/**
 * プレイヤー1人の表示を制御するためのクラス（PlayerStatusViewの行にあたる）
 */
class PlayerView {
    private final TextField name;
    private final HandIndicatorView indicator;
    private final HashMap<String, Label> labels;
    private final ComboBox<String> classSelector;
    private Player player;  // 関連付けされているプレイヤーインスタンス

    public void update(HistoryProvider hp) {
        RecordBook rb = hp.getRecordBook(player);
        Record lr = rb.getLastRecord();
        int win = Counter.countJudge(rb, Judge.WIN);
        int lose = Counter.countJudge(rb, Judge.LOSE);
        int draw = Counter.countJudge(rb, Judge.DRAW);
        setWin(win);
        setLose(lose);
        setDraw(draw);
        setWinRate((double)win / rb.countRecords());
        if (lr != null) {
            setIndicator(lr.getHand());
        }
    }

    public void fillNameField(String name) {
        this.name.setText(name);
    }

    public PlayerView(PlayerInstantiator instantiator) {
        name = new TextField();
        name.setPromptText("Enter a name.");
        name.setPrefWidth(120);
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (player != null) {
                    player.setName(newValue);
                }
            }
        });
        indicator = new HandIndicatorView();
        labels = new HashMap<>();
        labels.put("win", new Label("0"));
        labels.put("lose", new Label("0"));
        labels.put("draw", new Label("0"));
        labels.put("winRate", new Label("0%"));
        labels.put("score", new Label("0"));
        labels.put("winSequence", new Label("0"));
        labels.put("loseSequence", new Label("0"));
        classSelector = new ComboBox<>(FXCollections.observableArrayList(
            instantiator.getPlayerClassKeys()
        ));
        classSelector.setOnAction(event -> {
            String key = classSelector.getValue();
            // プレイヤーインスタンスの生成
            try {
                player = instantiator.instantiate(key);
                player.setName(name.getText());
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        // デフォルトはRandomPlayer
        classSelector.setValue("RandomPlayer(built-in)");
        player = new RandomPlayer();
    }

    public void setIndicator(Hand indicator) {
        this.indicator.setIndicator(indicator);
    }

    public void setWin(int count) {
        labels.get("win").setText(String.valueOf(count));
    }

    public void setLose(int count) {
        labels.get("lose").setText(String.valueOf(count));
    }

    public void setDraw(int count) {
        labels.get("draw").setText(String.valueOf(count));
    }

    public void setWinRate(double rate) {
        labels.get("winRate").setText(String.format("%.0f%%", rate * 100));
    }

    public void setScore(int score) {
        labels.get("score").setText(String.valueOf(score));
    }

    public void setWinSeq(int winSeq) {
        labels.get("winSequence").setText(String.valueOf(winSeq));
    }

    public void setLoseSeq(int loseSeq) {
        labels.get("loseSequence").setText(String.valueOf(loseSeq));
    }

    public Player getPlayer() {
        return player;
    }

    public Node[] getAllNodes() {
        return new Node[] {
                name,
                classSelector,
                indicator,
                labels.get("win"),
                labels.get("lose"),
                labels.get("draw"),
                labels.get("winRate"),
                labels.get("score"),
                labels.get("winSequence"),
                labels.get("loseSequence"),
        };
    }

}