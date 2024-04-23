module com.example.blackjack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens com.example.blackjack to javafx.fxml;
    exports com.example.blackjack;
}