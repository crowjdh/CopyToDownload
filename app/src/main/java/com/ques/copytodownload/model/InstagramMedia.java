package com.ques.copytodownload.model;

import java.util.List;

/**
 * Created by jeong on 22/11/2018.
 */

public class InstagramMedia extends Downloadable {
    public String title;
    public String id;
    public List<DisplayResource> display_resources;
    public List<InstagramMedia> other_images;

    @Override
    public String getDownloadTitle() {
        return title;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getMediaUrl() {
        // TODO: Pick largest image, instead of assuming the last image is the largest
        return display_resources.get(display_resources.size() - 1).src;
    }

    @Override
    public DownloadPolicy getPolicy() {
        return new DownloadPolicy.Builder().setAllowOverMetered(true).build();
    }

    @Override
    public List<? extends Downloadable> getAdditionalDownloadables() {
        return other_images.subList(1, other_images.size());
    }

    class DisplayResource {
        String src;
        String config_width;
        String config_height;
    }
}
