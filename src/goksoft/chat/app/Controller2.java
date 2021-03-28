package goksoft.chat.app;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Controller2 extends HBox{
    @FXML private SplitPane splitPane;
    @FXML private TextField searchFriendField;
    @FXML private BorderPane chatBorderPane;
    @FXML private ScrollPane friendScrollPane;
    @FXML public BorderPane settingsBorderPane;
    @FXML public HBox operationsHBox;
    @FXML public VBox mixedVBox;
    @FXML public  VBox friendSection;
    @FXML public VBox mailboxSection;
    @FXML public VBox addFriendSection;
    @FXML private VBox friendsVBox;
    @FXML private VBox notificationVBox;
    @FXML private VBox usersVBox;
    @FXML private VBox settingsTopVBox;
    @FXML private TextField searchUserField;
    @FXML public Circle profilePhoto;
    @FXML public Circle settingsButton;
    @FXML public Button mailboxButton;
    @FXML public Circle chatFriendProfilePhoto;
    @FXML public Label chatFriendName;
    @FXML public TextField messageField;
    @FXML public ListView<String> listView;
    @FXML public ChoiceBox<String> languageChoiceBox;
    @FXML public String currentFriend;
    @FXML private Label settingsUsername;
    @FXML private Label noFriendLabel;
    @FXML private Label noNotifLabel;
    @FXML private Label noUserLabel;
    @FXML private TextField newPassField;
    @FXML private TextField newNameField;
    @FXML private Label changeLabel;
    @FXML private RadioButton darkThemeButton;

    ArrayList<String> friendsNameList;
    ArrayList<String> friendRequestsNameList;
    List<Object> friendArray;

    //Execute on program start
    @FXML
    public void initialize(){
        noUserLabel.setPadding(new Insets(25,0,0,0));

        new Function2(chatBorderPane, settingsBorderPane, operationsHBox, friendScrollPane,mixedVBox, friendSection, mailboxSection, addFriendSection,
                friendsVBox,notificationVBox,usersVBox,settingsTopVBox,searchUserField,searchFriendField,profilePhoto, settingsButton, mailboxButton, chatFriendProfilePhoto, chatFriendName,  messageField,
                listView,  languageChoiceBox,  currentFriend,  friendsNameList,
                friendArray,noFriendLabel,noNotifLabel,noUserLabel,darkThemeButton);

        Function2.getFriends();
        Function2.getProfilePhoto(false);
        Function2.getFriendRequests();
        Function2.getLanguages();

        settingsUsername.setText(LoginController.loggedUser);
        messageField.setOnKeyReleased(event -> {    //Bind Enter to send messages
            if (event.getCode() == KeyCode.ENTER) sendMessage();
        });

        //Prevents the divider from moving
        final double pos = splitPane.getDividers().get(0).getPosition();
        splitPane.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                splitPane.getDividers().get(0).setPosition(pos);
            }
        });

        Thread statsThread = new Thread(() -> {
            try {
                while(true){
                    Thread.sleep(1000);

                    String friendArray = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/getFriends.php","");
                    JSONParser jsonParser = new JSONParser();
                    JSONArray jsonArray = (JSONArray) jsonParser.parse(friendArray);

                    final int[] index = {0}; //To properly sort friends with notifications.

                    for(int i = 0; i<jsonArray.size(); i++){
                        JSONArray jsonArray2 = (JSONArray) jsonArray.get(i);

                        String username = jsonArray2.get(0).toString();
                        String notifcount = jsonArray2.get(1).toString();
                        String lastMsg = jsonArray2.get(2).toString();
                        String passedTime = jsonArray2.get(3).toString();

                        final String imageName = username;
                        BufferedImage image;

                        try {
                            image = ImageIO.read(new URL(ServerFunctions.serverURL + "/getProfilePhoto.php?username=" + imageName));
                            Image imagefx = SwingFXUtils.toFXImage(image, null);

                            BorderPane friend = Function2.friendBox(imagefx,username,lastMsg,notifcount,passedTime);
                            friend.setId(username);

                            //Find the friend by his pane's id and update his status.
                            for (int j = 0; j < friendsVBox.getChildren().size(); j++) {
                                if (friendsVBox.getChildren().get(j).getId().equals(username)){
                                    int finalJ = j;
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            friendsVBox.getChildren().remove(finalJ);
                                            friendsVBox.getChildren().add(index[0],friend);
                                            index[0]++;
                                        }
                                    });
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("stats...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        statsThread.setDaemon(true);
        statsThread.start();


        Thread requestsThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000 * 5);

                    String stringArray = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/getRequests.php", "");
                    System.out.println(stringArray);
                    JSONParser jsonParser = new JSONParser();
                    JSONArray jsonArray = (JSONArray) jsonParser.parse(stringArray);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        int finalI = i;
                        String username = jsonArray.get(finalI).toString();
                        if(!Function2.friendRequestNameList.contains(username)){
                            Platform.runLater(() -> notificationVBox.getChildren().add(0,Function2.requestBox(username)));
                            Function2.friendRequestNameList.add(username);
                            Function2.dropShadowEffect(Color.RED,0.60,1,1,15,mailboxButton);
                            System.out.println("new request");
                        }
                    }
                    System.out.println("checking requests");
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        });
        requestsThread.setDaemon(true);
        requestsThread.start();

    }

    public Stage getStage(){
        return (Stage) chatBorderPane.getScene().getWindow();
    }

    public void sendMessage(){
        Function2.sendMessage();
    }

    public void searchUsers(KeyEvent event){
        Function2.searchOnUsers(event);
    }

    public void searchFriend(KeyEvent event){
        Function2.searchFriend(event);
    }

    public void openAddFriend(MouseEvent event){
        Function2.openAndCloseSections(addFriendSection.isManaged(),addFriendSection);
    }

    public void openMailbox(MouseEvent event){
        Function2.openAndCloseSections(mailboxSection.isManaged(),mailboxSection);
    }

    //Switch between Settings and Chat secions
    public void openSettings(MouseEvent event){
        if (!settingsBorderPane.isVisible()){
            settingsBorderPane.setVisible(true);
            getStage().setTitle("Settings");
        } else {
            settingsBorderPane.setVisible(false);
            getStage().setTitle("Chat");
        }
    }

    public void onMouseEnterProfilePhoto(MouseEvent event){
        Function2.getProfilePhoto(true);
    }

    public void onMouseExitProfilePhoto(MouseEvent event){
        Function2.getProfilePhoto(false);
    }

    public void changeProfilePhoto(MouseEvent event){
        Function2.changeProfilePhoto(event);
        Function2.getProfilePhoto(false);
    }

    //under development
    public void changePassword(MouseEvent event){
        /*String newpass = ServerFunctions.encodeURL(newPassField.getText());
        try {
            String result = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/changePassword.php","password=" + newpass);
            if (result.equals("password changed")){
                Function2.warningMessage("Password successfully changed!");
            } else{
                Function2.warningMessage("Password couldn't changed!");
            }
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    //under development
    public void changeUsername(MouseEvent event){
        /*String newusername = ServerFunctions.encodeURL(newNameField.getText());
        try {
            String result = ServerFunctions.HTMLRequest(ServerFunctions.serverURL + "/changeUsername.php","username=" + newusername);
            if (result.equals("username changed")){
                Function2.warningMessage("Username successfully changed!");
            } else{
                Function2.warningMessage("Username couldn't changed!");
            }
            System.out.println("change username: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void logOff(MouseEvent event){
        Function2.logOff(event);
    }

    public void contactUs(MouseEvent event){
        Function2.contactUs(event);
    }
}
