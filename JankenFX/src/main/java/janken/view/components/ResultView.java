package janken.view.components;

import janken.system.core.GameDirector;
import janken.system.core.ScoreGameDirector;
import janken.system.datamodel.Judge;
import janken.system.manager.HistoryProvider;
import janken.system.core.Player;
import janken.system.datamodel.RecordBook;
import janken.system.tool.ScoreCalculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class ResultView extends Pane {

    @FXML
    private GridPane resultTable;
    @FXML
    private ResourceBundle resources;
    @FXML
    private Label gameCntMsg;

    public ResultView() {
        ResourceBundle bundle = ResourceBundle.getBundle("janken.bundles.UIResources");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("result-view.fxml"), bundle);
        fxmlLoader.setControllerFactory(param -> this);
        try {
            Node view = fxmlLoader.load();
            this.getChildren().add(view);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void setResult(GameDirector director) {
        ArrayList<Player> players = new ArrayList<>(List.of(director.getPlayers()));

        if (director instanceof ScoreGameDirector) {
            ScoreGameDirector sgd = (ScoreGameDirector) director;
            players.sort((p1, p2) -> {
                if (sgd.getScore(p1) < sgd.getScore(p2)) {
                    return 1;
                } else if (sgd.getScore(p1) == sgd.getScore(p2)) {
                    return 0;
                } else {
                    return -1;
                }
            });
            for (int i = 0; i < players.size(); i++) {
                String name = players.get(i).getName();
                String rank = String.valueOf(i + 1);
                switch (rank) {
                    case "1" -> rank += resources.getString("st");
                    case "2" -> rank += resources.getString("nd");
                    case "3" -> rank += resources.getString("rd");
                    default -> rank += resources.getString("th");
                }
                resultTable.add(new Label(rank), 0, i);
                resultTable.add(new Label(name), 1, i);

                String points = String.valueOf(sgd.getScore(players.get(i)));
                resultTable.add(new Label(points + " " + resources.getString("points")), 2, i);
            }
        } else {
            players.sort((p1, p2) -> {
                RecordBook rb1, rb2;
                rb1 = director.getProvider().getRecordBook(p1);
                rb2 = director.getProvider().getRecordBook(p2);
                int win1, win2;
                win1 = rb1.countJudges().get(Judge.WIN);
                win2 = rb2.countJudges().get(Judge.WIN);
                return Integer.compare(win2, win1);
            });
            for (int i = 0; i < players.size(); i++) {
                String name = players.get(i).getName();
                String rank = String.valueOf(i + 1);
                switch (rank) {
                    case "1" -> rank += resources.getString("st");
                    case "2" -> rank += resources.getString("nd");
                    case "3" -> rank += resources.getString("rd");
                    default -> rank += resources.getString("th");
                }
                resultTable.add(new Label(rank), 0, i);
                resultTable.add(new Label(name), 1, i);

                RecordBook rb = director.getProvider().getRecordBook(players.get(i));
                String win = String.valueOf(rb.countJudges().get(Judge.WIN));
                resultTable.add(new Label(win + " " + resources.getString("wins")), 2, i);
            }
        }

        gameCntMsg.setText(resources.getString("gameCountsProcessed") + ": " + director.getGameCounts());

    }

    @FXML
    protected void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) resultTable.getScene().getWindow();
        stage.close();
    }
}
