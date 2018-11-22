package com.ques.copytodownload.model.apis;

import com.ques.copytodownload.model.InstagramMedia;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface InstagramApi {
    @GET("p/{post_id}/?__a=1")
    Call<InstagramMedia> loadMedia(@Path("post_id") String post_id);
}
