package goksoft.chat.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WarningWindowController{
    //Creates a warning window with a given message.

    @FXML private Label messageLabel;

    public void setLabelText(String text){
        messageLabel.setText(text);
    }

    public static void warningMessage(String text){
        try {
            FXMLLoader loader = new FXMLLoader(WarningWindowController.class.getResource("userinterfaces/warningWindow.fxml"));
            Parent root = loader.load();
            WarningWindowController windowController = loader.getController();
            windowController.setLabelText(text);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setFullScreen(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
