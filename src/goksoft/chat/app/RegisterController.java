package goksoft.chat.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

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

    Functions functions = new Functions(null,null,null,null);
    public void changeSceneToLogin(ActionEvent event){
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("userinterfaces/login.fxml"));
            Scene loginScene = new Scene(loginRoot);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Function2.warningMessage(warning);
            return;
        }
        if(!password1Field.getText().equals(password2Field.getText())){
            String warning = "Passwords are not matching!";
            Function2.warningMessage(warning);
            return;
        }
        if(password1Field.getText().length() < 4 || password2Field.getText().length() < 4){
            String warning = "Password must be at least 4 characters length!";
            Function2.warningMessage(warning);
            return;
        }
        if(!functions.registerNewUser(usernameField.getText(),password1Field.getText())){
            String warning = "An error occurred!";
            Function2.warningMessage(warning);
        }
        else {
            String warning = "Registration successful!";
            Function2.warningMessage(warning);
        }

    }
}
