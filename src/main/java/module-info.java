module com.tira {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires lombok;

    requires org.apache.logging.log4j;
    requires org.apache.commons.lang3;

    opens com.tira to javafx.fxml;
    exports com.tira;
}