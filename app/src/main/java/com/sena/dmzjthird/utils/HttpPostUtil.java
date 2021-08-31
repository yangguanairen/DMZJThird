package com.sena.dmzjthird.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/31
 * Time: 15:29
 */
public class HttpPostUtil {

    private URL url;
    HttpURLConnection connection;
    String boundary = UUID.randomUUID().toString();
    DataOutputStream ds;
    private File file;

    public HttpPostUtil (String url) throws Exception {
        this.url = new URL(url);
    }

    public void addFileParameter(File file) {
        this.file = file;
    }

    public String send() throws Exception {

        initConnection();

        ds = new DataOutputStream(connection.getOutputStream());

        writeFileParams();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = connection.getInputStream();
        int len;
        while ((len = in.read()) != -1) {
            out.write(len);
        }
        out.close();
        in.close();
        return out.toString();

    }

    private void initConnection() throws Exception {
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setChunkedStreamingMode(1024*50);
        connection.setRequestProperty("Content-Length", file.length()+"");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setReadTimeout(5000);
    }

    private void writeFileParams() throws Exception {
        String name = file.getName();
        // 添加开头数据
        ds.writeBytes("--" + boundary + "\r\n");

        ds.writeBytes("Content-Disposition: form-data; name=\""
                + "userfile[]" + "\"; filename=\"" + encode(name) + "\"\r\n");
        ds.writeBytes("Content-Type: application/octet-stream\r\n");
        ds.writeBytes("charset=utf-8\r\n");

        // 写文件数据
        ds.writeBytes("\r\n");
        ds.write(getBytes(file));
        ds.writeBytes("\r\n");

        // 添加结尾数据
        ds.writeBytes("--" + boundary + "\r\n");
        ds.writeBytes("\r\n");
    }

    private String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }

    private byte[] getBytes(File f) throws Exception {
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while (-1 != (len = in.read(buffer))) {
            out.write(buffer, 0, len);
        }
        in.close();
        return out.toByteArray();
    }



}
