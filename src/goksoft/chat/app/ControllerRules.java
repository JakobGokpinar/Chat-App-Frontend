package goksoft.chat.app;

import goksoft.chat.app.errorClass.Result;

public class ControllerRules {
    public static Result Run(Result... logics){
        for(var result : logics){
            if(!result._success){
                return result;
            }
        }
        return null;
    }
}
