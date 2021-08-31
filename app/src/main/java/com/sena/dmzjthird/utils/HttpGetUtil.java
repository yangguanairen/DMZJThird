package com.sena.dmzjthird.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/31
 * Time: 19:57
 */
public class HttpGetUtil {

    private URL url;


    public HttpGetUtil(String url) throws Exception {
        this.url = new URL(url);
    }

    public String getResponse() throws Exception {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = connection.getInputStream();
        int len;
        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
        connection.disconnect();

        return out.toString();



    }

}
