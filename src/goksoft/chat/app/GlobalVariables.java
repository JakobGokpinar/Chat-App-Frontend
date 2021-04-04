package goksoft.chat.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class GlobalVariables {

    public static boolean isThread = true;
    public static String loggedUser = "";
    public static Parent loginRoot;
    public static Parent registerRoot;
    public static Scene loginScene;
    public static Scene registerScene;

    static {
        try {
            loginRoot = FXMLLoader.load(GlobalVariables.class.getResource("userinterfaces/login.fxml"));
            loginScene = new Scene(loginRoot);
            registerRoot = FXMLLoader.load(GlobalVariables.class.getResource("userinterfaces/register.fxml"));
            registerScene = new Scene(registerRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setIsThread(boolean value){
        isThread = value;
    }

    public static String getLoggedUser(){
        return loggedUser;
    }

    public static void setLoggedUser(String user){
        loggedUser = user;
    }
}
