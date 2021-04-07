package goksoft.chat.app.ErrorClass;

import goksoft.chat.app.WarningWindowController;

public class ErrorResult extends Result {

    public ErrorResult(String message) {
        super(false, message);

        WarningWindowController.warningMessage(message);
        System.out.println("Branch1");
    }

    public ErrorResult() {
        super(false);
    }
}
