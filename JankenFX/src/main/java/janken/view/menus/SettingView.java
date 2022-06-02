package janken.view.menus;

import janken.system.core.Player;
import janken.system.core.Rule;
import janken.system.tool.PlayerInstantiator;
import janken.system.io.PlayerLoader;
import janken.system.preferences.Settings;
import janken.view.components.InvalidClassNotificationView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 設定画面を構成するビューを制御します．
 */
public class SettingView extends Pane {

    @FXML
    public ToggleGroup ruleToggle;
    public RadioButton defaultRadio;
    public RadioButton scoreRadio;
    @FXML
    private ResourceBundle resources;

    @FXML
    private CheckBox concludePerProceedCheckBox, showResultCheckBox;

    @FXML
    private ListView<String> playerClassList;
    @FXML
    private Spinner<Integer> numberOfPlayers;

    private Settings settings;

    private HashMap<String, Class<Player>> reservedClasses;
    private ArrayList<String> reservedKeys;
    private ArrayList<String> deletedKeys;

    private boolean settingsUpdated = false;


    public SettingView() {
        ResourceBundle bundle = ResourceBundle.getBundle("janken.bundles.UIResources");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("setting-view.fxml"), bundle);
        fxmlLoader.setControllerFactory(param -> this);
        try {
            Node view = fxmlLoader.load();
            this.getChildren().add(view);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void initialize() {
        reservedKeys = new ArrayList<>();
        reservedClasses = new HashMap<>();
        deletedKeys = new ArrayList<>();
        numberOfPlayers.editorProperty().get().setAlignment(Pos.CENTER);

        // リストビューのコンテキストメニューの設定（削除機能）
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenu = new MenuItem(resources.getString("delete"));
        contextMenu.getItems().add(deleteMenu);

        playerClassList.setContextMenu(contextMenu);

        playerClassList.setOnContextMenuRequested(e -> {
            contextMenu.show(playerClassList, e.getScreenX(), e.getScreenY());
            e.consume();
        });

        deleteMenu.setOnAction(e -> {
            String selected = playerClassList.getSelectionModel().getSelectedItem();
            deletedKeys.add(selected);
            playerClassList.getItems().remove(selected);
        });

        concludePerProceedCheckBox.setOnAction(e -> {
            if (concludePerProceedCheckBox.isSelected()) {
                showResultCheckBox.setDisable(false);
            } else {
                showResultCheckBox.setDisable(true);
            }
        });
    }

    /**
     * 設定を適用せずにウィンドウを閉じます．（Cancelボタンの動作）
     * @param event イベントの発生したNode
     */
    @FXML
    protected void discardChange(ActionEvent event) {
        Node node = (Node)event.getSource();
        Stage stage = (Stage) node.getParent().getScene().getWindow();
        stage.close();
    }

    /**
     * 設定を適用してウィンドウを閉じます（OKボタンの動作）
     * @param event イベントの発生したNode
     */
    @FXML
    protected void applyChange(ActionEvent event) {
        Node node = (Node)event.getSource();
        String id = node.getId();
        settingsUpdated = true;
        settings.setNumberOfPlayers(numberOfPlayers.getValue());
        PlayerInstantiator instantiator = settings.getInstantiator();
        for (String key : reservedKeys) {
            instantiator.addPlayerClass(key, reservedClasses.get(key));
        }
        for (String key : deletedKeys) {
            instantiator.removePlayerClass(key);
        }
        // ルールの設定
        if (defaultRadio.isSelected()) {
            settings.setRule(Rule.DEFAULT);
        } else if (scoreRadio.isSelected()) {
            settings.setRule(Rule.SCORE_BASE);
        }

        settings.setConcludeMatchPerProceed(concludePerProceedCheckBox.isSelected());
        settings.setShowResultWindow(showResultCheckBox.isSelected());

        if (id.equals("okBtn")) {
            Stage stage = (Stage)node.getParent().getScene().getWindow();
            stage.close();
        }
    }

    /**
     * ユーザー定義のPlayerクラスを読み込みます．
     * @throws IOException
     */
    @FXML
    protected void loadClass() throws IOException {
        PlayerLoader playerLoader = new PlayerLoader();
        List<File> files = playerLoader.selectFiles();
        if (files == null) {
            System.out.println("File selection was canceled.");
            return;
        }
        // 読み込めないクラス
        ArrayList<File> invalidFiles = new ArrayList<>();

        for (File file : files) {
            String key = getFileNameWithoutExtension(file.getName());
            Class<Player> clazz = playerLoader.load(file);
            PlayerInstantiator instantiator = settings.getInstantiator();
            int number = 1;
            // 名前の重複が解消されるまで
            while (instantiator.keyIsUsed(key) || reservedKeys.contains(key)) {
                key += "_" + number;
                number++;
            }
            // Playerクラスのサブクラス可能であれば追加
            Class<Player> superClass = Player.class;
            if (superClass.isAssignableFrom(clazz)) {
                reservedClasses.put(key, clazz);
                reservedKeys.add(key);
                playerClassList.getItems().add(key);
            } else {
                invalidFiles.add(file);
            }
        }

        if (!invalidFiles.isEmpty()) {
            showInvalidClassNotification(invalidFiles.toArray(new File[0]));
        }

    }

    private void showInvalidClassNotification(File[] files) {
        Stage stage = new Stage();
        InvalidClassNotificationView view = new InvalidClassNotificationView();
        view.setFiles(files);
        Scene scene = new Scene(view);
        stage.setScene(scene);
        stage.setTitle(resources.getString("invalid-class-window-title"));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private String getFileNameWithoutExtension(String filename) {
       int index = filename.lastIndexOf(".");
       if (index == -1) {
           return filename;
       } else {
           return filename.substring(0, index);
       }
    }

    /**
     * Settingsオブジェクトをもとにビューを更新します．
     */
    private void update() {
        String[] keys = settings.getInstantiator().getPlayerClassKeys();
        playerClassList.getItems().clear();
        playerClassList.getItems().addAll(keys);
        numberOfPlayers.getValueFactory().setValue(settings.getNumberOfPlayers());
        Rule rule = settings.getRule();
        if (rule == Rule.DEFAULT) {
            defaultRadio.setSelected(true);
        } else {
            scoreRadio.setSelected(true);
        }
        concludePerProceedCheckBox.setSelected(settings.isConcludeMatchPerProceed());
        showResultCheckBox.setSelected(settings.isShowResultWindow());
        if (concludePerProceedCheckBox.isSelected()) {
            showResultCheckBox.setDisable(false);
        } else {
            showResultCheckBox.setDisable(true);
        }
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        update();
    }

    public boolean isSettingsUpdated() {
        return settingsUpdated;
    }
}
