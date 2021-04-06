package goksoft.chat.app;

import goksoft.chat.app.ErrorClass.ErrorResult;
import goksoft.chat.app.ErrorClass.Result;
import goksoft.chat.app.ErrorClass.SuccessResult;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ContactPanelController {

    @FXML
    private TextField emailText;
    @FXML
    private TextField subjectText;
    @FXML
    private TextArea messageArea;

    public Result sendEmail(MouseEvent event){
        String email = emailText.getText();
        email = ServerFunctions.encodeURL(email);
        String subject = subjectText.getText();
        subject = ServerFunctions.encodeURL(subject);
        String message = messageArea.getText();
        message = ServerFunctions.encodeURL(message);

        var result = ControllerRules.Run(CheckSpacesIsEmpty(email, subject, message));
        if(result != null){
            return result;
        }
        String response = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/sendmail.php", "email=" + email + "&subject=" + subject + "&message=" + message);
        System.out.println(response);
        WarningWindowController.warningMessage(response);

        return new SuccessResult();
    }

    private Result CheckSpacesIsEmpty(String... spaces){
        for (var space : spaces) {
            if(space.isBlank()){
                return new ErrorResult("Please fill out all the spaces!");
            }
        }
        return new SuccessResult();
    }
}


