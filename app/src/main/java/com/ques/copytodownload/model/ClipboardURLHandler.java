package com.ques.copytodownload.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ques.copytodownload.model.apis.InstagramApi;
import com.ques.copytodownload.utils.Logger;
import com.ques.copytodownload.utils.ServiceIdentifier;
import com.ques.copytodownload.utils.ServiceIdentifier.ApiType;

import retrofit2.Call;
import retrofit2.Callback;
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.instagram.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        INSTAGRAM_API = retrofit.create(InstagramApi.class);
    }

    private ClipboardURLHandler() {
        throw new AssertionError("You MUST NOT create the instance of this class!!");
    }

    public static void tryToDownloadMedia(Context context, String text) {
        ApiType type = ServiceIdentifier.getApiType(text);
        if (type == null) {
            Logger.dOrLongToast(context, "No matching api. Skipping download.");
            return;
        }
        switch (type) {
            case Instagram:
                ClipboardURLHandler.downloadInstagramImage(context, text);
                break;
        }
    }

    private static void downloadInstagramImage(final Context context, String url) {
        Call<OEmbed> call = INSTAGRAM_API.loadOEmbed(url);
        call.enqueue(new Callback<OEmbed>() {
            @Override
            public void onResponse(@NonNull Call<OEmbed> call, @NonNull Response<OEmbed> response) {
                OEmbed oEmbed = response.body();
                if (oEmbed == null) {
                    return;
                }
                // TODO: Check content type(image/video)
                new OEmbedImageDownloader(context, oEmbed).execute();
            }

            @Override
            public void onFailure(@NonNull Call<OEmbed> call, @NonNull Throwable t) {
                Logger.dOrLongToast(context, "Failed to convert instagram url to OEmbed.");
            }
        });
    }
}
