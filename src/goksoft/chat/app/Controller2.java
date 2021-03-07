package goksoft.chat.app;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;

public class Controller2 extends HBox{
    @FXML private SplitPane splitPane;
    @FXML private TextField searchFriendField;
    @FXML private BorderPane chatBorderPane;
    @FXML private ScrollPane friendScrollPane;
    @FXML public BorderPane settingsBorderPane;
    @FXML public HBox operationsHBox;
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
    List<Object> friendArray;

    //Execute on program start
    @FXML
    public void initialize(){
        noUserLabel.setPadding(new Insets(25,0,0,0));
        new Function2(chatBorderPane, settingsBorderPane, operationsHBox, friendScrollPane,friendSection,  mailboxSection,  addFriendSection,
                friendsVBox,notificationVBox,usersVBox,settingsTopVBox,searchUserField,searchFriendField,profilePhoto, settingsButton,  chatFriendProfilePhoto, chatFriendName,  messageField,
                listView,  languageChoiceBox,  currentFriend,  friendsNameList,
                friendArray,noFriendLabel,noNotifLabel,noUserLabel,darkThemeButton);

        //Platform.runLater(() -> {
        Function2.getFriends();
        Function2.getProfilePhoto(false);
        Function2.getFriendRequests();
        Function2.getLanguages();
        settingsUsername.setText(LoginController.loggedUser);
        messageField.setOnKeyReleased(event -> {    //Bind Enter to send messages
            if (event.getCode() == KeyCode.ENTER) sendMessage();
        });
        //});

        //Prevents the divider from moving
        final double pos = splitPane.getDividers().get(0).getPosition();
        splitPane.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                splitPane.getDividers().get(0).setPosition(pos);
            }
        });
    }

    public Stage getStage(){
        return (Stage) chatBorderPane.getScene().getWindow();
    }

    public void sendMessage(){
        //Platform.runLater(Function2::sendMessage);
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
        String newusername = ServerFunctions.encodeURL(newNameField.getText());
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
        }
    }

    public void logOff(MouseEvent event){
        Function2.logOff(event);
    }

    public void contactUs(MouseEvent event){
        Function2.contactUs(event);
    }
}
