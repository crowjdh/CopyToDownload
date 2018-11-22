package com.ques.copytodownload.model;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ques.copytodownload.model.apis.InstagramApi;
import com.ques.copytodownload.model.apis.TwitterApi;
import com.ques.copytodownload.model.retrofit.InstagramDeserializer;
import com.ques.copytodownload.utils.DownloadUtils;
import com.ques.copytodownload.utils.Logger;
import com.ques.copytodownload.utils.ServiceIdentifier;
import com.ques.copytodownload.utils.ServiceIdentifier.ApiType;
import com.twitter.sdk.android.core.models.Tweet;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jeong on 13/05/2018.
 *
 * Utility class for handling copied text from clipboard.
 */

public class ClipboardURLHandler {
    private static final InstagramApi INSTAGRAM_API;

    static {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(InstagramMedia.class, new InstagramDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.instagram.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        INSTAGRAM_API = retrofit.create(InstagramApi.class);
    }

    private ClipboardURLHandler() {
        throw new AssertionError("You MUST NOT create the instance of this class!!");
    }

    public static void tryToDownloadMedia(Context context, String url) {
        ApiType type = ServiceIdentifier.getApiType(url);
        if (type == null) {
            Logger.dOrLongToast(context, "No matching api. Skipping download.");
            return;
        }

        CompletableFuture<Downloadable> task = buildCreateUrlTask(context, url, type);
        if (task == null) {
            Logger.dOrLongToast(context, "Failed to parse url: " + url);
            return;
        }
        task = applyFailureHandler(context, task);
        applyRequestDownload(context, task);
    }

    @Nullable
    private static CompletableFuture<Downloadable> buildCreateUrlTask(Context context, String url, ApiType type) {
        CompletableFuture<Downloadable> task = null;

        switch (type) {
            case Instagram:
                task = createUrlToInstagramModelTask(url);
                break;
            case Twitter:
                task = createUrlToTwitterModelTask(context, url);
                break;
        }
        return task;
    }

    private static CompletableFuture<Downloadable> applyFailureHandler(Context context, CompletableFuture<Downloadable> task) {
        task = task.handle((downloadable, throwable) -> {
            if (downloadable == null || throwable != null) {
                Logger.dOrLongToast(context, "Failed to convert url.");

                return null;
            }
            return downloadable;
        });
        return task;
    }

    private static void applyRequestDownload(Context context, CompletableFuture<Downloadable> task) {
        task.thenAccept(downloadable -> {
            try {
                DownloadUtils.requestDownload(context, downloadable);
            } catch (NetworkErrorException e) {
                Logger.i(e.getMessage());
            }
        });
    }

    @Nullable
    private static CompletableFuture<Downloadable> createUrlToInstagramModelTask(String url) {
        return CompletableFuture.supplyAsync(() -> {
            String id = ServiceIdentifier.parseInstagramPostId(url);

            if (id == null) {
                return null;
            }

            Call<InstagramMedia> call = INSTAGRAM_API.loadMedia(id);

            return executeCall(call);
        });
    }

    private static CompletableFuture<Downloadable> createUrlToTwitterModelTask(final Context context, String url) {
        return CompletableFuture.supplyAsync(() -> {
            Long statusId = ServiceIdentifier.parseTwitterStatusId(url);
            Call<Tweet> call = TwitterApi.loadTweet(context, statusId);

            return TwitterVideo.from(executeCall(call));
        });
    }

    private static <T> T executeCall(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (!response.isSuccessful()) {
                return null;
            }

            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
