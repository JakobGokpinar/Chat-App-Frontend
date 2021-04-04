package goksoft.chat.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("Chat app is active");

        primaryStage.setScene(GlobalVariables.loginScene);
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
