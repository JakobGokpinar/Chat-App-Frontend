package goksoft.chat.app;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Function {

    public static BorderPane chatBorderPane;
    public static BorderPane settingsBorderPane;
    public static ScrollPane friendScrollPane;
    public static HBox operationsHBox;
    public static VBox mixedVBox;
    public static VBox friendSection;
    public static VBox mailboxSection;
    public static VBox addFriendSection;
    public static VBox friendsVBox;
    public static VBox notificationVBox;
    public static VBox usersVBox;
    public static VBox settingsTopVBox;
    public static TextField searchUserField;
    public static TextField searchFriendField;
    public static Circle profilePhoto;
    public static Circle settingsButton;
    public static Button mailboxButton;
    public static Circle chatFriendProfilePhoto;
    public static Label chatFriendName;
    public static TextField messageField;
    public static ListView<String> listView;
    public static ChoiceBox<String> languageChoiceBox;
    public static String currentFriend; //Chatted friend
    public static BorderPane currentPane; //Chatted friend's pane
    public static ArrayList<String> friendsNameList; //Stores friends' names to search over later.
    public static List<Object> friendArray; //
    public static Label noFriendLabel;
    public static Label noNotifLabel;
    public static Label noUserLabel;
    public static RadioButton darkThemeButton;

    public static int currentTimer;
    public static ArrayList<String> friendRequestNameList  = new ArrayList<>();

    //Constructor of the Function class
    public Function(BorderPane chatBorderPane, BorderPane settingsBorderPane, HBox operationsHBox, ScrollPane friendScrollPane, VBox mixedVBox, VBox friendSection, VBox mailboxSection, VBox addFriendSection,
                    VBox friendsVBox, VBox notificationVBox, VBox usersVBox, VBox settingsTopVBox, TextField searchUserField, TextField searchFriendField, Circle profilePhoto, Circle settingsButton,
                    Button mailboxButton, Circle chatFriendProfilePhoto, Label chatFriendName, TextField messageField,
                    ListView<String> listView, ChoiceBox<String> languageChoiceBox, String currentFriend, ArrayList<String> friendsNameList,
                    List<Object> friendArray, Label noFriendLabel, Label noNotifLabel, Label noUserLabel, RadioButton darkThemeButton){
        Function.chatBorderPane = chatBorderPane;
        Function.settingsBorderPane = settingsBorderPane;
        Function.operationsHBox = operationsHBox;
        Function.friendScrollPane = friendScrollPane;
        Function.mixedVBox = mixedVBox;
        Function.friendSection = friendSection;
        Function.mailboxSection = mailboxSection;
        Function.addFriendSection = addFriendSection;
        Function.friendsVBox = friendsVBox;
        Function.notificationVBox = notificationVBox;
        Function.usersVBox = usersVBox;
        Function.settingsTopVBox = settingsTopVBox;
        Function.searchUserField = searchUserField;
        Function.searchFriendField = searchFriendField;
        Function.profilePhoto = profilePhoto;
        Function.settingsButton = settingsButton;
        Function.mailboxButton = mailboxButton;
        Function.chatFriendProfilePhoto = chatFriendProfilePhoto;
        Function.chatFriendName = chatFriendName;
        Function.messageField = messageField;
        Function.listView = listView;
        Function.languageChoiceBox = languageChoiceBox;
        Function.currentFriend = currentFriend;
        Function.friendsNameList = friendsNameList;
        Function.friendArray = friendArray;
        Function.noFriendLabel = noFriendLabel;
        Function.noNotifLabel = noNotifLabel;
        Function.noUserLabel = noUserLabel;
        Function.darkThemeButton = darkThemeButton;
    }

    public static void getClickedFriend(Image friendPhoto, String friendName, BorderPane pane){

        Function.chatFriendName.setText(friendName);
        Function.chatFriendProfilePhoto.setFill(new ImagePattern(friendPhoto));
        Function.chatFriendProfilePhoto.setStrokeWidth(0);
        Function.chatBorderPane.setVisible(true);
        Function.settingsBorderPane.setVisible(false);
        Function.currentFriend = friendName;
        Function.currentPane = pane;

        final String curFriend = ServerFunctions.encodeURL(currentFriend);
        Thread thread = new Thread(()-> {
            currentTimer = (int) (Math.random() * 1000);
            int selv = currentTimer;
            while(selv == currentTimer && GlobalVariables.isThread) {
                try {
                    /*Get notification count from the server. If count is more than 0, it means it has been sent a message from opponent side.
                    Get all messages.*/
                    String cevap = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/checkNotif.php", "chatter=" + curFriend);
                    System.out.println("notif count: " + cevap);
                    Thread.sleep(1000);
                    if (!cevap.equals("0"))
                        Platform.runLater(Function::getMessages);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        getMessages();
        Function.chatBorderPane.getScene().getWindow().setOnCloseRequest(e -> System.exit(0));
    }

    //Show no match labels.
    public static void checkNoResult(String string,Label label){
        if (string.equals("[]")){
            label.setManaged(true);
            label.setVisible(true);
        } else {
            label.setVisible(false);
            label.setManaged(false);
        }
    }

    //Get logged in user's profile photo
    public static void getProfilePhoto(boolean mouse) {
        Image image = GUIComponents.returnPhoto(GlobalVariables.getLoggedUser());
        Platform.runLater(() -> {
            if (image.isError()){ //Fill profile photo in blue if image has error
                profilePhoto.setFill(Color.DODGERBLUE);
            } else {
                profilePhoto.setFill(new ImagePattern(image));
                settingsButton.setFill(new ImagePattern(image)); //Set image into the circle at bottom-left.
            }
            //Fill profile photo with black when mouseEvent is true
            if (mouse){
                profilePhoto.setFill(Color.BLACK);
                Tooltip.install(
                        profilePhoto,
                        new Tooltip("Change Profile Photo")
                );
            }
        });

    }

    public static void getFriends(){
        friendsNameList = new ArrayList<>();
        friendArray = new ArrayList<>();
        friendsVBox.getChildren().clear();
        friendsNameList.clear();

        Thread thread = new Thread(() -> {
            try {
                String stringArray = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/getFriends.php", ""); //Receive the returned json array from server
                System.out.println(stringArray);
                //Parse json array to use properly in java
                JSONParser jsonParser = new JSONParser();
                JSONArray jsonArray = (JSONArray) jsonParser.parse(stringArray);

                for(int i = 0; i<jsonArray.size(); i++){
                    JSONArray jsonArray2 = (JSONArray) jsonArray.get(i); //Get each row from array
                    String username = jsonArray2.get(0).toString();
                    String notifcount = jsonArray2.get(1).toString();
                    String lastMsg = jsonArray2.get(2).toString();
                    String passedTime = jsonArray2.get(3).toString();

                    //Create a new borderpane box for the friend with received information from server.
                    BorderPane friend = GUIComponents.friendBox(username,lastMsg,notifcount,passedTime);
                    //Show the friend at the top of the list if there is a new or unread message. Add to next index if there is not.
                    friendArray.add(friendArray.size(),friend);
                    friendsNameList.add(jsonArray2.get(0).toString());
                }
                //Get each friend from the list and add them to the friendsVBox to make them visible in the app
                for (Object element : friendArray) {
                    Platform.runLater(() -> friendsVBox.getChildren().add((Node) element));
                }
                checkNoResult(stringArray,noFriendLabel); //Check if the user has any friends, and if not, show noFriendLabel
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public static void getFriendRequests(){

        Thread thread = new Thread(() -> {
            try {
                String stringArray = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/getRequests.php", "");
                System.out.println(stringArray);
                JSONParser jsonParser = new JSONParser();
                JSONArray jsonArray = (JSONArray) jsonParser.parse(stringArray);
                Platform.runLater(() -> notificationVBox.getChildren().clear());
                //Get each requester's name from the server and create a request box with requestBox(requester name) function.
                //Add boxes to notificationVBox to show in the app.
                for(int i = 0; i<jsonArray.size(); i++){
                    int finalI = i;
                    String username = jsonArray.get(finalI).toString();
                    Platform.runLater(() -> notificationVBox.getChildren().add(0,GUIComponents.requestBox(username)));
                    friendRequestNameList.add(username);
                }
                checkNoResult(stringArray,noNotifLabel);
                if(!friendRequestNameList.isEmpty()){
                    dropShadowEffect(Color.RED, 0.60,1,1,15,mailboxButton);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public static void dropShadowEffect(Color shadowColor, double spread, int duration1, int duration2, int cycleCount, Node region){
        DropShadow shadow = new DropShadow();
        shadow.setColor(shadowColor);
        shadow.setSpread(spread);

        Timeline shadowAnimation = new Timeline(
                new KeyFrame(Duration.millis(1000 * duration1), new KeyValue(shadow.radiusProperty(), 0d)),
                new KeyFrame(Duration.millis(1000 * duration2), new KeyValue(shadow.radiusProperty(), 20d)));
        shadowAnimation.setCycleCount(cycleCount);

        Node target = region;
        target.setEffect(shadow);
        shadowAnimation.setOnFinished(evt -> target.setEffect(null));
        shadowAnimation.play();
    }

    public static void sendMessage(){
        String msg = ServerFunctions.encodeURL(messageField.getText()); //Get typed message
        String curFriend = ServerFunctions.encodeURL(currentFriend); //Get current chatted friend
        listView.getItems().add(LoginController.loggedUser + ": " + messageField.getText()); //Add message to message list
        messageField.setText(""); //Clear the message field

        Thread thread = new Thread(() ->{
            //Send message to the server for the friend to see.
            String response = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/sendMessage.php","receiver=" + curFriend + "&message=" + msg);
            System.out.println(response);
        });
        thread.start();
        friendScrollPane.setVvalue(friendScrollPane.getHmin());
    }

    public static void getMessages(){
        ArrayList<String> msgList = new ArrayList<>(); //Create a message list
        String curFriend = ServerFunctions.encodeURL(currentFriend);

        Thread thread = new Thread(() -> {
            String stringArray;
            try {
                //Send request to server and receive an array of messages with the chatted friend.
                stringArray = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/getMessage.php", "receiver=" + curFriend);
                JSONParser jsonParser = new JSONParser();
                JSONArray jsonArray = (JSONArray) jsonParser.parse(stringArray);
                for (int i = 0; i < jsonArray.size(); i++){
                    JSONArray msgArray = (JSONArray) jsonArray.get(i);
                    String user = msgArray.get(0).toString(); //Get the message owner
                    String message = msgArray.get(1).toString(); //Get the message
                    msgList.add(user + ": " + message); //Add them to the list as owner:message
                }
                Platform.runLater(() -> listView.getItems().clear());

                //Add each message form to listView
                for (int i = 0; i < msgList.size(); i++){
                    int finalI = i;
                    Platform.runLater(() -> listView.getItems().add(msgList.get(finalI)));
                }
                Platform.runLater(() -> listView.scrollTo(listView.getItems().size()-1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    //Add available languages into the app.
    public static void getLanguages(){
        languageChoiceBox.getItems().removeAll(languageChoiceBox.getItems());
        languageChoiceBox.getItems().addAll("Turkish-Türkçe", "English", "Norwegian-Norsk");
        languageChoiceBox.getSelectionModel().select("English");
    }

    public static void changeProfilePhoto(MouseEvent event){
            FileChooser fileChooser = new FileChooser();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            File file = fileChooser.showOpenDialog(stage); //Get the selected file

            if (file != null){
                try {
                    //Send selected file to the server
                    String string = ServerFunctions.FILERequest(ServerFunctions.serverURL + "/changePhoto.php", file, "photo");
                    System.out.println(string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    public static void searchFriend(KeyEvent event){
        friendsVBox.getChildren().clear(); //Remove all existing friends from view.
        String searchedFriend = searchFriendField.getText(); //Get typed characters on frontend.
        Thread thread = new Thread(() -> {
            //Search through all friends' names.
            for (int i = 0; i < friendsNameList.size(); i++) {
                //Check if friend's name includes typed characters.
                if (friendsNameList.get(i).contains(searchedFriend)) {
                    int finalI = i;
                    //Add searched friend to the emptied vbox.
                    Platform.runLater(() -> friendsVBox.getChildren().add(0, (Node) friendArray.get(finalI)));
                }
            }
        });
        thread.start();
    }

    public static void searchOnUsers(KeyEvent event){
        String writtenName = ServerFunctions.encodeURL(searchUserField.getText()); //Get typed characters on frontend.
        Thread thread = new Thread(() -> {
            try {
                //Send request to server with typed characters and receive a json array of possible users.
                String stringArray = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/getUsernames.php","username=" + writtenName);
                System.out.println(stringArray);
                JSONParser jsonParser = new JSONParser();
                JSONArray jsonArray = (JSONArray) jsonParser.parse(stringArray);
                Platform.runLater(() -> usersVBox.getChildren().clear());
                //Create box for each possible user and add it to list.
                for(int i = 0; i<jsonArray.size(); i++){
                    int finalI = i;
                    String username = jsonArray.get(finalI).toString();
                    Platform.runLater(() -> usersVBox.getChildren().add(0,GUIComponents.userBox(username)));
                }
                checkNoResult(stringArray,noUserLabel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }


    //Open or close a spesific section with animation.
    public static void openAndCloseSections(boolean value, VBox vBox){
        Stage currentStage = (Stage) Stage.getWindows().get(0); //Get current stage

        if(!value){
            Timeline timeline = new Timeline(); //Create timeline for animation.
            if(vBox==mailboxSection){
                if(!mixedVBox.getChildren().get(1).getId().equals("addFriendSection")){
                    mixedVBox.getChildren().remove(addFriendSection);
                    mixedVBox.getChildren().add(1,addFriendSection);
                    timeline.setOnFinished(actionEvent -> {
                        currentStage.setTitle("Mailbox");
                        addFriendSection.setVisible(false);
                        friendSection.setVisible(false);
                    });
                }
            }
            if(vBox==addFriendSection){
                if(!mixedVBox.getChildren().get(1).getId().equals("mailboxSection")){
                    mixedVBox.getChildren().remove(mailboxSection);
                    mixedVBox.getChildren().add(1,mailboxSection);
                    timeline.setOnFinished(actionEvent -> {
                        currentStage.setTitle("Add Friend");
                        mailboxSection.setVisible(false);
                        friendSection.setVisible(false);
                    });
                }
            }
            friendSection.setManaged(false);
            mailboxSection.setManaged(false);
            addFriendSection.setManaged(false);
            vBox.setManaged(true); vBox.setVisible(true);
            vBox.translateYProperty().set(addFriendSection.getHeight());
            KeyValue kv  = new KeyValue(vBox.translateYProperty(),0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.seconds(1),kv);
            timeline.getKeyFrames().add(kf);
            timeline.play();
        } else{
            //Close add friend section and mailbox section, open friend section.
            currentStage.setTitle("Chat");
            vBox.setVisible(false);
            vBox.setManaged(false);
            friendSection.setVisible(true);
            friendSection.setManaged(true);
        }
    }

    public static void switchBetweenRegisterAndLogin(Event event, String scene){
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (scene.equals("login")){
             window.setScene(GlobalVariables.loginScene);
        }
        if (scene.equals("register")){
            window.setScene(GlobalVariables.registerScene);
        }
        window.setTitle(Character.toUpperCase(scene.charAt(0)) + scene.substring(1));
    }

    public static void logOff(MouseEvent event){
        try{
            //Load login scene with a new stage and hide the former one.
            FXMLLoader loader = new FXMLLoader(Function.class.getResource("userinterfaces/login.fxml"));
            Parent loginPanel = loader.load();
            Scene scene = new Scene(loginPanel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.close();
            Stage newWindow = new Stage();
            newWindow.setScene(scene);
            newWindow.setResizable(false);
            newWindow.setFullScreen(false);
            newWindow.setTitle("Login");
            newWindow.show();

            //Clear stored username and password from settings.txt
            GlobalVariables.setIsThread(false);
            File file = new File(System.getProperty("user.home") + "/settings.txt");
            if(file.exists()){
                //FileWriter writer = new FileWriter(file);
                //writer.write("");
                //writer.close();
            }
            newWindow.setOnCloseRequest(windowEvent -> System.exit(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Load the contact panel
    public static void contactUs(MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(Function.class.getResource("userinterfaces/ContactPanel.fxml"));
            Parent contactPanel = loader.load();
            Scene scene = new Scene(contactPanel);
            Stage newWindow = new Stage();
            newWindow.setScene(scene);
            newWindow.setResizable(false);
            newWindow.setFullScreen(false);
            newWindow.setTitle("Contact");
            newWindow.show();
            Stage mainStage = (Stage) mixedVBox.getScene().getWindow();
            mainStage.setOnCloseRequest(windowEvent -> newWindow.close());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

