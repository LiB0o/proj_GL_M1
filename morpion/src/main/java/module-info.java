module gl.morpion {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens gl.morpion to javafx.fxml;
    exports gl.morpion;
    exports gl.morpion.controllers;
    opens gl.morpion.controllers to javafx.fxml;
}