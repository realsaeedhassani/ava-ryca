package com.lauzy.freedom.lyricview.api;


import android.content.Context;
import android.text.TextUtils;

import com.lauzy.freedom.lyricview.AppController;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static final long cacheSize = 50 * 1024 * 1024; // 5 MB
    private static String API_BASE_URL = "http://3d4.ir/";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static ServiceGenerator instance;
    private static Context mContext;

    public static ServiceGenerator getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new ServiceGenerator();
        }
        return instance;
    }

    public static <S> S createService(Class<S> serviceClass) throws KeyManagementException, NoSuchAlgorithmException {
        return createService(serviceClass, null, null);
    }

    private static <S> S createService(
            Class<S> serviceClass, String username, String password) throws NoSuchAlgorithmException, KeyManagementException {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
//            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, null);
//            return createService(serviceClass, authToken);
        }

        return createServiceHttps(serviceClass, null, null);
    }


    private static <S> S createService(
            Class<S> serviceClass, final String authToken) throws KeyManagementException, NoSuchAlgorithmException {
        AuthenticationInterceptor interceptor = null;
        if (!TextUtils.isEmpty(authToken)) {

            interceptor = new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return createServiceHttps(serviceClass, interceptor, null);
    }

    private static <S> S createServiceHttps(Class<S> serviceClass, Interceptor interceptor, Converter.Factory[] factories) throws NoSuchAlgorithmException, KeyManagementException {

        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient.Builder httpClient =
                new OkHttpClient
                        .Builder()
                        .cache(cache());

        httpClient.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        httpClient.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });


        Retrofit.Builder builder = new Retrofit.Builder();

        //check if contains default converter(JSON)
        boolean containsDefaultConverter = false;
        if (factories != null && factories.length > 0) {
            for (Converter.Factory factory : factories) {
                //The factory who takes care for serialization and deserialization of objects
                builder.addConverterFactory(factory);
                if (factory instanceof GsonConverterFactory)
                    containsDefaultConverter = true;
            }
        }

        //if it does not contain default converter, then add it
        if (!containsDefaultConverter)
            builder.addConverterFactory(GsonConverterFactory.create());

        builder.baseUrl(API_BASE_URL);
        if (interceptor != null && !httpClient.interceptors().contains(interceptor))
            httpClient.addInterceptor(interceptor);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.writeTimeout(30, TimeUnit.SECONDS);
        builder.client(httpClient.build());
        Retrofit retrofit = builder
                .build();
        return retrofit.create(serviceClass);
    }

    private static Cache cache() {
        return new Cache(new File(AppController.getInstance().getCacheDir(),
                "someIdentifier"), cacheSize);
    }

    public static Api getApi() {
        return retrofit.create(Api.class);
    }
}


