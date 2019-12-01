module app {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    opens app to javafx.fxml;
    opens app.gui.auth to javafx.fxml;
    opens app.gui.profile to javafx.fxml;
    opens app.gui.registration to javafx.fxml;

    exports app.gui.auth to javafx.fxml;
    exports app.gui.profile to javafx.fxml;
    exports app.gui.registration to javafx.fxml;
    exports app;
}