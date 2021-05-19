package goksoft.chat.app.audioRecording;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class FileHelper {

    public static String createPath(File file){
        String systemPath = System.getProperty("user.dir");
        String path = systemPath + "/src/goksoft/chat/app/audios/";
        //Find file extension, and you can use Gauva Library in here
        int index = file.getName().lastIndexOf('.');
        String extension = file.getName().substring(index + 1);

        Date date = new Date();
        UUID uuid = UUID.randomUUID();
        String result = uuid + "_" + date.getMonth() + "_" + date.getDay() + "_" + date.getYear() + "." + extension;

        return path + result;
    }


    public static String createPath(String extension){
        String systemPath = System.getProperty("user.dir");
        String path = systemPath + "/src/goksoft/chat/app/audios/";

        Date date = new Date();
        UUID uuid = UUID.randomUUID();
        String result = uuid + "_" + date.getMonth() + "_" + date.getDay() + "_" + date.getYear() + "." + extension;

        return path + result;
    }
}
