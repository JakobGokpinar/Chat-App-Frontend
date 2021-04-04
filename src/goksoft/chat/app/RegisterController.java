package goksoft.chat.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.io.FileNotFoundException;

public class RegisterController{

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField password1Field;
    @FXML
    private PasswordField password2Field;
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private CheckBox showPasswordsButton;

    public void changeSceneToLogin(ActionEvent event) throws FileNotFoundException {
        Function.switchBetweenRegisterAndLogin(event,"login");
    }

    public void showPasswords(){
        String pass1 = password1Field.getText();
        String pass2 = password2Field.getText();

        if(showPasswordsButton.isSelected()){
            password1Field.setVisible(false);
            textField1.setText(pass1);
            textField1.setVisible(true);
            password2Field.setVisible(false);
            textField2.setText(pass2);
            textField2.setVisible(true);
            return;
        }
        password1Field.setText(textField1.getText());
        password2Field.setText(textField2.getText());
        textField1.setVisible(false);
        password1Field.setVisible(true);
        textField2.setVisible(false);
        password2Field.setVisible(true);
    }

    public void registerButton(MouseEvent event){
        if(showPasswordsButton.isSelected()){
            showPasswordsButton.setSelected(false);
            showPasswords();
        }
        if(usernameField.getText().equals("") || usernameField.getText().equals(" ") || password1Field.getText().equals(" ") || password1Field.getText().equals("") || password2Field.getText().equals(" ") || password2Field.getText().equals("")){
            String warning = "Fill out all places!";
            WarningWindowController.warningMessage(warning);
            return;
        }
        if(!password1Field.getText().equals(password2Field.getText())){
            String warning = "Passwords are not matching!";
            WarningWindowController.warningMessage(warning);
            return;
        }
        if(password1Field.getText().length() < 4 || password2Field.getText().length() < 4){
            String warning = "Password must be at least 4 characters length!";
            WarningWindowController.warningMessage(warning);
            return;
        }

        String name = ServerFunctions.encodeURL(usernameField.getText());
        String pass = ServerFunctions.encodeURL(password1Field.getText());
        String response = "";
        try{
            response = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/register.php", "username=" + name + "&password=" + pass);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(response.equals("register successful")){
            String warning = "Registration successful!";
            WarningWindowController.warningMessage(warning);

        } else {
            String warning = "An error occurred! Could not complete your registration!";
            WarningWindowController.warningMessage(warning);
        }

    }
}
