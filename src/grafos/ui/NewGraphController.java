package grafos.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by dfcarvalho on 8/21/15.
 */
public class NewGraphController implements Initializable {
    @FXML private TextField textFieldVertices;

    private DrawingController drawingController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFieldVertices.requestFocus();
    }

    public void setDrawingController(DrawingController drawingController) {
        this.drawingController = drawingController;
    }

    @FXML protected void onCreateButtonPressed(ActionEvent event) {
        String strV = textFieldVertices.getText();
        try {
            int v = Integer.parseInt(strV);
            drawingController.createGraph(v);

            Button src = (Button) event.getSource();
            Stage thisStage = (Stage) src.getScene().getWindow();
            thisStage.close();
        } catch (NumberFormatException e) {
            // TODO: show error
            e.printStackTrace();
        } catch (ClassCastException e) {
            // TODO: can't recover
            e.printStackTrace();
        }
    }
}
