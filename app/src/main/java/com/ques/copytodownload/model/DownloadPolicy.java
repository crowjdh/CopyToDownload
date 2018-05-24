package com.ques.copytodownload.model;

public class DownloadPolicy {
    public final boolean allowOverMetered;

    private DownloadPolicy(boolean allowOverMetered) {
        this.allowOverMetered = allowOverMetered;
    }

    static class Builder {
        private boolean allowOverMetered;

        Builder setAllowOverMetered(boolean allowOverMetered) {
            this.allowOverMetered = allowOverMetered;

            return this;
        }

        DownloadPolicy build() {
            return new DownloadPolicy(allowOverMetered);
        }
    }
}
