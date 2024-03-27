module com.gnegdev.gpaint {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;


    opens com.gnegdev.gpaint to javafx.fxml;
    exports com.gnegdev.gpaint;
}