package com.jackie.network.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteFile {
    public static byte[] getUrlFileData(String fileUrl) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.connect();
        InputStream cin = httpConn.getInputStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = cin.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        cin.close();
        byte[] fileData = outStream.toByteArray();
        outStream.close();
        return fileData;
    }
}
