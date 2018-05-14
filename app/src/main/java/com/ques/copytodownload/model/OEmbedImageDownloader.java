package com.ques.copytodownload.model;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;

import com.ques.copytodownload.utils.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

public class OEmbedImageDownloader extends AsyncTask<Void, Void, File> {
    private WeakReference<Context> mContext;
    private OEmbed mOEmbed;

    OEmbedImageDownloader(Context context, OEmbed oEmbed) {
        mContext = new WeakReference<>(context);
        mOEmbed = oEmbed;
    }

    @Override
    protected File doInBackground(Void... ignored) {
        try {
            return downloadImage(mOEmbed);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(@Nullable File file) {
        super.onPostExecute(file);

        Context context = mContext.get();
        if (context == null) {
            return;
        }

        if (file != null) {
            Logger.iOrLongToast(mContext.get(), "Image downloaded: \n" + mOEmbed.toSimpleString());
        } else {
            showFailureToast(context, mOEmbed);
        }
    }

    private void showFailureToast(Context context, OEmbed failedOEmbed) {
        Logger.showLongToast(context, "Failed to download:\n" + failedOEmbed.toString());
    }

    private File downloadImage(OEmbed oEmbed) throws IOException {
        File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        URL url = new URL(oEmbed.thumbnail_url);
        URLConnection urlConnection = url.openConnection();

        String fileName = addFileExtension(oEmbed.media_id, urlConnection);
        File outFile = new File(downloadDirectory, fileName);
        try (InputStream is = urlConnection.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(is);
             FileOutputStream fos = new FileOutputStream(outFile)) {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = bis.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.flush();

            return outFile;
        }
    }

    private String addFileExtension(String fileName, URLConnection urlConnection) {
        String mimeType = urlConnection.getHeaderField("Content-Type");
        String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);

        return extension != null ? fileName + "." + extension : fileName;
    }
}
