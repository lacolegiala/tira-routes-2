module com.tira.tiramap {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires lombok;

    opens com.tira.tiramap to javafx.fxml;
    exports com.tira.tiramap;
}