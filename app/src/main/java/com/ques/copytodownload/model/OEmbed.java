package com.ques.copytodownload.model;

/**
 * Created by jeong on 13/05/2018.
 *
 * Class representing OEmbed.
 * @see <a href="https://oembed.com">https://oembed.com</>
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class OEmbed extends Downloadable {
    public String title;
    public String author_name;
    public String provider_name;
    public String media_id;
    public String thumbnail_url;

    @Override
    public String toString() {
        return String.format("%s\n\nURL: %s", toSimpleString(), thumbnail_url);
    }

    public String toSimpleString() {
        return String.format("%s@%s - %s", author_name, provider_name, title);
    }

    @Override
    public String getDownloadTitle() {
        return author_name + " - " + title;
    }

    @Override
    public String getId() {
        return media_id;
    }

    @Override
    public String getMediaUrl() {
        return thumbnail_url;
    }

    @Override
    public DownloadPolicy getPolicy() {
        return new DownloadPolicy.Builder().setAllowOverMetered(true).build();
    }
}
