package janken.view.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * 読み込めないPlayerクラスファイルを通知するビュー
 */
public class InvalidClassNotificationView extends Pane {
    @FXML
    ListView<String> list;

    private Node view;
    private InvalidClassNotificationView controller;

    public InvalidClassNotificationView() {
        ResourceBundle bundle = ResourceBundle.getBundle("janken.bundles.UIResources");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("invalid-class-notification-view.fxml"), bundle);
        fxmlLoader.setControllerFactory(param -> controller = this);
        try {
            view = fxmlLoader.load();
        } catch (IOException e) {
            System.err.println(e);
        }
        this.getChildren().add(view);
    }


    public void initialize() {
        list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setMinWidth(param.getWidth());
                    setMaxWidth(param.getWidth());
                    setPrefWidth(param.getWidth());

                    setWrapText(true);
                    setText(item.toString());
                }
            }
        });
    }

    public void setFiles(File[] files) {
        for (File file : files) {
            list.getItems().add(file.getAbsolutePath());
        }
    }

    @FXML
    protected void closeWindow() {
        Stage stage = (Stage)list.getScene().getWindow();
        stage.close();
    }
}
