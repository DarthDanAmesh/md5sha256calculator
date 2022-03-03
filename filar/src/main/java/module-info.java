module com.example.filar {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.filar to javafx.fxml;
    exports com.example.filar;
}