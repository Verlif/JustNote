package study.verlif.ui.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.util.Optional;

public class SimpleMsgAlert {

    private final Alert alert;

    public SimpleMsgAlert(String msg, Window owner) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("消息");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.setResizable(false);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(owner);
    }

    protected void confirm() {

    }

    public void show() {
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            confirm();
        }
    }

    public void showAndWait() {
        alert.showAndWait();
    }
}
