package goksoft.chat.app.ErrorClass;

import goksoft.chat.app.WarningWindowController;

public class SuccessResult extends Result{

    public SuccessResult(String message) {
        super(true, message);
        WarningWindowController.warningMessage(message);
    }

    public SuccessResult() {
        super(true);
    }
}
