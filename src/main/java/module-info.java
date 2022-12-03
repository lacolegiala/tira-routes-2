module com.tira {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.tira to javafx.fxml;
    exports com.tira;
}