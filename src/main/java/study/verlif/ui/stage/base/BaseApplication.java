package study.verlif.ui.stage.base;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class BaseApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage = getStage();
        primaryStage.show();
    }

    public abstract BaseStage getStage() throws IOException;

}
