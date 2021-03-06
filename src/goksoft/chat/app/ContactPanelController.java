package goksoft.chat.app;

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

    public void sendEmail(MouseEvent event){
        String email = emailText.getText();
        email = ServerFunctions.encodeURL(email);
        String subject = subjectText.getText();
        subject = ServerFunctions.encodeURL(subject);
        String message = messageArea.getText();
        message = ServerFunctions.encodeURL(message);

        try {
            if (email.equals("") || email.equals(" ") || subject.equals(" ") || subject.equals("") || message.equals(" ") || message.equals("")){
                Function2.warningMessage("Please fill out all the spaces!");
                return;
            }
            String response = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/sendmail.php", "email=" + email + "&subject=" + subject + "&message=" + message);
            Function2.warningMessage("Thanks For Your Feedback!");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
