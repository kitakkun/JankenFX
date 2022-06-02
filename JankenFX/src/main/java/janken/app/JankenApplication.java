package janken.app;

import janken.system.preferences.Settings;
import janken.view.JankenView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * アプリケーションのメイン制御を行うクラス
 */
public class JankenApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Settings settings = new Settings();
        ResourceBundle bundle = ResourceBundle.getBundle("janken.bundles.UIResources");
        // ビューの準備
        JankenView jc = new JankenView();
        jc.setSettings(settings);
        jc.setStage(stage);

        // ウィンドウ周りの設定
        Scene scene = new Scene(jc);

        stage.setTitle(bundle.getString("app-name"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        stage.setOnCloseRequest(e -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}