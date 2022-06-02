package janken.view.components;

import janken.system.datamodel.Hand;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * GUIで出す手を選択するためのビュー
 */
public class SelectHandView extends Pane {

    private Node view;
    private SelectHandView controller;

    @FXML
    private HandIndicatorView indicator;

    public SelectHandView() {
        ResourceBundle bundle = ResourceBundle.getBundle("janken.bundles.UIResources");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("select-hand-view.fxml"), bundle);
        fxmlLoader.setControllerFactory(param -> controller = this);
        try {
            view = fxmlLoader.load();
        } catch (IOException e) {
            System.err.println(e);
        }
        if (view == null) {
            System.out.println("null");
        } else {
            this.getChildren().add(view);
        }
    }

    public void initialize() {
        indicator.rock.setOnMouseClicked(e -> indicator.setIndicator(Hand.ROCK));
        indicator.paper.setOnMouseClicked(e -> indicator.setIndicator(Hand.PAPER));
        indicator.scissors.setOnMouseClicked(e -> indicator.setIndicator(Hand.SCISSORS));
    }

    public Hand getHand(){
        return indicator.getIndicator();
    }

    public void resetIndicator() {
        indicator.setIndicator(null);
    }

    @FXML
    protected void exit() {
        Stage stage = (Stage)indicator.getScene().getWindow();
        stage.close();
    }
}
