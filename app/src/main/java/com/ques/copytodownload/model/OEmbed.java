package com.ques.copytodownload.model;

/**
 * Created by jeong on 13/05/2018.
 */

public class OEmbed {
    public String title;
    public String author_name;
    public String provider_name;
    public String media_id;
    public String thumbnail_url;

    @Override
    public String toString() {
        return String.format("%s@%s - %s\n\nURL: %s", author_name, provider_name, title, thumbnail_url);
    }
}
