module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jfree.fxgraphics2d;
    requires jfreechart;
    requires java.desktop;
    requires java.xml.crypto;
    requires java.sql;
    requires javafx.swing;
    requires poi;
    requires poi.ooxml;
    requires commons.codec;



    opens app to javafx.fxml;
    opens app.gui to javafx.fxml;
    opens app.gui.auth to javafx.fxml;
    opens app.gui.profile to javafx.fxml;
    opens app.gui.registration to javafx.fxml;
    opens app.gui.project to  javafx.fxml,javafx.base;
    opens app.gui.home to javafx.fxml;
    opens app.gui.administration to javafx.fxml;
    opens app.db to javafx.base;

    exports app.gui to javafx.fxml;
    exports app.gui.auth to javafx.fxml;
    exports app.gui.profile to javafx.fxml;
    exports app.gui.registration to javafx.fxml;
    exports app.gui.project to  javafx.fxml;
    exports app.gui.home to javafx.fxml;
    exports app.gui.administration to javafx.fxml;
    exports app;
}