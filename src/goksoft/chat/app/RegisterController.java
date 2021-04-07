package goksoft.chat.app;



import goksoft.chat.app.ErrorClass.ErrorResult;
import goksoft.chat.app.ErrorClass.Result;
import goksoft.chat.app.ErrorClass.SuccessResult;
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

    public Result registerButton(MouseEvent event){
        if(showPasswordsButton.isSelected()){
            showPasswordsButton.setSelected(false);
            showPasswords();
        }

        var result = ControllerRules.Run(CheckPlaceEmpty(), CheckLength(), CheckIfNotMatch());
        if(result != null){
            return result;
        }

        String name = ServerFunctions.encodeURL(usernameField.getText());
        String pass = ServerFunctions.encodeURL(password1Field.getText());
        String response = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/register.php", "username=" + name + "&password=" + pass);
        System.out.println(response);

        return CheckRegisterIsSuccessful(response);
    }

    private Result CheckPlaceEmpty(){
        if(usernameField.getText().isBlank() || password1Field.getText().isBlank() || password2Field.getText().isBlank()){
            String warning = "Please fill out all places!";
            return new ErrorResult(warning);
        }
        return new SuccessResult();
    }

    private Result CheckLength(){
        if(password1Field.getText().length() < 4 || password2Field.getText().length() < 4){
            String warning = "Password must be at least 4 characters length!";
            return new ErrorResult(warning);
        }
        return new SuccessResult();
    }

    private Result CheckIfNotMatch(){
        if(!password1Field.getText().equals(password2Field.getText())){
            String warning = "Passwords are not matching!";
            return new ErrorResult(warning);
        }
        return new SuccessResult();
    }

    private Result CheckRegisterIsSuccessful(String response){
        if(response.equals("register successful")){
            String warning = "Registration successful!";
            return new SuccessResult(warning);
        }
        String warning = "An error occurred! Could not complete your registration!";
        return new ErrorResult(warning);
    }
}
