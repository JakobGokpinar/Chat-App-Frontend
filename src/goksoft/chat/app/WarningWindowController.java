package goksoft.chat.app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class WarningWindowController implements Initializable{

    @FXML
    private Label messageLabel;

    public void setLabelText(String text){
        messageLabel.setText(text);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
