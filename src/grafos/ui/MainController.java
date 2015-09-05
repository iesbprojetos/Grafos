package grafos.ui;

import grafos.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML private ToggleGroup graphType;
    @FXML private ToggleGroup algorithmType;

    @FXML protected void handleStartButtonAction(ActionEvent event) {
        try {
            RadioButton selectedGraphType = (RadioButton) graphType.getSelectedToggle();

            int graphTypeCode = -1;

            if ("Grafo".equals(selectedGraphType.getText())) {
                graphTypeCode = Constants.TYPE_GRAPH;
            } else if ("Digrafo".equals(selectedGraphType.getText())) {
                graphTypeCode = Constants.TYPE_DIGRAPH;
            }

            RadioButton selectedAlgorithm = (RadioButton) algorithmType.getSelectedToggle();

            int algoTypeCode = -1;

            if ("Matriz de Adjacência".equals(selectedAlgorithm.getText())) {
                algoTypeCode = Constants.ALGO_MATRIX;
            } else if ("Vetor de Adjacência".equals(selectedAlgorithm.getText())) {
                algoTypeCode = Constants.ALGO_LIST;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/drawing.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            DrawingController controller = fxmlLoader.getController();
            controller.setGraphType(graphTypeCode);
            controller.setAlgoType(algoTypeCode);

            Stage drawingStage = new Stage();
            drawingStage.setTitle(selectedGraphType.getText() + " - " + selectedAlgorithm.getText());
            drawingStage.setScene(new Scene(root, 500, 500));
            drawingStage.show();

            // close main window
            Button button = (Button) event.getSource();
            Stage mainStage = (Stage) button.getScene().getWindow();
            mainStage.close();
        } catch (IOException e) {
            // TODO: ERROR
            e.printStackTrace();
        }
    }
}