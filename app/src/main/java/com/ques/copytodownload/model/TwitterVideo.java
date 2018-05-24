package com.ques.copytodownload.model;

import android.support.annotation.Nullable;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.VideoInfo;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings({"WeakerAccess", "unused"})
public class TwitterVideo extends Downloadable {
    public final String userName;
    public final String text;
    public final String id;
    public final String mediaUrl;

    private TwitterVideo(String userName, String text, String id, String mediaUrl) {
        this.userName = userName;
        this.text = text;
        this.id = id;
        this.mediaUrl = mediaUrl;
    }

    @Nullable
    public static TwitterVideo from(@Nullable Tweet tweet) {
        if (tweet == null) {
            return null;
        }
        List<MediaEntity> media = tweet.extendedEntities.media;

        Stream<VideoInfo.Variant> variants = Stream.of(Optional.of(media))
                .map(Optional::get).flatMap(Collection::stream)
                .map(mediaEntity -> mediaEntity.videoInfo).map(Optional::of)
                .map(Optional::get).flatMap(videoInfo -> videoInfo.variants.stream());
        try {
            Optional<String> mediaUrl = variants.max(Comparator.comparingLong(variant -> variant.bitrate))
                    .map(variant -> variant.url);

            return mediaUrl.map(s -> new TwitterVideo(tweet.user.name, tweet.text, tweet.idStr, s)).orElse(null);
        } catch(NullPointerException e) {
            // Error occurs when Stream<VideoInfo.Variant>'s state has problem.
            // Now, just ignoring works fine.
            return null;
        }

    }

    @Override
    public String getDownloadTitle() {
        return userName + " - " + text.substring(0, Math.min(text.length(), 15));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getMediaUrl() {
        return mediaUrl;
    }

    @Override
    public DownloadPolicy getPolicy() {
        return new DownloadPolicy.Builder().setAllowOverMetered(false).build();
    }
}
