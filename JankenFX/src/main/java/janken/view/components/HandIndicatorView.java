package janken.view.components;

import janken.system.datamodel.Hand;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;

/**
 * 現在出している手が何かを示すビュー
 */
public class HandIndicatorView extends Pane {

    @FXML
    protected Pane rock, paper, scissors;

    private Hand indicator;
    private HashMap<Hand, Pane> indicators;

    private Node view;
    private HandIndicatorView controller;

    public HandIndicatorView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hand-indicator-view.fxml"));
        fxmlLoader.setControllerFactory(param -> controller = this);
        try {
            view = fxmlLoader.load();
        } catch (IOException e) {
            System.err.println(e);
        }
        this.getChildren().add(view);
    }

    public void initialize() {
        indicators = new HashMap<>();
        indicators.put(Hand.ROCK, rock);
        indicators.put(Hand.SCISSORS, scissors);
        indicators.put(Hand.PAPER, paper);
        indicator = null;
    }


    public void setIndicator(Hand indicator) {
        this.indicator = indicator;
        updateView();
    }

    public Hand getIndicator() {
        return indicator;
    }

    private void updateView() {
        for(Hand key : indicators.keySet()) {
            Pane pane = indicators.get(key);
            pane.getStyleClass().clear();
            if (key == indicator) {
                pane.getStyleClass().add("red-bg");
            } else {
                pane.getStyleClass().add("white-bg");
            }
        }
    }
}
