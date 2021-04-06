package goksoft.chat.app.ErrorClass;

public class Result {
    public String _message;
    public boolean _success;

    public Result(boolean success, String message){ //inherit base constructor of this class
        _success = success;
        _message = message;
    }

    public Result(boolean success){
        _success = success;
    }
}
