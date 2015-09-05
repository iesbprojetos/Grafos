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
 * Created by dfcarvalho on 8/22/15.
 */
public class NewArcController implements Initializable {
    @FXML private TextField textFieldV;
    @FXML private TextField textFieldW;

    private DrawingController drawingController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFieldV.requestFocus();
    }

    public void setDrawingController(DrawingController drawingController) {
        this.drawingController = drawingController;
    }

    @FXML protected void onCreateButtonPressed(ActionEvent event) {
        String strV = textFieldV.getText();
        String strW = textFieldW.getText();

        try {
            int v = Integer.parseInt(strV);
            int w = Integer.parseInt(strW);

            drawingController.insertArc(v, w);

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
