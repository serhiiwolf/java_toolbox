module flow.tools.toolbox {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens flow.tools.toolbox to javafx.fxml;
    opens flow.tools.toolbox.controller to javafx.fxml;

    exports flow.tools.toolbox;
}