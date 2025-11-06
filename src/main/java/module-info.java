module org.example.myarkanoid {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires com.almasb.fxgl.all;
    requires annotations;

    opens org.example.myarkanoid to javafx.fxml;
    opens object to javafx.fxml;

    exports org.example.myarkanoid;
    exports object;
    exports InitResource;
}
