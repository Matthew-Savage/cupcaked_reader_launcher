package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Controller {

    @FXML
    private Pane top;
    @FXML
    private Pane mid;
    @FXML
    private Pane bot;

    public static SimpleStringProperty paneTop = new SimpleStringProperty();
    public static SimpleStringProperty paneMid  = new SimpleStringProperty();
    private Executor executor = Executors.newSingleThreadExecutor();

    public void initialize() {
        paneTop.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setTopImage(newValue);
            }
        });

        paneMid.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setMidImage(newValue);
            }
        });
        executor.execute(this::prelaunch);
    }

    private void prelaunch() {
        setTopImage("top_update_1");
        try {
            ReaderVersion.checkForUpdate();
        } catch (Exception e) {
            Controller.paneMid.setValue("mid_update_2");
            e.printStackTrace();
            Logging.logError(e.toString());
        } finally {
            //run update method with its own tcf block that then runs launcher? kek? i dont know.
        }
        Launcher.launchReader();
    }

    private void setTopImage(String imageName) {
        top.setBackground(new Background(new BackgroundImage(new Image("/assets/" + imageName + ".png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(222, 20, false, false, false, false))));
    }

    private void setMidImage(String imageName) {
        mid.setBackground(new Background(new BackgroundImage(new Image("/assets/" + imageName + ".png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(222, 20, false, false, false, false))));
    }
}
