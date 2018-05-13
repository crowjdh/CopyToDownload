package com.ques.copytodownload.model.instagram;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jeong on 13/05/2018.
 */

public interface InstagramService {
    @GET("oembed")
    Call<OEmbed> loadOEmbed(@Query("url") String url);
}
