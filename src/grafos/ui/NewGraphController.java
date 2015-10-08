package grafos.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
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
            if (v <= 0) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/alert_dialog.fxml"));
                    Parent root = (Parent) fxmlLoader.load();

                    AlertDialogController controller = fxmlLoader.getController();
                    controller.setMessage("Número de vértices precisa ser maior que 0.");

                    Scene scene = new Scene(root);

                    Stage dialog = new Stage(StageStyle.UTILITY);
                    dialog.initModality(Modality.WINDOW_MODAL);
                    dialog.setTitle("Alerta");
                    dialog.setScene(scene);
                    dialog.sizeToScene();
                    dialog.show();

                    return;
                } catch (IOException e) {
                    // TODO:
                    e.printStackTrace();
                }
            }

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
