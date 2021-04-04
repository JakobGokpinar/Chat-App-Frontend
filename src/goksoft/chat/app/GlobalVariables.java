package goksoft.chat.app;

public class GlobalVariables {

    public static boolean isThread = true;

    public static String loggedUser = LoginController.loggedUser;

    public static void setIsThread(boolean value){
        isThread = value;
    }

    public static String getLoggedUser(){
        return loggedUser;
    }
}
