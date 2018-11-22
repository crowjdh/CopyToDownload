package com.ques.copytodownload.utils;

import android.accounts.NetworkErrorException;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.ques.copytodownload.model.Downloadable;

import java.io.File;

/**
 * Created by jeong on 24/05/2018.
 *
 * Simple wrapper for android.app.DownloadManager.
 */

public class DownloadUtils {
    private DownloadUtils() {
        throw new AssertionError("You MUST NOT create the instance of this class!!");
    }

    public static void requestDownload(Context context, Downloadable downloadable) throws NetworkErrorException {
        boolean policyNotMet = NetworkUtils.isConnectedToMetered(context) && !downloadable.getPolicy().allowOverMetered;
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (policyNotMet || downloadmanager == null) {
            String message = policyNotMet ? "Network policy not met" : "No download manager found";
            throw new NetworkErrorException(message);
        }

        enqueueDownload(downloadable, downloadmanager);
        enqueueAdditionalDownloads(downloadable, downloadmanager);
    }

    private static void enqueueDownload(Downloadable downloadable, DownloadManager downloadmanager) {
        DownloadManager.Request request = buildDownloadRequest(downloadable);
        downloadmanager.enqueue(request);
    }

    private static void enqueueAdditionalDownloads(Downloadable downloadable, DownloadManager downloadmanager) {
        if (downloadable.hasAdditionalDownloadables()) {
            for (Downloadable additionalDownloadable : downloadable.getAdditionalDownloadables()) {
                enqueueDownload(additionalDownloadable, downloadmanager);
            }
        }
    }

    @NonNull
    private static DownloadManager.Request buildDownloadRequest(Downloadable downloadable) {
        File outFile = makeOutputFile(downloadable);
        return buildDownloadRequest(downloadable, outFile);
    }

    @NonNull
    private static File makeOutputFile(Downloadable downloadable) {
        File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return new File(downloadDirectory, downloadable.getFileName());
    }

    @NonNull
    private static DownloadManager.Request buildDownloadRequest(Downloadable downloadable, File outFile) {
        Uri uri = Uri.parse(downloadable.getMediaUrl());

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadable.getDownloadTitle());
        request.setAllowedOverMetered(downloadable.getPolicy().allowOverMetered);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(outFile));

        return request;
    }
}
