module com.bugra.calculator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires exp4j;
    requires apfloat;

    opens com.bugra.calculator to javafx.fxml;
    exports com.bugra.calculator;
}
