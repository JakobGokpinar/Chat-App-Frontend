package goksoft.chat.app;


//File is not used right now!



import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Functions {
    @FXML
    AnchorPane mixPane;

    ScrollPane friendsScrollPane;
    AnchorPane addFriendPane;
    AnchorPane mailboxPane;

    public Functions(AnchorPane mixPane, ScrollPane friendsScrollPane, AnchorPane addFriendPane, AnchorPane mailboxPane){
        this.mixPane = mixPane;
        this.friendsScrollPane = friendsScrollPane;
        this.addFriendPane = addFriendPane;
        this.mailboxPane = mailboxPane;
    }

    public String signAndRegisterRequest(String type, String username, String password){
        String name = ServerFunctions.encodeURL(username);
        String pass = ServerFunctions.encodeURL(password);
        String cevap;
        try{
            cevap = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/" + type, "username=" + name + "&password=" + pass);
            System.out.println(cevap);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return cevap;
    }

    public boolean signIn(String username, String password) {
            return signAndRegisterRequest("login.php", username, password).equals("login successful");
    }

    public boolean registerNewUser(String username, String password){
            return signAndRegisterRequest("register.php", username, password).equals("register successful");
    }

    public void openAndCloseSections(String onOff, AnchorPane pane){
        Timeline timeline = new Timeline();

        if (onOff.equals("off")){
            pane.translateYProperty().set(0);
            KeyValue keyValue = new KeyValue(pane.translateYProperty(),mixPane.getScene().getHeight(), Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5),keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.setOnFinished(event1 -> {
                addFriendPane.setVisible(false);
                mailboxPane.setVisible(false);
                friendsScrollPane.setVisible(true);
            });
            timeline.play();
        }
        else {
            friendsScrollPane.setVisible(false);
            addFriendPane.setVisible(false);
            mailboxPane.setVisible(false);
            pane.translateYProperty().set(mixPane.getScene().getHeight());
            KeyValue kv = new KeyValue(pane.translateYProperty(),0, Interpolator.EASE_IN);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5),kv);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
            pane.setVisible(true);
        }
    }

    public void warningMessageWindow(String text){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userinterfaces/warningWindow.fxml"));
            Parent root = loader.load();
            WarningWindowController windowController = loader.getController();
            windowController.setLabelText(text);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setFullScreen(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AnchorPane showUserPane(String nameLabel, TextField textField){

        String style = "-fx-background-color:  #c2c0c0";

        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(273);
        pane.setPrefHeight(60);
        pane.setStyle(style);
        pane.setCursor(Cursor.HAND);

        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setLayoutX(39);
        circle.setLayoutY(30);
        circle.setStroke(Color.BLACK);

        Label name = new Label(nameLabel);
        name.setAlignment(Pos.CENTER);
        name.setLayoutX(69);
        name.setLayoutY(17);
        name.setFont(new Font("Dubai", 16));

        pane.getChildren().addAll(name,circle);
        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                returnUsername(name.getText(), textField);
            }
        });
        final String imageName = ServerFunctions.encodeURL(nameLabel);

        Thread thread = new Thread(()-> {
            try {
                BufferedImage image = ImageIO.read(new URL(ServerFunctions.serverURL + "/getProfilePhoto.php?username=" + imageName));
                Image imagefx = SwingFXUtils.toFXImage(image, null);

                if (imagefx.isError()){
                    circle.setFill(Color.DODGERBLUE);
                } else {
                    circle.setFill(new ImagePattern(imagefx));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();

        return pane;
    }

    public void returnUsername(String name, TextField textField){
        textField.setText(name);
    }

    public AnchorPane createNewFriendBox(String nameLabel, String lastMessage, String lastChat){
        String style = "-fx-background-color: linear-gradient(#00f8f8,#70f800)";
        AnchorPane pane = new AnchorPane();
        pane.setPrefHeight(60);
        pane.setPrefWidth(266);
        pane.setStyle(style);
        pane.setCursor(Cursor.HAND);
        Circle profilePhoto = new Circle();
        profilePhoto.setRadius(21);
        profilePhoto.setLayoutX(35);
        profilePhoto.setLayoutY(30);

        Label friendName = new Label(nameLabel);
        friendName.setLayoutX(81);
        friendName.setLayoutY(8);
        friendName.setFont(new Font("System", 16));

        Label lastMessageLabel = new Label(lastMessage);
        lastMessageLabel.setLayoutX(81);
        lastMessageLabel.setLayoutY(34);
        lastMessageLabel.setPrefWidth(132);
        lastMessageLabel.setPrefHeight(18);

        Line line = new Line();
        line.setLayoutX(205);
        line.setLayoutY(40);
        line.setRotate(90);
        line.setStartX(25);

        Label lastChatTime = new Label(lastChat);
        lastChatTime.setLayoutX(233);
        lastChatTime.setLayoutY(30);
        lastChatTime.setPrefWidth(33);
        lastChatTime.setPrefHeight(20);
        lastChatTime.setFont(new Font(10));
        pane.getChildren().addAll(profilePhoto,friendName,lastMessageLabel,line,lastChatTime);

        final String imageName = ServerFunctions.encodeURL(nameLabel);

        Thread thread = new Thread(()-> {
            try {
                BufferedImage image1 = ImageIO.read(new URL(ServerFunctions.serverURL + "/getProfilePhoto.php?username=" + imageName));
                Image imagefx = SwingFXUtils.toFXImage(image1, null);

                if (imagefx.isError()){
                    profilePhoto.setFill(Color.DODGERBLUE);
                } else {
                    profilePhoto.setFill(new ImagePattern(imagefx));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        return pane;
    }

    public AnchorPane createFriendRequestNotification(String senderUser){
        String style = "-fx-background-color:  #c2c0c0";
        AnchorPane pane = new AnchorPane();
        pane.setPrefWidth(266);
        pane.setPrefHeight(107);
        pane.setStyle(style);

        Circle circle = new Circle();
        circle.setRadius(21);
        circle.setLayoutX(35);
        circle.setLayoutY(30);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.DODGERBLUE);

        Label name = new Label(senderUser);
        name.setAlignment(Pos.CENTER);
        name.setLayoutX(68);
        name.setLayoutY(17);
        name.setFont(new Font("Dubai", 16));

        Button acceptButton = new Button("Accept");
        acceptButton.setPrefWidth(80);
        acceptButton.setPrefHeight(32);
        acceptButton.setLayoutX(68);
        acceptButton.setLayoutY(59);
        acceptButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    String cevap = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/becomeFriend.php", "added=" + name.getText());
                    System.out.println(cevap);
                    if (cevap.equals("addfriend successful")){
                        warningMessageWindow("Friend added!");
                    } else if (cevap.equals("addfriend unsuccessful")){
                        warningMessageWindow("Friend could not added!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button rejectButton = new Button("Reject");
        rejectButton.setPrefWidth(80);
        rejectButton.setPrefHeight(32);
        rejectButton.setLayoutX(170);
        rejectButton.setLayoutY(59);
        rejectButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    String cevap = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/rejectUser.php", "blockedUser=" + name.getText());
                    System.out.println(cevap);
                    if (cevap.equals("rejection successful")){
                        warningMessageWindow("Request rejected!");
                    } else if (cevap.equals("rejection unsuccessful")){
                        warningMessageWindow("Request could not rejected!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        pane.getChildren().addAll(name,circle,acceptButton,rejectButton);
        final String imageName = ServerFunctions.encodeURL(senderUser);

        Thread thread = new Thread(()-> {
            try {
                BufferedImage image1 = ImageIO.read(new URL(ServerFunctions.serverURL + "/getProfilePhoto.php?username=" + imageName));
                Image imagefx = SwingFXUtils.toFXImage(image1, null);

                if (imagefx.isError()){
                    circle.setFill(Color.DODGERBLUE);
                } else {
                    circle.setFill(new ImagePattern(imagefx));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        return pane;
    }

}
