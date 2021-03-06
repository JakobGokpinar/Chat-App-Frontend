package goksoft.chat.app;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("userinterfaces/login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();

        if(loginController.isRememberMe()){
            loginController.rememberMeFill();
        }
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);
        primaryStage.setX(455);
        primaryStage.setY(155);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
