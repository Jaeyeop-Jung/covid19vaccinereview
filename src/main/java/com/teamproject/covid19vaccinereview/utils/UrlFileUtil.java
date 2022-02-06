package com.teamproject.covid19vaccinereview.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class UrlFileUtil {

    /**
     * methodName : urlToByteArray
     * author : Jaeyeop Jung
     * description : *
     *
     * @param imageUrl the image url
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public byte[] urlToByteArray(String imageUrl) throws IOException {

        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        connection.connect();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(connection.getInputStream(), byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

}
