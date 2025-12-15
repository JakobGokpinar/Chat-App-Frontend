package goksoft.chat.app;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ServerFunctions {

    static String cookie = null;

    //The server URL to which all the server requests will be sent.
    static final String serverURL =
            //"https://calm-mountain-05477.herokuapp.com/"
            //"http://localhost/"
            "http://localhost:8888/Chat-App-Backend/"
            //"http://localhost:8080/api"
            ;

    //Makes a string more proper and secure for URLs.
    static String encodeURL(String rawString){
        try {
            return URLEncoder.encode(rawString, StandardCharsets.UTF_8.toString());
        } catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }

    //Sends requests to server and returns its response.
    static String HTMLRequestBytes(String URL, byte[] bytes, String type) {
        StringBuilder output = new StringBuilder("");
        String boundary =  "*****";
        java.net.URL request_url = null;
        try {
            request_url = new URL(URL);

        HttpURLConnection http_conn = (HttpURLConnection) request_url.openConnection();
        http_conn.setRequestMethod("POST");
        if (type.equals("tip")){
            http_conn.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);
        }
        http_conn.setDoOutput(true);
        System.out.println("cookie: " + cookie);

        if(cookie != null)
            http_conn.setRequestProperty("Cookie", cookie);

        try (DataOutputStream wr = new DataOutputStream(http_conn.getOutputStream())) {
            wr.write(bytes);
            wr.flush();
        } catch (Exception ignored){}

        BufferedReader in = new BufferedReader(
                new InputStreamReader(http_conn.getInputStream()));
        String line;
        while((line = in.readLine()) != null)
            output.append(line);
        in.close();

        if(cookie == null)
            cookie = http_conn.getHeaderField("set-cookie");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    //Function for changing the profile photo.
    static String FILERequest(String URL, File file, String name) {
        String attachmentName = name;
        String attachmentFileName = file.getName();
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        ByteArrayOutputStream stream = null;

        try {
            stream = new ByteArrayOutputStream();
            stream.write((twoHyphens + boundary + crlf).getBytes());

            stream.write(("Content-Disposition: form-data; name=\"" +
                    attachmentName + "\";filename=\"" +
                    attachmentFileName + "\"" + crlf).getBytes());

            stream.write(crlf.getBytes());

            stream.write(Files.readAllBytes(file.toPath()));
            stream.write(crlf.getBytes());
            stream.write((twoHyphens + boundary +
                    twoHyphens + crlf).getBytes());
        } catch (Exception e){
            e.printStackTrace();
        }

        return HTMLRequestBytes(URL, stream.toByteArray(),"tip");
    }

    //Function for sending requests to an exact URL with string-typed parameters and returns the response from server.
    static String HTMLRequest(String URL, String params) {

        return HTMLRequestBytes(URL, params.getBytes(),"");
    }
}
