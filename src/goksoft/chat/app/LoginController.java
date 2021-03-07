package goksoft.chat.app;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LoginController{

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField textField;
    @FXML
    private CheckBox rememberMeButton;
    @FXML
    private CheckBox showPasswordButton;

    public static String loggedUser;

    public void setUsernameField(String text){ usernameField.setText(text); }

    public void setPasswordField(String text){
        passwordField.setText(text);
    }

    //Switch to register scene from login scene
    public void changeSceneToRegister(MouseEvent event){
        try {
            Parent registerRoot = FXMLLoader.load(getClass().getResource("userinterfaces/register.fxml"));
            Scene registerScene = new Scene(registerRoot);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(registerScene);
            window.setTitle("Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Save the user's username and password in settings.txt file if remember me checkbox is selected.
    public void rememberMeListener(MouseEvent event){
        try {
            File file = new File(System.getProperty("user.home") + "/settings.txt");
            FileWriter writer = new FileWriter(file);
            if(rememberMeButton.isSelected()){
                writer.write("rememberme:true " + usernameField.getText() + " " + passwordField.getText());
                writer.close();
            } else {
                writer.write("rememberme:false");
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Check if remember me is true
    public boolean isRememberMe() throws FileNotFoundException {
        File file = new File(System.getProperty("user.home") + "/settings.txt");    //get settings.txt file

        if(file.exists()){
            Scanner scanner = new Scanner(file); //read the file

            while(scanner.hasNextLine()){
                String line = scanner.nextLine(); //Read the next line
                if(line.contains("rememberme:true")){
                    return true;
                }
            }
        }
        return false;
    }

    public void rememberMeFill() throws FileNotFoundException {
        File file = new File(System.getProperty("user.home") + "/settings.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.contains("rememberme:true")){
                String[] array = line.split(" "); //Parse the datas from line by " ".
                if(array.length == 3){  //Check if line contains both username and password
                    setUsernameField(array[1]);
                    setPasswordField(array[2]);
                    rememberMeButton.setSelected(true);
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
        //Get username and password and encode them.
        String name = ServerFunctions.encodeURL(usernameField.getText());
        String pass = ServerFunctions.encodeURL(passwordField.getText());

        String cevap;
        try{
            //Send request to server with parameters.
            cevap = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/login.php", "username=" + name + "&password=" + pass);
            System.out.println(cevap);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //Check the response if it is successful
        if(!cevap.equals("login successful")){
            String warningMessage = "Wrong password or username!";
            Function2.warningMessage(warningMessage);
        }
        else {
            try {
                loggedUser = usernameField.getText();
                Parent mainPanel = FXMLLoader.load(getClass().getResource("userinterfaces/panel2.fxml")); //Load main panel
                Scene scene = new Scene(mainPanel); //Create a scene with main panel
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Get stage
                stage.hide(); //Hide current stage with login panel
                Stage newStage = new Stage(); //Create new stage
                newStage.setScene(scene); //Set new stage's scene with main panel
                newStage.setTitle("Chat"); //Set the stage's title
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
