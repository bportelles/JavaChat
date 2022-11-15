package javachat.client.chatclient;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.Objects;

public class ClientGuiController {
    /*
    Initializes client objects:

          Type        | Component
        - AnchorPane -> screen_anchor
        - MenuBar    -> menu_bar
        - TextArea   -> message_box
        - Button     -> send_button
        - VBox       -> message_display
        - VBox       -> user_list
        - TextField  -> font_size_label
     */
    @FXML
    public Button leave_button;
    @FXML
    public AnchorPane screen_anchor;
    @FXML
    public MenuBar menu_bar;
    @FXML
    private TextArea message_box;
    @FXML
    private Button send_button;
    @FXML
    private VBox message_display;
    @FXML
    private TextField font_size_label;
    @FXML
    private TextField server_connection_input;
    @FXML
    private TextField username_input;
    private int font_size = 12;
    private ArrayList<Text> text_queue = new ArrayList<Text>();
    private ClientHandler client = new ClientHandler("defaultUsername");

    @FXML
    public void onSendButtonClick(MouseEvent event) {
        if (!(message_box.getText().equals(""))) {
            Text userMessage = new Text(client.getUsername() + ": " + message_box.getText() + "\n");
            userMessage.setFont(Font.font("System", font_size));
            text_queue.add(userMessage);
            client.sendMessage(userMessage.getText());
            // message_display.getChildren().add(userMessage);
            message_box.setText("");
            message_display.getChildren().add(userMessage);
        }
    }

    @FXML
    public void onMessageBoxEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER) && !(Objects.equals(message_box.getText(), ""))) {
            Text userMessage = new Text(client.getUsername() + ": " + message_box.getText());
            userMessage.setFont(Font.font("System", font_size));
            text_queue.add(userMessage);
            client.sendMessage(userMessage.getText());
            message_box.setText("");
            message_display.getChildren().add(userMessage);
        }
    }

    public void onFontSizeChange(KeyEvent event) {
        try {
            if (event.getCode().equals(KeyCode.ENTER)) {
                message_display.getChildren().setAll(new Text(""));
                font_size = Integer.parseInt(font_size_label.getText());

                for (int i = 0; i <= text_queue.size() - 1; i++) {
                    Text new_text = new Text(text_queue.get(i).getText());
                    new_text.setFont(Font.font(font_size));
                    text_queue.set(i, new_text);
                    message_display.getChildren().add(new_text);
                }
            }
        }
        catch (NumberFormatException error) {
            Text error_text = new Text("You must use a valid font size!");
            message_box.setText(error_text.getText());
        }
    }

    public void setNewUsername(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            String tempUser = client.getUsername();
            message_display.getChildren().add(new Text(tempUser + " is now " + username_input.getText() + "\n"));
            client.setUsername(username_input.getText());
        }
    }

    public void onNewServerConnect(KeyEvent event) {
       if (event.getCode().equals(KeyCode.ENTER) && !client.getConnectionStatus()) {
           System.out.println(server_connection_input.getText());
           client.connectToServer(server_connection_input.getText());
       }
    }

    public void onServerDisconnect(MouseEvent event) {
        if (client.getConnectionStatus()) {
            client.disconnectFromServer();
        }
    }

    public Text getNewMessages() {
        if (client.getConnectionStatus()) {
            Text new_message = new Text(client.getServerMessages());
            return new_message;
        } else {
            return new Text("Waiting for server...");
        }
    }
}