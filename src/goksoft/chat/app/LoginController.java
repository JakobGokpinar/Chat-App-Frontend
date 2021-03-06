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

    Functions functions = new Functions(null,null,null,null);
    public void setUsernameField(String text){ usernameField.setText(text); }

    public void setPasswordField(String text){
        passwordField.setText(text);
    }

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

    public boolean isRememberMe() throws FileNotFoundException {
        File file = new File(System.getProperty("user.home") + "/settings.txt");
        if(!file.exists()){
            System.out.println("yok");
        }
        else{
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
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
                String[] array = line.split(" ");
                if(array.length == 3){
                    setUsernameField(array[1]);
                    setPasswordField(array[2]);
                    rememberMeButton.setSelected(true);
                }
            }
        }
    }

    public void showPassword(MouseEvent event){
        String pass = passwordField.getText();
        if(showPasswordButton.isSelected()) {
            passwordField.setVisible(false);
            textField.setText(pass);
            textField.setVisible(true);
            return;
        }
        textField.setVisible(false);
        passwordField.setVisible(true);
    }


    public void signInButton(MouseEvent event) {
        String name = ServerFunctions.encodeURL(usernameField.getText());
        String pass = ServerFunctions.encodeURL(passwordField.getText());
        String cevap;
        try{
            cevap = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/login.php", "username=" + name + "&password=" + pass);
            System.out.println(cevap);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if(!cevap.equals("login successful")){
            String warningMessage = "Wrong password or username!";
            Function2.warningMessage(warningMessage);
        }
        else {
            try {
                loggedUser = usernameField.getText();
                Parent mainPanel = FXMLLoader.load(getClass().getResource("userinterfaces/panel2.fxml"));
                Scene scene = new Scene(mainPanel);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.hide();
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.setTitle("Chat");
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
