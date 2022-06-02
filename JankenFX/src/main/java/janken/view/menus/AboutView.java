package janken.view.menus;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * 「このアプリについて」の画面を表示するビュー
 */
public class AboutView extends Pane {
    private Node view;

    public AboutView() {
        ResourceBundle bundle = ResourceBundle.getBundle("janken.bundles.UIResources");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about-view.fxml"), bundle);
        try {
            view = fxmlLoader.load();
        } catch (IOException e) {
            System.err.println(e);
        }
        this.getChildren().add(view);
    }
}
