package com.ques.copytodownload.model.apis;

import com.ques.copytodownload.model.OEmbed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InstagramApi {
    @GET("oembed")
    Call<OEmbed> loadOEmbed(@Query("url") String url);
}
