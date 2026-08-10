module com.echoes.echoes {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;
    requires bcrypt;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.media;
    opens com.echoes.echoes to javafx.fxml;
    opens com.echoes.echoes.model to javafx.fxml;
    opens com.echoes.echoes.spotify to com.fasterxml.jackson.databind;
    exports com.echoes.echoes.spotify;
    requires com.fasterxml.jackson.databind;
    exports com.echoes.echoes;
    exports com.echoes.echoes.database;
    exports com.echoes.echoes.model;
    exports com.echoes.echoes.controller;
    opens com.echoes.echoes.controller to javafx.fxml;
}