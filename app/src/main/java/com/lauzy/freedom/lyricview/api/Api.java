package com.lauzy.freedom.lyricview.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface Api {
    @GET("files/{url}")
    @Streaming
    Call<ResponseBody> getLrc(@Path("url") String url);
}










