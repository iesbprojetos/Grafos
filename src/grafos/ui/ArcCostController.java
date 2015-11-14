package grafos.ui;

import grafos.datatypes.CostSetListener;
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
 * Created by dfcarvalho on 8/22/15.
 */
public class ArcCostController implements Initializable {
    @FXML private TextField textFieldCost;
    private int v;
    private int w;

    private CostSetListener listener;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFieldCost.requestFocus();
    }

    public void setV(int v) {
        this.v = v;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setListener(CostSetListener listener) {
        this.listener = listener;
    }

    @FXML protected void onSetCostButtonPressed(ActionEvent event) {
        String strCost = textFieldCost.getText();

        try {
            int cost = Integer.parseInt(strCost);
            listener.costSet(v, w, cost);

            Button src = (Button) event.getSource();
            Stage thisStage = (Stage) src.getScene().getWindow();
            thisStage.close();
        } catch (NumberFormatException e) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/alert_dialog.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                AlertDialogController controller = fxmlLoader.getController();
                controller.setMessage("O valor do vertice deve ser numerico.");

                Scene scene = new Scene(root);

                Stage dialog = new Stage(StageStyle.UTILITY);
                dialog.initModality(Modality.WINDOW_MODAL);
                dialog.setTitle("Alerta");
                dialog.setScene(scene);
                dialog.sizeToScene();
                dialog.show();
            } catch (IOException ex) {
                // TODO:
                e.printStackTrace();
            }
        } catch (ClassCastException e) {
            // TODO: can't recover
            e.printStackTrace();
        }
    }
}
