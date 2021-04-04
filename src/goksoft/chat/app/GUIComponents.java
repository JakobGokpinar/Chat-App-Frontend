package goksoft.chat.app;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class GUIComponents {

    public static Image returnPhoto(String username){
        String imageName = ServerFunctions.encodeURL(username);
        Image imagefx = null;
        try {
            BufferedImage image = ImageIO.read(new URL(ServerFunctions.serverURL + "/getProfilePhoto.php?username=" + imageName));
            imagefx = SwingFXUtils.toFXImage(image,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagefx;
    }

    private static Circle createPhotoCircle(Image photo, double radius, double strokeWidth){
        Circle circle = new Circle();
        circle.setRadius(radius);
        circle.setStrokeWidth(strokeWidth);
        if (photo.isError()){
            circle.setFill(Color.BLACK);
        } else {
            circle.setFill(new ImagePattern(photo));
        }
        return circle;
    }

    private static BorderPane createBorderPane(double height, double width, String style, Cursor cursor, String id){
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefHeight(height);
        borderPane.setPrefWidth(width);
        if(style != null) borderPane.setStyle(style);
        if(cursor != null) borderPane.setCursor(cursor);
        if(id != null) borderPane.setId(id);
        return borderPane;
    }

    private static Label createLabel(double maxHeight, double maxWidth, String text, double height, double width, double font, Color color, String stringColor, Pos pos){
        Label label = new Label(text);
        if (maxHeight != 0) label.setMaxHeight(maxHeight);
        if (maxWidth != 0) label.setMaxWidth(maxWidth);
        label.setPrefHeight(height);
        label.setPrefWidth(width);
        if (font != 0) label.setFont(new Font(font));
        if (color != null) label.setTextFill(color);
        if (stringColor != null) label.setTextFill(Color.web(stringColor));
        if (pos != null) label.setAlignment(pos);
        return label;
    }

    private static HBox createHBox(double maxHeight, double maxWidth, double height, double width, String style, Pos pos, double[] insetArray){
        HBox hBox = new HBox();
        if (maxHeight != 0) hBox.setMaxHeight(maxHeight);
        if (maxWidth != 0) hBox.setMaxWidth(maxWidth);
        hBox.setPrefHeight(height);
        hBox.setPrefWidth(width);
        if (style != null) hBox.setStyle(style);
        if (pos != null) hBox.setAlignment(pos);
        if (insetArray != null) hBox.setPadding(new Insets(insetArray[0],insetArray[1],insetArray[2], insetArray[3]));
        return hBox;
    }

    private static Button createButton(double height, double width,String text, String style, Color color, EventHandler event, Pos pos){
        Button button = new Button(text);
        button.setPrefHeight(height);
        button.setPrefWidth(width);
        if (style != null) button.setStyle(style);
        if (color != null) button.setTextFill(color);
        if (event != null){
            button.setOnMouseClicked((event));
            button.setOnKeyReleased(e -> { if (e.getCode() == KeyCode.ENTER) event.handle(e); });
        }
        if (pos != null) button.setAlignment(pos);
        return button;
    }

    private static EventHandler addFriendEvent(String userName){
        EventHandler event = (EventHandler<?>) mouseEvent -> {
            String receiverUser = ServerFunctions.encodeURL(userName);
            String cevap = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/sendFriendRequest.php", "receiverUser=" + receiverUser);
            System.out.println(cevap);
            if (cevap.equals("already friends")){
                WarningWindowController.warningMessage("You're already friends!");
            } else if (cevap.equals("request sent")){
                WarningWindowController.warningMessage("New request sent");
            } else if (cevap.equals("already sent")){
                WarningWindowController.warningMessage("You already sent request!");
            }
        };
        return event;
    }

    private static EventHandler acceptFriendEvent(String senderName) {
        String addedName = ServerFunctions.encodeURL(senderName);
        EventHandler event = (EventHandler<?>) mouseEvent -> {
            String cevap = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/becomeFriend.php", "added=" + addedName);
            System.out.println(cevap);
            if (cevap.equals("addfriend successful")) {
                WarningWindowController.warningMessage("Friend added!");
            } else if (cevap.equals("addfriend unsuccessful")) {
                WarningWindowController.warningMessage("Friend could not added!");
            }
            Function2.getFriendRequests();
            Function2.getFriends();
        };
        return event;
    }

    private static EventHandler rejectFriendEvent(String senderName) {
        String blockedName = ServerFunctions.encodeURL(senderName);
        EventHandler event = (EventHandler<?>) mouseEvent -> {
            String cevap = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/rejectUser.php", "blockedUser=" + blockedName);
            System.out.println(cevap);
            if (cevap.equals("rejection successful")){
                WarningWindowController.warningMessage("Request rejected!");
            } else if (cevap.equals("rejection unsuccessful")){
                WarningWindowController.warningMessage("Request could not rejected!");
            }
            Function2.getFriendRequests();
        };
        return event;
    }

    public static BorderPane friendBox(String friendName, String lastMessage, String notifCount, String lastDate){
        //Style of the borders of the pane
        String style = "-fx-border-color: #949494; -fx-border-width: 0.5px 0px 0.5px 0px";

        //Create the border pane
        BorderPane borderPane = createBorderPane(86,237,style,Cursor.HAND, friendName);

        Image friendPhoto = returnPhoto(friendName);
        //Create a circle for the user's profile photo and locate it in the border pane
        Circle friendProfilePhoto = createPhotoCircle(friendPhoto,21,0);
        BorderPane.setAlignment(friendProfilePhoto, Pos.CENTER);
        BorderPane.setMargin(friendProfilePhoto,new Insets(0,0,30,10));

        //Create and locate username
        Label friend = createLabel(0,0,friendName,30,246,15,Color.WHITE,null,null);
        friend.setPadding(new Insets(10,0,0,70));
        BorderPane.setAlignment(friend,Pos.CENTER_LEFT);

        //Create and locate last message
        Label lstMsg = createLabel(0,1.7976931348623157E308,lastMessage,18,49,0,null,"#949494",null);
        BorderPane.setAlignment(lstMsg,Pos.CENTER_LEFT);
        BorderPane.setMargin(lstMsg,new Insets(0,0,10,15));

        //Create Hbox where notification count will be shown
        double[] array = {0,0,10,0};
        HBox hBox = createHBox(0,1.7976931348623157E308,42,87,null,Pos.CENTER_LEFT,array);
        BorderPane.setAlignment(hBox,Pos.CENTER_LEFT);

        //Circle for notification count
        Circle notifCircle = new Circle(9);
        notifCircle.setFill(Color.web("#ff6f00")); //Bakcground color of the circle
        notifCircle.setStroke(Color.web("#ff6f00")); //Border color of the circle
        Text text = new Text(notifCount); //Text to show notification count
        text.setFill(Color.WHITE);
        text.setBoundsType(TextBoundsType.VISUAL);
        StackPane stack = new StackPane();
        stack.getChildren().addAll(notifCircle, text);

        //Create and locate last chat date in the pane
        Label lstDt = createLabel(0,1.7976931348623157E308,lastDate,18,71,0,null,"#949494",Pos.CENTER);
        BorderPane.setMargin(lstDt,new Insets(0,10,0,0));

        if (Integer.parseInt(notifCount) > 0){ hBox.getChildren().addAll(stack,lstDt); } //Show notification count if only it is more than 0
        else { hBox.getChildren().addAll(lstDt); }

        //Set locations of the properties in the pane
        borderPane.setLeft(friendProfilePhoto);
        borderPane.setTop(friend);
        borderPane.setCenter(lstMsg);
        borderPane.setRight(hBox);

        //Create an action when border pane is clicked.
        borderPane.setOnMouseClicked((event)-> Function2.getClickedFriend(friendPhoto,friendName, borderPane));
        return borderPane;
    }

    public static BorderPane requestBox(String requesterName){
        String style = "-fx-background-color: #ffb700; -fx-border-color: #ffaa00; -fx-border-width: 1.5px";
        String style1 = "-fx-background-color: #ff6f00";
        String style2 = "-fx-background-color: #1c1b1b";

        BorderPane requestPane = createBorderPane(75,237,style,null,null);

        //Circle for profile photo
        Image image = returnPhoto(requesterName);
        Circle circle = createPhotoCircle(image,21,0);
        BorderPane.setAlignment(circle,Pos.CENTER);
        BorderPane.setMargin(circle,new Insets(10,0,0,10));

        //Label for requester's username
        Label senderName = createLabel(0,0,requesterName,24,158,14,null,null,null);
        senderName.setPadding(new Insets(10,0,0,0));
        BorderPane.setAlignment(senderName,Pos.CENTER_LEFT);
        BorderPane.setMargin(senderName,new Insets(0,0,0,20));

        //HBox where accept and reject button will be located
        double[] array = {0,0,5,0};
        HBox hBox = createHBox(0,0,38,166,null,Pos.CENTER_LEFT,array);
        hBox.setSpacing(15);
        BorderPane.setMargin(hBox,new Insets(0,0,0,55));
        BorderPane.setAlignment(hBox,Pos.CENTER);

        //Accept button to accept the request
        Button acceptButton = createButton(26,65,"Accept",style1,Color.WHITE,acceptFriendEvent(requesterName),null);
        //Reject button to deny the request and block the user.
        Button rejectButton = createButton(26,59,"Reject",style2,Color.WHITE,rejectFriendEvent(requesterName),null);
        hBox.getChildren().addAll(acceptButton,rejectButton);

        //Locate the properties in the pane
        requestPane.setLeft(circle);
        requestPane.setCenter(senderName);
        requestPane.setBottom(hBox);

        return requestPane;
    }

    public static HBox userBox(String userName){ // creates a box that contains user's information when a user is searched.
        String backgroundStyle = "-fx-background-color:   #ff7d1a; -fx-border-color: #ff6f00; -fx-border-width: 1.5px";
        String buttonStyle = "-fx-background-color: #1c1b1b";

        //HBox where all properties will be gathered
        HBox hBox = createHBox(0,0,59,237,backgroundStyle, Pos.CENTER_LEFT,null);

        Image photo = returnPhoto(userName);
        Circle userPhoto = createPhotoCircle(photo,21,0);
        HBox.setMargin(userPhoto,new Insets(0,0,0,10));

        Label username = createLabel(0,1.7976931348623157E308,userName,20,87,14,Color.WHITE,null, Pos.CENTER);
        //HBox.setHgrow(username, Priority.ALWAYS);
        HBox.setMargin(username,new Insets(0,10,0,10));

        //An 'Add' button to send friend request to the user.
        Button addButton = createButton(26,51,"+ Add", buttonStyle,Color.WHITE, addFriendEvent(userName),null);
        HBox.setMargin(addButton,new Insets(0,10,0,0));

        hBox.getChildren().addAll(userPhoto,username,addButton);

        return hBox;
    }
}
