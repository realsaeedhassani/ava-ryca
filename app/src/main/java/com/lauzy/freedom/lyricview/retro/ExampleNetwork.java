//package com.lauzy.freedom.lyricview.retro;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.http.GET;
//import retrofit2.http.Path;
//
//public class ExampleNetwork {
//    private IExampleNetwork mIExampleNetwork, mICachedExampleNetwork;
//
//    ExampleNetwork(RetrofitManager retrofitManager) {
//        mIExampleNetwork = retrofitManager.getRetrofit().create(IExampleNetwork.class);
//        mICachedExampleNetwork = retrofitManager.getCachedRetrofit().create(IExampleNetwork.class);
//    }
//
//    interface IExampleNetwork {
//        @GET("/api/details")
//        Call<ResponseBody> getDetails(@Path("url") String url);
//    }
//
//    @Override
//    public Call<Re> getDetails(String id) {
//        return mIExampleNetwork.getDetails(id);
//    }
//
//    @Override
//    public Single<Details> getCachedDetails(String id) {
//        return mICachedExampleNetwork.getDetails(id);
//    }
//}
