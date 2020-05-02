package study.verlif.ui.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.Optional;

public abstract class ConfirmAlertBuilder {

    private final Alert alert;

    public ConfirmAlertBuilder(String msg) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(msg);
        alert.setResizable(false);
        alert.initModality(Modality.APPLICATION_MODAL);
    }

    public abstract void confirm();

    public void show() {
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            confirm();
        }
    }
}
