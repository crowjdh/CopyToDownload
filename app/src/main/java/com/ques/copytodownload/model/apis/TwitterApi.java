package com.ques.copytodownload.model.apis;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ques.copytodownload.BuildConfig;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import retrofit2.Call;

/**
 * Created by jeong on 24/05/2018.
 *
 * Simple wrapper for android twitter library.
 */

public class TwitterApi {
    private TwitterApi() {
        throw new AssertionError("You MUST NOT create the instance of this class!!");
    }

    @Nullable
    public static Call<Tweet> loadTweet(Context context, @Nullable Long statusId) {
        if (statusId == null) {
            return null;
        }
        TwitterConfig config = new TwitterConfig.Builder(context)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET))
                .debug(true)
                .build();

        Twitter.initialize(config);

        TwitterApiClient client = TwitterCore.getInstance().getApiClient();
        StatusesService service = client.getStatusesService();
        return service.show(statusId, false, false, true);
    }
}
