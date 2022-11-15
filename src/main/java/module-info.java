module javachat.client.chatclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens javachat.client.chatclient to javafx.fxml;
    exports javachat.client.chatclient;
}