package com.example.login;

import android.content.Context;
import android.net.Uri;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileCopy {
    public static void ioCopyFromUri(Context context, Uri sourceUri, String destinationPath) throws IOException {

        try (InputStream inputStream = context.getContentResolver().openInputStream(sourceUri); OutputStream outputStream = new FileOutputStream(destinationPath)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}
