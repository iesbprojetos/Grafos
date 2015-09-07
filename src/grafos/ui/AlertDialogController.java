package grafos.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by dfcarvalho on 9/5/15.
 */
public class AlertDialogController {
    @FXML
    protected Label msgLabel;

    @FXML
    protected void onOKButtonPressed(ActionEvent event) {
        Button src = (Button) event.getSource();
        Stage thisStage = (Stage) src.getScene().getWindow();
        thisStage.close();
    }

    public void setMessage(String msg) {
        msgLabel.setText(msg);
    }
}
