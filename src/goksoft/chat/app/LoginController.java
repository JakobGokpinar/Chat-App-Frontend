package goksoft.chat.app;

import goksoft.chat.app.ErrorClass.ErrorResult;
import goksoft.chat.app.ErrorClass.Result;
import goksoft.chat.app.ErrorClass.SuccessResult;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LoginController{

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField textField;
    @FXML private CheckBox rememberMeButton;
    @FXML private CheckBox showPasswordButton;
    @FXML private Button signinbutton;

    public static String loggedUser;

    @FXML
    public void initialize() throws FileNotFoundException {
        //default login user
        usernameField.setText("jakob");
        passwordField.setText("1234");

        //Login by pressing enter
        usernameField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) signIn();
        });
        passwordField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) signIn();
        });
        signinbutton.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) signIn();
        });
        rememberMeFill();
    }

    //Switch to register scene from login scene
    public void changeSceneToRegister(MouseEvent event){
        Function.switchBetweenRegisterAndLogin(event,"register");
    }

    public  void setUsernameField(String text){ usernameField.setText(text); }

    public  void setPasswordField(String text){
        passwordField.setText(text);
    }

    //Save the user's username and password in settings.txt file if remember me checkbox is selected.
    public void rememberMeListener(MouseEvent event){
        try {
            File file = new File(System.getProperty("user.home") + "/settings.txt");
            FileWriter writer = new FileWriter(file);
            if(rememberMeButton.isSelected()){
                writer.write("rememberme:true\n" + "username:"+usernameField.getText() + "\n" + "pass:"+passwordField.getText());
                writer.close();
            } else {
                writer.write("rememberme:false");
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void rememberMeFill() throws FileNotFoundException {
        File file = new File(System.getProperty("user.home") + "/settings.txt");
        if (file.exists()){
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if(line.equals("rememberme:true")){
                    line = scanner.nextLine();
                    if (line.contains("username:")){
                        String[] user = line.split(":");
                        if (user.length > 2){
                            setUsernameField(user[1]);
                        }
                        line = scanner.nextLine();
                        if (line.contains("pass:")){
                            user = line.split(":");
                            if (user.length > 2){
                                setPasswordField(user[1]);
                                rememberMeButton.setSelected(true);
                            }
                        }
                    }
                }
            }
        }
    }

    //Show text field instead of password field so that user can see the password uncensored.
    public void showPassword(MouseEvent event){
        String pass = passwordField.getText();
        if(showPasswordButton.isSelected()) {
            passwordField.setVisible(false);
            textField.setText(pass);
            textField.setVisible(true);
            return;
        }
        textField.setVisible(false);
        textField.setText(pass);
        passwordField.setVisible(true);
    }

    public void signInButton(MouseEvent event) {
        signIn();
    }

    public Result signIn(){
        //Get username and password and encode them.
        String name = ServerFunctions.encodeURL(usernameField.getText());
        String pass = ServerFunctions.encodeURL(passwordField.getText());

        String loginUrl = "/login.php"; // /login.php
        String url = ServerFunctions.serverURL + loginUrl;
        //Send request to server with parameters.
        String cevap = ServerFunctions.HTMLRequest(url, "username=" + name + "&password=" + pass);
        System.out.println(cevap);

        //Check the response if it is successful
        if(!cevap.equals("login successful")){
            String warningMessage = "Wrong password or username!";
            return new ErrorResult(warningMessage);
        }
        else {
            try {
                loggedUser = usernameField.getText();
                GlobalVariables.setLoggedUser(loggedUser);
                Parent mainPanel = FXMLLoader.load(LoginController.class.getResource("userinterfaces/MainPanel.fxml")); //Load main panel
                Scene scene = new Scene(mainPanel); //Create a scene with main panel
                Stage stage = (Stage) textField.getScene().getWindow();
                stage.close(); //Hide current stage with login panel
                Stage newStage = new Stage(); //Create new stage
                newStage.setScene(scene); //Set new stage's scene with main panel
                newStage.setTitle("Chat"); //Set the stage's title
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                return new ErrorResult("An Error Occurred");
            }
        }
        return new SuccessResult();
    }

}
