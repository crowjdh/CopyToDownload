package com.ques.copytodownload.model;

import android.util.Log;

import com.ques.copytodownload.model.instagram.InstagramService;
import com.ques.copytodownload.model.instagram.OEmbed;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jeong on 13/05/2018.
 */

public class ClipboardURLHandler {
    private static final String TAG = ClipboardURLHandler.class.getSimpleName();
    private static final InstagramService sInstagramService;

    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.instagram.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sInstagramService = retrofit.create(InstagramService.class);
    }

    private ClipboardURLHandler() {
        throw new AssertionError("You MUST NOT create the instance of this class!!");
    }

    public static void downloadInstagramImage(String url) {
        Call<OEmbed> call = sInstagramService.loadOEmbed(url);
        call.enqueue(new Callback<OEmbed>() {
            @Override
            public void onResponse(Call<OEmbed> call, Response<OEmbed> response) {
                OEmbed oEmbed = response.body();
                if (oEmbed == null) {
                    return;
                }
                Log.i(TAG, oEmbed.thumbnail_url);
                new ImageDownloader().execute(oEmbed.thumbnail_url);
            }

            @Override
            public void onFailure(Call<OEmbed> call, Throwable t) {

            }
        });
    }
}
