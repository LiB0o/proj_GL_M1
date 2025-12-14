module gl.morpion {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires javafx.base;

    requires com.google.gson;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires annotations;

    opens gl.morpion to javafx.fxml;
    exports gl.morpion;

    exports gl.morpion.controllers;
    opens gl.morpion.controllers to javafx.fxml;

    opens gl.morpion.persistence;

    opens gl.morpion.model to com.google.gson;
}
