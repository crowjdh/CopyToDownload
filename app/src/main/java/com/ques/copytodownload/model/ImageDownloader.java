package com.ques.copytodownload.model;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jeong on 13/05/2018.
 */

public class ImageDownloader extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = ImageDownloader.class.getSimpleName();

    @Override
    protected Boolean doInBackground(String... urls) {
        for (String url: urls) {
            try {
                File file = downloadImage(url);
                if (file != null) {
                    Log.d(TAG, "Image downloaded at: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                Log.d(TAG, "Failed to download image: " + url);
            }
        }
        return false;
    }

    @Nullable
    private File downloadImage(String urlString) throws IOException {
        File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File outFile = new File(downloadDirectory, "temp.jpg");

        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        try (InputStream is = urlConnection.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(is);
             FileOutputStream fos = new FileOutputStream(outFile)) {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = bis.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }

            return outFile;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
