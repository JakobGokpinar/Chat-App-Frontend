package goksoft.chat.app.ErrorClass;

import goksoft.chat.app.WarningWindowController;

public class ErrorResult extends Result {

    public ErrorResult(String message) {
        super(false, message);
        WarningWindowController.warningMessage(message);
    }

    public ErrorResult() {
        super(false);
    }
}
