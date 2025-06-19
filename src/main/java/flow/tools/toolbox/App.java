package flow.tools.toolbox;
import flow.tools.toolbox.controller.MainController;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/flow/tools/toolbox/main_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        MainController controller = fxmlLoader.getController();
        controller.setPrimaryWindow(stage);

        stage.setTitle("Photo Toolbox");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);


    }
}
