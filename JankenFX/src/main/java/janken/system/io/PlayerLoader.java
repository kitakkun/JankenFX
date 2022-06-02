package janken.system.io;

import janken.system.core.Player;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * プログラム外で作成したプレイヤークラスをロードするためのクラス
 */
public class PlayerLoader extends ClassLoader {
    private static final int BUF_SIZE = 1024;

    /**
     * ファイル選択をさせます．
     * @return 選択したJavaクラスファイル
     */
    public File selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a class file to load");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Java Class Files", "*.class")
        );
        Stage stage = new Stage();
        return fileChooser.showOpenDialog(stage);
    }

    /**
     * ファイル選択をさせます（複数ファイル）．
     * @return 選択した複数のJavaクラスファイル
     */
    public List<File> selectFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select class files to load");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Class Files", "*.class"));
        Stage stage = new Stage();
        return fileChooser.showOpenMultipleDialog(stage);
    }

    public Class<Player> load(File classFile) throws IOException {
        FileInputStream in = new FileInputStream(classFile);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[BUF_SIZE];
        int len = in.read(buf);
        while(len != -1) {
            out.write(buf, 0, len);
            len = in.read(buf);
        }
        byte[] loadedData = out.toByteArray();
        return (Class<Player>) defineClass(null, loadedData, 0, loadedData.length);
    }
}
