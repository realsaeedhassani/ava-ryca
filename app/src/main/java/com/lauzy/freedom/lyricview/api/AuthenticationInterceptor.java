package com.lauzy.freedom.lyricview.api;

import com.lauzy.freedom.lyricview.AppController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private String authToken;

    public AuthenticationInterceptor(String token) {
        this.authToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .addHeader("Content-Type",
                        "application/json; charset=utf-8")
                .header("Authorization", authToken);
        Request request = chain.request();
        ;
        if (!AppController.hasNetwork()) {
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build();


            request = builder
                    .cacheControl(cacheControl)
                    .build();
        }
        return chain.proceed(request);
    }
}
