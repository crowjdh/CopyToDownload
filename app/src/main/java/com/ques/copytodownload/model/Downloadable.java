package com.ques.copytodownload.model;

import android.webkit.MimeTypeMap;

public abstract class Downloadable {
    public abstract String getDownloadTitle();
    public abstract String getId();
    public abstract String getMediaUrl();
    public abstract DownloadPolicy getPolicy();

    public String getFileName() {
        String extension = MimeTypeMap.getFileExtensionFromUrl(getMediaUrl());

        return extension != null ? getId() + "." + extension : getId();
    }
}
