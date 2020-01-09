package com.lauzy.freedom.lyricview.api;

import com.lauzy.freedom.lyricview.AppController;

import java.io.IOException;
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
//        Request original = chain.request();
//        Request.Builder builder;
//                = original.newBuilder()
//                .addHeader("Content-Type",
//                        "application/json; charset=utf-8")
//                .header("Authorization", authToken);
//        Request request = chain.request();



        Request request = chain.request();
        if (AppController.hasNetwork()) {
            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
        } else {
            request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
        }
        return chain.proceed(request);


//        Response originalResponse = chain.proceed(chain.request());
//
//        if (AppController.hasNetwork()) {
//            int maxAge = 60; // read from cache for 1 minute
//            return originalResponse.newBuilder()
//                    .header("Cache-Control", "public, max-age=" + maxAge)
//                    .build();
//        } else {
//            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
//            return originalResponse.newBuilder()
//                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                    .build();
//        }




//        if (!AppController.hasNetwork()) {
//            CacheControl cacheControl = new CacheControl.Builder()
//                    .maxStale(7, TimeUnit.DAYS)
//                    .build();
//            request = builder
//                    .cacheControl(cacheControl)
//                    .build();
//        }
//        return chain.proceed(request);
    }
}
